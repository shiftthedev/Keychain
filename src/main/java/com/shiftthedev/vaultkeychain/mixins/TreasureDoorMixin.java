package com.shiftthedev.vaultkeychain.mixins;

import com.shiftthedev.vaultkeychain.item.KeychainItem;
import iskallia.vault.block.TreasureDoorBlock;
import iskallia.vault.core.event.CommonEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TreasureDoorBlock.class)
public abstract class TreasureDoorMixin extends DoorBlock
{
    public TreasureDoorMixin(Properties p_52737_)
    {
        super(p_52737_);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use_keychain(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir)
    {
        Boolean isOpen = (Boolean) state.getValue(TreasureDoorBlock.OPEN);
        if (!isOpen)
        {
            ItemStack heldStack = player.getItemInHand(hand);
            TreasureDoorBlock.Type type = (TreasureDoorBlock.Type) state.getValue(TreasureDoorBlock.TYPE);
            ItemStack keyStack = new ItemStack(type.getKey(), 1);

            if (KeychainItem.hasKey(heldStack, keyStack))
            {
                KeychainItem.extractKeys(heldStack, keyStack);
                this.setOpen(player, world, state, pos, true);
                CommonEvents.TREASURE_ROOM_OPEN.invoke(world, player, pos);

                cir.setReturnValue(InteractionResult.SUCCESS);
                cir.cancel();
                return;
            }
        }
    }
}
