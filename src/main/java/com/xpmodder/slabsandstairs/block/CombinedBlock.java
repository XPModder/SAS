package com.xpmodder.slabsandstairs.block;


import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.xpmodder.slabsandstairs.block.SlabBlock.FACING;
import static com.xpmodder.slabsandstairs.block.SlabBlock.INVERTED;
import static com.xpmodder.slabsandstairs.utility.Util.getBlockFromItem;

public class CombinedBlock extends Block implements EntityBlock {


    private final BlockState block1;

    public CombinedBlock(BlockBehaviour.Properties properties, BlockState initialState) {
        super(properties);
        block1 = initialState;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState state) {

        CombinedBlockEntity be = new CombinedBlockEntity(blockPos, state, this.block1);
        be.updateModelData();
        return be;

    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {

        CombinedBlockEntity be = (CombinedBlockEntity) getter.getBlockEntity(pos);

        if (be == null) {
            return this.block1.getShape(getter, pos);
        }

        VoxelShape finalShape = be.Block1.getShape(getter, pos);

        if (be.numSubBlocks >= 2) {
            finalShape = Shapes.or(finalShape, be.Block2.getShape(getter, pos));
        }
        if (be.numSubBlocks >= 3) {
            finalShape = Shapes.or(finalShape, be.Block3.getShape(getter, pos));
        }
        if (be.numSubBlocks >= 4) {
            finalShape = Shapes.or(finalShape, be.Block4.getShape(getter, pos));
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

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {

        LogHelper.info("onPlace! Client:" + level.isClientSide());

        super.onPlace(state, level, pos, state2, bool);

        if(level.getBlockEntity(pos) instanceof CombinedBlockEntity){
            level.getBlockEntity(pos).setChanged();
            ((CombinedBlockEntity) level.getBlockEntity(pos)).updateModelData();
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);
        }

    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if(!(level.getBlockEntity(pos) instanceof CombinedBlockEntity)){
            return InteractionResult.PASS;
        }

        Item heldItem = player.getItemInHand(hand).getItem();
        if(heldItem == Items.AIR){
            return InteractionResult.PASS;
        }


        CombinedBlockEntity be = (CombinedBlockEntity) level.getBlockEntity(pos);

        VoxelShape fullBlock = Block.box(0, 0, 0, 16, 16, 16);

        if(be.numSubBlocks == 2){

            if(be.Block1.getBlock() instanceof SlabBlock && (!(be.Block1.getBlock() instanceof StairBlock)) && (!(be.Block1.getBlock() instanceof QuarterBlock))){
                if(be.Block2.getBlock() instanceof QuarterBlock){
                    //Block1: Slab, Block2: Quarter
                    if(getBlockFromItem(heldItem) instanceof QuarterBlock){
                        for(Direction dir : Direction.values()){
                            BlockState quarterState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, false);
                            BlockState invState = quarterState.setValue(INVERTED, true);
                            if(Shapes.or(state.getShape(level, pos), quarterState.getShape(level, pos)) == fullBlock){

                                be.Block3 = quarterState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData();

                                level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                                SoundType sound = quarterState.getSoundType();
                                level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                                return InteractionResult.SUCCESS;
                            }
                            else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == fullBlock){

                                be.Block3 = invState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData();

                                level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                                SoundType sound = invState.getSoundType();
                                level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                                return InteractionResult.SUCCESS;

                            }
                        }
                    }
                }
            }
            else if(be.Block1.getBlock() instanceof QuarterBlock){
                if(be.Block2.getBlock() instanceof SlabBlock && !(be.Block2.getBlock() instanceof StairBlock) && !(be.Block2.getBlock() instanceof QuarterBlock)){
                    //Block1: Quarter, Block2: Slab
                    if(getBlockFromItem(heldItem) instanceof QuarterBlock){
                        for(Direction dir : Direction.values()){
                            BlockState quarterState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, false);
                            BlockState invState = quarterState.setValue(INVERTED, true);
                            if(Shapes.or(state.getShape(level, pos), quarterState.getShape(level, pos)) == fullBlock){

                                be.Block3 = quarterState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData();

                                level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                                SoundType sound = quarterState.getSoundType();
                                level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                                return InteractionResult.SUCCESS;
                            }
                            else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == fullBlock){

                                be.Block3 = invState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData();

                                level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                                SoundType sound = invState.getSoundType();
                                level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                                return InteractionResult.SUCCESS;

                            }
                        }
                    }
                }
                else if(be.Block2.getBlock() instanceof QuarterBlock){
                    //Block1: Quarter, Block2: Quarter
                    if(getBlockFromItem(heldItem) instanceof QuarterBlock){

                    }
                    else if(getBlockFromItem(heldItem) instanceof SlabBlock && !(getBlockFromItem(heldItem) instanceof StairBlock)){
                        for(Direction dir : Direction.values()){
                            BlockState slabState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, false);
                            BlockState invState = slabState.setValue(INVERTED, true);
                            if(Shapes.or(state.getShape(level, pos), slabState.getShape(level, pos)) == fullBlock){

                                be.Block3 = slabState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData();

                                level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                                SoundType sound = slabState.getSoundType();
                                level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                                return InteractionResult.SUCCESS;
                            }
                            else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == fullBlock){

                                be.Block3 = invState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData();

                                level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                                SoundType sound = invState.getSoundType();
                                level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                                return InteractionResult.SUCCESS;

                            }
                        }
                    }
                }
            }

        }
        else if(be.numSubBlocks == 3){
            if(be.Block1.getBlock() instanceof QuarterBlock && be.Block2.getBlock() instanceof QuarterBlock && be.Block3.getBlock() instanceof QuarterBlock){
                //Block1: Quarter, Block2: Quarter, Block3: Quarter
                if(getBlockFromItem(heldItem) instanceof QuarterBlock){
                    for(Direction dir : Direction.values()){
                        BlockState quarterState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, false);
                        BlockState invState = quarterState.setValue(INVERTED, true);
                        if(Shapes.or(state.getShape(level, pos), quarterState.getShape(level, pos)) == fullBlock){

                            be.Block4 = quarterState;
                            be.numSubBlocks = 4;
                            be.setChanged();
                            be.updateModelData();

                            level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                            SoundType sound = quarterState.getSoundType();
                            level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                            return InteractionResult.SUCCESS;
                        }
                        else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == fullBlock){

                            be.Block4 = invState;
                            be.numSubBlocks = 4;
                            be.setChanged();
                            be.updateModelData();

                            level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

                            SoundType sound = invState.getSoundType();
                            level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);

                            return InteractionResult.SUCCESS;

                        }
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

}