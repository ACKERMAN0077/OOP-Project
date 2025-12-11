package mainpkg.ecoresort.Housekeeping;

import java.io.Serializable;

public class StockItem implements Serializable {
    private String itemName;
    private int quantity;
    private int reorderLevel;
    private String category;

    public StockItem() {
    }

    public StockItem(String itemName, int quantity, int reorderLevel, String category) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
