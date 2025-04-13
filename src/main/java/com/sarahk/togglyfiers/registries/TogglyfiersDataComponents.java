package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import com.sarahk.togglyfiers.data.components.TogglyOwnerComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TogglyfiersDataComponents {
	private static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Togglyfiers.MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<TogglyOwnerComponent>> TOGGLYFIER_OWNER = REGISTRY.register("owner", () ->
			new DataComponentType.Builder<TogglyOwnerComponent>()
					.persistent(TogglyOwnerComponent.CODEC)
					.networkSynchronized(TogglyOwnerComponent.STREAM_CODEC)
					.build());

	public static void register(IEventBus bus) {
		REGISTRY.register(bus);
	}
}
