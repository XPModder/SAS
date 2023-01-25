package com.xpmodder.slabsandstairs.utility;

import net.minecraft.core.Direction;
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

    public static Direction nextDirection(Direction in){
        return switch (in) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.UP;
            case UP -> Direction.DOWN;
            default -> Direction.NORTH;
        };
    }

}
