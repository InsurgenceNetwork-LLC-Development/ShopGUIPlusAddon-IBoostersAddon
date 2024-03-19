package com.insurgencedev.shopguiplusaddon.listeners;

import com.google.common.util.concurrent.AtomicDouble;
import com.insurgencedev.shopguiplusaddon.settings.MyConfig;
import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.ShopManager;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.data.BoosterFindResult;
import org.insurgencedev.insurgenceboosters.data.PermanentBoosterData;

import java.util.List;
import java.util.Optional;

public final class ShopGUIPlusEventListener implements Listener {

    private final String TYPE = "Sell";
    private final String NAMESPACE = "SHOPGUI_ECONOMY";

    @EventHandler
    private void onTransact(ShopPreTransactionEvent event) {
        if (event.getShopAction().equals(ShopManager.ShopAction.BUY)) {
            return;
        }

        Player player = event.getPlayer();
        AtomicDouble totalMulti = new AtomicDouble(getPersonalPermMulti(player) + getGlobalPermMulti());

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(event.getPlayer()).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            totalMulti.getAndAdd(boosterResult.getBoosterData().getMultiplier());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            totalMulti.addAndGet(globalBooster.getMultiplier());
            return null;
        }, () -> null);

        if (totalMulti.get() > 0 && canBoost(event)) {
            event.setPrice(calculateAmount(event.getPrice(), totalMulti.get()));
        }
    }

    private boolean canBoost(ShopPreTransactionEvent event) {
        if (!MyConfig.isShopItemRestrictionEnabled && !MyConfig.isShopCategoryRestrictionEnabled) {
            return true;
        }

        if (MyConfig.isShopCategoryRestrictionEnabled) {
            if (MyConfig.getShopCategories().contains(event.getShopItem().getShop().getId())) {
                return true;
            }
        }

        if (MyConfig.isShopItemRestrictionEnabled) {
            ShopItem item = event.getShopItem();
            List<String> list = MyConfig.getShopItems().get(item.getShop().getId());
            return list != null && list.contains(item.getId());
        }

        return false;
    }

    private double getPersonalPermMulti(Player uuid) {
        Optional<PermanentBoosterData> foundMulti = Optional.ofNullable(IBoosterAPI.INSTANCE.getCache(uuid).getPermanentBoosts().getPermanentBooster(TYPE, NAMESPACE));
        return foundMulti.map(PermanentBoosterData::getMulti).orElse(0d);
    }

    private double getGlobalPermMulti() {
        AtomicDouble multi = new AtomicDouble(0d);

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findPermanentBooster(TYPE, NAMESPACE, data -> {
            multi.set(data.getMulti());
            return null;
        }, () -> null);

        return multi.get();
    }

    private double calculateAmount(double amount, double multi) {
        return amount * (multi < 1 ? 1 + multi : multi);
    }
}
