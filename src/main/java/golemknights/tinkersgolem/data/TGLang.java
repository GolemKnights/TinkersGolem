package golemknights.tinkersgolem.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.modulargolems.init.ModularGolems;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.entity.SlimeGolemPartType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import slimeknights.tconstruct.TConstruct;

import javax.annotation.Nullable;
import java.util.Locale;

public enum TGLang {
	OVERSLIME_INFO("info.overslime", "Overslime: %s/%s", 2, null);

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;

	TGLang(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = TinkersGolem.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public String key() {
		return key;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static MutableComponent getTranslate(String s) {
		return Component.translatable(ModularGolems.MODID + "." + s);
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = Component.translatable(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (TGLang lang : TGLang.values()) {
			pvd.add(lang.key, lang.def);
		}

		pvd.add("tabs." + TinkersGolem.MODID + ".items", "Tinkers' Golem: Items");
		pvd.add("tabs." + TinkersGolem.MODID + ".tools", "Tinkers' Golem: Tools");
		pvd.add(TinkersGolem.MODID + ".max_overslime", "Max Overslime");
		pvd.add(TinkersGolem.MODID + ".overslime_recovery", "Overslime Recovery Factor");
		pvd.add(TinkersGolem.MODID + ".tank_capacity", "Tank Capacity");
		pvd.add("golem_material." + TinkersGolem.MODID + ".earthslime", "Earthslime");
		pvd.add("golem_material." + TinkersGolem.MODID + ".skyslime", "Skyslime");
		pvd.add("golem_material." + TinkersGolem.MODID + ".ichor", "Ichor");
		pvd.add("golem_material." + TinkersGolem.MODID + ".enderslime", "Enderslime");
		pvd.add("golem_material." + TConstruct.MOD_ID + ".slimesteel", "Slimesteel");
		pvd.add("golem_material." + TConstruct.MOD_ID + ".cinderslime", "Cinderslime");
		pvd.add("golem_material." + TConstruct.MOD_ID + ".queens_slime", "Queen's Slime");
		pvd.add("pattern." + TinkersGolem.MODID + ".metal_golem_plating", "Metal Golem Plating");
		pvd.add("pattern." + TinkersGolem.MODID + ".helmet_metal_golem_plating", "Metal Golem Helmet Plating");
		pvd.add("pattern." + TinkersGolem.MODID + ".chestplate_metal_golem_plating", "Metal Golem Chestplate Plating");
		pvd.add("pattern." + TinkersGolem.MODID + ".leggings_metal_golem_plating", "Metal Golem Leggings Plating");
		pvd.add("pattern." + TinkersGolem.MODID + ".boots_metal_golem_plating", "Metal Golem Boots Plating");
		pvd.add("pattern." + TinkersGolem.MODID + ".golem_template", "Metal Golem Template");
		pvd.add("gui." + TinkersGolem.MODID + ".metal_golem_armor", "Metal Golem Armor");
		pvd.add("gui." + TinkersGolem.MODID + ".metal_golem_armor.description", "Metal golem armor is a defensive armor for metal golems.");
		pvd.add("item." + TinkersGolem.MODID + ".helmet_metal_golem_plating", "Metal Golem Helmet Plating");
		pvd.add("item." + TinkersGolem.MODID + ".chestplate_metal_golem_plating", "Metal Golem Chestplate Plating");
		pvd.add("item." + TinkersGolem.MODID + ".leggings_metal_golem_plating", "Metal Golem Leggings Plating");
		pvd.add("item." + TinkersGolem.MODID + ".boots_metal_golem_plating", "Metal Golem Boots Plating");
		pvd.add("item." + TinkersGolem.MODID + ".helmet_metal_golem_plating_cast", "Metal Golem Helmet Plating Gold Cast");
		pvd.add("item." + TinkersGolem.MODID + ".chestplate_metal_golem_plating_cast", "Metal Golem Chestplate Plating Gold Cast");
		pvd.add("item." + TinkersGolem.MODID + ".leggings_metal_golem_plating_cast", "Metal Golem Leggings Plating Gold Cast");
		pvd.add("item." + TinkersGolem.MODID + ".boots_metal_golem_plating_cast", "Metal Golem Boots Plating Gold Cast");
		pvd.add("item." + TinkersGolem.MODID + ".helmet_metal_golem_plating_sand_cast", "Metal Golem Helmet Plating Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".chestplate_metal_golem_plating_sand_cast", "Metal Golem Chestplate Plating Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".leggings_metal_golem_plating_sand_cast", "Metal Golem Leggings Plating Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".boots_metal_golem_plating_sand_cast", "Metal Golem Boots Plating Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".helmet_metal_golem_plating_red_sand_cast", "Metal Golem Helmet Plating Red Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".chestplate_metal_golem_plating_red_sand_cast", "Metal Golem Chestplate Plating Red Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".leggings_metal_golem_plating_red_sand_cast", "Metal Golem Leggings Plating Red Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".boots_metal_golem_plating_red_sand_cast", "Metal Golem Boots Plating Red Sand Cast");
		pvd.add("item." + TinkersGolem.MODID + ".metal_golem_helmet", "Metal Golem Helmet");
		pvd.add("item." + TinkersGolem.MODID + ".metal_golem_chestplate", "Metal Golem Chestplate");
		pvd.add("item." + TinkersGolem.MODID + ".metal_golem_leggings", "Metal Golem Leggings");
		pvd.add("item." + TinkersGolem.MODID + ".metal_golem_boots", "Metal Golem Boots");

		for (var type : SlimeGolemPartType.values()) {
			String name = type.name().toLowerCase(Locale.ROOT);
			pvd.add("golem_part.slime_golem." + name, RegistrateLangProvider.toEnglishName(name) + ": %s");
		}

		pvd.add("config.jade.plugin_tinkers_golem.overslime", "Overslime");

	}

}
