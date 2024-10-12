package com.shiftthedev.vaultkeychain.network;

import com.shiftthedev.vaultkeychain.VaultKeychain;
import com.shiftthedev.vaultkeychain.config.VKConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfigSyncMessage
{
    private boolean pickup;

    public ConfigSyncMessage()
    {
        pickup = VKConfig.ENABLE_PICKUP.get();
    }

    public ConfigSyncMessage(boolean pickup)
    {
        this.pickup = pickup;
    }

    public static void encode(ConfigSyncMessage message, FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.pickup);
    }

    public static ConfigSyncMessage decode(FriendlyByteBuf buffer)
    {
        return new ConfigSyncMessage(buffer.readBoolean());
    }

    public static void handle(ConfigSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() ->
        {
            VKConfig.updateFromServer(message.pickup);
            VaultKeychain.LOGGER.info("Received config from server.");
        });
        context.setPacketHandled(true);
    }
}
