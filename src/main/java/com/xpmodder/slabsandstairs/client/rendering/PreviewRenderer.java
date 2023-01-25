package com.xpmodder.slabsandstairs.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.xpmodder.slabsandstairs.block.QuarterBlock;
import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.block.StairBlock;
import com.xpmodder.slabsandstairs.init.BlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class PreviewRenderer {

    @SubscribeEvent
    public static void renderWorldLastEvent(RenderLevelStageEvent event){

        if(event.getStage() != RenderLevelStageEvent.Stage.fromRenderType(RenderType.cutout())){
            return;
        }

        Minecraft instance = Minecraft.getInstance();
        Player player = instance.player;
        Level worldIn = instance.level;

        if(player == null){
            return;
        }

        if(worldIn == null){
            return;
        }

        InteractionHand hand = null;
        if(player.getMainHandItem().getItem() instanceof BlockItem &&
                ((BlockItem)player.getMainHandItem().getItem()).getBlock() instanceof SlabBlock){
            hand = InteractionHand.MAIN_HAND;
        }
        else if(player.getOffhandItem().getItem() instanceof BlockItem &&
                ((BlockItem)player.getOffhandItem().getItem()).getBlock() instanceof SlabBlock){
            hand = InteractionHand.OFF_HAND;
        }

        if(hand == null){
            return;
        }

        HitResult blockResult = player.pick(20, 0, false);

        if(blockResult.getType() == HitResult.Type.BLOCK){

            BlockPos pos = ((BlockHitResult)blockResult).getBlockPos();
            Direction face = ((BlockHitResult)blockResult).getDirection();
            //Get the position one block in the direction of the player aka. the position where the block would be placed
            pos = pos.offset(face.getNormal());

            ItemStack stack = player.getItemInHand(hand);

            BlockPlaceContext useContext = new BlockPlaceContext(worldIn, player, hand, stack, (BlockHitResult) blockResult);
            BlockState placementState = BlockInit.previewSlab.get().getStateForPlacement(useContext);

            if(((BlockItem) stack.getItem()).getBlock() instanceof StairBlock){
                placementState = BlockInit.previewStair.get().getStateForPlacement(useContext);
            } else if (((BlockItem) stack.getItem()).getBlock() instanceof QuarterBlock) {
                placementState = BlockInit.previewQuarter.get().getStateForPlacement(useContext);
            }

            if(placementState == null){
                return;
            }

            PoseStack matrixStack = event.getPoseStack();

            Vec3 cameraPosition = event.getCamera().getPosition();

            renderBlock(matrixStack, placementState, new BlockPos(pos.getX() + 0.075f, pos.getY() + 0.075, pos.getZ() + 0.075f), cameraPosition);

        }

    }

    public static void renderBlock(PoseStack matrixStack, BlockState blockState, BlockPos pos, Vec3 cameraPos)
    {

        Minecraft instance = Minecraft.getInstance();

        BlockRenderDispatcher blockRendererDispatcher = instance.getBlockRenderer();

        matrixStack.pushPose();
        matrixStack.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y, pos.getZ() - cameraPos.z);

        matrixStack.scale(1.0f, 1.0f, 1.0f);

        blockRendererDispatcher.getModelRenderer().renderModel(matrixStack.last(), instance.renderBuffers().bufferSource().getBuffer(RenderType.translucent()), blockState, blockRendererDispatcher.getBlockModel(blockState), 255.0f, 255.0f, 255.0f, 15728880, OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);

        matrixStack.popPose();
    }

}
