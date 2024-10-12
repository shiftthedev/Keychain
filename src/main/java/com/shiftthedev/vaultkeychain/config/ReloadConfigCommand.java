package com.shiftthedev.vaultkeychain.config;

import com.mojang.brigadier.CommandDispatcher;
import com.shiftthedev.vaultkeychain.VaultKeychain;
import com.shiftthedev.vaultkeychain.network.ConfigSyncMessage;
import com.shiftthedev.vaultkeychain.network.NetworkManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

public class ReloadConfigCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("keychain")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(commandContext -> {
                            reloadConfig(commandContext.getSource().getEntity());
                            commandContext.getSource().sendSuccess(new TextComponent("Keychain configs reloaded!"), true);
                            return 1;
                        })));
    }

    private static void reloadConfig(Entity sourceEntity)
    {
        if (sourceEntity == null || sourceEntity instanceof ServerPlayer)
        {
            VKConfig.reloadConfig();
            VaultKeychain.LOGGER.info("Syncing reloaded configs to all players");
            NetworkManager.CHANNEL.send(PacketDistributor.ALL.noArg(), new ConfigSyncMessage());
        }
        else
        {
            VKConfig.reloadConfig();
        }
    }
}
