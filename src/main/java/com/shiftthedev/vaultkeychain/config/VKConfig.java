package com.shiftthedev.vaultkeychain.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class VKConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_PICKUP;

    public VKConfig()
    {}

    public static void reloadConfig()
    {
        CONFIG.afterReload();
    }

    public static void updateFromServer(boolean serverPickup)
    {
        VKConfig.ENABLE_PICKUP.set(serverPickup);
    }

    static
    {
        BUILDER.push("Common");

        ENABLE_PICKUP = BUILDER.comment("Enable / Disable the possibility to pickup keys directly to the pouch.").define("Enable Pickup", true);

        BUILDER.pop();

        CONFIG = BUILDER.build();
    }
}
