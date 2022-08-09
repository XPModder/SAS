package com.xpmodder.slabsandstairs.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.sun.javafx.geom.Vec3d;
import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class PreviewRenderer {

    @SubscribeEvent
    public static void renderWorldLastEvent(RenderWorldLastEvent event){

        Minecraft instance = Minecraft.getInstance();
        PlayerEntity player = instance.player;
        World worldIn = instance.world;

        Vector3d projectedView = instance.gameRenderer.getActiveRenderInfo().getProjectedView();

        if(player == null){
            return;
        }

        if(worldIn == null){
            return;
        }

        Hand hand = null;
        if(player.getHeldItemMainhand().getItem() instanceof BlockItem &&
                ((BlockItem)player.getHeldItemMainhand().getItem()).getBlock() instanceof SlabBlock){
            hand = Hand.MAIN_HAND;
        }
        else if(player.getHeldItemOffhand().getItem() instanceof BlockItem &&
                ((BlockItem)player.getHeldItemOffhand().getItem()).getBlock() instanceof SlabBlock){
            hand = Hand.OFF_HAND;
        }

        if(hand == null){
            return;
        }

        RayTraceResult blockResult = player.pick(20, 0, false);

        if(blockResult.getType() == RayTraceResult.Type.BLOCK){

            BlockPos pos = ((BlockRayTraceResult)blockResult).getPos();
            Direction face = ((BlockRayTraceResult)blockResult).getFace();
            //Get the position one block in the direction of the player aka. the position where the block would be placed
            pos = pos.offset(face);

            ItemStack stack = player.getHeldItem(hand);

            BlockItemUseContext useContext = new BlockItemUseContext(worldIn, player, hand, stack, (BlockRayTraceResult) blockResult);
            BlockState placementState = ((BlockItem)stack.getItem()).getBlock().getStateForPlacement(useContext);

            if(placementState == null){
                return;
            }

            MatrixStack matrixStack = event.getMatrixStack();

            IRenderTypeBuffer.Impl renderTypeBuffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

            renderBlock(matrixStack, renderTypeBuffer, worldIn, placementState, pos, projectedView, new Vector3d(pos.getX() + 0.075f, pos.getY() + 0.075, pos.getZ() + 0.075f));

            renderTypeBuffer.finish();

        }

    }

    public static void renderBlock(MatrixStack matrixStack, IRenderTypeBuffer.Impl renderTypeBuffer, World world, BlockState blockState, BlockPos logicPos, Vector3d projectedView, Vector3d renderCoordinates)
    {
        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        int i = OverlayTexture.NO_OVERLAY;

        matrixStack.push();
        matrixStack.translate(-projectedView.x + renderCoordinates.x, -projectedView.y + renderCoordinates.y, -projectedView.z + renderCoordinates.z);

        matrixStack.scale(0.85f, 0.85f, 0.85f);


        //TODO: make rendering translucent

        for(RenderType renderType : RenderType.getBlockRenderTypes())
        {
            if(RenderTypeLookup.canRenderInLayer(blockState, renderType))
                blockRendererDispatcher.getBlockModelRenderer().renderModel(world, blockRendererDispatcher.getModelForState(blockState), blockState, logicPos, matrixStack, renderTypeBuffer.getBuffer(renderType), true, new Random(), blockState.getPositionRandom(logicPos), i, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
        }

        matrixStack.pop();
    }

}
