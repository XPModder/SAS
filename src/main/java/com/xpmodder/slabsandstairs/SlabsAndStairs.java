package com.xpmodder.slabsandstairs;

import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.init.ItemInit;
import com.xpmodder.slabsandstairs.init.KeyInit;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.ModResourceLoader;
import com.xpmodder.slabsandstairs.utility.ResourceGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class SlabsAndStairs {

    public static Logger ModLogger = LogManager.getLogger();
    public static final CreativeModeTab ITEM_GROUP_SAS = new CreativeModeTab("slabs_and_stairs") {
        @Override
        public ItemStack makeIcon() {
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

        BlockInit.BLOCKS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
        eventBus.register(new ItemInit());

        MinecraftForge.EVENT_BUS.register(resourceLoader);

        resourceLoader.load();

    }

    private void setup(final FMLCommonSetupEvent event){

        new ResourceGenerator().generate();
        if(resourceLoader.hasGenerated) {
            Minecraft.getInstance().reloadResourcePacks();
        }

    }

    private void clientSetup(final FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(BlockInit.previewStair.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.previewSlab.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlockInit.previewQuarter.get(), RenderType.translucent());

        KeyInit.init();
    }

}
