package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class CombinedBlockEntity extends BlockEntity {

    private String block1, block2, block3, block4;
    private Direction block1Dir, block2Dir, block3Dir, block4Dir;
    private Boolean block1Inv, block2Inv, block3Inv, block4Inv;

    private BlockState Block1, Block2, Block3, Block4;

    private int numSubBlocks;

    public CombinedBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.COMBINED_BLOCK.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {

        tag.putInt("numSubBlocks", this.numSubBlocks);

        tag.putString("block1", this.block1);
        tag.putString("block1Dir", this.block1Dir.getName());
        tag.putBoolean("block1Inv", this.block1Inv);

        tag.putString("block2", this.block1);
        tag.putString("block2Dir", this.block1Dir.getName());
        tag.putBoolean("block2Inv", this.block1Inv);

        tag.putString("block3", this.block1);
        tag.putString("block3Dir", this.block1Dir.getName());
        tag.putBoolean("block3Inv", this.block1Inv);

        tag.putString("block4", this.block1);
        tag.putString("block4Dir", this.block1Dir.getName());
        tag.putBoolean("block4Inv", this.block1Inv);

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {

        this.numSubBlocks = tag.getInt("numSubBlocks");

        this.block1 = tag.getString("block1");
        this.block1Dir = Direction.byName(tag.getString("block1Dir"));
        this.block1Inv = tag.getBoolean("block1Inv");

        this.block2 = tag.getString("block1");
        this.block2Dir = Direction.byName(tag.getString("block1Dir"));
        this.block2Inv = tag.getBoolean("block1Inv");

        this.block3 = tag.getString("block1");
        this.block3Dir = Direction.byName(tag.getString("block1Dir"));
        this.block3Inv = tag.getBoolean("block1Inv");

        this.block4 = tag.getString("block1");
        this.block4Dir = Direction.byName(tag.getString("block1Dir"));
        this.block4Inv = tag.getBoolean("block1Inv");

        //this.Block1 = ForgeRegistries.BLOCKS.getValue(block1).getStateDefinition().any().setValue()

        super.load(tag);
    }
}
