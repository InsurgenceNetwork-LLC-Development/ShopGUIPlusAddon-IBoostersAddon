package com.insurgencedev.shopguiplusaddon;

import com.insurgencedev.shopguiplusaddon.listeners.ShopGUIPlusEventListener;
import org.bukkit.Bukkit;
import org.insurgencedev.insurgenceboosters.api.addon.IBoostersAddon;
import org.insurgencedev.insurgenceboosters.api.addon.InsurgenceBoostersAddon;

@IBoostersAddon(name = "ShopGuiPlusAddon", version = "1.0.1", author = "InsurgenceDev", description = "ShopGUI+ Support")
public class ShopGUIPlusAddon extends InsurgenceBoostersAddon {

    @Override
    public void onAddonReloadablesStart() {
        if (Bukkit.getPluginManager().isPluginEnabled("ShopGUIPlus")) {
            registerEvent(new ShopGUIPlusEventListener());
        }
    }
}
