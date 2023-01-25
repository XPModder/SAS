package com.xpmodder.slabsandstairs.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public final class KeyInit {

    public static KeyMapping placementModeMapping;
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
