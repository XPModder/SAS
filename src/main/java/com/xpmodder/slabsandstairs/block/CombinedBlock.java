package com.xpmodder.slabsandstairs.block;


import com.xpmodder.slabsandstairs.init.KeyInit;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import com.xpmodder.slabsandstairs.utility.ShapeUtil;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.xpmodder.slabsandstairs.block.SlabBlock.FACING;
import static com.xpmodder.slabsandstairs.block.SlabBlock.INVERTED;
import static com.xpmodder.slabsandstairs.utility.Util.getBlockFromItem;

public class CombinedBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {

    private final BlockState block1;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    public static BlockBehaviour.Properties props;

    public CombinedBlock(BlockBehaviour.Properties properties, BlockState initialState) {
        super(properties);
        props = properties;
        block1 = initialState;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false).setValue(LEVEL, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, LEVEL);
    }

    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
        CombinedBlockEntity be = (CombinedBlockEntity) accessor.getBlockEntity(pos);
        if(be != null) {
            be.updateModelData();
        }
        return state;
    }

    public boolean isSignalSource(BlockState state) {
        return false;
    }

    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        if(level == null){
            return 0;
        }
        CombinedBlockEntity be = (CombinedBlockEntity) level.getBlockEntity(pos);
        if(be == null){
            return 0;
        }
        int Power = be.Block1.getSignal(level, pos, dir);

        if(be.numSubBlocks >= 2){
            Power += be.Block2.getSignal(level, pos, dir);
        }
        if(be.numSubBlocks >= 3){
            Power += be.Block3.getSignal(level, pos, dir);
        }
        if(be.numSubBlocks == 4){
            Power += be.Block4.getSignal(level, pos, dir);
        }

        if(Power > 15){
            Power = 15;
        }

        be.POWER = Power;

        return Power;
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player){
        return this.getCloneItemStack(level, pos, state);
    }

    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack itemStack = new ItemStack(this);
        CombinedBlockEntity blockEntity = (CombinedBlockEntity) level.getBlockEntity(pos);
        if(blockEntity != null) {
            blockEntity.saveToItem(itemStack);
        }
        return itemStack;
    }



    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player){

        CombinedBlockEntity blockEntity = (CombinedBlockEntity) level.getBlockEntity(pos);

        ItemStack stack = player.getMainHandItem().copy();

        super.playerWillDestroy(level, pos, state, player);

        if(blockEntity == null){
            return;
        }

        player.causeFoodExhaustion(0.005F);


        if(KeyInit.placementModeMapping.isDown()){

            if(!player.isCreative()) {
                ItemStack itemStack = getCloneItemStack(level, pos, state);
                popResource(level, pos, itemStack);
            }

            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return;
        }


        if(!blockEntity.Block4.isAir()) {

            player.awardStat(Stats.BLOCK_MINED.get(blockEntity.Block4.getBlock()));
            if(!player.isCreative()) {
                dropResources(blockEntity.Block4, level, pos, blockEntity, player, stack);
            }

            blockEntity.Block4 = Blocks.AIR.defaultBlockState();
            blockEntity.numSubBlocks = 3;
            blockEntity.setChanged();
            blockEntity.updateModelData(level, pos);
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

        }
        else if(!blockEntity.Block3.isAir()){

            player.awardStat(Stats.BLOCK_MINED.get(blockEntity.Block3.getBlock()));
            if(!player.isCreative()) {
                dropResources(blockEntity.Block3, level, pos, blockEntity, player, stack);
            }

            blockEntity.Block3 = Blocks.AIR.defaultBlockState();
            blockEntity.numSubBlocks = 2;
            blockEntity.setChanged();
            blockEntity.updateModelData(level, pos);
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

        }
        else if(!blockEntity.Block2.isAir()){

            player.awardStat(Stats.BLOCK_MINED.get(blockEntity.Block2.getBlock()));
            if(!player.isCreative()) {
                dropResources(blockEntity.Block2, level, pos, blockEntity, player, stack);
            }

            BlockState newState = blockEntity.Block1;

            level.setBlockAndUpdate(pos, newState);

        }

    }

    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid){
        this.playerWillDestroy(level, pos, state, player);
        return true;
    }

    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {

    }

    public boolean dropFromExplosion(Explosion p_49826_) {
        return false;
    }


    protected void updateBlock(CombinedBlockEntity blockEntity, Level level, BlockPos pos, SoundType soundType, Player player, InteractionHand hand, boolean use){
        level.markAndNotifyBlock(pos, level.getChunkAt(pos), level.getBlockState(pos), level.getBlockState(pos), 3, 512);

        level.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);

        if(!player.isCreative() && use) {
            player.getItemInHand(hand).shrink(1);
        }
    }


    //TODO: fix render after placement

    public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState state2, boolean bool) {

        level.setBlockAndUpdate(pos, state);

        if(level.getBlockEntity(pos) instanceof CombinedBlockEntity){
            LogHelper.info(level.getBlockEntity(pos));
            ((CombinedBlockEntity) Objects.requireNonNull(level.getBlockEntity(pos))).updateModelData(level, pos);
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), state2, state, 3, 512);
        }

    }

    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {

        if(!(level.getBlockEntity(pos) instanceof CombinedBlockEntity)){
            return InteractionResult.PASS;
        }

        Item heldItem = player.getItemInHand(hand).getItem();
        if(heldItem == Items.AIR){
            return InteractionResult.PASS;
        }


        CombinedBlockEntity be = (CombinedBlockEntity) level.getBlockEntity(pos);

        if(be.numSubBlocks == 2){

            if(be.Block1.getBlock() instanceof SlabBlock && (!(be.Block1.getBlock() instanceof StairBlock)) && (!(be.Block1.getBlock() instanceof QuarterBlock))){
                if(be.Block2.getBlock() instanceof QuarterBlock){
                    //Block1: Slab, Block2: Quarter
                    if(getBlockFromItem(heldItem) instanceof QuarterBlock){
                        for(Direction dir : Direction.values()){
                            BlockState quarterState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, false);
                            BlockState invState = quarterState.setValue(INVERTED, true);
                            if(Shapes.or(state.getShape(level, pos), quarterState.getShape(level, pos)) == ShapeUtil.fullBlock){

                                be.Block3 = quarterState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData(level, pos);

                                updateBlock(be, level, pos, quarterState.getSoundType(), player, hand, true);

                                return InteractionResult.SUCCESS;
                            }
                            else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == ShapeUtil.fullBlock){

                                be.Block3 = invState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData(level, pos);

                                updateBlock(be, level, pos, invState.getSoundType(), player, hand, true);

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
                            if(Shapes.or(state.getShape(level, pos), quarterState.getShape(level, pos)) == ShapeUtil.fullBlock){

                                be.Block3 = quarterState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData(level, pos);

                                updateBlock(be, level, pos, quarterState.getSoundType(), player, hand, true);

                                return InteractionResult.SUCCESS;
                            }
                            else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == ShapeUtil.fullBlock){

                                be.Block3 = invState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData(level, pos);

                                updateBlock(be, level, pos, invState.getSoundType(), player, hand, true);

                                return InteractionResult.SUCCESS;

                            }
                        }
                    }
                }
                else if(be.Block2.getBlock() instanceof QuarterBlock){
                    //Block1: Quarter, Block2: Quarter
                    if(getBlockFromItem(heldItem) instanceof QuarterBlock){

                        boolean NEU = false, NED = false, NWU = false, NWD = false, SEU = false, SED = false, SWU = false, SWD = false;

                        NEU = ShapeUtil.occupiesPart(ShapeUtil.getPart("NEU").shape, state, level, pos);
                        NED = ShapeUtil.occupiesPart(ShapeUtil.getPart("NED").shape, state, level, pos);
                        NWU = ShapeUtil.occupiesPart(ShapeUtil.getPart("NWU").shape, state, level, pos);
                        NWD = ShapeUtil.occupiesPart(ShapeUtil.getPart("NWD").shape, state, level, pos);
                        SEU = ShapeUtil.occupiesPart(ShapeUtil.getPart("SEU").shape, state, level, pos);
                        SED = ShapeUtil.occupiesPart(ShapeUtil.getPart("SED").shape, state, level, pos);
                        SWU = ShapeUtil.occupiesPart(ShapeUtil.getPart("SWU").shape, state, level, pos);
                        SWD = ShapeUtil.occupiesPart(ShapeUtil.getPart("SWD").shape, state, level, pos);

                        BlockState placeState = null;

                        for(Direction dir : Direction.values()){
                            BlockState invState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, true);

                            if(NEU && ShapeUtil.occupiesPart(ShapeUtil.getPart("NEU").shape, invState, level, pos)){
                                continue;
                            }
                            if(NED && ShapeUtil.occupiesPart(ShapeUtil.getPart("NED").shape, invState, level, pos)){
                                continue;
                            }
                            if(NWU && ShapeUtil.occupiesPart(ShapeUtil.getPart("NWU").shape, invState, level, pos)){
                                continue;
                            }
                            if(NWD && ShapeUtil.occupiesPart(ShapeUtil.getPart("NWD").shape, invState, level, pos)){
                                continue;
                            }
                            if(SEU && ShapeUtil.occupiesPart(ShapeUtil.getPart("SEU").shape, invState, level, pos)){
                                continue;
                            }
                            if(SED && ShapeUtil.occupiesPart(ShapeUtil.getPart("SED").shape, invState, level, pos)){
                                continue;
                            }
                            if(SWU && ShapeUtil.occupiesPart(ShapeUtil.getPart("SWU").shape, invState, level, pos)){
                                continue;
                            }
                            if(SWD && ShapeUtil.occupiesPart(ShapeUtil.getPart("SWD").shape, invState, level, pos)){
                                continue;
                            }
                            placeState = invState;

                        }

                        for(Direction dir : Direction.values()) {
                            BlockState quarterState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, false);

                            if (NEU && ShapeUtil.occupiesPart(ShapeUtil.getPart("NEU").shape, quarterState, level, pos)) {
                                continue;
                            }
                            if (NED && ShapeUtil.occupiesPart(ShapeUtil.getPart("NED").shape, quarterState, level, pos)) {
                                continue;
                            }
                            if (NWU && ShapeUtil.occupiesPart(ShapeUtil.getPart("NWU").shape, quarterState, level, pos)) {
                                continue;
                            }
                            if (NWD && ShapeUtil.occupiesPart(ShapeUtil.getPart("NWD").shape, quarterState, level, pos)) {
                                continue;
                            }
                            if (SEU && ShapeUtil.occupiesPart(ShapeUtil.getPart("SEU").shape, quarterState, level, pos)) {
                                continue;
                            }
                            if (SED && ShapeUtil.occupiesPart(ShapeUtil.getPart("SED").shape, quarterState, level, pos)) {
                                continue;
                            }
                            if (SWU && ShapeUtil.occupiesPart(ShapeUtil.getPart("SWU").shape, quarterState, level, pos)) {
                                continue;
                            }
                            if (SWD && ShapeUtil.occupiesPart(ShapeUtil.getPart("SWD").shape, quarterState, level, pos)) {
                                continue;
                            }
                            placeState = quarterState;

                        }

                        if(placeState != null){

                            be.Block3 = placeState;
                            be.numSubBlocks = 3;
                            be.setChanged();
                            be.updateModelData(level, pos);

                            updateBlock(be, level, pos, placeState.getSoundType(), player, hand, true);

                            return InteractionResult.SUCCESS;

                        }

                    }
                    else if(getBlockFromItem(heldItem) instanceof SlabBlock && !(getBlockFromItem(heldItem) instanceof StairBlock)){
                        for(Direction dir : Direction.values()){
                            BlockState slabState = getBlockFromItem(heldItem).defaultBlockState().setValue(FACING, dir).setValue(INVERTED, false);
                            BlockState invState = slabState.setValue(INVERTED, true);
                            if(Shapes.or(state.getShape(level, pos), slabState.getShape(level, pos)) == ShapeUtil.fullBlock){

                                be.Block3 = slabState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData(level, pos);

                                updateBlock(be, level, pos, slabState.getSoundType(), player, hand, true);

                                return InteractionResult.SUCCESS;
                            }
                            else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == ShapeUtil.fullBlock){

                                be.Block3 = invState;
                                be.numSubBlocks = 3;
                                be.setChanged();
                                be.updateModelData(level, pos);

                                updateBlock(be, level, pos, invState.getSoundType(), player, hand, true);

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
                        if(Shapes.or(state.getShape(level, pos), quarterState.getShape(level, pos)) == ShapeUtil.fullBlock){

                            be.Block4 = quarterState;
                            be.numSubBlocks = 4;
                            be.setChanged();
                            be.updateModelData(level, pos);

                            updateBlock(be, level, pos, quarterState.getSoundType(), player, hand, true);

                            return InteractionResult.SUCCESS;
                        }
                        else if(Shapes.or(state.getShape(level, pos), invState.getShape(level, pos)) == ShapeUtil.fullBlock){

                            be.Block4 = invState;
                            be.numSubBlocks = 4;
                            be.setChanged();
                            be.updateModelData(level, pos);

                            updateBlock(be, level, pos, invState.getSoundType(), player, hand, true);

                            return InteractionResult.SUCCESS;

                        }
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

}