package paste3.bakersbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * A custom adapter for the shopping list activity - to display the shopping list items
 * and checkboxes.
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {
    // Initialise the onClickListener
    private View.OnClickListener onClickListener = null;
    // Create the adapter and pass it the list
    public ShoppingListAdapter(Context context, List<ShoppingListItem> shoppingListItems) {
        super(context, 0, shoppingListItems);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ShoppingListItem shoppingListItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.shopping_list_item_multiple_choice, parent, false);
        }
        // Lookup view for data population
        TextView itemDescription = (TextView) convertView.findViewById(R.id.itemDescription);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        // Populate the data into the template view using the data object
        itemDescription.setText(shoppingListItem.getItemDescription());
        checkBox.setChecked(shoppingListItem.isChecked());
        checkBox.setOnClickListener(v -> {
            shoppingListItem.setChecked(checkBox.isChecked());
            if (onClickListener != null)
                onClickListener.onClick(v);
        });
        // Return the completed view to render on screen
        return convertView;
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
