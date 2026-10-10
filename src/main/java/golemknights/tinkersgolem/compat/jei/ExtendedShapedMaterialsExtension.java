package golemknights.tinkersgolem.compat.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.plugin.jei.MantleJEIConstants;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipeCache;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.plugin.jei.material.MaterialsCraftingExtension;
import slimeknights.tconstruct.plugin.jei.util.CategoryUtil;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.library.recipes.ExtendedShapedMaterialsRecipe;
import golemknights.tinkersgolem.library.recipes.ExtendedShapedMaterialsRecipe.ExtendedMaterialIngredient;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

/**
 * JEI 显示。基于 TConstruct 3.12.1 的 {@code MaterialsCraftingExtension} + {@code ShapedMaterialsExtension}
 * 合并自用：直接吃 {@link ExtendedShapedMaterialsRecipe}，不再需要 {@code ShapedMaterialsRecipe} 桥接；
 * 并把 {@code MaterialRecipeCache.getMaterial(stack)} 换成 {@link ExtendedShapedMaterialsRecipe#getMaterial}，
 * 以支持"一个物品对应多个材料"。
 */
public class ExtendedShapedMaterialsExtension implements ICraftingCategoryExtension {
  private static final String RESULT_SLOT = "result";

  protected final ExtendedShapedMaterialsRecipe recipe;
  /** 材料部件，按下标（材料 slot）排列，未用到的槽为 Ingredient.EMPTY */
  private final List<Ingredient> parts;
  private final ItemStack plainResult;
  private final List<ItemStack> result;
  /** 单部件时：与产物做 focus link 的格子 */
  @Nullable
  private final int[] outputLink;
  /** 每个部件（按材料下标顺序）占用的格子 */
  private final List<int[]> partSlots;

  protected ExtendedShapedMaterialsExtension(ExtendedShapedMaterialsRecipe recipe) {
    this.recipe = recipe;
    this.parts = collectParts(recipe);
    this.plainResult = recipe.getResultItem(Objects.requireNonNull(SafeClientAccess.getRegistryAccess()));

    if (parts.size() == 1) {
      // 单部件：枚举该部件的所有材料变种作为产物，并与该部件的输入槽做 focus link
      Ingredient firstPart = parts.get(0);
      this.result = Arrays.stream(firstPart.getItems()).map(variant -> {
        ItemStack stack = plainResult.copy();
        setMaterial(stack, materialOf(variant, firstPart));
        return stack;
      }).toList();
      this.outputLink = getMaterialSlots(firstPart);
      this.partSlots = List.of();
    } else {
      // 多部件：产物用显示工具，材料对应交给 onDisplayedIngredientsUpdate
      this.result = List.of(IModifiableDisplay.getDisplayStack(plainResult));
      this.outputLink = null;
      this.partSlots = parts.stream().map(this::getMaterialSlots).toList();
    }
  }

  public static ExtendedShapedMaterialsExtension create(ExtendedShapedMaterialsRecipe recipe) {
    for (Ingredient part : collectParts(recipe)) {
      if (part != Ingredient.EMPTY && part.getItems().length == 0)
        return null;
    }
    try {
      return new ExtendedShapedMaterialsExtension(recipe);
    } catch (RuntimeException e) {
      TinkersGolem.LOGGER.error("Fail to show recipe {}:{}", recipe.getId(), e);
      return null;
    }
  }

  /** 从配方原料里，按材料 slot 收成一条部件列表 */
  private static List<Ingredient> collectParts(ExtendedShapedMaterialsRecipe recipe) {
    List<Ingredient> parts = new ArrayList<>();
    for (Ingredient ing : recipe.getIngredients()) {
      if (ing instanceof ExtendedMaterialIngredient ext) {
        while (parts.size() <= ext.slot)
          parts.add(Ingredient.EMPTY);
        parts.set(ext.slot, ext);
      }
    }
    return parts;
  }

  /** TConstruct ShapedMaterialsRecipe.setMaterial 的等价实现（本模组无 extraMaterials） */
  private static void setMaterial(ItemStack stack, MaterialVariantId material) {
    if (stack.getItem() instanceof IMaterialItem materialItem)
      materialItem.setMaterial(stack, material);
    else
      ToolStack.from(stack).setMaterials(MaterialNBT.builder().add(material).build());
  }

  /** 材料查找：本模组原料走自己的支持多材料的版本，其余回退到原版 */
  private static MaterialVariantId materialOf(ItemStack stack, Ingredient part) {
    if (part instanceof ExtendedMaterialIngredient ext)
      return ExtendedShapedMaterialsRecipe.getMaterial(stack, ext);
    return MaterialRecipeCache.getMaterial(stack);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
    // using Collectors.toList to ensure the list is mutable
    List<List<ItemStack>> inputs = recipe.getIngredients().stream()
        .map(ingredient -> List.of(ingredient.getItems())).collect(Collectors.toList());

    // 聚焦产物工具时，按该工具每个部件的材料把对应输入槽过滤成只含该材料的物品
    ItemStack focus = CategoryUtil.getResultItemFocus(focuses);
    if (!focus.isEmpty()) {
      MaterialIdNBT materials = MaterialIdNBT.from(focus);
      for (int i = 0; i < partSlots.size(); i++) {
        Ingredient part = parts.get(i);
        MaterialVariantId material = materials.getMaterial(i);
        List<ItemStack> matching = Arrays.stream(part.getItems())
            .filter(stack -> material.matchesVariant(materialOf(stack, part)))
            .toList();
        if (!matching.isEmpty())
          for (int slot : partSlots.get(i))
            inputs.set(slot, matching);
      }
    }
    MaterialsCraftingExtension.setRecipe(this, builder, craftingGridHelper, recipe.getId(), inputs, result, plainResult, outputLink);
  }

  @Override
  public void onDisplayedIngredientsUpdate(List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
    // 只有多部件才需要这个对应
    if (partSlots.size() <= 1)
      return;
    IRecipeSlotDrawable resultSlot = CategoryUtil.findSlot(recipeSlots, RESULT_SLOT);
    if (resultSlot == null)
      return;
    int width = getWidth();
    int height = getHeight();
    if (width <= 0 || height <= 0)
      width = height = CategoryUtil.getShapelessSize(recipe.getIngredients().size());

    // 取每个部件第一个槽"当前显示"的物品，推出材料，覆盖产物显示
    List<MaterialVariantId> variants = new ArrayList<>(partSlots.size());
    for (int i = 0; i < partSlots.size(); i++) {
      int[] partSlot = partSlots.get(i);
      ItemStack stack = recipeSlots.get(MantleJEIConstants.getCraftingIndex(partSlot[0], width, height))
          .getDisplayedItemStack().orElse(ItemStack.EMPTY);
      variants.add(materialOf(stack, parts.get(i)));
    }
    resultSlot.createDisplayOverrides().addItemStack(new MaterialIdNBT(variants).updateStack(plainResult.copy()));
  }

  /** 该部件在配方里的所有格子下标 */
  protected int[] getMaterialSlots(Ingredient part) {
    List<Ingredient> inputs = recipe.getIngredients();
    return IntStream.range(0, inputs.size()).filter(i -> inputs.get(i) == part).toArray();
  }

  @Override
  public ResourceLocation getRegistryName() {
    return recipe.getId();
  }

  @Override
  public int getWidth() {
    return recipe.getWidth();
  }

  @Override
  public int getHeight() {
    return recipe.getHeight();
  }
}
