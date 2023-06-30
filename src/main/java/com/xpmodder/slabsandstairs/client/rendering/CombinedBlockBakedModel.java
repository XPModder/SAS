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

            if (extraData.hasProperty(NUM_BLOCKS)) {

                if (extraData.getData(NUM_BLOCKS) >= 1) {
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK1)).getQuads(extraData.getData(BLOCK1), side, rand, extraData));
                }
                if (extraData.getData(NUM_BLOCKS) >= 2) {
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK2)).getQuads(extraData.getData(BLOCK2), side, rand, extraData));
                }
                if (extraData.getData(NUM_BLOCKS) >= 3) {
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK3)).getQuads(extraData.getData(BLOCK3), side, rand, extraData));
                }
                if (extraData.getData(NUM_BLOCKS) == 4) {
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK4)).getQuads(extraData.getData(BLOCK4), side, rand, extraData));
                }

            }
            else{
                quads.addAll(shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getQuads(BlockInit.previewSlab.get().defaultBlockState(), side, rand, extraData));
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
    public @NotNull TextureAtlasSprite getParticleIcon() {
        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
        return shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getParticleIcon(new ModelDataMap.Builder().build());
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return new CombinedBlockItemOverrides();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(IModelData modelData){
        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
        if(modelData.hasProperty(BLOCK4) && modelData.getData(BLOCK4) != null && !modelData.getData(BLOCK4).isAir()){

            return shaper.getBlockModel(modelData.getData(BLOCK4)).getParticleIcon(new ModelDataMap.Builder().build());

        }
        else if(modelData.hasProperty(BLOCK3) && modelData.getData(BLOCK3) != null && !modelData.getData(BLOCK3).isAir()){

            return shaper.getBlockModel(modelData.getData(BLOCK3)).getParticleIcon(new ModelDataMap.Builder().build());

        }
        else if(modelData.hasProperty(BLOCK2) && modelData.getData(BLOCK2) != null && !modelData.getData(BLOCK2).isAir()){

            return shaper.getBlockModel(modelData.getData(BLOCK2)).getParticleIcon(new ModelDataMap.Builder().build());

        }
        else if(modelData.hasProperty(BLOCK1) && modelData.getData(BLOCK1) != null && !modelData.getData(BLOCK1).isAir()){

            return shaper.getBlockModel(modelData.getData(BLOCK1)).getParticleIcon(new ModelDataMap.Builder().build());

        }
        return shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getParticleIcon(new ModelDataMap.Builder().build());
    }




}
