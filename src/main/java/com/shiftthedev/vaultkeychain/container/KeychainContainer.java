package com.shiftthedev.vaultkeychain.container;

import com.shiftthedev.vaultkeychain.VKRegistry;
import com.shiftthedev.vaultkeychain.item.KeychainItem;
import iskallia.vault.container.oversized.OverSizedSlotContainer;
import iskallia.vault.container.slot.ConditionalReadSlot;
import iskallia.vault.init.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class KeychainContainer extends OverSizedSlotContainer
{
    private final int pouchSlot;
    private final Inventory inventory;

    public KeychainContainer(int id, Inventory playerInventory, int pouchSlot)
    {
        super(VKRegistry.KEYCHAIN_CONTAINER, id, playerInventory.player);
        this.inventory = playerInventory;
        this.pouchSlot = pouchSlot;

        if (!this.hasPouch(playerInventory.player))
        {
            return;
        }

        playerInventory.player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(playerHandler -> {

            ItemStack pouch = this.inventory.getItem(this.pouchSlot);
            if (pouch.isEmpty())
            {
                return;
            }

            pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(keychainHandler -> {
                this.initSlots(playerHandler, keychainHandler);
            });

        });
    }

    private void initSlots(IItemHandler playerHandler, final IItemHandler pouchHandler)
    {
        int hotbarSlot;
        // Player Inventory
        for (hotbarSlot = 0; hotbarSlot < 3; ++hotbarSlot)
        {
            for (int column = 0; column < 9; ++column)
            {
                this.addSlot(new ConditionalReadSlot(playerHandler, column + hotbarSlot * 9 + 9, 8 + column * 18, 74 + hotbarSlot * 18, this::canAccess));
            }
        }

        // Player Hotbar
        for (hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot)
        {
            this.addSlot(new ConditionalReadSlot(playerHandler, hotbarSlot, 8 + hotbarSlot * 18, 132, this::canAccess));
        }

        // Keychain Slots
        this.addSlot(new KeySlot(pouchHandler, 0, 40, 19, this, ModItems.ASHIUM_KEY));
        this.addSlot(new KeySlot(pouchHandler, 1, 60, 19, this, ModItems.BOMIGNITE_KEY));
        this.addSlot(new KeySlot(pouchHandler, 2, 80, 19, this, ModItems.GORGINITE_KEY));
        this.addSlot(new KeySlot(pouchHandler, 3, 100, 19, this, ModItems.ISKALLIUM_KEY));
        this.addSlot(new KeySlot(pouchHandler, 4, 120, 19, this, ModItems.PETZANITE_KEY));

        this.addSlot(new KeySlot(pouchHandler, 5, 50, 39, this, ModItems.SPARKLETINE_KEY));
        this.addSlot(new KeySlot(pouchHandler, 6, 70, 39, this, ModItems.TUBIUM_KEY));
        this.addSlot(new KeySlot(pouchHandler, 7, 90, 39, this, ModItems.UPALINE_KEY));
        this.addSlot(new KeySlot(pouchHandler, 8, 110, 39, this, ModItems.XENIUM_KEY));
    }

    @Override
    public boolean stillValid(Player player)
    {
        return this.hasPouch(player);
    }

    public boolean canAccess(int slot, ItemStack slotStack)
    {
        return !(slotStack.getItem() instanceof KeychainItem);
    }

    public boolean hasPouch(Player player)
    {
        if (this.pouchSlot == -1)
        {
            return false;
        }

        ItemStack pouchStack = this.inventory.getItem(this.pouchSlot);
        return !pouchStack.isEmpty();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (index >= 0 && index < 36 && this.moveItemStackTo(slotStack, 36, 45, false))
            {
                return itemStack;
            }

            if (index >= 0 && index < 27)
            {
                if (!this.moveItemStackTo(slotStack, 27, 36, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 27 && index < 36)
            {
                if (!this.moveItemStackTo(slotStack, 0, 27, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotStack, 0, 36, false))
            {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return itemStack;
    }
}
