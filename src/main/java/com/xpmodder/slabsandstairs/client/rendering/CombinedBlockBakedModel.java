package com.xpmodder.slabsandstairs.client.rendering;

import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import com.xpmodder.slabsandstairs.utility.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CombinedBlockBakedModel implements IDynamicBakedModel {

    public static ModelProperty<BlockState> BLOCK1 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK2 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK3 = new ModelProperty<>();
    public static ModelProperty<BlockState> BLOCK4 = new ModelProperty<>();
    public static ModelProperty<Integer> NUM_BLOCKS = new ModelProperty<>();


    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data){
        return ChunkRenderTypeSet.of(RenderType.translucent());
    }


    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, RenderType type) {

        List<BakedQuad> quads = new ArrayList<>();

        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();

        try {

            List<BakedQuad> removeList = new ArrayList<>();
            List<BakedQuad> transparentQuads = new ArrayList<>();
            List<BakedQuad> partiallyCovered = new ArrayList<>();

            if (extraData.has(NUM_BLOCKS)) {

                if (extraData.get(NUM_BLOCKS) >= 1) {
                    //Get the quads from the block, remove any that are in the same place as existing quads, mark corresponding existing quad for removal and add remaining quads
                    List<BakedQuad> model = shaper.getBlockModel(Objects.requireNonNull(extraData.get(BLOCK1))).getQuads(extraData.get(BLOCK1), side, rand, extraData, type);
                    if(Objects.requireNonNull(extraData.get(BLOCK1)).getMaterial() == Material.GLASS){
                        transparentQuads.addAll(model);
                    }
                    quads.addAll(model);
                }
                if (extraData.get(NUM_BLOCKS) >= 2) {
                    List<BakedQuad> model = shaper.getBlockModel(Objects.requireNonNull(extraData.get(BLOCK2))).getQuads(extraData.get(BLOCK2), side, rand, extraData, type);
                    if(Objects.requireNonNull(extraData.get(BLOCK2)).getMaterial() == Material.GLASS){
                        transparentQuads.addAll(model);
                    }
                    quads.addAll(model);
                }
                if (extraData.get(NUM_BLOCKS) >= 3) {
                    List<BakedQuad> model = shaper.getBlockModel(Objects.requireNonNull(extraData.get(BLOCK3))).getQuads(extraData.get(BLOCK3), side, rand, extraData, type);
                    if(Objects.requireNonNull(extraData.get(BLOCK3)).getMaterial() == Material.GLASS){
                        transparentQuads.addAll(model);
                    }
                    quads.addAll(model);
                }
                if (extraData.get(NUM_BLOCKS) == 4) {
                    List<BakedQuad> model = shaper.getBlockModel(Objects.requireNonNull(extraData.get(BLOCK4))).getQuads(extraData.get(BLOCK4), side, rand, extraData, type);
                    if(Objects.requireNonNull(extraData.get(BLOCK4)).getMaterial() == Material.GLASS){
                        transparentQuads.addAll(model);
                    }
                    quads.addAll(model);
                }

            }
            else{
                quads.addAll(shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getQuads(BlockInit.previewSlab.get().defaultBlockState(), side, rand, extraData, type));
            }

            for(int i = 0; i < quads.size(); i++){
                for(int j = 0; j < quads.size(); j++){
                    if(i != j && !removeList.contains(quads.get(j))){
                        int res = Rect.fromQuad(quads.get(i)).compareTo(Rect.fromQuad(quads.get(j)));
                        if(res == 0){
                            if(transparentQuads.contains(quads.get(i)) && !transparentQuads.contains(quads.get(j))){
                                removeList.add(quads.get(i));
                            } else if(transparentQuads.contains(quads.get(j)) && !transparentQuads.contains(quads.get(i))){
                                removeList.add(quads.get(j));
                            } else {
                                removeList.add(quads.get(i));
                                removeList.add(quads.get(j));
                            }
                        } else if(res == 1){
                            if(transparentQuads.contains(quads.get(j)) && !transparentQuads.contains(quads.get(i))){
                                continue;
                            }
                            removeList.add(quads.get(i));
                            if(partiallyCovered.contains(quads.get(j))){
                                removeList.add(quads.get(j));
                            } else {
                                partiallyCovered.add(quads.get(j));
                            }
                        }
                    }
                }
            }

            //LogHelper.info(removeList.size() + " quads marked for removal!");
            //Remove all quads that were marked for removal, thus completely removing duplicate faces
            quads.removeAll(removeList);


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
        return shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getParticleIcon(ModelData.builder().build());
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return new CombinedBlockItemOverrides();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(ModelData modelData){
        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
        if(modelData.has(BLOCK4) && modelData.get(BLOCK4) != null && !Objects.requireNonNull(modelData.get(BLOCK4)).isAir()){

            return shaper.getBlockModel(Objects.requireNonNull(modelData.get(BLOCK4))).getParticleIcon(ModelData.builder().build());

        }
        else if(modelData.has(BLOCK3) && modelData.get(BLOCK3) != null && !Objects.requireNonNull(modelData.get(BLOCK3)).isAir()){

            return shaper.getBlockModel(Objects.requireNonNull(modelData.get(BLOCK3))).getParticleIcon(ModelData.builder().build());

        }
        else if(modelData.has(BLOCK2) && modelData.get(BLOCK2) != null && !Objects.requireNonNull(modelData.get(BLOCK2)).isAir()){

            return shaper.getBlockModel(Objects.requireNonNull(modelData.get(BLOCK2))).getParticleIcon(ModelData.builder().build());

        }
        else if(modelData.has(BLOCK1) && modelData.get(BLOCK1) != null && !Objects.requireNonNull(modelData.get(BLOCK1)).isAir()){

            return shaper.getBlockModel(Objects.requireNonNull(modelData.get(BLOCK1))).getParticleIcon(ModelData.builder().build());

        }
        return shaper.getBlockModel(BlockInit.previewSlab.get().defaultBlockState()).getParticleIcon(ModelData.builder().build());
    }




}
