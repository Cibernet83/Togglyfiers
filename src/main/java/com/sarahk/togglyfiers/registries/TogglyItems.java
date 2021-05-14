package com.sarahk.togglyfiers.registries;

import com.sarahk.togglyfiers.Togglyfiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Togglyfiers.MODID)
@Mod.EventBusSubscriber(modid = Togglyfiers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TogglyItems
{

    public static final Item ENDER_CUBE = getNull();
    public static final Item TOGGLYFICATION_CORE = getNull();


    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T getNull()
    {
        return null;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName("ender_cube"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName("togglyfication_core"));
    }
}
