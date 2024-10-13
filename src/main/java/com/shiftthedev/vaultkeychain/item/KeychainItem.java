package com.shiftthedev.vaultkeychain.item;

import com.shiftthedev.vaultkeychain.container.KeychainContainer;
import iskallia.vault.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.shiftthedev.vaultkeychain.VaultKeychain.MOD_ID;

public class KeychainItem extends Item
{
    public KeychainItem(String id)
    {
        super(new Item.Properties().stacksTo(1).tab(ModItems.VAULT_MOD_GROUP));
        this.setRegistryName(id);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if (!player.isCrouching())
        {
            return super.use(level, player, hand);
        }

        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer)
        {
            int keychainSlot;
            if (hand == InteractionHand.OFF_HAND)
            {
                keychainSlot = 40;
            }
            else
            {
                keychainSlot = player.getInventory().selected;
            }

            openGUI(player, keychainSlot);
        }

        return InteractionResultHolder.pass(stack);
    }

    public static void openGUI(Player player, int slot)
    {
        NetworkHooks.openGui((ServerPlayer) player, new MenuProvider()
        {
            @Override
            public Component getDisplayName()
            {
                return new TranslatableComponent("item." + MOD_ID + ".keychain");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
            {
                return new KeychainContainer(windowId, inventory, slot);
            }
        }, buf -> {
            buf.writeInt(slot);
        });
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag p_41424_)
    {
        super.appendHoverText(stack, level, tooltip, p_41424_);
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".info").withStyle(ChatFormatting.GRAY));

        tooltip.add(new TextComponent(""));

        ItemStack[] keys = getKeys(stack);
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".ashium", keys[0].getCount()).withStyle(Style.EMPTY.withColor(14512657)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".bomignite", keys[1].getCount()).withStyle(Style.EMPTY.withColor(4423906)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".gorginite", keys[2].getCount()).withStyle(Style.EMPTY.withColor(13508215)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".iskallium", keys[3].getCount()).withStyle(Style.EMPTY.withColor(8564995)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".petzanite", keys[4].getCount()).withStyle(Style.EMPTY.withColor(8846274)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".sparkletine", keys[5].getCount()).withStyle(Style.EMPTY.withColor(13220357)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".tubium", keys[6].getCount()).withStyle(Style.EMPTY.withColor(39512)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".upaline", keys[7].getCount()).withStyle(Style.EMPTY.withColor(13343165)));
        tooltip.add(new TranslatableComponent("tooltip." + MOD_ID + ".xenium", keys[8].getCount()).withStyle(Style.EMPTY.withColor(4141457)));

        tooltip.add(new TextComponent(""));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return oldStack.getItem() != newStack.getItem();
    }

    public static NonNullSupplier<IItemHandler> getInventorySupplier(final ItemStack itemStack)
    {
        return new NonNullSupplier<IItemHandler>()
        {
            @NotNull
            @Override
            public IItemHandler get()
            {
                return new Handler(itemStack);
            }
        };
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new ICapabilityProvider()
        {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
            {
                return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? LazyOptional.of(KeychainItem.getInventorySupplier(stack)).cast() : LazyOptional.empty();
            }
        };
    }

    public static int getTotalKeys(ItemStack keychain)
    {
        CompoundTag tag = keychain.getOrCreateTagElement("Inventory");
        return !tag.contains("total") ? 0 : tag.getInt("total");
    }

    public static boolean hasKey(ItemStack keychain, ItemStack key)
    {
        CompoundTag tag = keychain.getOrCreateTagElement("Inventory");
        if (key.getItem() == ModItems.ASHIUM_KEY)
        {
            return tag.contains("ashiumCount") && tag.getInt("ashiumCount") > 0;
        }
        if (key.getItem() == ModItems.BOMIGNITE_KEY)
        {
            return tag.contains("bomigniteCount") && tag.getInt("bomigniteCount") > 0;
        }

        if (key.getItem() == ModItems.GORGINITE_KEY)
        {
            return tag.contains("gorginiteCount") && tag.getInt("gorginiteCount") > 0;
        }

        if (key.getItem() == ModItems.ISKALLIUM_KEY)
        {
            return tag.contains("iskalliumCount") && tag.getInt("iskalliumCount") > 0;
        }

        if (key.getItem() == ModItems.PETZANITE_KEY)
        {
            return tag.contains("petzaniteCount") && tag.getInt("petzaniteCount") > 0;
        }

        if (key.getItem() == ModItems.SPARKLETINE_KEY)
        {
            return tag.contains("sparkletineCount") && tag.getInt("sparkletineCount") > 0;
        }

        if (key.getItem() == ModItems.TUBIUM_KEY)
        {
            return tag.contains("tubiumCount") && tag.getInt("tubiumCount") > 0;
        }

        if (key.getItem() == ModItems.UPALINE_KEY)
        {
            return tag.contains("upalineCount") && tag.getInt("upalineCount") > 0;
        }

        if (key.getItem() == ModItems.XENIUM_KEY)
        {
            return tag.contains("xeniumCount") && tag.getInt("xeniumCount") > 0;
        }

        return false;
    }

    public static void extractKeys(ItemStack keychain, ItemStack key)
    {
        CompoundTag tag = keychain.getOrCreateTagElement("Inventory");
        if (key.getItem() == ModItems.ASHIUM_KEY)
        {
            if (tag.contains("ashiumCount") && tag.getInt("ashiumCount") > 0)
            {
                setKeyStack(keychain, 0, tag.getInt("ashiumCount") - 1);
                return;
            }
        }
        else if (key.getItem() == ModItems.BOMIGNITE_KEY)
        {
            if (tag.contains("bomigniteCount") && tag.getInt("bomigniteCount") > 0)
            {
                setKeyStack(keychain, 1, tag.getInt("bomigniteCount") - 1);
                return;
            }
        }
        else if (key.getItem() == ModItems.GORGINITE_KEY)
        {
            {
                if (tag.contains("gorginiteCount") && tag.getInt("gorginiteCount") > 0)
                {
                    setKeyStack(keychain, 2, tag.getInt("gorginiteCount") - 1);
                    return;
                }
            }
        }
        else if (key.getItem() == ModItems.ISKALLIUM_KEY)
        {
            {
                if (tag.contains("iskalliumCount") && tag.getInt("iskalliumCount") > 0)
                {
                    setKeyStack(keychain, 3, tag.getInt("iskalliumCount") - 1);
                    return;
                }
            }
        }
        else if (key.getItem() == ModItems.PETZANITE_KEY)
        {
            {
                if (tag.contains("petzaniteCount") && tag.getInt("petzaniteCount") > 0)
                {
                    setKeyStack(keychain, 4, tag.getInt("petzaniteCount") - 1);
                    return;
                }
            }
        }
        else if (key.getItem() == ModItems.SPARKLETINE_KEY)
        {
            {
                if (tag.contains("sparkletineCount") && tag.getInt("sparkletineCount") > 0)
                {
                    setKeyStack(keychain, 5, tag.getInt("sparkletineCount") - 1);
                    return;
                }
            }
        }
        else if (key.getItem() == ModItems.TUBIUM_KEY)
        {
            {
                if (tag.contains("tubiumCount") && tag.getInt("tubiumCount") > 0)
                {
                    setKeyStack(keychain, 6, tag.getInt("tubiumCount") - 1);
                    return;
                }
            }
        }
        else if (key.getItem() == ModItems.UPALINE_KEY)
        {
            {
                if (tag.contains("upalineCount") && tag.getInt("upalineCount") > 0)
                {
                    setKeyStack(keychain, 7, tag.getInt("upalineCount") - 1);
                    return;
                }
            }
        }
        else if (key.getItem() == ModItems.XENIUM_KEY)
        {
            {
                if (tag.contains("xeniumCount") && tag.getInt("xeniumCount") > 0)
                {
                    setKeyStack(keychain, 8, tag.getInt("xeniumCount") - 1);
                    return;
                }
            }
        }
    }

    public static ItemStack[] getKeys(ItemStack keychain)
    {
        CompoundTag tag = keychain.getOrCreateTagElement("Inventory");
        int ashiumCount = tag.contains("ashiumCount") ? tag.getInt("ashiumCount") : 0;
        int bomigniteCount = tag.contains("bomigniteCount") ? tag.getInt("bomigniteCount") : 0;
        int gorginiteCount = tag.contains("gorginiteCount") ? tag.getInt("gorginiteCount") : 0;
        int iskalliumCount = tag.contains("iskalliumCount") ? tag.getInt("iskalliumCount") : 0;
        int petzaniteCount = tag.contains("petzaniteCount") ? tag.getInt("petzaniteCount") : 0;
        int sparkletineCount = tag.contains("sparkletineCount") ? tag.getInt("sparkletineCount") : 0;
        int tubiumCount = tag.contains("tubiumCount") ? tag.getInt("tubiumCount") : 0;
        int upalineCount = tag.contains("upalineCount") ? tag.getInt("upalineCount") : 0;
        int xeniumCount = tag.contains("xeniumCount") ? tag.getInt("xeniumCount") : 0;

        return new ItemStack[]{
                new ItemStack(ModItems.ASHIUM_KEY, ashiumCount),
                new ItemStack(ModItems.BOMIGNITE_KEY, bomigniteCount),
                new ItemStack(ModItems.GORGINITE_KEY, gorginiteCount),
                new ItemStack(ModItems.ISKALLIUM_KEY, iskalliumCount),
                new ItemStack(ModItems.PETZANITE_KEY, petzaniteCount),
                new ItemStack(ModItems.SPARKLETINE_KEY, sparkletineCount),
                new ItemStack(ModItems.TUBIUM_KEY, tubiumCount),
                new ItemStack(ModItems.UPALINE_KEY, upalineCount),
                new ItemStack(ModItems.XENIUM_KEY, xeniumCount)
        };
    }

    public static void setKeyStack(ItemStack keychain, int slot, int count)
    {
        CompoundTag tag = keychain.getOrCreateTagElement("Inventory");
        switch (slot)
        {
            case 0 -> tag.putInt("ashiumCount", count);
            case 1 -> tag.putInt("bomigniteCount", count);
            case 2 -> tag.putInt("gorginiteCount", count);
            case 3 -> tag.putInt("iskalliumCount", count);
            case 4 -> tag.putInt("petzaniteCount", count);
            case 5 -> tag.putInt("sparkletineCount", count);
            case 6 -> tag.putInt("tubiumCount", count);
            case 7 -> tag.putInt("upalineCount", count);
            case 8 -> tag.putInt("xeniumCount", count);
        }

        updateIcon(keychain, tag);
    }

    private static void updateIcon(ItemStack keychain, CompoundTag tag)
    {
        int total = 0;
        ItemStack[] keys = getKeys(keychain);
        for (ItemStack key : keys)
        {
            if (key.getCount() > 0)
            {
                total++;
            }

            if (total >= 3)
            {
                break;
            }
        }

        tag.putInt("total", total);
    }

    public static class Handler extends ItemStackHandler
    {
        protected final ItemStack delegate;

        public Handler(ItemStack delegate)
        {
            super();
            this.delegate = delegate;

            ItemStack[] keys = KeychainItem.getKeys(this.delegate);
            setSize(keys.length);
            for (int i = 0; i < keys.length; i++)
            {
                this.stacks.set(i, keys[i]);
            }
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            KeychainItem.setKeyStack(this.delegate, slot, this.getStackInSlot(slot).getCount());
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack)
        {
            validateSlotIndex(slot);
            this.stacks.set(slot, stack);
            this.onContentsChanged(slot);
        }

        public ItemStack extractItemGUI(int slot, int amount, boolean simulate)
        {
            if (amount == 0)
            {
                return ItemStack.EMPTY;
            }

            validateSlotIndex(slot);

            ItemStack existing = this.stacks.get(slot);
            if (existing.isEmpty())
            {
                return ItemStack.EMPTY;
            }

            int toExtract = Math.min(amount, existing.getMaxStackSize());
            if (existing.getCount() <= toExtract)
            {
                if (!simulate)
                {
                    this.stacks.set(slot, ItemStack.EMPTY);
                    onContentsChanged(slot);
                    return existing;
                }
                else
                {
                    return existing.copy();
                }
            }
            else
            {
                if (!simulate)
                {
                    this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                    onContentsChanged(slot);
                }

                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 64;
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack)
        {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack)
        {
            return (stack.getItem() == ModItems.ASHIUM_KEY && slot == 0) ||
                    (stack.getItem() == ModItems.BOMIGNITE_KEY && slot == 1) ||
                    (stack.getItem() == ModItems.GORGINITE_KEY && slot == 2) ||
                    (stack.getItem() == ModItems.ISKALLIUM_KEY && slot == 3) ||
                    (stack.getItem() == ModItems.PETZANITE_KEY && slot == 4) ||
                    (stack.getItem() == ModItems.SPARKLETINE_KEY && slot == 5) ||
                    (stack.getItem() == ModItems.TUBIUM_KEY && slot == 6) ||
                    (stack.getItem() == ModItems.UPALINE_KEY && slot == 7) ||
                    (stack.getItem() == ModItems.XENIUM_KEY && slot == 8);
        }
    }
}
