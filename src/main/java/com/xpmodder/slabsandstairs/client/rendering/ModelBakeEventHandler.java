package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.init.BlockInit;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@SuppressWarnings("unused")
public class ModelBakeEventHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onBakeModel(ModelEvent.BakingCompleted event){

        for(BlockState state : BlockInit.combinedBlock.get().getStateDefinition().getPossibleStates()){

            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(state);

            event.getModels().put(location, new CombinedBlockBakedModel());

        }

        ModelResourceLocation location = new ModelResourceLocation(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(BlockInit.combinedBlock.get())), "inventory");
        event.getModels().put(location, new CombinedBlockBakedModel());

    }

}
