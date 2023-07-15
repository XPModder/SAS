package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.client.rendering.CombinedBlockBakedModel;
import com.xpmodder.slabsandstairs.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CombinedBlockEntity extends BlockEntity {

    public BlockState Block1 = Blocks.AIR.defaultBlockState(),
            Block2 = Blocks.AIR.defaultBlockState(),
            Block3 = Blocks.AIR.defaultBlockState(),
            Block4 = Blocks.AIR.defaultBlockState();

    public int numSubBlocks = 1;

    public int POWER = 0;

    public CombinedBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.COMBINED_BLOCK.get(), blockPos, blockState);
    }

    public CombinedBlockEntity(BlockPos blockPos, BlockState blockState, BlockState block1){
        super(BlockEntityInit.COMBINED_BLOCK.get(), blockPos, blockState);
        this.Block1 = block1;
        this.numSubBlocks = 1;
    }

    public void updateModelData(){
        updateModelData(this.level, this.getBlockPos());
    }

    public void updateModelData(Level level, BlockPos pos){
        updateModelData(level, pos, Direction.NORTH);
    }

    public void updateModelData(Level level, BlockPos pos, Direction dir){

        int Light = this.Block1.getLightEmission(level, pos);

        if(this.numSubBlocks >= 2){
            Light += this.Block2.getLightEmission(level, pos);
        }
        if(this.numSubBlocks >= 3){
            Light += this.Block3.getLightEmission(level, pos);
        }
        if(this.numSubBlocks == 4){
            Light += this.Block4.getLightEmission(level, pos);
        }

        if(Light > 15){
            Light = 15;
        }

        if(level != null && !level.isClientSide) {
            level.setBlock(pos, this.getBlockState().setValue(CombinedBlock.LEVEL, Light), 2);
        }

        requestModelDataUpdate();
    }


    public CompoundTag write(CompoundTag tag){

        tag.putInt("numSubBlocks", this.numSubBlocks);

        if(numSubBlocks >= 1) {

            tag.putString("block1", this.Block1.getBlock().getRegistryName().toString());
            tag.putString("block1Dir", this.Block1.getValue(SlabBlock.FACING).getName());
            tag.putBoolean("block1Inv", this.Block1.getValue(StairBlock.INVERTED));

        }

        if(numSubBlocks >= 2) {

            tag.putString("block2", this.Block2.getBlock().getRegistryName().toString());
            tag.putString("block2Dir", this.Block2.getValue(SlabBlock.FACING).getName());
            tag.putBoolean("block2Inv", this.Block2.getValue(StairBlock.INVERTED));

        }

        if(numSubBlocks >= 3) {

            tag.putString("block3", this.Block3.getBlock().getRegistryName().toString());
            tag.putString("block3Dir", this.Block3.getValue(SlabBlock.FACING).getName());
            tag.putBoolean("block3Inv", this.Block3.getValue(StairBlock.INVERTED));

        }

        if(numSubBlocks >= 4) {

            tag.putString("block4", this.Block4.getBlock().getRegistryName().toString());
            tag.putString("block4Dir", this.Block4.getValue(SlabBlock.FACING).getName());
            tag.putBoolean("block4Inv", this.Block4.getValue(StairBlock.INVERTED));

        }

        return tag;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(){
        CompoundTag tag = new CompoundTag();
        return write(tag);

    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public void fromItem(ItemStack stack){
        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        if(tag != null){
            load(tag);
        }
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {

        tag = write(tag);

        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {

        super.load(tag);

        String block1, block2, block3, block4;
        Direction block1Dir, block2Dir, block3Dir, block4Dir;
        boolean block1Inv, block2Inv, block3Inv, block4Inv;

        this.numSubBlocks = tag.getInt("numSubBlocks");

        Block block;

        if(tag.contains("block1")) {

            block1 = tag.getString("block1");
            block1Dir = Direction.byName(tag.getString("block1Dir"));
            block1Inv = tag.getBoolean("block1Inv");

            if(block1Dir == null){
                block1Dir = Direction.NORTH;
            }

            block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block1));
            if(block instanceof SlabBlock){
                this.Block1 = block.defaultBlockState().setValue(SlabBlock.FACING, block1Dir).setValue(StairBlock.INVERTED, block1Inv);
            }

        }

        if(tag.contains("block2")) {

            block2 = tag.getString("block2");
            block2Dir = Direction.byName(tag.getString("block2Dir"));
            block2Inv = tag.getBoolean("block2Inv");

            if(block2Dir == null){
                block2Dir = Direction.NORTH;
            }

            block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block2));
            if(block instanceof SlabBlock){
                this.Block2 = block.defaultBlockState().setValue(SlabBlock.FACING, block2Dir).setValue(StairBlock.INVERTED, block2Inv);
            }

        }

        if(tag.contains("block3")) {

            block3 = tag.getString("block3");
            block3Dir = Direction.byName(tag.getString("block3Dir"));
            block3Inv = tag.getBoolean("block3Inv");

            if(block3Dir == null){
                block3Dir = Direction.NORTH;
            }

            block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block3));
            if(block instanceof SlabBlock){
                this.Block3 = block.defaultBlockState().setValue(SlabBlock.FACING, block3Dir).setValue(StairBlock.INVERTED, block3Inv);
            }

        }

        if(tag.contains("block4")) {

            block4 = tag.getString("block4");
            block4Dir = Direction.byName(tag.getString("block4Dir"));
            block4Inv = tag.getBoolean("block4Inv");

            if(block4Dir == null){
                block4Dir = Direction.NORTH;
            }

            block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block4));
            if(block instanceof SlabBlock){
                this.Block4 = block.defaultBlockState().setValue(SlabBlock.FACING, block4Dir).setValue(StairBlock.INVERTED, block4Inv);
            }

        }

        updateModelData();
    }

    @NotNull
    @Override
    public IModelData getModelData() {

        IModelData data = CombinedBlockBakedModel.getEmptyIModelData();

        data.setData(CombinedBlockBakedModel.BLOCK1, this.Block1);
        data.setData(CombinedBlockBakedModel.BLOCK2, this.Block2);
        data.setData(CombinedBlockBakedModel.BLOCK3, this.Block3);
        data.setData(CombinedBlockBakedModel.BLOCK4, this.Block4);
        data.setData(CombinedBlockBakedModel.NUM_BLOCKS, this.numSubBlocks);

        return data;
    }


    @Override
    public String toString(){
        String output = "com.xpmodder.slabsandstairs.block.CombinedBlockEntity:";

        output += write(new CompoundTag()).toString();

        return output;

    }


}
