package com.xpmodder.slabsandstairs.block;

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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

import static com.xpmodder.slabsandstairs.utility.Util.getBlockFromItem;
import static net.minecraft.core.Direction.*;

public class QuarterBlock extends SlabBlock {

    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

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

    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
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

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

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
                return InteractionResult.SUCCESS;
            }
        }
        else if(StairBlock == getBlockFromItem(heldItem)){
            if(StairBlock instanceof StairBlock){
                //Quarter (in world) right-clicked with stair (in hand)

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
        return InteractionResult.PASS;
    }


}
