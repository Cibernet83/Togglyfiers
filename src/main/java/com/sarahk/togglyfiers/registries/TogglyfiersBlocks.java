package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import com.sarahk.togglyfiers.block.ChangeBlock;
import com.sarahk.togglyfiers.block.TogglyfierBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TogglyfiersBlocks{


	protected static DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Togglyfiers.MODID);

	public static final DeferredBlock<TogglyfierBlock> TOGGLYFIER = REGISTRY.registerBlock("togglyfier", TogglyfierBlock::new);
	public static final DeferredBlock<ChangeBlock> CHANGE_BLOCK = REGISTRY.registerBlock("change_block", ChangeBlock::new);

	public static void register(IEventBus bus) {
		REGISTRY.register(bus);
	}
}
