package com.xpmodder.slabsandstairs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class FenceBlock extends net.minecraft.world.level.block.FenceBlock {

    protected String BaseBlock = ForgeRegistries.BLOCKS.getKey(Blocks.AIR).toString();

    protected int Power = 0;

    public FenceBlock(Properties properties) {
        super(properties);
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

    public boolean isSignalSource(BlockState state) {
        return this.Power > 0;
    }

    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return this.Power;
    }

    public int getPower(){
        return this.Power;
    }


}
