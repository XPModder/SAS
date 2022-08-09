package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.SlabsAndStairs;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.LinkedHashSet;
import java.util.Set;

public class ItemInit {

    public static Set<Item> MY_ITEMS = new LinkedHashSet<>();


    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Item> event){

        IForgeRegistry registry = event.getRegistry();

        for(Block block : BlockInit.MY_BLOCKS){
            Item newItem = new BlockItem(block, new Item.Properties().group(SlabsAndStairs.ITEM_GROUP_SAS)).setRegistryName(block.getRegistryName().getPath());
            registry.register(newItem);
            MY_ITEMS.add(newItem);
        }

    }


}
