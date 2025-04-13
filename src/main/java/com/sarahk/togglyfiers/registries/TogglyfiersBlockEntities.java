package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import com.sarahk.togglyfiers.blockEntity.ChangeBlockEntity;
import com.sarahk.togglyfiers.blockEntity.TogglyfierBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TogglyfiersBlockEntities {

	private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Togglyfiers.MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TogglyfierBlockEntity>> TOGGLYFIER =
			REGISTRY.register("togglyfier", () -> BlockEntityType.Builder.of(TogglyfierBlockEntity::new,
					TogglyfiersBlocks.TOGGLYFIER.get()
			).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChangeBlockEntity>> CHANGE_BLOCK =
			REGISTRY.register("change_block", () -> BlockEntityType.Builder.of(ChangeBlockEntity::new,
					TogglyfiersBlocks.CHANGE_BLOCK.get()
			).build(null));

	public static void register(IEventBus bus) {
		REGISTRY.register(bus);
	}
}
