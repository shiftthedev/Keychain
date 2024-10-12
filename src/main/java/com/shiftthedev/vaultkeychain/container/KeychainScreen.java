package com.shiftthedev.vaultkeychain.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.shiftthedev.vaultkeychain.VaultKeychain.MOD_ID;

public class KeychainScreen extends AbstractContainerScreen<KeychainContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/keychain.png");

    public KeychainScreen(KeychainContainer screenContainer, Inventory inventory, Component titleIn)
    {
        super(screenContainer, inventory, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 156;
        this.titleLabelX = 33;
        this.inventoryLabelY = 63;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int offsetX = (this.width - this.imageWidth) / 2;
        int offsetY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
