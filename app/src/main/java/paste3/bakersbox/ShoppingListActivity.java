package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * The shopping list activity class allows the user to add ingredient, from any recipe in the
 * recipeMap, to a shopping list. Items have checkboxes to check once purchased, checked items can
 * be deleted and the whole list can be deleted.The state of the shopping list is saved as it is,
 * including check marks.
 */
public class ShoppingListActivity extends AppCompatActivity {

    ListView shoppingListData;
    ShoppingListAdapter shoppingListAdapter;
    List<ShoppingListItem> shoppingItems = RecipeManager.getShoppingList();
    Spinner recipeSpinner;
    Spinner ingredientSpinner;
    Recipe recipe = new Recipe();
    String ingredientSpinnerSelection;
    List<String> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.shoppingListActivity);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // Create Shopping List Adapter
        shoppingListData = findViewById(R.id.shoppingList);
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingItems);
        shoppingListAdapter.setOnClickListener(this::saveIngredientChecked);
        shoppingListData.setAdapter(shoppingListAdapter);
        RecipeManager.setOnShoppingListLoaded(shoppingListAdapter::notifyDataSetChanged);

        // RECIPE SPINNER
        recipeSpinner = findViewById(R.id.recipeSpinner);
        // Recipe Spinner click listener
        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String recipeSpinnerSelection = parent.getItemAtPosition(position).toString();
                recipe = RecipeManager.getRecipe(recipeSpinnerSelection);
                onRecipeSelected();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Recipe spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RecipeManager.getRecipeNameList());
        // Drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to recipe spinner
        recipeSpinner.setAdapter(recipeDataAdapter);

        // INGREDIENT SPINNER
        ingredientList.add("                   ");
        ingredientSpinner = findViewById(R.id.ingredientSpinner3);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredientSpinnerSelection = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> ingredientDataAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, ingredientList);
        // Drop down layout style
        ingredientDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(ingredientDataAdapter);
    }

    /**
     * Sets display list for Ingredient Spinner based on the Recipe selected.
     */
    public void onRecipeSelected() {
        ingredientList.clear();
        for (RecipeIngredient item : recipe.getRecipeIngredients()) {
            ingredientList.add(item.getIngredient().getIngredientName() + ", "
                    + item.getQuantity() + " " + item.getUnit().getUnitLabel());
        }
    }

    /**
     * Adds Ingredient, including quantity and unit, to the shopping list.
     * @param view the current view
     */
    public void addIngredientToShoppingList(View view) {
        ShoppingListItem item = new ShoppingListItem(ingredientSpinnerSelection, false);
        shoppingItems.add(item);
        shoppingListData.setAdapter(shoppingListAdapter);
        RecipeManager.saveShoppingList();
    }

    /**
     * Saves which item was checked
     * @param view the current view
     */
    public void saveIngredientChecked(View view) {
        RecipeManager.saveShoppingList();
    }

    /**
     * Clears / deletes the shopping list
     * @param view the current view
     */
    public void clearShoppingList(View view) {
        shoppingItems.clear();
        shoppingListData.setAdapter(shoppingListAdapter);
        RecipeManager.clearShoppingList();
    }

    /**
     * Clears all the checked items from the list.
     * @param view the current view
     */
    public void clearCheckedItems(View view) {
        shoppingItems.removeIf(ShoppingListItem::isChecked);
        shoppingListAdapter.notifyDataSetChanged();
        RecipeManager.saveShoppingList();
    }

    /**
     * Triggered by back button - go back to previous activity layout, the main menu.
     */
    public void goBack(View view) {
        this.finish();
    }
}