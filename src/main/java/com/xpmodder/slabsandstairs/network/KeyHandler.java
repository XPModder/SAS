package com.xpmodder.slabsandstairs.network;

import com.xpmodder.slabsandstairs.init.KeyInit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyHandler {

    public static boolean isPlacementModeDown = false;
    public static boolean isPlacementRotateDown = false;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onKeyEvent(InputEvent.Key event){
        if(Minecraft.getInstance().getConnection() != null) {
            if (event.getKey() == KeyInit.placementModeMapping.getKey().getValue()) {
                if (isPlacementModeDown != KeyInit.placementModeMapping.isDown()) {
                    isPlacementModeDown = KeyInit.placementModeMapping.isDown();
                    ModPacketHandler.INSTANCE.sendToServer(new ModPacketHandler.KeyMessage(isPlacementModeDown, isPlacementRotateDown));
                }
            } else if (event.getKey() == KeyInit.placementRotateMapping.getKey().getValue()) {
                if (isPlacementRotateDown != KeyInit.placementRotateMapping.isDown()) {
                    isPlacementRotateDown = KeyInit.placementRotateMapping.isDown();
                    ModPacketHandler.INSTANCE.sendToServer(new ModPacketHandler.KeyMessage(isPlacementModeDown, isPlacementRotateDown));
                    if(KeyInit.placementRotateMapping.isDown()){
                        KeyInit.placementRotateMapping.consumeClick();
                    }
                }
            }
        }
    }

}
