package com.insurgencedev.shopguiplusaddon.settings;

import lombok.Getter;
import org.insurgencedev.insurgenceboosters.api.addon.AddonConfig;

import java.util.*;

public final class MyConfig extends AddonConfig {

    public static boolean isShopCategoryRestrictionEnabled;
    public static boolean isShopItemRestrictionEnabled;
    @Getter
    private static List<String> shopCategories;
    @Getter
    private static Map<String, List<String>> shopItems;

    public MyConfig() {
        shopCategories = new ArrayList<>();
        shopItems = new HashMap<>();
        loadAddonConfig("config.yml", "config.yml");
    }

    @Override
    protected void onLoad() {
        shopCategories.clear();
        shopItems.clear();

        isShopCategoryRestrictionEnabled = getBoolean("Shop_Category_Restriction.enabled");
        shopCategories = getStringList("Shop_Category_Restriction.categories");

        isShopItemRestrictionEnabled = getBoolean("Shop_Item_Restriction.enabled");
        getStringList("Shop_Item_Restriction.items").forEach(string -> {
            String[] arr = string.split(":");
            String category = arr[0].trim();
            String[] itemIds = arr[1].split(",");

            List<String> itemsList = new ArrayList<>();
            for (String itemId : itemIds) {
                itemsList.add(itemId.trim());
            }

            shopItems.put(category, itemsList);
        });
    }

}
