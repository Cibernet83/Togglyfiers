package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import com.sarahk.togglyfiers.blocks.ChangeBlock;
import com.sarahk.togglyfiers.blocks.TogglyfierBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Togglyfiers.MODID)
@Mod.EventBusSubscriber(modid = Togglyfiers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TogglyBlocks extends TogglyRegistry
{

    public static final Block TOGGLYFIER = getNull();
    public static final Block CHANGE_BLOCK = getNull();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(new TogglyfierBlock(Block.Properties.create(Material.IRON)).setRegistryName("togglyfier"));
        registry.register(new ChangeBlock(Block.Properties.create(Material.WOOL)).setRegistryName("change_block"));
    }
}
