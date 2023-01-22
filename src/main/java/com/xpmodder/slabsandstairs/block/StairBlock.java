package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class StairBlock extends SlabBlock{

    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final EnumProperty<StairsShape> CONNECTED = BlockStateProperties.STAIRS_SHAPE;

    protected static final VoxelShape NWD_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
    protected static final VoxelShape SWD_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape NWU_CORNER = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
    protected static final VoxelShape SWU_CORNER = Block.makeCuboidShape(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape NED_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    protected static final VoxelShape SED_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape NEU_CORNER = Block.makeCuboidShape(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SEU_CORNER = Block.makeCuboidShape(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);


    public StairBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(INVERTED, false).with(CONNECTED, StairsShape.STRAIGHT));
    }

    //Creates the correct voxel shape for the hitbox based on the orientation of the stairs
    public VoxelShape makeShape(Direction direction, boolean inverted, StairsShape connected){

        VoxelShape shape;

        switch (direction){

            case NORTH:
                if(inverted){

                    shape = VoxelShapes.or(NWU_CORNER, NEU_CORNER);
                    shape = VoxelShapes.or(shape, SWU_CORNER);
                    shape = VoxelShapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, NWD_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, NED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, SED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, SWD_CORNER);
                    }

                }
                else{

                    shape = VoxelShapes.or(NWD_CORNER, NED_CORNER);
                    shape = VoxelShapes.or(shape, SWD_CORNER);
                    shape = VoxelShapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, NWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, NEU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, SEU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, SWU_CORNER);
                    }

                }
                break;
            case EAST:
                if(inverted){

                    shape = VoxelShapes.or(NWU_CORNER, NEU_CORNER);
                    shape = VoxelShapes.or(shape, SWU_CORNER);
                    shape = VoxelShapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, NED_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, SED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, SWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, NWD_CORNER);
                    }

                }
                else{

                    shape = VoxelShapes.or(NWD_CORNER, NED_CORNER);
                    shape = VoxelShapes.or(shape, SWD_CORNER);
                    shape = VoxelShapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, NEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, SEU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, SWU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, NWU_CORNER);
                    }

                }
                break;
            case SOUTH:
                if(inverted){

                    shape = VoxelShapes.or(NWU_CORNER, NEU_CORNER);
                    shape = VoxelShapes.or(shape, SWU_CORNER);
                    shape = VoxelShapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, SED_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, SWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, NWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, NED_CORNER);
                    }

                }
                else{

                    shape = VoxelShapes.or(NWD_CORNER, NED_CORNER);
                    shape = VoxelShapes.or(shape, SWD_CORNER);
                    shape = VoxelShapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, SEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, SWU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, NWU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, NEU_CORNER);
                    }

                }
                break;
            case WEST:
                if(inverted){

                    shape = VoxelShapes.or(NWU_CORNER, NEU_CORNER);
                    shape = VoxelShapes.or(shape, SWU_CORNER);
                    shape = VoxelShapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, SWD_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, NWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, NED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, SED_CORNER);
                    }

                }
                else{

                    shape = VoxelShapes.or(NWD_CORNER, NED_CORNER);
                    shape = VoxelShapes.or(shape, SWD_CORNER);
                    shape = VoxelShapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, SWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, NWU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, NEU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, SEU_CORNER);
                    }

                }
                break;
            case UP:
                if(inverted){

                    shape = VoxelShapes.or(NWU_CORNER, NEU_CORNER);
                    shape = VoxelShapes.or(shape, NWD_CORNER);
                    shape = VoxelShapes.or(shape, NED_CORNER);

                    //For vertical stairs, RIGHT corner is considered DOWN and LEFT is UP
                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, SWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, SWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, SED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, SEU_CORNER);
                    }

                }
                else{

                    shape = VoxelShapes.or(NWU_CORNER, NEU_CORNER);
                    shape = VoxelShapes.or(shape, NWD_CORNER);
                    shape = VoxelShapes.or(shape, NED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, SEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, SED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, SWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, SWU_CORNER);
                    }

                }
                break;
            case DOWN:
                if(inverted){

                    shape = VoxelShapes.or(SWU_CORNER, SEU_CORNER);
                    shape = VoxelShapes.or(shape, SWD_CORNER);
                    shape = VoxelShapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, NWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, NWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, NED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, NEU_CORNER);
                    }

                }
                else{

                    shape = VoxelShapes.or(SWU_CORNER, SEU_CORNER);
                    shape = VoxelShapes.or(shape, SWD_CORNER);
                    shape = VoxelShapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = VoxelShapes.or(shape, NEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = VoxelShapes.or(shape, NED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = VoxelShapes.or(shape, NWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = VoxelShapes.or(shape, NWU_CORNER);
                    }

                }
                break;
            default:
                shape = VoxelShapes.or(NWD_CORNER, NED_CORNER);
                shape = VoxelShapes.or(shape, SWD_CORNER);
                shape = VoxelShapes.or(shape, SED_CORNER);

                if(connected != StairsShape.INNER_RIGHT) {
                    shape = VoxelShapes.or(shape, NWU_CORNER);
                }
                if(connected != StairsShape.INNER_LEFT){
                    shape = VoxelShapes.or(shape, NEU_CORNER);
                }

                if(connected == StairsShape.OUTER_RIGHT){
                    shape = VoxelShapes.or(shape, SEU_CORNER);
                }
                else if(connected == StairsShape.OUTER_LEFT){
                    shape = VoxelShapes.or(shape, SWU_CORNER);
                }

        }

        return shape;

    }

    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return makeShape(state.get(FACING), state.get(INVERTED), state.get(CONNECTED));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, INVERTED, CONNECTED);
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        Item heldItem = player.getHeldItem(handIn).getItem();
        if(this.SlabQuarterBlock == getBlockFromItem(heldItem)){
            if(this.SlabQuarterBlock instanceof QuarterBlock){
                worldIn.setBlockState(pos, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).getDefaultState());
                SoundType soundType = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).getDefaultState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, soundType.volume, soundType.pitch);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }



    public BlockState getStateForPlacement(BlockItemUseContext context){

        FluidState fluidState = context.getWorld().getFluidState(context.getPos());
        Direction direction = context.getNearestLookingDirection();

        return (BlockState) this.getDefaultState().with(FACING, direction).with(WATERLOGGED, Boolean.valueOf(fluidState.getFluid() == Fluids.WATER));

    }


    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        //Get all the neighboring blocks
        BlockState north = worldIn.getBlockState(pos.north());
        BlockState east  = worldIn.getBlockState(pos.east());
        BlockState south = worldIn.getBlockState(pos.south());
        BlockState west  = worldIn.getBlockState(pos.west());
        BlockState up    = worldIn.getBlockState(pos.up());
        BlockState down  = worldIn.getBlockState(pos.down());

        if(north.getBlock() instanceof SlabBlock){

        }

    }

}
