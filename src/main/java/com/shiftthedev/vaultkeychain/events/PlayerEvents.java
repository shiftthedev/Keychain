package com.shiftthedev.vaultkeychain.events;

import com.shiftthedev.vaultkeychain.VaultKeychain;
import com.shiftthedev.vaultkeychain.network.ConfigSyncMessage;
import com.shiftthedev.vaultkeychain.network.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class PlayerEvents
{
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        VaultKeychain.LOGGER.info("Syncing config to {} ({})", event.getPlayer().getGameProfile().getName(), event.getPlayer().getGameProfile().getId());
        NetworkManager.CHANNEL.sendTo(new ConfigSyncMessage(), ((ServerPlayer) event.getPlayer()).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
