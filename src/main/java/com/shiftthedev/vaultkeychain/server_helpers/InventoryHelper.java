package com.shiftthedev.vaultkeychain.server_helpers;

import com.shiftthedev.vaultkeychain.VKRegistry;
import iskallia.vault.init.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;

public class InventoryHelper
{
    public static boolean try_pickupKeysToKeychain(ItemStack itemStack, Inventory thisInventory)
    {
        ItemStack pouchStack = ItemStack.EMPTY;
        Optional<ItemStack> possiblePouch = thisInventory.items.stream().filter(plStack -> plStack.is(VKRegistry.KEYCHAIN)).findFirst();
        if (possiblePouch.isEmpty())
        {
            return false;
        }

        pouchStack = possiblePouch.get();
        pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> handleKeychain(itemStack, iItemHandler));
        if (itemStack.isEmpty())
        {
            return true;
        }

        return false;
    }

    private static void handleKeychain(ItemStack key, IItemHandler iItemHandler)
    {
        ItemStack remainder = ItemStack.EMPTY;

        if (key.getItem() == ModItems.ASHIUM_KEY)
        {
            remainder = iItemHandler.insertItem(0, key, false);
        }
        else if (key.getItem() == ModItems.BOMIGNITE_KEY)
        {
            remainder = iItemHandler.insertItem(1, key, false);
        }
        else if (key.getItem() == ModItems.GORGINITE_KEY)
        {
            remainder = iItemHandler.insertItem(2, key, false);
        }
        else if (key.getItem() == ModItems.ISKALLIUM_KEY)
        {
            remainder = iItemHandler.insertItem(3, key, false);
        }
        else if (key.getItem() == ModItems.PETZANITE_KEY)
        {
            remainder = iItemHandler.insertItem(4, key, false);
        }
        else if (key.getItem() == ModItems.SPARKLETINE_KEY)
        {
            remainder = iItemHandler.insertItem(5, key, false);
        }
        else if (key.getItem() == ModItems.TUBIUM_KEY)
        {
            remainder = iItemHandler.insertItem(6, key, false);
        }
        else if (key.getItem() == ModItems.UPALINE_KEY)
        {
            remainder = iItemHandler.insertItem(7, key, false);
        }
        else if (key.getItem() == ModItems.XENIUM_KEY)
        {
            remainder = iItemHandler.insertItem(8, key, false);
        }

        key.setCount(remainder.getCount());
    }
}
