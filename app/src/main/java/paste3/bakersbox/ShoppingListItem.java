package paste3.bakersbox;

/**
 * The Shopping list item class creates new shopping list items. Each has an item description
 * and a boolean value which shoes if the item's checkbox has been checked or not.
 */
public class ShoppingListItem {
    private String _itemDescription;
    private boolean _checked;

    /**
     * ShoppingListItem main constructor
     * @param itemDescription the item's name, and quantity
     * @param checked a boolean value indicating whether the item has been checked
     */
    public ShoppingListItem(String itemDescription, boolean checked) {
        this._itemDescription = itemDescription;
        this._checked = checked;
    }

    /**
     * Blank constructor - needed for application to run. Do not delete.
     */
    public ShoppingListItem() {
        this._itemDescription = "";
        this._checked = false;
    }

    /**
     * GETTERS AND SETTERS
     */

    public String getItemDescription() {
        return _itemDescription;
    }

    // This setter is used! Do not delete.
    public void setItemDescription(String itemDescription) {
        this._itemDescription = itemDescription;
    }

    public boolean isChecked() {
        return _checked;
    }

    public void setChecked(boolean _isChecked) {
        this._checked = _isChecked;
    }
}
