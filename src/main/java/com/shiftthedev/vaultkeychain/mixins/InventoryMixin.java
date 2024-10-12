package com.shiftthedev.vaultkeychain.mixins;

import com.shiftthedev.vaultkeychain.config.VKConfig;
import com.shiftthedev.vaultkeychain.container.KeychainContainer;
import com.shiftthedev.vaultkeychain.server_helpers.InventoryHelper;
import iskallia.vault.item.ItemVaultKey;
import iskallia.vault.world.data.InventorySnapshotData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Inventory.class, priority = 900)
public abstract class InventoryMixin implements InventorySnapshotData.InventoryAccessor
{
    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void add_coinpouch(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir)
    {
        if (VKConfig.ENABLE_PICKUP.get())
        {
            if (itemStack.getItem() instanceof ItemVaultKey)
            {
                if (!(this.player.containerMenu instanceof KeychainContainer))
                {
                    if (InventoryHelper.try_pickupKeysToKeychain(itemStack, (Inventory) (Object) this))
                    {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Shadow
    @Final
    public Player player;

    public InventoryMixin()
    {
    }
}
