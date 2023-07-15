package com.xpmodder.slabsandstairs.client.gui;

import com.xpmodder.slabsandstairs.config.BlockListHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GUIHandler {

    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.InitScreenEvent event){

        if(!BlockListHandler.correctVersion && event.getScreen() instanceof TitleScreen){
            Screen oldScreen = Minecraft.getInstance().screen;
            Minecraft.getInstance().setScreen(new OldVersionScreen(oldScreen, BlockListHandler.version));
        }

    }

}
