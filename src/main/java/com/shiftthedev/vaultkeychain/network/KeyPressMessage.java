package com.shiftthedev.vaultkeychain.network;

import com.shiftthedev.vaultkeychain.item.KeychainItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KeyPressMessage
{
    private final int slotIndex;

    public KeyPressMessage(int slotIndex)
    {
        this.slotIndex = slotIndex;
    }

    public int getSlotIndex()
    {
        return this.slotIndex;
    }

    public static void encode(KeyPressMessage message, FriendlyByteBuf buf)
    {
        buf.writeInt(message.getSlotIndex());
    }

    public static KeyPressMessage decode(FriendlyByteBuf buf)
    {
        return new KeyPressMessage(buf.readInt());
    }

    public static void handle(KeyPressMessage packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        contextSupplier.get().enqueueWork(() -> enqueueWork(packet, contextSupplier));
        contextSupplier.get().setPacketHandled(true);
    }

    private static void enqueueWork(KeyPressMessage packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        KeychainItem.openGUI(contextSupplier.get().getSender(), packet.getSlotIndex());
    }
}
