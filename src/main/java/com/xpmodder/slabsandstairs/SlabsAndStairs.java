package com.xpmodder.slabsandstairs;

import com.xpmodder.slabsandstairs.client.rendering.ModelBakeEventHandler;
import com.xpmodder.slabsandstairs.config.BlockListHandler;
import com.xpmodder.slabsandstairs.config.ConfigurationHandler;
import com.xpmodder.slabsandstairs.init.BlockEntityInit;
import com.xpmodder.slabsandstairs.init.BlockInit;
import com.xpmodder.slabsandstairs.init.ItemInit;
import com.xpmodder.slabsandstairs.init.KeyInit;
import com.xpmodder.slabsandstairs.network.KeyHandler;
import com.xpmodder.slabsandstairs.network.ModPacketHandler;
import com.xpmodder.slabsandstairs.reference.Reference;
import com.xpmodder.slabsandstairs.utility.BlockTagTypes;
import com.xpmodder.slabsandstairs.utility.LogHelper;
import com.xpmodder.slabsandstairs.utility.ModResourceLoader;
import com.xpmodder.slabsandstairs.utility.ResourceGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod(Reference.MODID)
public class SlabsAndStairs {

    public static Logger ModLogger = LogManager.getLogger();
    public static final CreativeModeTab ITEM_GROUP_SAS = new CreativeModeTab("slabs_and_stairs") {
        @Override
        public @NotNull ItemStack makeIcon() {
            if(BlockInit.MY_BLOCKS.size() > 0) {
                return new ItemStack(BlockInit.MY_BLOCKS.stream().findFirst().get());
            }
            else{
                return new ItemStack(Blocks.OAK_PLANKS);
            }
        }
    };

    ModResourceLoader resourceLoader = new ModResourceLoader();
    ModelBakeEventHandler modelBakeEventHandler = new ModelBakeEventHandler();

    public SlabsAndStairs(){

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(KeyInit::onRegisterKeyMappings);
        eventBus.register(modelBakeEventHandler);

        BlockInit.BLOCKS.register(eventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(eventBus);
        ItemInit.ITEMS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(resourceLoader);

        MinecraftForge.EVENT_BUS.register(KeyHandler.class);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.COMMON_SPEC);

        ModPacketHandler.register();

        LogHelper.info("HANDLER");

        BlockListHandler.read();

        resourceLoader.load();

    }


    private void setup(final FMLCommonSetupEvent event){

        LogHelper.info("SETUP");
        boolean hasGenerated = ResourceGenerator.generate();
        if((resourceLoader.hasGenerated || hasGenerated) && FMLEnvironment.dist.isClient()) {
            Minecraft.getInstance().reloadResourcePacks();
        }

        ResourceGenerator.addToTag(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(BlockInit.combinedBlock.get())).toString(), BlockTagTypes.AXE);
        ResourceGenerator.addToTag(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(BlockInit.combinedBlock.get())).toString(), BlockTagTypes.PICKAXE);

    }

    private void clientSetup(final FMLClientSetupEvent event){

    }


}
