package com.xpmodder.slabsandstairs.client.rendering;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.xpmodder.slabsandstairs.block.CombinedBlock;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombinedBlockBakedModel implements BakedModel {

    private BakedModel baseModel;
    public ModelProperty<BlockState> OTHER_MODEL1 = new ModelProperty<>();
    public ModelProperty<BlockState> OTHER_MODEL2 = new ModelProperty<>();
    public ModelProperty<BlockState> OTHER_MODEL3 = new ModelProperty<>();
    public ModelProperty<BlockState> OTHER_MODEL4 = new ModelProperty<>();

    public CombinedBlockBakedModel(BakedModel baseModel){
        this.baseModel = baseModel;
    }

    public ModelDataMap getEmptyIModelData() {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        builder.withInitial(OTHER_MODEL1, null);
        return builder.build();
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BakedModel base = this.baseModel;
        if (!(state.getBlock() instanceof CombinedBlock)) {
            return base.getQuads(state, side, rand, extraData);
        }
        BlockState state1 = ((CombinedBlock)state.getBlock()).otherBlockStates.get(0);
        if (state1 == null) {
            return base.getQuads(state, side, rand, extraData);
        }
        BlockState state2 = ((CombinedBlock)state.getBlock()).otherBlockStates.get(1);
        BlockState state3 = ((CombinedBlock)state.getBlock()).otherBlockStates.get(2);
        BlockState state4 = ((CombinedBlock)state.getBlock()).otherBlockStates.get(3);
        ModelManager modelManager = Minecraft.getInstance().getModelManager();
        BakedModel blockModel1 = modelManager.getBlockModelShaper().getBlockModel(state1);

        Matrix4f matrix = new Matrix4f(new Quaternion(-90.0f, 0.0f, 0.0f, true));
        matrix.multiply(Matrix4f.createScaleMatrix(0.4f, 0.4f, 0.4f));
        matrix.multiply(Matrix4f.createTranslateMatrix(0.7f, -2.05f, 0.45f));
        Transformation tm = new Transformation(matrix);
        QuadTransformer transformer = new QuadTransformer(tm);
        List<BakedQuad> updatedBlockQuads1 = transformer.processMany(blockModel1.getQuads(state, side, rand, extraData));
        List<BakedQuad> quads = new ArrayList<>();
        quads.addAll(base.getQuads(state, side, rand, extraData));
        quads.addAll(updatedBlockQuads1);

        if(state2 != null) {
            BakedModel blockModel2 = modelManager.getBlockModelShaper().getBlockModel(state2);
            List<BakedQuad> updatedBlockQuads2 = transformer.processMany(blockModel2.getQuads(state, side, rand, extraData));
            quads.addAll(updatedBlockQuads2);
        }
        if(state3 != null) {
            BakedModel blockModel3 = modelManager.getBlockModelShaper().getBlockModel(state3);
            List<BakedQuad> updatedBlockQuads3 = transformer.processMany(blockModel3.getQuads(state, side, rand, extraData));
            quads.addAll(updatedBlockQuads3);
        }
        if(state4 != null) {
            BakedModel blockModel4 = modelManager.getBlockModelShaper().getBlockModel(state4);
            List<BakedQuad> updatedBlockQuads4 = transformer.processMany(blockModel4.getQuads(state, side, rand, extraData));
            quads.addAll(updatedBlockQuads4);
        }

        return quads;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        if (state == world.getBlockState(pos)) {
            BlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() instanceof CombinedBlock) {
                if (((CombinedBlock) blockState.getBlock()) != Blocks.AIR) {
                    ModelDataMap map = getEmptyIModelData();
                    map.setData(OTHER_MODEL1, blockState);
                    return map;
                }
            }
        }
        return getEmptyIModelData();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@Nonnull IModelData data) {
        return baseModel.getParticleIcon();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return null;
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
