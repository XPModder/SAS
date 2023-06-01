package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class CombinedBlockBakedModel implements IDynamicBakedModel {


    public static ModelProperty<BlockState> BLOCK1 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK2 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK3 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK4 = new ModelProperty<>();
    public static ModelProperty<Integer> NUM_BLOCKS = new ModelProperty<>();

    private final ModelState modelState;
    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final ItemOverrides overrides;
    private final ItemTransforms transforms;


    public CombinedBlockBakedModel(ModelState modelState, Function<Material, TextureAtlasSprite> spriteGetter, ItemOverrides overrides, ItemTransforms itemTransforms){

        this.modelState = modelState;
        this.spriteGetter = spriteGetter;
        this.overrides = overrides;
        this.transforms = itemTransforms;

    }

    public CombinedBlockBakedModel(){

        this.modelState = null;
        this.spriteGetter = null;
        this.overrides = null;
        this.transforms = null;

    }


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
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK1)).getQuads(extraData.getData(BLOCK1), side, rand, EmptyModelData.INSTANCE));
                }
                if(extraData.getData(NUM_BLOCKS) >= 2){
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK2)).getQuads(extraData.getData(BLOCK2), side, rand, EmptyModelData.INSTANCE));
                }
                if(extraData.getData(NUM_BLOCKS) >= 3){
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK3)).getQuads(extraData.getData(BLOCK3), side, rand, EmptyModelData.INSTANCE));
                }
                if(extraData.getData(NUM_BLOCKS) == 4){
                    quads.addAll(shaper.getBlockModel(extraData.getData(BLOCK4)).getQuads(extraData.getData(BLOCK4), side, rand, EmptyModelData.INSTANCE));
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
        return overrides;
    }
}
