package paste3.bakersbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListActivity extends AppCompatActivity {

    ListView shoppingListData;
    ShoppingListAdapter shoppingListAdapter;
    List<ShoppingListItem> shoppingItems = RecipeManager.getShoppingList();
    Spinner recipeSpinner;
    Spinner ingredientSpinner;
    Recipe recipe = new Recipe();
    String ingredientSpinnerSelection;
    List<String> ingredientList = new ArrayList<>();
    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

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
                onRecipeSelected(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Recipe spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, RecipeManager.getRecipeNameList());
        // Drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to recipe spinner
        recipeSpinner.setAdapter(recipeDataAdapter);

        // INGREDIENT SPINNER
        ingredientList.add("- Select Ingredient -");
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
        ArrayAdapter<String> ingredientDataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, ingredientList);
        // Drop down layout style
        ingredientDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(ingredientDataAdapter);
    }

    // Sets display list for Ingredient Spinner based on the Recipe selected.
    public void onRecipeSelected(AdapterView<?> parent, int position) {
        ingredientList.clear();
        for (RecipeIngredient item : recipe.getRecipeItems()) {
            ingredientList.add(item.getIngredient().getIngredientName() + ", "
                    + item.getQuantity() + " " + item.getUnit().getUnitLabel());
        }
    }

    // Adds Ingredient, including quantity and unit, to the shopping list.
    public void addIngredientToShoppingList(View view) {
        ShoppingListItem item = new ShoppingListItem(ingredientSpinnerSelection, false);
        shoppingItems.add(item);
        shoppingListData.setAdapter(shoppingListAdapter);
        RecipeManager.saveShoppingList();
    }

    public void saveIngredientChecked(View view) {
        RecipeManager.saveShoppingList();
    }

    // Clears shopping list
    public void clearShoppingList(View view) {
        shoppingItems.clear();
        shoppingListData.setAdapter(shoppingListAdapter);
        RecipeManager.clearShoppingList();
    }

    // Clears all the checked items from the list.
    public void clearCheckedItems(View view) {
        shoppingItems.removeIf(ShoppingListItem::isChecked);
        shoppingListAdapter.notifyDataSetChanged();
        RecipeManager.saveShoppingList();
    }

    public void goBack(View view) {
        this.finish();
    }
}