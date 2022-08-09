package com.xpmodder.slabsandstairs.block;

import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class QuarterBlock extends SlabBlock {

    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    protected static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_WEST = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_EAST = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(8.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_DOWN = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_UP_INV = Block.makeCuboidShape(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_DOWN_INV = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public QuarterBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(INVERTED, false));
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
                return (state.get(INVERTED) ? SHAPE_UP_INV : SHAPE_UP);
            case DOWN:
                return (state.get(INVERTED) ? SHAPE_DOWN_INV : SHAPE_DOWN);
            default:
                return SHAPE_NORTH;
        }
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, INVERTED);
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        Item heldItem = player.getHeldItem(handIn).getItem();
        if(getSelf() == getBlockFromItem(heldItem)){
            if(getBlockFromItem(heldItem) instanceof QuarterBlock){
                worldIn.setBlockState(pos, this.SlabQuarterBlock.getDefaultState().with(FACING, state.get(FACING)));
                SoundType soundType = this.SlabQuarterBlock.getDefaultState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, soundType.volume, soundType.pitch);
                return ActionResultType.SUCCESS;
            }
        }
        else if(this.SlabQuarterBlock == getBlockFromItem(heldItem)){
            if(this.SlabQuarterBlock instanceof SlabBlock){
                worldIn.setBlockState(pos, this.StairBlock.getDefaultState().with(FACING, state.get(FACING)).with(INVERTED, state.get(INVERTED)));
                SoundType soundType = this.StairBlock.getDefaultState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, soundType.volume, soundType.pitch);
                return ActionResultType.SUCCESS;
            }
        }
        else if(this.StairBlock == getBlockFromItem(heldItem)){
            if(this.StairBlock instanceof StairBlock){
                worldIn.setBlockState(pos, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).getDefaultState());
                SoundType soundType = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.BaseBlock)).getDefaultState().getSoundType();
                worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, soundType.volume, soundType.pitch);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

}
