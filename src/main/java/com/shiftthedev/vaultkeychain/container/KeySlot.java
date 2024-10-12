package com.shiftthedev.vaultkeychain.container;

import com.shiftthedev.vaultkeychain.item.KeychainItem;
import iskallia.vault.container.slot.ConditionalReadSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class KeySlot extends ConditionalReadSlot
{
    public KeySlot(IItemHandler inventory, int index, int xPosition, int yPosition, KeychainContainer container, Item defaultItem)
    {
        super(inventory, index, xPosition, yPosition, (slot, itemStack) -> container.canAccess(slot, itemStack) && itemStack.getItem() == defaultItem);
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public void set(@NotNull ItemStack stack)
    {
        ((KeychainItem.Handler) this.getItemHandler()).setStackInSlot(this.getSlotIndex(), stack);
        this.setChanged();
    }

    @NotNull
    @Override
    public ItemStack remove(int amount)
    {
        ItemStack stack = ((KeychainItem.Handler) this.getItemHandler()).extractItemGUI(this.getSlotIndex(), amount, false);
        this.setChanged();

        return stack;
    }

    @Override
    public void onTake(Player p_150645_, ItemStack p_150646_)
    {
        this.setChanged();

        ((KeychainItem.Handler) this.getItemHandler()).setStackInSlot(this.getSlotIndex(), getItem());
    }
}
