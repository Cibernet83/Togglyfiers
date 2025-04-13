package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TogglyfiersItems {


	protected static DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Togglyfiers.MODID);

	public static final DeferredItem<Item> TOGGLYFICATION_CORE = REGISTRY.registerSimpleItem("togglyfication_core");

	public static final DeferredItem<BlockItem> TOGGLYFIER = REGISTRY.registerSimpleBlockItem(TogglyfiersBlocks.TOGGLYFIER);
	public static final DeferredItem<BlockItem> CHANGE_BLOCK = REGISTRY.registerSimpleBlockItem(TogglyfiersBlocks.CHANGE_BLOCK);

	public static void register(IEventBus bus) {
		REGISTRY.register(bus);
	}
}
