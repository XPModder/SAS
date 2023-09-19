package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.utility.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public class FenceBlock extends net.minecraft.world.level.block.FenceBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;


    protected String BaseBlock = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(Blocks.AIR)).toString();

    protected int Power = 0;

    public FenceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
    }

    public String getBaseBlock(){
        return this.BaseBlock;
    }

    public void setReferenceBlocks(String baseBlock){
        this.BaseBlock = baseBlock;
    }

    public void setPower(int power){
        this.Power = power;
    }

    @SuppressWarnings("deprecation")
    public boolean isSignalSource(@NotNull BlockState state) {
        return this.Power > 0;
    }

    @SuppressWarnings("deprecation")
    public int getSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction dir) {
        return this.Power;
    }

    public int getPower(){
        return this.Power;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockPos blockpos1, blockpos2, blockpos3, blockpos4;
        if(context.getClickedFace() == Direction.UP || context.getClickedFace() == Direction.DOWN) {
            blockpos1 = blockpos.north();
            blockpos2 = blockpos.east();
            blockpos3 = blockpos.south();
            blockpos4 = blockpos.west();
        }
        else if(context.getClickedFace() == Direction.NORTH || context.getClickedFace() == Direction.SOUTH){
            blockpos1 = blockpos.below();
            blockpos2 = blockpos.east();
            blockpos3 = blockpos.above();
            blockpos4 = blockpos.west();
        }
        else{
            blockpos1 = blockpos.north();
            blockpos2 = blockpos.below();
            blockpos3 = blockpos.south();
            blockpos4 = blockpos.above();
        }
        BlockState facingState = this.defaultBlockState().setValue(FACING, context.getClickedFace());
        BlockState blockstate = blockgetter.getBlockState(blockpos1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos3);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos4);
        return super.getStateForPlacement(context).setValue(NORTH, this.connectsTo(facingState, blockstate, blockstate.isFaceSturdy(blockgetter, blockpos1, Direction.SOUTH), Direction.SOUTH)).setValue(EAST, this.connectsTo(facingState, blockstate1, blockstate1.isFaceSturdy(blockgetter, blockpos2, Direction.WEST), Direction.WEST)).setValue(SOUTH, this.connectsTo(facingState, blockstate2, blockstate2.isFaceSturdy(blockgetter, blockpos3, Direction.NORTH), Direction.NORTH)).setValue(WEST, this.connectsTo(facingState, blockstate3, blockstate3.isFaceSturdy(blockgetter, blockpos4, Direction.EAST), Direction.EAST)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(FACING, context.getClickedFace());
    }

    public BlockState updateShape(BlockState blockstate, Direction direction, BlockState blockstate2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        if (blockstate.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if(blockstate.getValue(FACING) == Direction.UP || blockstate.getValue(FACING) == Direction.DOWN){
            if(direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL){
                return blockstate.setValue(PROPERTY_BY_DIRECTION.get(direction), this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
            }
        }
        else if(blockstate.getValue(FACING) == Direction.NORTH || blockstate.getValue(FACING) == Direction.SOUTH){
            switch (direction){
                case EAST -> {
                    return blockstate.setValue(EAST, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
                case WEST -> {
                    return blockstate.setValue(WEST, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
                case UP -> {
                    return blockstate.setValue(SOUTH, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
                case DOWN -> {
                    return blockstate.setValue(NORTH, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
            }
        }
        else {
            switch (direction){
                case NORTH -> {
                    return blockstate.setValue(NORTH, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
                case SOUTH -> {
                    return blockstate.setValue(SOUTH, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
                case UP -> {
                    return blockstate.setValue(WEST, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
                case DOWN -> {
                    return blockstate.setValue(EAST, this.connectsTo(blockstate, blockstate2, blockstate2.isFaceSturdy(level, pos2, direction.getOpposite()), direction.getOpposite()));
                }
            }
        }

        return blockstate;

    }

    public boolean connectsTo(BlockState blockstate, boolean bool, Direction direction) {
        Block block = blockstate.getBlock();
        boolean flag = this.isSameFence(blockstate);
        boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(blockstate, direction);
        return !isExceptionForConnection(blockstate) && bool || flag || flag1;
    }

    public boolean connectsTo(BlockState blockstate, BlockState blockstate2, boolean bool, Direction direction) {
        Block block = blockstate2.getBlock();
        boolean flag = this.isSameFence(blockstate2) && (blockstate.getValue(FACING) == blockstate2.getValue(FACING) || blockstate.getValue(FACING) == blockstate2.getValue(FACING).getOpposite());
        boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(blockstate2, direction);
        return !isExceptionForConnection(blockstate2) && bool || flag || flag1;
    }

    private boolean isSameFence(BlockState blockstate) {
        return blockstate.is(BlockTags.FENCES) && blockstate.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
    }

    public @NotNull VoxelShape getVisualShape(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {

        return this.getOcclusionShape(state, getter, pos);

    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {

        return this.getOcclusionShape(state, getter, pos);

    }

    public @NotNull VoxelShape getOcclusionShape(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos) {

        switch(state.getValue(FACING)){
            case UP,DOWN -> {
                VoxelShape shape = ShapeUtil.FencePost;
                if(state.getValue(NORTH)){
                    shape = Shapes.or(shape, ShapeUtil.FenceNorth);
                }
                if(state.getValue(EAST)){
                    shape = Shapes.or(shape, ShapeUtil.FenceEast);
                }
                if(state.getValue(SOUTH)){
                    shape = Shapes.or(shape, ShapeUtil.FenceSouth);
                }
                if(state.getValue(WEST)){
                    shape = Shapes.or(shape, ShapeUtil.FenceWest);
                }
                return shape;
            }
            case NORTH,SOUTH -> {
                VoxelShape shape = ShapeUtil.FencePostHNS;
                if(state.getValue(NORTH)){
                    shape = Shapes.or(shape, ShapeUtil.FenceDownNS);
                }
                if(state.getValue(EAST)){
                    shape = Shapes.or(shape, ShapeUtil.FenceEastH);
                }
                if(state.getValue(SOUTH)){
                    shape = Shapes.or(shape, ShapeUtil.FenceUpNS);
                }
                if(state.getValue(WEST)){
                    shape = Shapes.or(shape, ShapeUtil.FenceWestH);
                }
                return shape;
            }
            case EAST,WEST -> {
                VoxelShape shape = ShapeUtil.FencePostHEW;
                if(state.getValue(NORTH)){
                    shape = Shapes.or(shape, ShapeUtil.FenceNorthH);
                }
                if(state.getValue(EAST)){
                    shape = Shapes.or(shape, ShapeUtil.FenceDownEW);
                }
                if(state.getValue(SOUTH)){
                    shape = Shapes.or(shape, ShapeUtil.FenceSouthH);
                }
                if(state.getValue(WEST)){
                    shape = Shapes.or(shape, ShapeUtil.FenceUpEW);
                }
                return shape;
            }

        }

        return ShapeUtil.FencePost;

    }

}
