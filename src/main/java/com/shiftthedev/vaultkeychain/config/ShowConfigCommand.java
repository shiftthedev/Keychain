package com.shiftthedev.vaultkeychain.config;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class ShowConfigCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("keychain")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("config")
                        .executes(commandContext -> {
                            Minecraft.getInstance().tell(() -> {
                                try
                                {
                                    openConfigScreen();
                                    commandContext.getSource().sendSuccess(new TextComponent("Opening Keychain Configs"), true);
                                }
                                catch (Exception e2)
                                {
                                    commandContext.getSource().sendFailure(new TextComponent("Failed to run keychain command with error: " + e2.getMessage()));
                                }
                            });
                            return 1;
                        })));
    }

    private static void openConfigScreen() throws CommandSyntaxException
    {
        if (Minecraft.getInstance().player == null)
        {
            throw new CommandSyntaxException(null, new TextComponent("Not in single-player!"));
        }

        SubMenuConfigScreen screen = SubMenuConfigScreen.find(ConfigHelper.ConfigPath.parse("vaultkeychain:common.Common"));
        Minecraft.getInstance().setScreen(screen);
    }
}
