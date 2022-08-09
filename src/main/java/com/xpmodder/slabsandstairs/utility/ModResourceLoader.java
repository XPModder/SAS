package com.xpmodder.slabsandstairs.utility;


import com.google.common.collect.Lists;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.command.impl.DataPackCommand;
import net.minecraft.resources.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.xpmodder.slabsandstairs.reference.Reference.DATAPACK_FOLDER;
import static com.xpmodder.slabsandstairs.reference.Reference.RESOURCE_FOLDER;

public class ModResourceLoader {


    public boolean hasGenerated = false;

    public void load(){

        if(!RESOURCE_FOLDER.exists() || !RESOURCE_FOLDER.isDirectory()){
            if(RESOURCE_FOLDER.isFile()) {
                LogHelper.error("Mod resource folder not present! Found file!");
            }
            if(!RESOURCE_FOLDER.mkdirs()){
                LogHelper.error("Could not create mod resource folder!");
                LogHelper.error("Blocks will have missing textures and models!");
            }
            else{
                LogHelper.info("Mod resource folder created!");
                try {
                    File mcmetaFile = new File(RESOURCE_FOLDER + "/pack.mcmeta");
                    if(!mcmetaFile.exists()){
                        if(!mcmetaFile.createNewFile()){
                            LogHelper.error("Exception trying to create new mcmeta!");
                        }
                    }
                    OutputStream mcmeta = new FileOutputStream(mcmetaFile);
                    InputStream in = this.getClass().getResourceAsStream("/assets/" + Reference.MODID + "/default/pack.mcmeta");
                    IOUtils.copy(in, mcmeta);
                }
                catch (Exception ex){
                    LogHelper.error("Exception trying to extract mcmeta!");
                    LogHelper.error(ex.fillInStackTrace());
                }
                hasGenerated = true;
            }
        }

        if(!DATAPACK_FOLDER.exists() || !DATAPACK_FOLDER.isDirectory()){
            if(DATAPACK_FOLDER.isFile()) {
                LogHelper.error("Mod resource folder not present! Found file!");
            }
            if(!DATAPACK_FOLDER.mkdirs()){
                LogHelper.error("Could not create mod resource folder!");
                LogHelper.error("Blocks will have missing textures and models!");
            }
            else{
                LogHelper.info("Mod resource folder created!");
                try {
                    File mcmetaFile = new File(DATAPACK_FOLDER + "/pack.mcmeta");
                    if(!mcmetaFile.exists()){
                        if(!mcmetaFile.createNewFile()){
                            LogHelper.error("Exception trying to create new mcmeta!");
                        }
                    }
                    OutputStream mcmeta = new FileOutputStream(mcmetaFile);
                    InputStream in = this.getClass().getResourceAsStream("/assets/" + Reference.MODID + "/default/datapack.mcmeta");
                    IOUtils.copy(in, mcmeta);
                }
                catch (Exception ex){
                    LogHelper.error("Exception trying to extract mcmeta!");
                    LogHelper.error(ex.fillInStackTrace());
                }
                hasGenerated = true;
            }
        }

        Minecraft.getInstance().getResourcePackList().addPackFinder((consumer, iFactory) -> {
            final ResourcePackInfo packInfo = ResourcePackInfo.createResourcePack(Reference.MODID, true, () -> new FolderPack(RESOURCE_FOLDER), iFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
            if(packInfo == null){
                LogHelper.error("Failed to load resource pack!");
                LogHelper.error("Blocks will have missing textures and models!");
                return;
            }
            consumer.accept(packInfo);
        });

    }

    @SubscribeEvent
    public void onLoadWorld(FMLServerStartingEvent event){

        try {

            MinecraftServer server = event.getServer();

            if(server == null){
                return;
            }

            server.getResourcePacks().addPackFinder((consumer, iFactory) -> {
                final ResourcePackInfo packInfo = ResourcePackInfo.createResourcePack(Reference.MODID, true, () -> new FolderPack(DATAPACK_FOLDER), iFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
                if (packInfo == null) {
                    LogHelper.error("Failed to load datapack!");
                    LogHelper.error("Blocks will have missing recipes!");
                    return;
                }
                consumer.accept(packInfo);
            });

            server.getResourcePacks().reloadPacksFromFinders();

            ResourcePackList resourcePackList = server.getResourcePacks();
            List<ResourcePackInfo> list = Lists.newArrayList(resourcePackList.getEnabledPacks());
            server.func_240780_a_(list.stream().map(ResourcePackInfo::getName).collect(Collectors.toList()));

        }
        catch(NullPointerException ex){
            LogHelper.info("NullpointerException in datapacksync!");
        }

    }


}
