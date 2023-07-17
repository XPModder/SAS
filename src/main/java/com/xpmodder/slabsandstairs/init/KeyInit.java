package com.xpmodder.slabsandstairs.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.xpmodder.slabsandstairs.network.ModPacketHandler;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class KeyInit {

    @OnlyIn(Dist.CLIENT)
    public static KeyMapping placementModeMapping;
    @OnlyIn(Dist.CLIENT)
    public static KeyMapping placementRotateMapping;

    public static void init(){

        placementModeMapping = registerKey("placementMode", InputConstants.KEY_LALT);
        placementRotateMapping = registerKey("placementRotate", InputConstants.MOUSE_BUTTON_MIDDLE);

    }

    public static KeyMapping registerKey(String name, int key){
        KeyMapping regKey = new KeyMapping("key." + Reference.MODID + "." + name, key, Reference.KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(regKey);
        return regKey;
    }


}
