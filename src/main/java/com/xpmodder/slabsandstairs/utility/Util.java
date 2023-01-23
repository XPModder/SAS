package com.xpmodder.slabsandstairs.utility;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Util {

    public static Block getBlockFromItem(Item item){
        if(item instanceof BlockItem){
            return ((BlockItem)item).getBlock();
        }
        else{
            return null;
        }
    }

}
