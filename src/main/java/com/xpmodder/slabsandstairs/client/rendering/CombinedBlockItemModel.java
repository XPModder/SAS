package com.xpmodder.slabsandstairs.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraftforge.common.model.TransformationHelper.quatFromXYZ;

public class CombinedBlockItemModel extends CombinedBlockBakedModel {

    private final BlockState Block1, Block2, Block3, Block4;

    public CombinedBlockItemModel(@Nullable BlockState state1,@Nullable  BlockState state2,@Nullable  BlockState state3,@Nullable  BlockState state4){
        super();
        Block1 = state1;
        Block2 = state2;
        Block3 = state3;
        Block4 = state4;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {

        List<BakedQuad> quads = new ArrayList<>();

        BlockModelShaper shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();

        try {

            if (Block1 != null) {
                quads.addAll(shaper.getBlockModel(Block1).getQuads(extraData.getData(BLOCK1), side, rand, extraData));
            }
            if (Block2 != null) {
                quads.addAll(shaper.getBlockModel(Block2).getQuads(extraData.getData(BLOCK2), side, rand, extraData));
            }
            if (Block3 != null) {
                quads.addAll(shaper.getBlockModel(Block3).getQuads(extraData.getData(BLOCK3), side, rand, extraData));
            }
            if (Block4 != null) {
                quads.addAll(shaper.getBlockModel(Block4).getQuads(extraData.getData(BLOCK4), side, rand, extraData));
            }


        }
        catch (NullPointerException ex){
            LogHelper.error("Could not combine all models! Got NullPointerException trying to get block!");
        }

        return quads;
    }

    @SuppressWarnings("deprecation")
    public BakedModel handlePerspective(ItemTransforms.TransformType type, PoseStack poseStack)
    {

        BakedModel model = this;

        ItemTransform transform = ItemTransform.NO_TRANSFORM;
        Vector3f scale3 = new Vector3f(0.375f, 0.375f, 0.375f);
        Vector3f scale1 = new Vector3f(0.6f, 0.6f, 0.6f);
        Vector3f rotate3 = new Vector3f(10.0f, -45.0f, 0.0f);
        Vector3f rotate2 = new Vector3f(70.0f, -45.0f, 0.0f);
        Vector3f rotate1 = new Vector3f(30.0f, -45.0f, 0.0f);
        Vector3f translate = new Vector3f(0.0f, 0.2f, 0.0f);

        Transformation tr;

        if(type == ItemTransforms.TransformType.GUI){
            tr = new Transformation(transform.translation, quatFromXYZ(rotate1, true), scale1, null);
        }
        else if(type == ItemTransforms.TransformType.GROUND){
            tr = new Transformation(transform.translation, quatFromXYZ(transform.rotation, true), scale3, null);
        }
        else if(type == ItemTransforms.TransformType.FIXED || type == ItemTransforms.TransformType.NONE){
            tr = new Transformation(transform.translation, quatFromXYZ(transform.rotation, true), transform.scale, null);
        }
        else{
            if(type.firstPerson()) {
                tr = new Transformation(transform.translation, quatFromXYZ(rotate3, true), scale3, null);
            }
            else{
                tr = new Transformation(translate, quatFromXYZ(rotate2, true), scale3, null);
            }
        }

        if(!tr.isIdentity()) {
            tr.push(poseStack);
        }
        return model;

    }


}
