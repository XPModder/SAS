package com.xpmodder.slabsandstairs;

import com.xpmodder.slabsandstairs.client.rendering.PreviewRenderer;
import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.init.ItemInit;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import com.xpmodder.slabsandstairs.utility.ModResourceLoader;
import com.xpmodder.slabsandstairs.utility.ResourceGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.resources.DownloadingPackFinder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.resource.VanillaResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class SlabsAndStairs {

    public static Logger ModLogger = LogManager.getLogger();
    public static final ItemGroup ITEM_GROUP_SAS = new ItemGroup("slabs_and_stairs") {
        @Override
        public ItemStack createIcon() {
            if(BlockInit.MY_BLOCKS.size() > 0) {
                return new ItemStack(BlockInit.MY_BLOCKS.stream().findFirst().get());
            }
            else{
                return new ItemStack(Blocks.OAK_PLANKS);
            }
        }
    };

    ModResourceLoader resourceLoader = new ModResourceLoader();

    public SlabsAndStairs(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        eventBus.register(new BlockInit());
        eventBus.register(new ItemInit());


        MinecraftForge.EVENT_BUS.register(resourceLoader);

        resourceLoader.load();

    }

    private void setup(final FMLCommonSetupEvent event){

        new ResourceGenerator().generate();
        if(resourceLoader.hasGenerated) {
            ForgeHooksClient.refreshResources(Minecraft.getInstance(), VanillaResourceType.MODELS);
        }

    }

    private void clientSetup(final FMLClientSetupEvent event){
        RenderTypeLookup.setRenderLayer(BlockInit.previewStair, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.previewSlab, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockInit.previewQuarter, RenderType.getTranslucent());
    }

}
