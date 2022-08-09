package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.block.CombinedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombinedBlockBakedModel implements IBakedModel {

    private IBakedModel baseModel;
    public ModelProperty<BlockState> OTHER_MODEL1 = new ModelProperty<>();
    public ModelProperty<BlockState> OTHER_MODEL2 = new ModelProperty<>();
    public ModelProperty<BlockState> OTHER_MODEL3 = new ModelProperty<>();
    public ModelProperty<BlockState> OTHER_MODEL4 = new ModelProperty<>();

    public CombinedBlockBakedModel(IBakedModel baseModel){
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
        IBakedModel base = this.baseModel;
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
        IBakedModel blockModel1 = modelManager.getBlockModelShapes().getModel(state1);

        Matrix4f matrix = new Matrix4f(new Quaternion(-90.0f, 0.0f, 0.0f, true));
        matrix.mul(Matrix4f.makeScale(0.4f, 0.4f, 0.4f));
        matrix.mul(Matrix4f.makeTranslate(0.7f, -2.05f, 0.45f));
        TransformationMatrix tm = new TransformationMatrix(matrix);
        QuadTransformer transformer = new QuadTransformer(tm);
        List<BakedQuad> updatedBlockQuads1 = transformer.processMany(blockModel1.getQuads(state, side, rand, extraData));
        List<BakedQuad> quads = new ArrayList<>();
        quads.addAll(base.getQuads(state, side, rand, extraData));
        quads.addAll(updatedBlockQuads1);

        if(state2 != null) {
            IBakedModel blockModel2 = modelManager.getBlockModelShapes().getModel(state2);
            List<BakedQuad> updatedBlockQuads2 = transformer.processMany(blockModel2.getQuads(state, side, rand, extraData));
            quads.addAll(updatedBlockQuads2);
        }
        if(state3 != null) {
            IBakedModel blockModel3 = modelManager.getBlockModelShapes().getModel(state3);
            List<BakedQuad> updatedBlockQuads3 = transformer.processMany(blockModel3.getQuads(state, side, rand, extraData));
            quads.addAll(updatedBlockQuads3);
        }
        if(state4 != null) {
            IBakedModel blockModel4 = modelManager.getBlockModelShapes().getModel(state4);
            List<BakedQuad> updatedBlockQuads4 = transformer.processMany(blockModel4.getQuads(state, side, rand, extraData));
            quads.addAll(updatedBlockQuads4);
        }

        return quads;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
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
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return baseModel.getParticleTexture();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return null;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
