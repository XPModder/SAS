package com.xpmodder.slabsandstairs.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class KeyInit {

    @OnlyIn(Dist.CLIENT)
    public static KeyMapping placementModeMapping;
    @OnlyIn(Dist.CLIENT)
    public static KeyMapping placementRotateMapping;

    public static List<KeyMapping> mappings = new ArrayList<>();

    static{

        placementModeMapping = registerKey("placementMode", InputConstants.KEY_LALT);
        placementRotateMapping = registerKey("placementRotate", InputConstants.KEY_V);

    }

    public static KeyMapping registerKey(String name, int key){
        KeyMapping mapping = new KeyMapping("key." + Reference.MODID + "." + name, key, Reference.KEY_CATEGORY);
        mappings.add(mapping);
        return mapping;
    }

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event){
        for(KeyMapping mapping : mappings){
            event.register(mapping);
        }
    }


}
