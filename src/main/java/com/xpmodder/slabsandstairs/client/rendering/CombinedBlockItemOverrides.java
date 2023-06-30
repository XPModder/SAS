package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.block.SlabBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CombinedBlockItemOverrides extends ItemOverrides {

    @Nullable
    @Override
    public BakedModel resolve(@NotNull BakedModel origModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int num) {

        if(stack.getTag() == null){
            return new CombinedBlockBakedModel();
        }

        String block;
        Direction dir;
        boolean inv;
        Block actualBlock;

        BlockState block1 = null, block2 = null, block3 = null, block4 = null;

        CompoundTag tag = stack.getTag().getCompound("BlockEntityTag");

        if(tag.contains("block1")){
            block = tag.getString("block1");
            dir = Direction.byName(tag.getString("block1Dir"));
            inv = tag.getBoolean("block1Inv");

            if(dir == null){
                dir = Direction.NORTH;
            }

            actualBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
            block1 = actualBlock.defaultBlockState().setValue(SlabBlock.FACING, dir).setValue(SlabBlock.INVERTED, inv);
        }

        if(tag.contains("block2")){
            block = tag.getString("block2");
            dir = Direction.byName(tag.getString("block2Dir"));
            inv = tag.getBoolean("block2Inv");

            if(dir == null){
                dir = Direction.NORTH;
            }

            actualBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
            block2 = actualBlock.defaultBlockState().setValue(SlabBlock.FACING, dir).setValue(SlabBlock.INVERTED, inv);
        }

        if(tag.contains("block3")){
            block = tag.getString("block3");
            dir = Direction.byName(tag.getString("block3Dir"));
            inv = tag.getBoolean("block3Inv");

            if(dir == null){
                dir = Direction.NORTH;
            }

            actualBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
            block3 = actualBlock.defaultBlockState().setValue(SlabBlock.FACING, dir).setValue(SlabBlock.INVERTED, inv);
        }

        if(tag.contains("block4")){
            block = tag.getString("block4");
            dir = Direction.byName(tag.getString("block4Dir"));
            inv = tag.getBoolean("block4Inv");

            if(dir == null){
                dir = Direction.NORTH;
            }

            actualBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
            block4 = actualBlock.defaultBlockState().setValue(SlabBlock.FACING, dir).setValue(SlabBlock.INVERTED, inv);
        }

        return new CombinedBlockItemModel(block1, block2, block3, block4);

    }


}
