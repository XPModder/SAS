package com.xpmodder.slabsandstairs.client.rendering;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class CombinedBlockModelLoader implements IModelLoader {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public IModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return null;
    }
}
