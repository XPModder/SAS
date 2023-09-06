package com.xpmodder.slabsandstairs.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.xpmodder.slabsandstairs.config.BlockListHandler;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;


@OnlyIn(Dist.CLIENT)
public class OldVersionScreen extends Screen {

    private Screen oldScreen;
    private String fileVersion;

    public OldVersionScreen(Screen oldScreen, String fileVersion) {
        super(Component.literal("File version mismatch"));
        this.oldScreen = oldScreen;
        this.fileVersion = fileVersion;
    }

    public void init(){
        super.init();
        this.addRenderableWidget(new Button(this.width / 2 - 205, this.height - 25, 200, 20, CommonComponents.GUI_NO, (button) -> {
            BlockListHandler.correctVersion = true;
            Minecraft.getInstance().setScreen(this.oldScreen);
        }));
        this.addRenderableWidget(new Button(this.width / 2 + 5, this.height - 25, 200, 20, CommonComponents.GUI_YES, (button) -> {
            BlockListHandler.replace();
            Minecraft.getInstance().stop();
        }));
    }

    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick){
        this.fillGradient(poseStack, 0, 0, this.width, this.height, -12574688, -11530224);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 40, 16777215);
        drawCenteredString(poseStack, this.font, Component.literal("Your Version: " + this.fileVersion + ", Current Version: " + Reference.VERSION), this.width / 2, 70, 16777215);
        drawMultiLineCenteredString(poseStack, this.font, Component.translatable("message.file_version_mismatch"), this.width / 2, 90);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    private void drawMultiLineCenteredString(PoseStack poseStack, Font fr, Component str, int x, int y) {
        for (FormattedCharSequence s : fr.split(str, this.width)) {
            fr.drawShadow(poseStack, s, (float) (x - fr.width(s) / 2.0), y, 0xFFFFFF);
            y+=fr.lineHeight;
        }
    }

}
