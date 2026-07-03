package golemknights.tinkersgolem.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.modulargolems.init.ModularGolems;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.entity.SlimeGolemPartType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

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

		pvd.add(TinkersGolem.MODID + ".max_overslime", "Max Overslime");
		pvd.add("golem_material." + TinkersGolem.MODID + ".earth_slime", "Earth Slime");
		pvd.add("golem_material." + TinkersGolem.MODID + ".sky_slime", "Sky Slime");
		pvd.add("golem_material." + TinkersGolem.MODID + ".ichor", "Ichor");
		pvd.add("golem_material." + TinkersGolem.MODID + ".ender_slime", "Ender Slime");

		for (var type : SlimeGolemPartType.values()) {
			String name = type.name().toLowerCase(Locale.ROOT);
			pvd.add("golem_part.slime_golem." + name, RegistrateLangProvider.toEnglishName(name) + ": %s");
		}

		pvd.add("config.jade.plugin_tinkers_golem.overslime", "Overslime");
		pvd.add("config.jade.plugin_tinkers_golem.overslime_recovery", "Overslime Recovery Factor");
		pvd.add("config.jade.plugin_tinkers_golem.tank_capacity", "Tank Capacity");

	}

}
