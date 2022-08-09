package com.xpmodder.slabsandstairs.block;

import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CombinedBlock extends SlabBlock{

    public List<BlockState> otherBlockStates = new ArrayList<>();

    public CombinedBlock(Properties properties) {
        super(properties);
    }


}
