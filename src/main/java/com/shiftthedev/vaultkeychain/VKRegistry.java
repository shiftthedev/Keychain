package com.shiftthedev.vaultkeychain;

import com.shiftthedev.vaultkeychain.container.KeychainContainer;
import com.shiftthedev.vaultkeychain.item.KeychainItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;

public class VKRegistry
{
    public static final KeychainItem KEYCHAIN = new KeychainItem("keychain");

    public static MenuType<KeychainContainer> KEYCHAIN_CONTAINER;

    public VKRegistry()
    {
    }

    public static void register(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(KEYCHAIN);
    }

    public static void registerMenu(RegistryEvent.Register<MenuType<?>> event)
    {
        KEYCHAIN_CONTAINER = IForgeMenuType.create((windowId, inv, data) -> createKeychain(windowId, inv, data));

        event.getRegistry().registerAll(new MenuType[]{(MenuType) KEYCHAIN_CONTAINER.setRegistryName("keychain_container")});
    }

    private static KeychainContainer createKeychain(int windowId, Inventory inv, FriendlyByteBuf data)
    {
        int pouchSlot = data.readInt();
        return new KeychainContainer(windowId, inv, pouchSlot);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerPredicates()
    {
        ItemProperties.register(KEYCHAIN, new ResourceLocation("keys"),
                (itemStack, level, livingEntity, i) -> {
                    int totalKeys = KeychainItem.getTotalKeys(itemStack);
                    return totalKeys >= 3 ? 3 : totalKeys;
                });
    }
}
