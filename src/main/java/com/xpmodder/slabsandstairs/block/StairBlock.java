package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.init.KeyInit;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.xpmodder.slabsandstairs.utility.Util.getBlockFromItem;
import static net.minecraft.core.Direction.*;

public class StairBlock extends SlabBlock{

    public static final EnumProperty<StairsShape> CONNECTED = BlockStateProperties.STAIRS_SHAPE;

    protected static final VoxelShape NWD_CORNER = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
    protected static final VoxelShape SWD_CORNER = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape NWU_CORNER = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
    protected static final VoxelShape SWU_CORNER = Block.box(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape NED_CORNER = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    protected static final VoxelShape SED_CORNER = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape NEU_CORNER = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SEU_CORNER = Block.box(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);


    public StairBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(INVERTED, false).setValue(CONNECTED, StairsShape.STRAIGHT));
    }

    //Creates the correct voxel shape for the hitbox based on the orientation of the stairs
    public VoxelShape makeShape(Direction direction, boolean inverted, StairsShape connected){

        VoxelShape shape;

        switch (direction){

            case NORTH:
                if(inverted){

                    shape = Shapes.or(NWU_CORNER, NEU_CORNER);
                    shape = Shapes.or(shape, SWU_CORNER);
                    shape = Shapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, NWD_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, NED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, SED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, SWD_CORNER);
                    }

                }
                else{

                    shape = Shapes.or(NWD_CORNER, NED_CORNER);
                    shape = Shapes.or(shape, SWD_CORNER);
                    shape = Shapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, NWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, NEU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, SEU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, SWU_CORNER);
                    }

                }
                break;
            case EAST:
                if(inverted){

                    shape = Shapes.or(NWU_CORNER, NEU_CORNER);
                    shape = Shapes.or(shape, SWU_CORNER);
                    shape = Shapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, NED_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, SED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, SWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, NWD_CORNER);
                    }

                }
                else{

                    shape = Shapes.or(NWD_CORNER, NED_CORNER);
                    shape = Shapes.or(shape, SWD_CORNER);
                    shape = Shapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, NEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, SEU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, SWU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, NWU_CORNER);
                    }

                }
                break;
            case SOUTH:
                if(inverted){

                    shape = Shapes.or(NWU_CORNER, NEU_CORNER);
                    shape = Shapes.or(shape, SWU_CORNER);
                    shape = Shapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, SED_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, SWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, NWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, NED_CORNER);
                    }

                }
                else{

                    shape = Shapes.or(NWD_CORNER, NED_CORNER);
                    shape = Shapes.or(shape, SWD_CORNER);
                    shape = Shapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, SEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, SWU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, NWU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, NEU_CORNER);
                    }

                }
                break;
            case WEST:
                if(inverted){

                    shape = Shapes.or(NWU_CORNER, NEU_CORNER);
                    shape = Shapes.or(shape, SWU_CORNER);
                    shape = Shapes.or(shape, SEU_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, SWD_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, NWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, NED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, SED_CORNER);
                    }

                }
                else{

                    shape = Shapes.or(NWD_CORNER, NED_CORNER);
                    shape = Shapes.or(shape, SWD_CORNER);
                    shape = Shapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, SWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, NWU_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, NEU_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, SEU_CORNER);
                    }

                }
                break;
            case UP:
                if(inverted){

                    shape = Shapes.or(NWU_CORNER, NEU_CORNER);
                    shape = Shapes.or(shape, NWD_CORNER);
                    shape = Shapes.or(shape, NED_CORNER);

                    //For vertical stairs, RIGHT corner is considered DOWN and LEFT is UP
                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, SWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, SWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, SED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, SEU_CORNER);
                    }

                }
                else{

                    shape = Shapes.or(NWU_CORNER, NEU_CORNER);
                    shape = Shapes.or(shape, NWD_CORNER);
                    shape = Shapes.or(shape, NED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, SEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, SED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, SWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, SWU_CORNER);
                    }

                }
                break;
            case DOWN:
                if(inverted){

                    shape = Shapes.or(SWU_CORNER, SEU_CORNER);
                    shape = Shapes.or(shape, SWD_CORNER);
                    shape = Shapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, NWU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, NWD_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, NED_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, NEU_CORNER);
                    }

                }
                else{

                    shape = Shapes.or(SWU_CORNER, SEU_CORNER);
                    shape = Shapes.or(shape, SWD_CORNER);
                    shape = Shapes.or(shape, SED_CORNER);

                    if(connected != StairsShape.INNER_RIGHT) {
                        shape = Shapes.or(shape, NEU_CORNER);
                    }
                    if(connected != StairsShape.INNER_LEFT){
                        shape = Shapes.or(shape, NED_CORNER);
                    }

                    if(connected == StairsShape.OUTER_RIGHT){
                        shape = Shapes.or(shape, NWD_CORNER);
                    }
                    else if(connected == StairsShape.OUTER_LEFT){
                        shape = Shapes.or(shape, NWU_CORNER);
                    }

                }
                break;
            default:
                shape = Shapes.or(NWD_CORNER, NED_CORNER);
                shape = Shapes.or(shape, SWD_CORNER);
                shape = Shapes.or(shape, SED_CORNER);

                if(connected != StairsShape.INNER_RIGHT) {
                    shape = Shapes.or(shape, NWU_CORNER);
                }
                if(connected != StairsShape.INNER_LEFT){
                    shape = Shapes.or(shape, NEU_CORNER);
                }

                if(connected == StairsShape.OUTER_RIGHT){
                    shape = Shapes.or(shape, SEU_CORNER);
                }
                else if(connected == StairsShape.OUTER_LEFT){
                    shape = Shapes.or(shape, SWU_CORNER);
                }

        }

        return shape;

    }

    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return makeShape(state.getValue(FACING), state.getValue(INVERTED), state.getValue(CONNECTED));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, INVERTED, CONNECTED);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

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

        Item heldItem = player.getItemInHand(handIn).getItem();
        if(SlabQuarterBlock == getBlockFromItem(heldItem)){
            if(SlabQuarterBlock instanceof QuarterBlock){
                worldIn.setBlockAndUpdate(pos, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).defaultBlockState());
                SoundType soundType = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).defaultBlockState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }


    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor accessor, BlockPos pos, BlockPos pos2){
        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }

        return state.setValue(CONNECTED, getShapeForNeighbours(state, pos, accessor));
    }

    public StairsShape getShapeForNeighbours(BlockState stair, BlockPos pos, LevelAccessor accessor){
        return getShapeForNeighbours(stair, accessor.getBlockState(pos.north()), accessor.getBlockState(pos.east()), accessor.getBlockState(pos.south()), accessor.getBlockState(pos.west()), accessor.getBlockState(pos.above()), accessor.getBlockState(pos.below()));
    }

    public StairsShape getShapeForNeighbours(BlockState stair, BlockPlaceContext context){
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return getShapeForNeighbours(stair, world.getBlockState(pos.north()), world.getBlockState(pos.east()), world.getBlockState(pos.south()), world.getBlockState(pos.west()), world.getBlockState(pos.above()), world.getBlockState(pos.below()));
    }

    public StairsShape getShapeForNeighbours(BlockState stair, BlockState north, BlockState east, BlockState south, BlockState west, BlockState up, BlockState down){

        if(!(stair.getBlock() instanceof StairBlock)){
            return StairsShape.STRAIGHT;
        }

        switch(stair.getValue(FACING)){
            case NORTH:

                if(north.getBlock() instanceof StairBlock){
                    if(north.getValue(FACING) == WEST && (stair.getValue(INVERTED) == north.getValue(INVERTED))){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(north.getValue(FACING) == EAST && (stair.getValue(INVERTED) == north.getValue(INVERTED))){
                        return StairsShape.INNER_RIGHT;
                    }
                }
                else if(north.getBlock() instanceof SlabBlock && !(north.getBlock() instanceof QuarterBlock)){
                    if(north.getValue(FACING) == EAST){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(north.getValue(FACING) == WEST){
                        return StairsShape.INNER_RIGHT;
                    }
                }

                if(south.getBlock() instanceof StairBlock){
                    if(south.getValue(FACING) == WEST && (stair.getValue(INVERTED) == south.getValue(INVERTED))){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(south.getValue(FACING) == EAST && (stair.getValue(INVERTED) == south.getValue(INVERTED))){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                else if(south.getBlock() instanceof SlabBlock && !(south.getBlock() instanceof QuarterBlock)){
                    if(south.getValue(FACING) == EAST){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(south.getValue(FACING) == WEST){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                break;
            case EAST:

                if(east.getBlock() instanceof StairBlock){
                    if(east.getValue(FACING) == NORTH && (stair.getValue(INVERTED) == east.getValue(INVERTED))){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(east.getValue(FACING) == SOUTH && (stair.getValue(INVERTED) == east.getValue(INVERTED))){
                        return StairsShape.INNER_RIGHT;
                    }
                }
                else if(east.getBlock() instanceof SlabBlock && !(east.getBlock() instanceof QuarterBlock)){
                    if(east.getValue(FACING) == SOUTH){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(east.getValue(FACING) == NORTH){
                        return StairsShape.INNER_RIGHT;
                    }
                }

                if(west.getBlock() instanceof StairBlock){
                    if(west.getValue(FACING) == NORTH && (stair.getValue(INVERTED) == west.getValue(INVERTED))){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(west.getValue(FACING) == SOUTH && (stair.getValue(INVERTED) == west.getValue(INVERTED))){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                else if(west.getBlock() instanceof SlabBlock && !(west.getBlock() instanceof QuarterBlock)){
                    if(west.getValue(FACING) == SOUTH){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(west.getValue(FACING) == NORTH){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                break;
            case SOUTH:

                if(south.getBlock() instanceof StairBlock){
                    if(south.getValue(FACING) == EAST && (stair.getValue(INVERTED) == south.getValue(INVERTED))){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(south.getValue(FACING) == WEST && (stair.getValue(INVERTED) == south.getValue(INVERTED))){
                        return StairsShape.INNER_RIGHT;
                    }
                }
                else if(south.getBlock() instanceof SlabBlock && !(south.getBlock() instanceof QuarterBlock)){
                    if(south.getValue(FACING) == WEST){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(south.getValue(FACING) == EAST){
                        return StairsShape.INNER_RIGHT;
                    }
                }

                if(north.getBlock() instanceof StairBlock){
                    if(north.getValue(FACING) == EAST && (stair.getValue(INVERTED) == north.getValue(INVERTED))){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(north.getValue(FACING) == WEST && (stair.getValue(INVERTED) == north.getValue(INVERTED))){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                else if(north.getBlock() instanceof SlabBlock && !(north.getBlock() instanceof QuarterBlock)){
                    if(north.getValue(FACING) == WEST){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(north.getValue(FACING) == EAST){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                break;
            case WEST:

                if(west.getBlock() instanceof StairBlock){
                    if(west.getValue(FACING) == SOUTH && (stair.getValue(INVERTED) == west.getValue(INVERTED))){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(west.getValue(FACING) == NORTH && (stair.getValue(INVERTED) == west.getValue(INVERTED))){
                        return StairsShape.INNER_RIGHT;
                    }
                }
                else if(west.getBlock() instanceof SlabBlock && !(west.getBlock() instanceof QuarterBlock)){
                    if(west.getValue(FACING) == NORTH){
                        return StairsShape.INNER_LEFT;
                    }
                    else if(west.getValue(FACING) == SOUTH){
                        return StairsShape.INNER_RIGHT;
                    }
                }

                if(east.getBlock() instanceof StairBlock){
                    if(east.getValue(FACING) == SOUTH && (stair.getValue(INVERTED) == east.getValue(INVERTED))){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(east.getValue(FACING) == NORTH && (stair.getValue(INVERTED) == east.getValue(INVERTED))){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                else if(east.getBlock() instanceof SlabBlock && !(east.getBlock() instanceof QuarterBlock)){
                    if(east.getValue(FACING) == NORTH){
                        return StairsShape.OUTER_LEFT;
                    }
                    else if(east.getValue(FACING) == SOUTH){
                        return StairsShape.OUTER_RIGHT;
                    }
                }
                break;
            case UP:

                //RIGHT == BOTTOM, LEFT == TOP

                if(stair.getValue(INVERTED)){

                    if (east.getBlock() instanceof StairBlock) {
                        if (east.getValue(FACING) == NORTH && !east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == EAST && !east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == SOUTH && !east.getValue(INVERTED) && east.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == NORTH && east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (east.getValue(FACING) == WEST && east.getValue(INVERTED) && east.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (east.getValue(FACING) == SOUTH && east.getValue(INVERTED) && east.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (east.getValue(FACING) == EAST && east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_RIGHT){
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (east.getBlock() instanceof SlabBlock && !(east.getBlock() instanceof QuarterBlock)) {
                        if (east.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (south.getBlock() instanceof StairBlock) {
                        if (south.getValue(FACING) == WEST && !south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (south.getValue(FACING) == SOUTH && !south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (south.getValue(FACING) == EAST && !south.getValue(INVERTED) && south.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (south.getValue(FACING) == WEST && south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (south.getValue(FACING) == SOUTH && south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (south.getValue(FACING) == EAST && south.getValue(INVERTED) && south.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (south.getBlock() instanceof SlabBlock && !(south.getBlock() instanceof QuarterBlock)) {
                        if (south.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        }
                        if (south.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (west.getBlock() instanceof StairBlock) {
                        if (west.getValue(FACING) == NORTH && west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_LEFT;
                        } else if (west.getValue(FACING) == NORTH && !west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_RIGHT;
                        }
                    } else if (west.getBlock() instanceof SlabBlock && !(west.getBlock() instanceof QuarterBlock)) {
                        if (west.getValue(FACING) == DOWN) {
                            return StairsShape.INNER_LEFT;
                        } else if (west.getValue(FACING) == UP) {
                            return StairsShape.INNER_RIGHT;
                        }
                    }

                }
                else {

                    if (west.getBlock() instanceof StairBlock) {
                        if (west.getValue(FACING) == NORTH && !west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == WEST && !west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == SOUTH && !west.getValue(INVERTED) && west.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == NORTH && west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (west.getValue(FACING) == EAST && west.getValue(INVERTED) && west.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (west.getValue(FACING) == SOUTH && west.getValue(INVERTED) && west.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (west.getValue(FACING) == WEST && west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_LEFT){
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (west.getBlock() instanceof SlabBlock && !(west.getBlock() instanceof QuarterBlock)) {
                        if (west.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (south.getBlock() instanceof StairBlock) {
                        if (south.getValue(FACING) == EAST && !south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (south.getValue(FACING) == SOUTH && !south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (south.getValue(FACING) == WEST && !south.getValue(INVERTED) && south.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (south.getValue(FACING) == EAST && south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (south.getValue(FACING) == SOUTH && south.getValue(INVERTED) && south.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (south.getValue(FACING) == WEST && south.getValue(INVERTED) && south.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (south.getBlock() instanceof SlabBlock && !(south.getBlock() instanceof QuarterBlock)) {
                        if (south.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        }
                        if (south.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (east.getBlock() instanceof StairBlock) {
                        if (east.getValue(FACING) == NORTH && east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_LEFT;
                        } else if (east.getValue(FACING) == NORTH && !east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_RIGHT;
                        }
                    } else if (east.getBlock() instanceof SlabBlock && !(east.getBlock() instanceof QuarterBlock)) {
                        if (east.getValue(FACING) == DOWN) {
                            return StairsShape.INNER_LEFT;
                        } else if (east.getValue(FACING) == UP) {
                            return StairsShape.INNER_RIGHT;
                        }
                    }
                }

                break;
            case DOWN:

                if(stair.getValue(INVERTED)){

                    if (east.getBlock() instanceof StairBlock) {
                        if (east.getValue(FACING) == SOUTH && !east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == EAST && !east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == NORTH && !east.getValue(INVERTED) && east.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == SOUTH && east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (east.getValue(FACING) == WEST && east.getValue(INVERTED) && east.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (east.getValue(FACING) == NORTH && east.getValue(INVERTED) && east.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (east.getValue(FACING) == EAST && east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_LEFT){
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (east.getBlock() instanceof SlabBlock && !(east.getBlock() instanceof QuarterBlock)) {
                        if (east.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (east.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (north.getBlock() instanceof StairBlock) {
                        if (north.getValue(FACING) == WEST && !north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (north.getValue(FACING) == NORTH && !north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (north.getValue(FACING) == EAST && !north.getValue(INVERTED) && north.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (north.getValue(FACING) == WEST && north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (north.getValue(FACING) == NORTH && north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (north.getValue(FACING) == EAST && north.getValue(INVERTED) && north.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (north.getBlock() instanceof SlabBlock && !(north.getBlock() instanceof QuarterBlock)) {
                        if (north.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        }
                        if (north.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (west.getBlock() instanceof StairBlock) {
                        if (west.getValue(FACING) == SOUTH && west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_LEFT;
                        } else if (west.getValue(FACING) == SOUTH && !west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_RIGHT;
                        }
                    } else if (west.getBlock() instanceof SlabBlock && !(west.getBlock() instanceof QuarterBlock)) {
                        if (west.getValue(FACING) == DOWN) {
                            return StairsShape.INNER_LEFT;
                        } else if (west.getValue(FACING) == UP) {
                            return StairsShape.INNER_RIGHT;
                        }
                    }

                }
                else {

                    if (west.getBlock() instanceof StairBlock) {
                        if (west.getValue(FACING) == SOUTH && !west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == WEST && !west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == NORTH && !west.getValue(INVERTED) && west.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == SOUTH && west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (west.getValue(FACING) == EAST && west.getValue(INVERTED) && west.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (west.getValue(FACING) == NORTH && west.getValue(INVERTED) && west.getValue(CONNECTED) == StairsShape.INNER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (west.getValue(FACING) == WEST && west.getValue(INVERTED) && west.getValue(CONNECTED) != StairsShape.OUTER_RIGHT){
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (west.getBlock() instanceof SlabBlock && !(west.getBlock() instanceof QuarterBlock)) {
                        if (west.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (west.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (north.getBlock() instanceof StairBlock) {
                        if (north.getValue(FACING) == EAST && !north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (north.getValue(FACING) == NORTH && !north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (north.getValue(FACING) == WEST && !north.getValue(INVERTED) && north.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_RIGHT;
                        } else if (north.getValue(FACING) == EAST && north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (north.getValue(FACING) == NORTH && north.getValue(INVERTED) && north.getValue(CONNECTED) != StairsShape.OUTER_RIGHT) {
                            return StairsShape.OUTER_LEFT;
                        } else if (north.getValue(FACING) == WEST && north.getValue(INVERTED) && north.getValue(CONNECTED) == StairsShape.INNER_LEFT) {
                            return StairsShape.OUTER_LEFT;
                        }
                    } else if (north.getBlock() instanceof SlabBlock && !(north.getBlock() instanceof QuarterBlock)) {
                        if (north.getValue(FACING) == UP) {
                            return StairsShape.OUTER_RIGHT;
                        }
                        if (north.getValue(FACING) == DOWN) {
                            return StairsShape.OUTER_LEFT;
                        }
                    }

                    if (east.getBlock() instanceof StairBlock) {
                        if (east.getValue(FACING) == SOUTH && east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_LEFT;
                        } else if (east.getValue(FACING) == SOUTH && !east.getValue(INVERTED) && east.getValue(CONNECTED) != StairsShape.OUTER_LEFT) {
                            return StairsShape.INNER_RIGHT;
                        }
                    } else if (east.getBlock() instanceof SlabBlock && !(east.getBlock() instanceof QuarterBlock)) {
                        if (east.getValue(FACING) == DOWN) {
                            return StairsShape.INNER_LEFT;
                        } else if (east.getValue(FACING) == UP) {
                            return StairsShape.INNER_RIGHT;
                        }
                    }
                }

                break;
        }

        return StairsShape.STRAIGHT;

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
                BlockState state = this.defaultBlockState().setValue(FACING, placementRotation).setValue(INVERTED, placementInverted).setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
                return state.setValue(CONNECTED, getShapeForNeighbours(state, context));
            }
            BlockState state = this.defaultBlockState().setValue(FACING, placementRotation).setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
            return state.setValue(CONNECTED, getShapeForNeighbours(state, context));
        }

        if((context.getClickLocation().y - (double)blockPos.getY()) > 0.5D){
            BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(INVERTED, true).setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
            return state.setValue(CONNECTED, getShapeForNeighbours(state, context));
        }
        else{
            BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(INVERTED, false).setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
            return state.setValue(CONNECTED, getShapeForNeighbours(state, context));
        }
    }


    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        //Get all the neighboring blocks
        BlockState north = worldIn.getBlockState(pos.north());
        BlockState east  = worldIn.getBlockState(pos.east());
        BlockState south = worldIn.getBlockState(pos.south());
        BlockState west  = worldIn.getBlockState(pos.west());
        BlockState up    = worldIn.getBlockState(pos.above());
        BlockState down  = worldIn.getBlockState(pos.below());

        state.setValue(CONNECTED, getShapeForNeighbours(state, north, east, south, west, up, down));

    }

}
