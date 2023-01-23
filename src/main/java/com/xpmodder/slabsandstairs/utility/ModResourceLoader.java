package com.xpmodder.slabsandstairs.utility;


import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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

        Minecraft.getInstance().getResourcePackRepository().addPackFinder((consumer, iFactory) -> {
            final Pack packInfo = Pack.create(Reference.MODID, true, () -> new FolderPackResources(RESOURCE_FOLDER), iFactory, Pack.Position.TOP, PackSource.BUILT_IN);
            if(packInfo == null){
                LogHelper.error("Failed to load resource pack!");
                LogHelper.error("Blocks will have missing textures and models!");
                return;
            }
            consumer.accept(packInfo);
        });

    }

    @SubscribeEvent
    public void onLoadWorld(ServerStartingEvent event){

        try {

            MinecraftServer server = event.getServer();

            if(server == null){
                return;
            }

            server.getPackRepository().addPackFinder((consumer, iFactory) -> {
                final Pack packInfo = Pack.create(Reference.MODID, true, () -> new FolderPackResources(DATAPACK_FOLDER), iFactory, Pack.Position.TOP, PackSource.BUILT_IN);
                if (packInfo == null) {
                    LogHelper.error("Failed to load datapack!");
                    LogHelper.error("Blocks will have missing recipes!");
                    return;
                }
                consumer.accept(packInfo);
            });

            server.getPackRepository().reload();

            PackRepository resourcePackList = server.getPackRepository();
            server.reloadResources(resourcePackList.getSelectedIds());

        }
        catch(NullPointerException ex){
            LogHelper.info("NullpointerException in datapacksync!");
        }

    }


}
