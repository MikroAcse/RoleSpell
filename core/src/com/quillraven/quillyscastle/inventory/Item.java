package com.quillraven.quillyscastle.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.quillraven.quillyscastle.utils.Utils;

public class Item {
    private static final String TAG = Item.class.getSimpleName();
    private static final ObjectMap<ItemType, Item> inventoryItemCache = new ObjectMap<ItemType, Item>();
    private ItemType type;
    private String atlasID;
    private ItemClassification classification;
    private ItemUseType useType;
    private String description;
    private boolean stackable;
    private int goldValue;
    private Item() {
        type = null;
        atlasID = null;
        classification = ItemClassification.NONE;
        useType = ItemUseType.NONE;
        description = null;
        stackable = false;
        goldValue = 0;
    }
    private Item(Item toCopy) {
        this.type = toCopy.type;
        this.atlasID = toCopy.atlasID;
        this.classification = toCopy.classification;
        this.useType = toCopy.useType;
        this.description = toCopy.description;
        this.stackable = toCopy.stackable;
        this.goldValue = toCopy.goldValue;
    }

    public static Item getItem(ItemType type) {
        Item result = inventoryItemCache.get(type);

        if (result == null) {
            // item not loaded yet -> load it
            result = loadItem(type);
        } else {
            // item already loaded -> create a new instance with the same config
            result = new Item(result);
        }

        return result;
    }

    private static Item loadItem(ItemType type) {
        Item result = null;

        // type not loaded yet -> load it
        Object fromJson = Utils.fromJson(null, Gdx.files.internal(type.getFilePath()));

        if (fromJson instanceof Array<?>) {
            // multiple entity types defined within one file
            // -> load each type at once
            for (Object val : (Array<?>) fromJson) {
                Item invItem = Utils.readJsonValue(Item.class, (JsonValue) val);
                inventoryItemCache.put(invItem.getType(), invItem);

                if (invItem.getType() == type) {
                    result = invItem;
                }
            }
        } else {
            // only one entity type defined -> load it
            result = Utils.readJsonValue(Item.class, (JsonValue) fromJson);
            inventoryItemCache.put(type, result);
        }

        if (result == null) {
            Gdx.app.error(TAG, "Could not load Item of type " + type);
        }

        return result;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getAtlasID() {
        return atlasID;
    }

    public void setAtlasID(String atlasID) {
        this.atlasID = atlasID;
    }

    public ItemClassification getClassification() {
        return classification;
    }

    public void setClassification(ItemClassification classification) {
        this.classification = classification;
    }

    public ItemUseType getUseType() {
        return useType;
    }

    public void setUseType(ItemUseType useType) {
        this.useType = useType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public int getGoldValue() {
        return goldValue;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }

    public int getSellValue() {
        return (int) (goldValue * 0.5f);
    }

    public enum ItemType {
        SWORD01("items/items.json"),
        SWORD02("items/items.json"),
        SHIELD01("items/items.json"),
        ARMOR01("items/items.json"),
        RING01("items/items.json"),
        GEM01("items/items.json");

        private final String filePath;

        ItemType(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    public enum ItemClassification {
        NONE,
        CONSUMABLE,
        EQUIPPABLE;
    }

    public enum ItemUseType {
        WEAPON_ONEHAND,
        ARMOR_HELMET,
        ARMOR_CHEST,
        ARMOR_SHIELD,
        ARMOR_BOOTS,
        AMULET,
        RING,
        NONE;
    }
}
