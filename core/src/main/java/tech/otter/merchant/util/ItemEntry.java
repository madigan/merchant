package tech.otter.merchant.util;

import tech.otter.merchant.model.Item;

import java.util.Comparator;

/**
 * Helper class used to render lists of items and quantities.
 */
public class ItemEntry {
    private Item type;
    private int count;

    public ItemEntry() {
        this(null, 0);
    }
    public ItemEntry(Item type, int count) {
        this.type = type;
        this.count = count;
    }

    public Item getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        if(type == null || type.getName() == null || type.getName().equals("")) return "";
        return "[" + this.count + "] " + this.type.getName(); // TODO "[%1$4s] %2$s"
    }

    public static class ItemEntryComparator implements Comparator<ItemEntry> {
        @Override
        public int compare(ItemEntry i1, ItemEntry i2) {
            if(i1.type == null) return -1;
            if(i2.type == null) return 1;
            return i1.getType().getName().compareToIgnoreCase(i2.getType().getName());
        }
    }
}
