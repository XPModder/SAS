package com.xpmodder.slabsandstairs.block;


import com.xpmodder.slabsandstairs.client.rendering.CombinedBlockBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CombinedBlock extends Block implements EntityBlock {


    private final BlockState block1;

    public CombinedBlock(BlockBehaviour.Properties properties, BlockState initialState) {
        super(properties);
        block1 = initialState;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState state) {
        return new CombinedBlockEntity(blockPos, state, this.block1);
    }


}
