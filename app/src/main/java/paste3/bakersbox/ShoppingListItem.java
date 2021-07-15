package paste3.bakersbox;

public class ShoppingListItem {
    private String _itemDescription;
    private boolean _checked;

    public ShoppingListItem(String itemDescription, boolean checked) {
        this._itemDescription = itemDescription;
        this._checked = checked;
    }

    public ShoppingListItem() {
        this._itemDescription = "";
        this._checked = false;
    }

    public String getItemDescription() {
        return _itemDescription;
    }

    public void setItemDescription(String _itemDescription) {
        this._itemDescription = _itemDescription;
    }

    public boolean isChecked() {
        return _checked;
    }

    public void setChecked(boolean _isChecked) {
        this._checked = _isChecked;
    }
}
