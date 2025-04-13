package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;

public class TogglyfiersCreativeTabs {

	private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Togglyfiers.MODID);

	public static final Holder<CreativeModeTab> GENERAL = REGISTRY.register("general", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.togglyfiers.general"))
			.icon(TogglyfiersItems.TOGGLYFICATION_CORE::toStack)
			.displayItems(((itemDisplayParameters, output) -> output.acceptAll(Arrays.asList(
					TogglyfiersItems.TOGGLYFICATION_CORE.toStack(),
					TogglyfiersItems.TOGGLYFIER.toStack(),
					TogglyfiersItems.CHANGE_BLOCK.toStack()
			))))
			.build());

	public static void register(IEventBus bus) {
		REGISTRY.register(bus);
	}
}
