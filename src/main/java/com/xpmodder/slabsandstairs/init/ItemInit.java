package com.xpmodder.slabsandstairs.init;

import com.xpmodder.slabsandstairs.SlabsAndStairs;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class ItemInit {

    public static Set<Item> MY_ITEMS = new LinkedHashSet<>();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    public static final RegistryObject<Item> combinedBlock = ITEMS.register("combined_block", () -> new BlockItem(BlockInit.combinedBlock.get(), new Item.Properties()));

    /*static{

        for(Block block : BlockInit.MY_BLOCKS){
            Item newItem = new BlockItem(block, new Item.Properties().tab(SlabsAndStairs.ITEM_GROUP_SAS)).setRegistryName(block.getRegistryName().getPath());
            ITEMS.register(block.getRegistryName().getPath(), () -> newItem);
            MY_ITEMS.add(newItem);
        }

    }*/


}
