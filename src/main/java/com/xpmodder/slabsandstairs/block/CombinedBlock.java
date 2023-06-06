package com.xpmodder.slabsandstairs.block;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CombinedBlock extends Block implements EntityBlock {


    private BlockState block1, block2, block3, block4;
    private CombinedBlockEntity be;
    private boolean needsSubBlocks = false;

    public CombinedBlock(BlockBehaviour.Properties properties, BlockState initialState) {
        super(properties);
        block1 = initialState;
    }

    public CombinedBlock(BlockBehaviour.Properties properties, BlockState block1, BlockState block2, BlockState block3, BlockState block4) {
        super(properties);
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
        this.block4 = block4;
        this.needsSubBlocks = true;
    }


    public void setBlocks(BlockState block1, BlockState block2) {
        this.block1 = block1;
        this.block2 = block2;
        if (this.be != null) {
            this.be.Block1 = block1;
            this.be.Block2 = block2;
            this.be.numSubBlocks = 2;
            this.be.updateModelData();
        }
        else{
            this.needsSubBlocks = true;
        }
    }

    public void setBlocks(BlockState block1, BlockState block2, BlockState block3) {
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
        if (this.be != null) {
            this.be.Block1 = block1;
            this.be.Block2 = block2;
            this.be.Block3 = block3;
            this.be.numSubBlocks = 3;
            this.be.updateModelData();
        }
        else{
            this.needsSubBlocks = true;
        }
    }

    public void setBlocks(BlockState block1, BlockState block2, BlockState block3, BlockState block4) {
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
        this.block4 = block4;
        if (this.be != null) {
            this.be.Block1 = block1;
            this.be.Block2 = block2;
            this.be.Block3 = block3;
            this.be.Block4 = block4;
            this.be.numSubBlocks = 4;
            this.be.updateModelData();
        }
        else{
            this.needsSubBlocks = true;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState state) {
        this.be = new CombinedBlockEntity(blockPos, state, this.block1);
        if(this.needsSubBlocks){
            this.be.Block1 = this.block1;
            this.be.Block2 = this.block2;
            this.be.Block3 = this.block3;
            this.be.Block4 = this.block4;
            this.be.updateModelData();
        }
        return this.be;
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {

        if (this.be == null) {
            return this.block1.getShape(getter, pos);
        }

        VoxelShape finalShape = this.be.Block1.getShape(getter, pos);

        if (this.be.numSubBlocks >= 2) {
            finalShape = Shapes.or(finalShape, this.be.Block2.getShape(getter, pos));
        }
        if (this.be.numSubBlocks >= 3) {
            finalShape = Shapes.or(finalShape, this.be.Block3.getShape(getter, pos));
        }
        if (this.be.numSubBlocks >= 4) {
            finalShape = Shapes.or(finalShape, this.be.Block4.getShape(getter, pos));
        }
        return finalShape;

    }

    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor accessor, BlockPos pos, BlockPos pos2) {
        return state;
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @javax.annotation.Nullable BlockEntity entity, ItemStack stack) {

        if(!(entity instanceof CombinedBlockEntity blockEntity)){
            return;
        }

        player.causeFoodExhaustion(0.005F);

        if(blockEntity.Block4 != null) {

            player.awardStat(Stats.BLOCK_MINED.get(blockEntity.Block4.getBlock()));
            dropResources(blockEntity.Block4, level, pos, entity, player, stack);

            blockEntity.Block4 = null;
            blockEntity.numSubBlocks = 3;
            blockEntity.updateModelData();

        }
        else if(blockEntity.Block3 != null){

            player.awardStat(Stats.BLOCK_MINED.get(blockEntity.Block3.getBlock()));
            dropResources(blockEntity.Block3, level, pos, entity, player, stack);

            blockEntity.Block3 = null;
            blockEntity.numSubBlocks = 2;
            blockEntity.updateModelData();

        }
        else if(blockEntity.Block2 != null){

            player.awardStat(Stats.BLOCK_MINED.get(blockEntity.Block2.getBlock()));
            dropResources(blockEntity.Block2, level, pos, entity, player, stack);

            BlockState newState = blockEntity.Block1;

            level.setBlockAndUpdate(pos, newState);

        }


    }

    public boolean dropFromExplosion(Explosion p_49826_) {
        return false;
    }

}