package com.insurgencedev.shopguiplusaddon;

import com.insurgencedev.shopguiplusaddon.listeners.ShopGUIPlusEventListener;
import org.insurgencedev.insurgenceboosters.api.addon.IBoostersAddon;
import org.insurgencedev.insurgenceboosters.api.addon.InsurgenceBoostersAddon;
import org.insurgencedev.insurgenceboosters.libs.fo.Common;

@IBoostersAddon(name = "ShopGuiPlusAddon", version = "1.0.2", author = "InsurgenceDev", description = "ShopGUI+ Support")
public class ShopGUIPlusAddon extends InsurgenceBoostersAddon {

    @Override
    public void onAddonReloadAblesStart() {
        if (Common.doesPluginExist("ShopGUIPlus")) {
            registerEvent(new ShopGUIPlusEventListener());
        }
    }
}
