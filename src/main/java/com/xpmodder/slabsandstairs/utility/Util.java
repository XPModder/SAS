package com.xpmodder.slabsandstairs.utility;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
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

    public static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }


    public static float[] getQuadVertices(BakedQuad quad){
        int[] vertices = quad.getVertices();

        float v1x = round(Float.intBitsToFloat(vertices[0]), 1);
        float v1y = round(Float.intBitsToFloat(vertices[1]), 1);
        float v1z = round(Float.intBitsToFloat(vertices[2]), 1);

        float v2x = round(Float.intBitsToFloat(vertices[8]), 1);
        float v2y = round(Float.intBitsToFloat(vertices[9]), 1);
        float v2z = round(Float.intBitsToFloat(vertices[10]), 1);

        float v3x = round(Float.intBitsToFloat(vertices[16]), 1);
        float v3y = round(Float.intBitsToFloat(vertices[17]), 1);
        float v3z = round(Float.intBitsToFloat(vertices[18]), 1);

        float v4x = round(Float.intBitsToFloat(vertices[24]), 1);
        float v4y = round(Float.intBitsToFloat(vertices[25]), 1);
        float v4z = round(Float.intBitsToFloat(vertices[26]), 1);

        return new float[]{v1x, v1y, v1z, v2x, v2y, v2z, v3x, v3y, v3z, v4x, v4y, v4z};
    }

    public static String printQuadVertices(BakedQuad quad){
        float[] vertices = getQuadVertices(quad);

        String out = "[x1:" + vertices[0] + "; \ty1:" + vertices[1] + "; \tz1:" + vertices[2];
        out += "; \tx2:" + vertices[3] + "; \ty2:" + vertices[4] + "; \tz2:" + vertices[5];
        out += "; \tx3:" + vertices[6] + "; \ty3:" + vertices[7] + "; \tz3:" + vertices[8];
        out += "; \tx4:" + vertices[9] + "; \ty4:" + vertices[10] + "; \tz4:" + vertices[11];
        out += "]";

        return out;

    }


}
