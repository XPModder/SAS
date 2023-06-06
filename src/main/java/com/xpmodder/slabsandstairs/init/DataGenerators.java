package com.xpmodder.slabsandstairs.init;


import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event){

        DataGenerator generator = event.getGenerator();

        if(event.includeClient()){
            generator.addProvider(new ModBlockStates(generator, event.getExistingFileHelper()));
        }

    }

}
