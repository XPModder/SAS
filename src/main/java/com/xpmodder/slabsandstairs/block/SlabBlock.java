package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.init.KeyInit;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import com.xpmodder.slabsandstairs.utility.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import static com.xpmodder.slabsandstairs.block.StairBlock.INVERTED;
import static com.xpmodder.slabsandstairs.utility.Util.getBlockFromItem;
import static net.minecraft.core.Direction.*;

public class SlabBlock extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_EAST = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    protected String BaseBlock = Blocks.AIR.getRegistryName().toString();
    protected String SlabQuarterBlock = Blocks.AIR.getRegistryName().toString();
    protected String StairBlock = Blocks.AIR.getRegistryName().toString();

    protected static Direction placementRotation = null;

    protected static boolean placementInverted = false;


    public SlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, NORTH).setValue(WATERLOGGED, false).setValue(INVERTED, false));
    }

    public String getBaseBlock(){
        return this.BaseBlock;
    }

    public void setReferenceBlocks(String baseBlock, String slabQuarterBlock, String stairBlock){
        this.BaseBlock = baseBlock;
        this.SlabQuarterBlock = slabQuarterBlock;
        this.StairBlock = stairBlock;
    }





    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        Item heldItem = player.getItemInHand(handIn).getItem();

        Block SlabQuarterBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.SlabQuarterBlock));
        Block StairBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.StairBlock));

        if(SlabQuarterBlock == Blocks.AIR){
            LogHelper.error("Got Air Block as SlabQuarterBlock!");
            return InteractionResult.PASS;
        }
        if(StairBlock == Blocks.AIR){
            LogHelper.error("Got Air Block as StairBlock!");
            return InteractionResult.PASS;
        }

        if(this == getBlockFromItem(heldItem)){
            if(getBlockFromItem(heldItem) instanceof SlabBlock){
                Block base = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock));
                if(base == null){
                    LogHelper.error("Error: Could not get Base Block for " + this.getRegistryName());
                    return InteractionResult.PASS;
                }
                worldIn.setBlockAndUpdate(pos, base.defaultBlockState());
                SoundType soundType = base.defaultBlockState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);
                return InteractionResult.SUCCESS;
            }
        }
        else if(SlabQuarterBlock == getBlockFromItem(heldItem)){
            if(SlabQuarterBlock instanceof QuarterBlock){

                if(StairBlock == null){
                    LogHelper.error("Error: Unable to get stair block for " + this.getRegistryName());
                    return InteractionResult.PASS;
                }

                BlockState stairState = StairBlock.defaultBlockState();

                switch(state.getValue(FACING)){
                    case NORTH:
                        if(hit.getDirection() == SOUTH || hit.getDirection() == NORTH) {
                            //Was the top half clicked?
                            if (hit.getLocation().y - (double) hit.getBlockPos().getY() > 0.5D) {
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                            } else {
                                stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                            }
                        }
                        else if(hit.getDirection() == UP){
                            stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                        }
                        else if(hit.getDirection() == DOWN){
                            stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == EAST){
                            stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, false);
                        }
                        else{   //WEST
                            stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, true);
                        }
                        break;
                    case EAST:
                        if(hit.getDirection() == WEST || hit.getDirection() == EAST){
                            //Was the top half clicked?
                            if (hit.getLocation().y - (double) hit.getBlockPos().getY() > 0.5D) {
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                            else{
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                        }
                        else if(hit.getDirection() == UP){
                            stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                        }
                        else if(hit.getDirection() == DOWN){
                            stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == NORTH){
                            stairState = stairState.setValue(FACING, UP).setValue(INVERTED, true);
                        }
                        else {   //SOUTH
                            stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, true);
                        }
                        break;
                    case SOUTH:
                        if(hit.getDirection() == NORTH || hit.getDirection() == SOUTH) {
                            //Was the top half clicked?
                            if (hit.getLocation().y - (double) hit.getBlockPos().getY() > 0.5D) {
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, true);
                            } else {
                                stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                            }
                        }
                        else if(hit.getDirection() == UP){
                            stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, true);
                        }
                        else if(hit.getDirection() == DOWN){
                            stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == EAST){
                            stairState = stairState.setValue(FACING, UP).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == WEST){
                            stairState = stairState.setValue(FACING, UP).setValue(INVERTED, true);
                        }
                        break;
                    case WEST:
                        if(hit.getDirection() == EAST || hit.getDirection() == WEST) {
                            //Was the top half clicked?
                            if (hit.getLocation().y - (double) hit.getBlockPos().getY() > 0.5D) {
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                            } else {
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                            }
                        }
                        else if(hit.getDirection() == UP){
                            stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                        }
                        else if(hit.getDirection() == DOWN){
                            stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == NORTH){
                            stairState = stairState.setValue(FACING, UP).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == SOUTH){
                            stairState = stairState.setValue(FACING, DOWN).setValue(INVERTED, false);
                        }
                        break;
                    case UP:
                        if(hit.getDirection() == UP || hit.getDirection() == DOWN) {
                            //Was the back half clicked?
                            if (hit.getLocation().x - (double) hit.getBlockPos().getX() > 0.5D) {
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                            } else {
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                            }
                        }
                        else if(hit.getDirection() == EAST){
                            stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == WEST){
                            stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == NORTH){
                            stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, false);
                        }
                        else if(hit.getDirection() == SOUTH){
                            stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, false);
                        }
                        break;
                    case DOWN:
                        if(hit.getDirection() == UP || hit.getDirection() == DOWN) {
                            //Was the back half clicked?
                            if (hit.getLocation().x - (double) hit.getBlockPos().getX() > 0.5D) {
                                stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                            } else {
                                stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                            }
                        }
                        else if(hit.getDirection() == EAST){
                            stairState = stairState.setValue(FACING, EAST).setValue(INVERTED, true);
                        }
                        else if(hit.getDirection() == WEST){
                            stairState = stairState.setValue(FACING, WEST).setValue(INVERTED, true);
                        }
                        else if(hit.getDirection() == NORTH){
                            stairState = stairState.setValue(FACING, NORTH).setValue(INVERTED, true);
                        }
                        else if(hit.getDirection() == SOUTH){
                            stairState = stairState.setValue(FACING, SOUTH).setValue(INVERTED, true);
                        }
                        break;
                    default:

                }
                worldIn.setBlockAndUpdate(pos, stairState.setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
                SoundType soundType = stairState.getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    protected void rotatePlacement(){
        if(placementRotation == DOWN){
            placementInverted = !placementInverted;
        }
        placementRotation = Util.nextDirection(placementRotation);
    }


    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        Direction direction = context.getNearestLookingDirection().getOpposite();
        BlockPos blockPos = context.getClickedPos();

        if(placementRotation == null){
            placementRotation = direction;
        }

        if(KeyInit.placementModeMapping.isDown()){
            if(KeyInit.placementRotateMapping.consumeClick()){
                this.rotatePlacement();
            }
            if(this.defaultBlockState().hasProperty(INVERTED)){
                return this.defaultBlockState().setValue(FACING, placementRotation).setValue(INVERTED, placementInverted).setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
            }
            return this.defaultBlockState().setValue(FACING, placementRotation).setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
        }

        if((context.getClickLocation().y - (double)blockPos.getY()) > 0.5D){
            return this.defaultBlockState().setValue(FACING, DOWN).setValue(WATERLOGGED, fluidState.is(Fluids.WATER)).setValue(INVERTED, false);
        }
        else{
            return this.defaultBlockState().setValue(FACING, UP).setValue(WATERLOGGED, fluidState.is(Fluids.WATER)).setValue(INVERTED, false);
        }
    }


    public @NotNull VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)) {
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            case SOUTH -> SHAPE_SOUTH;
            case UP -> SHAPE_UP;
            case DOWN -> SHAPE_DOWN;
            default -> SHAPE_NORTH;
        };
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, INVERTED);
    }

    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

}
