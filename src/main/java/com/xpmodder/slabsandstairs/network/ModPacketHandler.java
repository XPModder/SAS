package com.xpmodder.slabsandstairs.network;

import com.xpmodder.slabsandstairs.block.SlabBlock;
import com.xpmodder.slabsandstairs.reference.Reference;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MODID, "key-packet"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register(){
        int id = 0;
        INSTANCE.registerMessage(id++, KeyMessage.class, KeyMessage::encode, KeyMessage::decode, ModPacketHandler::handle);
        INSTANCE.registerMessage(id++, RotationMessage.class, RotationMessage::encode, RotationMessage::decode, ModPacketHandler::handle);
    }

    public static class KeyMessage{
        boolean placementMode, placementRotate;

        public KeyMessage(){}

        public KeyMessage(boolean placement, boolean rotate){
            this.placementMode = placement;
            this.placementRotate = rotate;
        }

        public void encode(FriendlyByteBuf buf){
            buf.writeBoolean(this.placementMode);
            buf.writeBoolean(this.placementRotate);
        }

        public static KeyMessage decode(FriendlyByteBuf buf){
            KeyMessage message = new KeyMessage();
            message.placementMode = buf.readBoolean();
            message.placementRotate = buf.readBoolean();
            return message;
        }

    }

    public static class RotationMessage{

        Direction rotation;
        boolean inverted;


        public RotationMessage(){}

        public RotationMessage(Direction dir, boolean inv){
            this.rotation = dir;
            this.inverted = inv;
        }

        public void encode(FriendlyByteBuf buf){
            buf.writeUtf(this.rotation.getName());
            buf.writeBoolean(this.inverted);
        }

        public static RotationMessage decode(FriendlyByteBuf buf){
            RotationMessage message = new RotationMessage();
            message.rotation = Direction.byName(buf.readUtf());
            message.inverted = buf.readBoolean();
            return message;
        }

    }

    public static void handle(KeyMessage msg, Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> {
            KeyHandler.isPlacementModeDown = msg.placementMode;
            KeyHandler.isPlacementRotateDown = msg.placementRotate;
        });
        context.get().setPacketHandled(true);
    }

    public static void handle(RotationMessage msg, Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> {
            SlabBlock.placementRotation = msg.rotation;
            SlabBlock.placementInverted = msg.inverted;
        });
        context.get().setPacketHandled(true);
    }


}
