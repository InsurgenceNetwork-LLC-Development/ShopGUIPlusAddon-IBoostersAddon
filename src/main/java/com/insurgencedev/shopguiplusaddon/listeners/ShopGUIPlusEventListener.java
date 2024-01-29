package com.insurgencedev.shopguiplusaddon.listeners;

import com.insurgencedev.shopguiplusaddon.settings.MyConfig;
import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.data.BoosterFindResult;

import java.util.List;

public final class ShopGUIPlusEventListener implements Listener {

    @EventHandler
    private void onTransact(ShopPreTransactionEvent event) {
        final String TYPE = "Sell";
        final String NAMESPACE = "SHOPGUI_ECONOMY";
        final double[] totalMulti = {0};

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(event.getPlayer()).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            totalMulti[0] += boosterResult.getBoosterData().getMultiplier();
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            totalMulti[0] += globalBooster.getMultiplier();
            return null;
        }, () -> null);

        if (totalMulti[0] > 0 && canBoost(event)) {
            event.setPrice(calculateAmount(event.getPrice(), totalMulti[0]));
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

    private long calculateAmount(double amount, double multi) {
        return (long) (amount * (multi < 1 ? 1 + multi : multi));
    }
}
