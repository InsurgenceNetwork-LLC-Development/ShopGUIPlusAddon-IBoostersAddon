package com.insurgencedev.shopguiplusaddon;

import com.insurgencedev.shopguiplusaddon.listeners.ShopGUIPlusEventListener;
import com.insurgencedev.shopguiplusaddon.settings.MyConfig;
import org.insurgencedev.insurgenceboosters.api.addon.IBoostersAddon;
import org.insurgencedev.insurgenceboosters.api.addon.InsurgenceBoostersAddon;
import org.insurgencedev.insurgenceboosters.libs.fo.Common;

@IBoostersAddon(name = "ShopGuiPlusAddon", version = "1.0.7", author = "InsurgenceDev", description = "ShopGUI+ Support")
public class ShopGUIPlusAddon extends InsurgenceBoostersAddon {

    private static MyConfig config;

    @Override
    public void onAddonStart() {
        config = new MyConfig();
    }

    @Override
    public void onAddonReloadAblesStart() {
        if (!Common.doesPluginExist("ShopGUIPlus")) {
            return;
        }

        config.reload();
        registerEvent(new ShopGUIPlusEventListener());
    }
}
