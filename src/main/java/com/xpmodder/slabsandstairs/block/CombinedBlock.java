package com.xpmodder.slabsandstairs.block;


import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CombinedBlock extends SlabBlock{

    public List<BlockState> otherBlockStates = new ArrayList<>();

    public CombinedBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


}
