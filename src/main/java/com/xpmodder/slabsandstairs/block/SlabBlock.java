package com.xpmodder.slabsandstairs.block;

import net.minecraft.block.*;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import static com.xpmodder.slabsandstairs.block.StairBlock.INVERTED;

public class SlabBlock extends Block implements IWaterLoggable {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_EAST = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_DOWN = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    protected String BaseBlock = Blocks.AIR.getRegistryName().toString();
    protected Block SlabQuarterBlock = Blocks.AIR;
    protected Block StairBlock = Blocks.AIR;


    public SlabBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    public String getBaseBlock(){
        return this.BaseBlock;
    }

    public void setReferenceBlocks(String baseBlock, Block slabQuarterBlock, Block stairBlock){
        this.BaseBlock = baseBlock;
        this.SlabQuarterBlock = slabQuarterBlock;
        this.StairBlock = stairBlock;
    }





    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        Item heldItem = player.getHeldItem(handIn).getItem();
        if(getSelf() == getBlockFromItem(heldItem)){
            if(getBlockFromItem(heldItem) instanceof SlabBlock){
                worldIn.setBlockState(pos, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).getDefaultState());
                SoundType soundType = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).getDefaultState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, soundType.volume, soundType.pitch);
                return ActionResultType.SUCCESS;
            }
        }
        else if(this.SlabQuarterBlock == getBlockFromItem(heldItem)){
            if(this.SlabQuarterBlock instanceof QuarterBlock){
                Direction newFacing = Direction.NORTH;
                boolean newInverted = false;
                switch(state.get(FACING)){
                    case NORTH:
                        newFacing = Direction.DOWN;
                        newInverted = true;
                        if(player.getHorizontalFacing() == Direction.EAST){
                            newInverted = false;
                        }
                        else if(player.getHorizontalFacing() == Direction.SOUTH){
                            if(player.getRotationYawHead() < 0.0f){
                                newFacing = Direction.DOWN;
                                newInverted = false;
                            }
                            else{
                                newFacing = Direction.DOWN;
                                newInverted = true;
                            }
                        }
                        break;
                    case EAST:
                        newFacing = Direction.UP;
                        newInverted = true;
                        if(player.getHorizontalFacing() == Direction.SOUTH){
                            newFacing = Direction.DOWN;
                        }
                        else if(player.getHorizontalFacing() == Direction.WEST){
                            if(player.getRotationYawHead() < 90.0f){
                                newFacing = Direction.DOWN;
                                newInverted = true;
                            }
                            else{
                                newFacing = Direction.UP;
                                newInverted = true;
                            }
                        }
                        break;
                    case SOUTH:
                        newFacing = Direction.UP;
                        newInverted = true;
                        if(player.getHorizontalFacing() == Direction.EAST){
                            newInverted = false;
                        }
                        else if(player.getHorizontalFacing() == Direction.NORTH){
                            if(player.getRotationYawHead() < 0.0f){
                                newFacing = Direction.UP;
                                newInverted = false;
                            }
                            else{
                                newFacing = Direction.UP;
                                newInverted = true;
                            }
                        }
                        break;
                    case WEST:
                        newFacing = Direction.DOWN;
                        newInverted = false;
                        if(player.getHorizontalFacing() == Direction.NORTH){
                            newFacing = Direction.UP;
                        }
                        else if(player.getHorizontalFacing() == Direction.EAST){
                            if(player.getRotationYawHead() < -90.0f){
                                newFacing = Direction.UP;
                                newInverted = false;
                            }
                            else{
                                newFacing = Direction.DOWN;
                                newInverted = false;
                            }
                        }
                        break;
                    case UP:
                        newFacing = player.getHorizontalFacing();
                        newInverted = false;
                        break;
                    case DOWN:
                        newFacing = player.getHorizontalFacing();
                        newInverted = true;
                        break;
                    default:

                }
                worldIn.setBlockState(pos, this.StairBlock.getDefaultState().with(FACING, newFacing).with(INVERTED, newInverted));
                SoundType soundType = this.StairBlock.getDefaultState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, soundType.volume, soundType.pitch);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Pose pose = context.getPlayer().getPose();
        FluidState fluidState = context.getWorld().getFluidState(context.getPos());
        Direction direction = context.getNearestLookingDirection();
        return (BlockState)this.getDefaultState().with(FACING, (pose == Pose.CROUCHING ? direction : direction.getOpposite())).with(WATERLOGGED, Boolean.valueOf(fluidState.getFluid() == Fluids.WATER));
    }

    public boolean isTransparent(BlockState blockState){
        return true;
    }

    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        switch(state.get(FACING)){
            case NORTH:
                return SHAPE_NORTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            case SOUTH:
                return SHAPE_SOUTH;
            case UP:
                return SHAPE_UP;
            case DOWN:
                return SHAPE_DOWN;
            default:
                return SHAPE_NORTH;
        }
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

}
