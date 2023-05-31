package com.xpmodder.slabsandstairs.client.rendering;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.xpmodder.slabsandstairs.block.CombinedBlock;
import com.xpmodder.slabsandstairs.block.CombinedBlockEntity;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
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


    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {

        List<BakedQuad> quads = new ArrayList<>();

        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();

        LogHelper.info("getQuads!");

        try {

            if(state == null){
                return Collections.emptyList();
            }

            if(extraData.hasProperty(NUM_BLOCKS)){

                LogHelper.info("NUM_BLOCKS:" + extraData.getData(NUM_BLOCKS));

                if(extraData.getData(NUM_BLOCKS) >= 1){
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK1)).getQuads(extraData.getData(BLOCK1), side, rand, extraData));
                }
                if(extraData.getData(NUM_BLOCKS) >= 2){
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK2)).getQuads(extraData.getData(BLOCK2), side, rand, extraData));
                }
                if(extraData.getData(NUM_BLOCKS) >= 3){
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK3)).getQuads(extraData.getData(BLOCK3), side, rand, extraData));
                }
                if(extraData.getData(NUM_BLOCKS) == 4){
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
        return null;
    }

    @Override
    public ItemOverrides getOverrides() {
        return null;
    }
}
