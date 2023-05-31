package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.block.CombinedBlock;
import com.xpmodder.slabsandstairs.init.BlockInit;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.client.model.data.MultipartModelData;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModelBakeEventHandler {

    /*@SubscribeEvent
    public void onBakeModel(ModelBakeEvent event){

        for(BlockState state : BlockInit.testBlock.get().getStateDefinition().getPossibleStates()){

            ModelResourceLocation resourceLocation = BlockModelShaper.stateToModelLocation(state);

            CombinedBlock block = (CombinedBlock) state.getBlock();

            Map<RenderType, UnbakedModel> models = new HashMap<>();

            for(BlockState subState : block.otherBlockStates){
                UnbakedModel submodel = event.getModelLoader().getModel(BlockModelShaper.stateToModelLocation(subState));
                models.put(RenderType.solid() , submodel);
            }

            BakedModel model = new MultiLayerModel(models).bake(, event.getModelLoader(), , , , resourceLocation);

            event.getModelRegistry().put(resourceLocation, model);


        }

    }*/

    @SubscribeEvent
    public void onBakeModel(ModelBakeEvent event){

        for(BlockState state : BlockInit.combinedBlock.get().getStateDefinition().getPossibleStates()){

            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(state);

            event.getModelRegistry().put(location, new CombinedBlockBakedModel());

        }

    }

}
