package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.init.BlockInit;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModelBakeEventHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onBakeModel(ModelBakeEvent event){

        for(BlockState state : BlockInit.combinedBlock.get().getStateDefinition().getPossibleStates()){

            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(state);

            event.getModelRegistry().put(location, new CombinedBlockBakedModel());

        }

        ModelResourceLocation location = new ModelResourceLocation(BlockInit.combinedBlock.get().getRegistryName(), "inventory");
        event.getModelRegistry().put(location, new CombinedBlockBakedModel());

    }

}
