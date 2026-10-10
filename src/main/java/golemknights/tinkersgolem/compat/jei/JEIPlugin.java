package golemknights.tinkersgolem.compat.jei;

import static golemknights.tinkersgolem.TinkersGolem.getResource;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.library.recipes.ExtendedShapedMaterialsRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final ResourceLocation UID = getResource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registry) {
        IExtendableRecipeCategory<CraftingRecipe, ICraftingCategoryExtension> craftingCategory = registry
                .getCraftingCategory();
        craftingCategory.addCategoryExtension(ExtendedShapedMaterialsRecipe.class,
                ExtendedShapedMaterialsExtension::create);
    }

}