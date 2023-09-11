package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import static com.xpmodder.slabsandstairs.utility.Util.getBlockFromItem;
import static net.minecraft.core.Direction.*;

public class QuarterBlock extends SlabBlock {

    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_WEST = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_EAST = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH_INV = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    protected static final VoxelShape SHAPE_WEST_INV = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_EAST_INV = Block.box(0.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH_INV = Block.box(0.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_UP_INV = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_DOWN_INV = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);


    public QuarterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(INVERTED, false));
    }

    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_, @NotNull CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)) {
            case EAST -> (state.getValue(INVERTED) ? SHAPE_EAST_INV : SHAPE_EAST);
            case WEST -> (state.getValue(INVERTED) ? SHAPE_WEST_INV : SHAPE_WEST);
            case SOUTH -> (state.getValue(INVERTED) ? SHAPE_SOUTH_INV : SHAPE_SOUTH);
            case UP -> (state.getValue(INVERTED) ? SHAPE_UP_INV : SHAPE_UP);
            case DOWN -> (state.getValue(INVERTED) ? SHAPE_DOWN_INV : SHAPE_DOWN);
            default -> (state.getValue(INVERTED) ? SHAPE_NORTH_INV : SHAPE_NORTH);
        };
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, INVERTED);
    }

    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {

        Block SlabQuarterBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.SlabQuarterBlock));
        Block StairBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.StairBlock));

        if(SlabQuarterBlock == Blocks.AIR || SlabQuarterBlock == null){
            LogHelper.error("Got Air Block as SlabQuarterBlock!");
            return InteractionResult.PASS;
        }
        if(StairBlock == Blocks.AIR || StairBlock == null){
            LogHelper.error("Got Air Block as StairBlock!");
            return InteractionResult.PASS;
        }

        Item heldItem = player.getItemInHand(handIn).getItem();
        if(this == getBlockFromItem(heldItem)){
            if(getBlockFromItem(heldItem) instanceof QuarterBlock){
                //Quarter (in world) right-clicked with Quarter (in hand)

                BlockState slabState = SlabQuarterBlock.defaultBlockState();

                if(state.getValue(FACING) == UP){
                    if(state.getValue(INVERTED)){
                        if(hit.getDirection() == WEST || hit.getDirection() == EAST){
                            slabState = slabState.setValue(FACING, UP);
                        }
                        else if(hit.getDirection() == UP || hit.getDirection() == DOWN){
                            slabState = slabState.setValue(FACING, WEST);
                        }
                        else{
                            slabState = slabState.setValue(FACING, UP);
                        }
                    }
                    else{
                        if(hit.getDirection() == EAST || hit.getDirection() == WEST){
                            slabState = slabState.setValue(FACING, UP);
                        }
                        else if(hit.getDirection() == UP || hit.getDirection() == DOWN){
                            slabState = slabState.setValue(FACING, EAST);
                        }
                        else{
                            slabState = slabState.setValue(FACING, UP);
                        }
                    }
                }
                else if(state.getValue(FACING) == DOWN){
                    if(state.getValue(INVERTED)){
                        if(hit.getDirection() == WEST || hit.getDirection() == EAST){
                            slabState = slabState.setValue(FACING, DOWN);
                        }
                        else if(hit.getDirection() == DOWN || hit.getDirection() == UP){
                            slabState = slabState.setValue(FACING, WEST);
                        }
                        else{
                            slabState = slabState.setValue(FACING, DOWN);
                        }
                    }
                    else{
                        if(hit.getDirection() == EAST || hit.getDirection() == WEST){
                            slabState = slabState.setValue(FACING, DOWN);
                        }
                        else if(hit.getDirection() == DOWN || hit.getDirection() ==UP){
                            slabState = slabState.setValue(FACING, EAST);
                        }
                        else{
                            slabState = slabState.setValue(FACING, DOWN);
                        }
                    }
                }
                else if(state.getValue(FACING) == NORTH){
                    if(state.getValue(INVERTED)) {
                        if (hit.getDirection() == NORTH || hit.getDirection() == SOUTH) {
                            slabState = slabState.setValue(FACING, DOWN);
                        } else if (hit.getDirection() == DOWN || hit.getDirection() == UP) {
                            slabState = slabState.setValue(FACING, NORTH);
                        } else {
                            slabState = slabState.setValue(FACING, DOWN);
                        }
                    }
                    else{
                        if (hit.getDirection() == WEST || hit.getDirection() == EAST) {
                            slabState = slabState.setValue(FACING, NORTH);
                        } else if (hit.getDirection() == NORTH || hit.getDirection() == SOUTH) {
                            slabState = slabState.setValue(FACING, WEST);
                        } else {
                            slabState = slabState.setValue(FACING, NORTH);
                        }
                    }
                }
                else if(state.getValue(FACING) == EAST){
                    if(state.getValue(INVERTED)) {
                        if (hit.getDirection() == NORTH || hit.getDirection() == SOUTH) {
                            slabState = slabState.setValue(FACING, UP);
                        } else if (hit.getDirection() == UP || hit.getDirection() == DOWN) {
                            slabState = slabState.setValue(FACING, NORTH);
                        } else {
                            slabState = slabState.setValue(FACING, UP);
                        }
                    }
                    else{
                        if (hit.getDirection() == EAST || hit.getDirection() == WEST) {
                            slabState = slabState.setValue(FACING, NORTH);
                        } else if (hit.getDirection() == NORTH || hit.getDirection() == SOUTH) {
                            slabState = slabState.setValue(FACING, EAST);
                        } else {
                            slabState = slabState.setValue(FACING, NORTH);
                        }
                    }
                }
                else if(state.getValue(FACING) == SOUTH){
                    if(state.getValue(INVERTED)) {
                        if (hit.getDirection() == SOUTH || hit.getDirection() == NORTH) {
                            slabState = slabState.setValue(FACING, UP);
                        } else if (hit.getDirection() == UP || hit.getDirection() == DOWN) {
                            slabState = slabState.setValue(FACING, SOUTH);
                        } else {
                            slabState = slabState.setValue(FACING, UP);
                        }
                    }
                    else{
                        if (hit.getDirection() == SOUTH || hit.getDirection() == NORTH) {
                            slabState = slabState.setValue(FACING, EAST);
                        } else if (hit.getDirection() == EAST || hit.getDirection() == WEST) {
                            slabState = slabState.setValue(FACING, SOUTH);
                        } else {
                            slabState = slabState.setValue(FACING, SOUTH);
                        }
                    }
                }
                else {  //WEST
                    if(state.getValue(INVERTED)) {
                        if (hit.getDirection() == SOUTH || hit.getDirection() == NORTH) {
                            slabState = slabState.setValue(FACING, DOWN);
                        } else if (hit.getDirection() == DOWN || hit.getDirection() == UP) {
                            slabState = slabState.setValue(FACING, SOUTH);
                        } else {
                            slabState = slabState.setValue(FACING, DOWN);
                        }
                    }
                    else{
                        if (hit.getDirection() == SOUTH || hit.getDirection() == NORTH) {
                            slabState = slabState.setValue(FACING, WEST);
                        } else if (hit.getDirection() == WEST || hit.getDirection() == EAST) {
                            slabState = slabState.setValue(FACING, SOUTH);
                        } else {
                            slabState = slabState.setValue(FACING, SOUTH);
                        }
                    }
                }

                worldIn.setBlockAndUpdate(pos, slabState.setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
                SoundType soundType = SlabQuarterBlock.defaultBlockState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);
                if(!player.isCreative()) {
                    player.getItemInHand(handIn).shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        else if(SlabQuarterBlock == getBlockFromItem(heldItem)){
            if(SlabQuarterBlock instanceof SlabBlock){
                //Quarter Block (in world), right-clicked with slab (in hand)

                BlockState stairState = StairBlock.defaultBlockState();

                switch(state.getValue(FACING)){
                    case UP:
                        if(state.getValue(INVERTED)){
                            if(hit.getDirection() == WEST || hit.getDirection() == EAST){
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                            else if(hit.getDirection() == UP || hit.getDirection() == DOWN){
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                            else{
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                        }
                        else{
                            if(hit.getDirection() == EAST || hit.getDirection() == WEST){
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                            else if(hit.getDirection() == UP || hit.getDirection() == DOWN){
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                            else{
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                        }
                        break;

                    case DOWN:
                        if(state.getValue(INVERTED)){
                            if(hit.getDirection() == WEST || hit.getDirection() == EAST){
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                            else if(hit.getDirection() == DOWN || hit.getDirection() == UP){
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                            else{
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                        }
                        else{
                            if(hit.getDirection() == EAST || hit.getDirection() == WEST){
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                            else if(hit.getDirection() == DOWN || hit.getDirection() == UP){
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                            else{
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                        }
                        break;

                    case NORTH:
                        if(state.getValue(INVERTED)){
                            if(hit.getDirection() == NORTH || hit.getDirection() == SOUTH){
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                            else if(hit.getDirection() == DOWN || hit.getDirection() == UP){
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                            else{
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                        }
                        else{
                            if(hit.getDirection() == WEST || hit.getDirection() == EAST){
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                            else if(hit.getDirection() == NORTH || hit.getDirection() == SOUTH){
                                stairState = stairState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                            else{
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                        }
                        break;

                    case EAST:
                        if(state.getValue(INVERTED)){
                            if(hit.getDirection() == NORTH || hit.getDirection() == SOUTH){
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                            else if(hit.getDirection() == UP || hit.getDirection() == DOWN){
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                            else{
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                        }
                        else{
                            if(hit.getDirection() == EAST || hit.getDirection() == WEST){
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                            else if(hit.getDirection() == NORTH || hit.getDirection() == SOUTH){
                                stairState = stairState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                            else{
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                        }
                        break;

                    case SOUTH:
                        if(state.getValue(INVERTED)){
                            if(hit.getDirection() == SOUTH || hit.getDirection() == NORTH){
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                            else if(hit.getDirection() == UP || hit.getDirection() == DOWN){
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                            else{
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                        }
                        else{
                            if(hit.getDirection() == SOUTH || hit.getDirection() == NORTH){
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                            else if(hit.getDirection() == EAST || hit.getDirection() == WEST){
                                stairState = stairState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                            else{
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                        }
                        break;

                    case WEST:
                        if(state.getValue(INVERTED)){
                            if(hit.getDirection() == SOUTH || hit.getDirection() == NORTH){
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                            else if(hit.getDirection() == DOWN || hit.getDirection() == UP){
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                            else{
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                        }
                        else{
                            if(hit.getDirection() == SOUTH || hit.getDirection() == NORTH){
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                            else if(hit.getDirection() == WEST || hit.getDirection() == EAST){
                                stairState = stairState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                            else{
                                stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                        }
                }

                worldIn.setBlockAndUpdate(pos, stairState.setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
                SoundType soundType = StairBlock.defaultBlockState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);
                if(!player.isCreative()) {
                    player.getItemInHand(handIn).shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        else if(StairBlock == getBlockFromItem(heldItem)){
            if(StairBlock instanceof StairBlock){
                //Quarter (in world) right-clicked with stair (in hand)

                Block base = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock));
                if(base == null){
                    LogHelper.error("Error: Could not get Base Block for " + ForgeRegistries.BLOCKS.getKey(this));
                    return InteractionResult.PASS;
                }

                worldIn.setBlockAndUpdate(pos, base.defaultBlockState());
                SoundType soundType = base.defaultBlockState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);
                if(!player.isCreative()) {
                    player.getItemInHand(handIn).shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }


        else if(getBlockFromItem(heldItem) instanceof SlabBlock && !(getBlockFromItem(heldItem) instanceof StairBlock) && !(getBlockFromItem(heldItem) instanceof QuarterBlock)){
            //Holding a slab - any slab
            BlockState slabState = getBlockFromItem(heldItem).defaultBlockState();
            Direction hitDirection = hit.getDirection();

            boolean shouldPlace = false;
            boolean invertDir = true;

            switch(state.getValue(FACING)){

                case NORTH:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == NORTH || hitDirection == DOWN){
                            shouldPlace = true;
                        }
                        else if(hitDirection == SOUTH || hitDirection == UP){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    else{
                        if(hitDirection == WEST || hitDirection == NORTH){
                            shouldPlace = true;
                        }
                        else if(hitDirection == EAST || hitDirection == SOUTH){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    break;
                case EAST:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == UP || hitDirection == NORTH){
                            shouldPlace = true;
                        }
                        else if(hitDirection == DOWN || hitDirection == SOUTH){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    else{
                        if(hitDirection == NORTH || hitDirection == EAST){
                            shouldPlace = true;
                        }
                        else if(hitDirection == SOUTH || hitDirection == WEST){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    break;
                case SOUTH:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == UP || hitDirection == SOUTH){
                            shouldPlace = true;
                        }
                        else if(hitDirection == DOWN || hitDirection == NORTH){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    else{
                        if(hitDirection == EAST || hitDirection == SOUTH){
                            shouldPlace = true;
                        }
                        else if(hitDirection == WEST || hitDirection == NORTH){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    break;
                case WEST:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == DOWN || hitDirection == SOUTH){
                            shouldPlace = true;
                        }
                        else if(hitDirection == UP || hitDirection == NORTH){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    else{
                        if(hitDirection == SOUTH || hitDirection == WEST){
                            shouldPlace = true;
                        }
                        else if(hitDirection == NORTH || hitDirection == EAST){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    break;
                case UP:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == UP || hitDirection == WEST){
                            shouldPlace = true;
                        }
                        else if(hitDirection == DOWN || hitDirection == EAST){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    else{
                        if(hitDirection == UP || hitDirection == EAST){
                            shouldPlace = true;
                        }
                        else if(hitDirection == DOWN || hitDirection == WEST){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    break;
                case DOWN:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == DOWN || hitDirection == WEST){
                            shouldPlace = true;
                        }
                        else if(hitDirection == UP || hitDirection == EAST){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    else{
                        if(hitDirection == DOWN || hitDirection == EAST){
                            shouldPlace = true;
                        }
                        else if(hitDirection == UP || hitDirection == WEST){
                            invertDir = false;
                            shouldPlace = true;
                        }
                    }
                    break;
            }

            if(!shouldPlace){
                return InteractionResult.PASS;
            }

            slabState = slabState.setValue(FACING, (invertDir ? hitDirection.getOpposite() : hitDirection)).setValue(INVERTED, true);

            BlockState combinedState = BlockInit.combinedBlock.get().defaultBlockState();

            worldIn.setBlockAndUpdate(pos, combinedState);
            if(worldIn.getBlockEntity(pos) == null){
                return InteractionResult.PASS;
            }
            CombinedBlockEntity be = ((CombinedBlockEntity) worldIn.getBlockEntity(pos));
            be.numSubBlocks = 2;
            be.Block1 = state;
            be.Block2 = slabState;
            be.updateModelData(worldIn, pos);

            SoundType sound = slabState.getSoundType();
            worldIn.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);
            if(!player.isCreative()) {
                player.getItemInHand(handIn).shrink(1);
            }
            worldIn.markAndNotifyBlock(pos, worldIn.getChunkAt(pos), combinedState, combinedState, 3, 512);

            return InteractionResult.SUCCESS;

        }


        else if(getBlockFromItem(heldItem) instanceof StairBlock){

            BlockState stairState = getBlockFromItem(heldItem).defaultBlockState();

            switch(state.getValue(FACING)){

                case NORTH:
                    if(state.getValue(INVERTED)){
                        stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                    }
                    else{
                        stairState = stairState.setValue(FACING, UP).setValue(INVERTED, true);
                    }
                    break;

                case EAST:
                    if(state.getValue(INVERTED)){
                        stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, true);
                    }
                    else{
                        stairState = stairState.setValue(FACING, UP).setValue(INVERTED, false);
                    }
                    break;

                case SOUTH:
                    if(state.getValue(INVERTED)){
                        stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                    }
                    else{
                        stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, false);
                    }
                    break;

                case WEST:
                    if(state.getValue(INVERTED)){
                        stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                    }
                    else{
                        stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, true);
                    }
                    break;

                case UP:
                    if(state.getValue(INVERTED)){
                        stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                    }
                    else{
                        stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                    }
                    break;

                case DOWN:
                    if(state.getValue(INVERTED)){
                        stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                    }
                    else{
                        stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                    }
                    break;
            }


            BlockState combinedState = BlockInit.combinedBlock.get().defaultBlockState();

            worldIn.setBlockAndUpdate(pos, combinedState);
            if(worldIn.getBlockEntity(pos) == null){
                return InteractionResult.PASS;
            }
            CombinedBlockEntity be = ((CombinedBlockEntity) worldIn.getBlockEntity(pos));
            be.numSubBlocks = 2;
            be.Block1 = state;
            be.Block2 = stairState;
            be.updateModelData(worldIn, pos);

            SoundType sound = stairState.getSoundType();
            worldIn.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);
            if(!player.isCreative()) {
                player.getItemInHand(handIn).shrink(1);
            }
            worldIn.markAndNotifyBlock(pos, worldIn.getChunkAt(pos), combinedState, combinedState, 3, 512);

            return InteractionResult.SUCCESS;


        }


        else if(getBlockFromItem(heldItem) instanceof QuarterBlock){

            BlockState quarterState = getBlockFromItem(heldItem).defaultBlockState();
            Direction hitDirection = hit.getDirection();
            Direction playerDirection = player.getDirection();

            switch(state.getValue(FACING)){

                case NORTH:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == NORTH|| hitDirection == SOUTH){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                        }
                        else if(hitDirection == UP|| hitDirection == DOWN){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                        }
                    }
                    else{
                        if(hitDirection == NORTH|| hitDirection == SOUTH){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                        }
                        else if(hitDirection == WEST|| hitDirection == EAST){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                        }
                    }
                    break;

                case EAST:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == NORTH|| hitDirection == SOUTH){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                        }
                        else if(hitDirection == UP|| hitDirection == DOWN){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, true);
                        }
                    }
                    else{
                        if(hitDirection == NORTH|| hitDirection == SOUTH){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                        }
                        else if(hitDirection == EAST|| hitDirection == WEST){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, false);
                        }
                    }
                    break;

                case SOUTH:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == SOUTH|| hitDirection == NORTH){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                        }
                        else if(hitDirection == UP|| hitDirection == DOWN){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, true);
                        }
                    }
                    else{
                        if(hitDirection == SOUTH|| hitDirection == NORTH){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                        }
                        else if(hitDirection == EAST|| hitDirection == WEST){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, false);
                        }
                    }
                    break;

                case WEST:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == DOWN|| hitDirection == UP){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                        }
                        else if(hitDirection == SOUTH|| hitDirection == NORTH){
                            if(playerDirection == EAST){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                            else if(playerDirection == WEST){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, true);
                        }
                    }
                    else{
                        if(hitDirection == SOUTH|| hitDirection == NORTH){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                        }
                        else if(hitDirection == WEST|| hitDirection == EAST){
                            if(hit.getLocation().y() - hit.getBlockPos().getY() > 0.7){
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                            else if(hit.getLocation().y() - hit.getBlockPos().getY() < 0.3){
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, false);
                        }
                    }
                    break;

                case UP:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == UP|| hitDirection == DOWN){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                        }
                        else if(hitDirection == WEST|| hitDirection == EAST){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, false);
                        }
                    }
                    else{
                        if(hitDirection == EAST|| hitDirection == WEST){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                        }
                        else if(hitDirection == UP|| hitDirection == DOWN){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, true);
                        }
                    }
                    break;

                case DOWN:
                    if(state.getValue(INVERTED)){
                        if(hitDirection == DOWN|| hitDirection == UP){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, true);
                            }
                        }
                        else if(hitDirection == WEST|| hitDirection == EAST){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, false);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, false);
                        }
                    }
                    else{
                        if(hitDirection == DOWN|| hitDirection == UP){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, EAST).setValue(INVERTED, true);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, false);
                            }
                        }
                        else if(hitDirection == EAST|| hitDirection == WEST){
                            if(playerDirection == NORTH){
                                quarterState = quarterState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                            else if(playerDirection == SOUTH){
                                quarterState = quarterState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                            else{
                                quarterState = quarterState.setValue(FACING, DOWN).setValue(INVERTED, true);
                            }
                        }
                        else{
                            quarterState = quarterState.setValue(FACING, UP).setValue(INVERTED, true);
                        }
                    }
                    break;

            }

            BlockState combinedState = BlockInit.combinedBlock.get().defaultBlockState();

            worldIn.setBlockAndUpdate(pos, combinedState);
            if(worldIn.getBlockEntity(pos) == null){
                return InteractionResult.PASS;
            }
            CombinedBlockEntity be = ((CombinedBlockEntity) worldIn.getBlockEntity(pos));
            be.numSubBlocks = 2;
            be.Block1 = state;
            be.Block2 = quarterState;
            be.updateModelData(worldIn, pos);

            SoundType sound = quarterState.getSoundType();
            worldIn.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, sound.volume, sound.pitch);
            if(!player.isCreative()) {
                player.getItemInHand(handIn).shrink(1);
            }
            worldIn.markAndNotifyBlock(pos, worldIn.getChunkAt(pos), combinedState, combinedState, 3, 512);

            return InteractionResult.SUCCESS;

        }


        return InteractionResult.PASS;
    }


}
