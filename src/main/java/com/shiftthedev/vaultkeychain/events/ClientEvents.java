package com.shiftthedev.vaultkeychain.events;

import com.shiftthedev.vaultkeychain.VaultKeychain;
import com.shiftthedev.vaultkeychain.client.KeyBindings;
import com.shiftthedev.vaultkeychain.container.KeychainScreen;
import com.shiftthedev.vaultkeychain.network.KeyPressMessage;
import com.shiftthedev.vaultkeychain.network.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.shiftthedev.vaultkeychain.VKRegistry.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientEvents
{
    @SubscribeEvent(
            priority = EventPriority.LOW
    )
    public static void setupClient(FMLClientSetupEvent event)
    {
        registerPredicates();
        registerScreen();

        KeyBindings.init();
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreen()
    {
        MenuScreens.register(KEYCHAIN_CONTAINER, KeychainScreen::new);
    }


    @EventBusSubscriber(modid = VaultKeychain.MOD_ID, value = {Dist.CLIENT})
    static class ClientForgeEvents
    {
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void onKeyInput(InputEvent.KeyInputEvent event)
        {
            if (KeyBindings.OPEN_KEYCHAIN.consumeClick())
            {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                if (player == null)
                {
                    return;
                }

                int slot = getKeychainSlot(player.getInventory());
                if (slot == -1)
                {
                    return;
                }

                NetworkManager.CHANNEL.sendToServer(new KeyPressMessage(slot));
            }
        }

        private static int getKeychainSlot(Inventory player)
        {
            for (int i = 0; i < player.items.size(); ++i)
            {
                ItemStack stack = player.items.get(i);
                if (!stack.isEmpty() && stack.is(KEYCHAIN))
                {
                    return i;
                }
            }

            return -1;
        }
    }
}
