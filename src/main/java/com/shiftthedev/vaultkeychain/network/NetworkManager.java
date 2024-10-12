package com.shiftthedev.vaultkeychain.network;

import com.shiftthedev.vaultkeychain.VaultKeychain;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static com.shiftthedev.vaultkeychain.VaultKeychain.MOD_ID;

public class NetworkManager
{
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL;
    private static int ID;

    public static void initializeNetwork()
    {
        VaultKeychain.LOGGER.info("Initializing network with version " + VERSION);
        CHANNEL.registerMessage(nextId(), ConfigSyncMessage.class, ConfigSyncMessage::encode, ConfigSyncMessage::decode, ConfigSyncMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(nextId(), KeyPressMessage.class, KeyPressMessage::encode, KeyPressMessage::decode, KeyPressMessage::handle);
    }

    public static int nextId()
    {
        return ID++;
    }

    private static boolean accepted(String version)
    {
        return version.equals(VERSION);
    }

    static
    {
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "network"), () -> VERSION, NetworkManager::accepted, NetworkManager::accepted);
        ID = 0;
    }
}
