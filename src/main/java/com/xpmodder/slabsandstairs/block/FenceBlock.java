package com.xpmodder.slabsandstairs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public class FenceBlock extends net.minecraft.world.level.block.FenceBlock {

    protected String BaseBlock = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(Blocks.AIR)).toString();

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


}
