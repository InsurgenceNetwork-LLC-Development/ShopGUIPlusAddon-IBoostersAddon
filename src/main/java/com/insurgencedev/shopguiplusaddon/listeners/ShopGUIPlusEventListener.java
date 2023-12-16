package com.insurgencedev.shopguiplusaddon.listeners;

import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.models.booster.GlobalBoosterManager;
import org.insurgencedev.insurgenceboosters.settings.IBoostersPlayerCache;

public final class ShopGUIPlusEventListener implements Listener {

    @EventHandler
    private void onTransact(ShopPreTransactionEvent event) {
        final String TYPE = "Sell";
        final String NAMESPACE = "SHOPGUI_ECONOMY";
        double totalMulti = 1;

        IBoostersPlayerCache.BoosterFindResult pResult = IBoosterAPI.getCache(event.getPlayer()).findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof IBoostersPlayerCache.BoosterFindResult.Success boosterResult) {
            totalMulti += getMulti(boosterResult.getBooster().getMultiplier());
        }

        GlobalBoosterManager.BoosterFindResult gResult = IBoosterAPI.getGlobalBoosterManager().findBooster(TYPE, NAMESPACE);
        if (gResult instanceof GlobalBoosterManager.BoosterFindResult.Success boosterResult) {
            totalMulti += getMulti(boosterResult.getBooster().getMultiplier());
        }

        event.setPrice(event.getPrice() * totalMulti);
    }

    private double getMulti(double amount) {
        return (amount >= 1) ? amount - 1 : amount;
    }
}
