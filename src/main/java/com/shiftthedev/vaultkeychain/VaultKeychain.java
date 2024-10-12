package com.shiftthedev.vaultkeychain;

import com.electronwill.nightconfig.core.file.FormatDetector;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mojang.logging.LogUtils;
import com.shiftthedev.vaultkeychain.config.ReloadConfigCommand;
import com.shiftthedev.vaultkeychain.config.ShowConfigCommand;
import com.shiftthedev.vaultkeychain.config.VKConfig;
import com.shiftthedev.vaultkeychain.network.NetworkManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(VaultKeychain.MOD_ID)
public class VaultKeychain
{
    public static final String MOD_ID = "vaultkeychain";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VaultKeychain()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::registerCommand);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::registerClientCommand);
        });

        FormatDetector.registerExtension("shift", TomlFormat::instance);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, VKConfig.CONFIG, "shift_mods/keychain/keychain-common.shift");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        NetworkManager.initializeNetwork();
    }

    private void registerCommand(RegisterCommandsEvent event)
    {
        ReloadConfigCommand.register(event.getDispatcher());
    }

    @OnlyIn(Dist.CLIENT)
    private void registerClientCommand(RegisterCommandsEvent event)
    {
        ShowConfigCommand.register(event.getDispatcher());
    }
}