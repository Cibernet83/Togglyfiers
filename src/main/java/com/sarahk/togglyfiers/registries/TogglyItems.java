package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Togglyfiers.MODID)
@Mod.EventBusSubscriber(modid = Togglyfiers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TogglyItems extends TogglyRegistry
{

    public static final Item ENDER_CUBE = getNull();
    public static final Item TOGGLYFICATION_CORE = getNull();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName("ender_cube"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName("togglyfication_core"));

        registry.register(new BlockItem(TogglyBlocks.TOGGLYFIER, new Item.Properties().group(ItemGroup.REDSTONE)).setRegistryName("togglyfier"));
        registerItemBlock(registry, TogglyBlocks.CHANGE_BLOCK, new Item.Properties());
    }

    private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block, Item.Properties properties)
    {
        return registerItemBlock(registry, new BlockItem(block, properties));
    }

    private static Item registerItemBlock(IForgeRegistry<Item> registry, BlockItem item)
    {
        if(item.getBlock().getRegistryName() == null)
            throw new IllegalArgumentException(String.format("The provided itemblock %s has a block without a registry name!", item.getBlock()));
        registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
        return item;
    }
}
