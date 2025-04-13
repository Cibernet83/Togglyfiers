package com.sarahk.togglyfiers;

import com.mojang.logging.LogUtils;
import com.sarahk.togglyfiers.registries.TogglyfiersBlocks;
import com.sarahk.togglyfiers.registries.TogglyfiersItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Togglyfiers.MODID)
public class Togglyfiers {
	public static final String MODID = "togglyfiers";
	private static final Logger LOGGER = LogUtils.getLogger();

	public Togglyfiers(IEventBus modEventBus, ModContainer modContainer) {

		TogglyfiersBlocks.register(modEventBus);
		TogglyfiersItems.register(modEventBus);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	public static ResourceLocation resourceLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

}
