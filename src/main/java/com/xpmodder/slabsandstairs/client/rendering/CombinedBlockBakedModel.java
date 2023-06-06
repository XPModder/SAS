package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CombinedBlockBakedModel implements IDynamicBakedModel {

    public static ModelProperty<BlockState> BLOCK1 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK2 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK3 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK4 = new ModelProperty<>();
    public static ModelProperty<Integer> NUM_BLOCKS = new ModelProperty<>();

    public static ModelDataMap getEmptyIModelData(){
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        builder.withInitial(NUM_BLOCKS, 1).withInitial(BLOCK1, BlockInit.previewSlab.get().defaultBlockState())
                .withProperty(BLOCK2).withProperty(BLOCK3).withProperty(BLOCK4);
        return builder.build();
    }


    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {

        List<BakedQuad> quads = new ArrayList<>();

        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();

        try {

            if(state == null){
                return Collections.emptyList();
            }

            if(extraData.hasProperty(NUM_BLOCKS)){

                LogHelper.info("NUM_BLOCKS:" + extraData.getData(NUM_BLOCKS));

                if(extraData.getData(NUM_BLOCKS) >= 1){
                    LogHelper.info("Block1: " + extraData.getData(BLOCK1));
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK1)).getQuads(extraData.getData(BLOCK1), side, rand, extraData));
                }
                if(extraData.getData(NUM_BLOCKS) >= 2){
                    LogHelper.info("Block2: " + extraData.getData(BLOCK2));
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK2)).getQuads(extraData.getData(BLOCK2), side, rand, extraData));
                }
                if(extraData.getData(NUM_BLOCKS) >= 3){
                    LogHelper.info("Block3: " + extraData.getData(BLOCK3));
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK3)).getQuads(extraData.getData(BLOCK3), side, rand, extraData));
                }
                if(extraData.getData(NUM_BLOCKS) == 4){
                    LogHelper.info("Block4: " + extraData.getData(BLOCK4));
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK4)).getQuads(extraData.getData(BLOCK4), side, rand, extraData));
                }

            }

        }
        catch (NullPointerException ex){
            LogHelper.error("Could not combine all models! Got NullPointerException trying to get block!");
        }

        return quads;
    }


    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
        return shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getParticleIcon(new ModelDataMap.Builder().build());
    }

    @Override
    public ItemOverrides getOverrides() {
        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
        return shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getOverrides();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(IModelData modelData){
        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
        if(modelData.hasProperty(BLOCK1)){
            return shaper.getBlockModel(modelData.getData(BLOCK1)).getParticleIcon(new ModelDataMap.Builder().build());
        }
        return shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getParticleIcon(new ModelDataMap.Builder().build());
    }

}
