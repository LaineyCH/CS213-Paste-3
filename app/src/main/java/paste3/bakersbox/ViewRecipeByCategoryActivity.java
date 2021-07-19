package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * The View Recipe Activity class allows the user to view any recipe in the recipeMap,
 * selected by the category, then by the recipe name for all recipes in that category
 * (alphabetically presented). The cost of the recipe (the sum of the
 * cost of all the ingredients) is displayed. The scale of the recipe can be adjusted, changing the
 * quantity of each of the ingredients, for ease of baking, and it adjusts the total cost as well.
 * The recipe can also be edited by clicking the edit button.
 */
public class ViewRecipeByCategoryActivity extends AppCompatActivity {
    // declare variables
    TextView name;
    TextView prep;
    TextView bakingTime;
    TextView servingSize;
    TextView costOutput;
    TextView scaleAmount;
    TextView ingredientsOutput;
    TextView methodsOutput;
    Spinner categorySpinner;
    String categorySpinnerSelection = "Bar";
    Spinner recipeSpinner;
    String roundCost;
    StringBuilder ingredientsOutputString;
    float recipeScale = 1;
    Recipe recipe = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_by_category);
        ConstraintLayout li = (ConstraintLayout) findViewById(R.id.viewCategory);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // find and cache all inputs
        name = findViewById(R.id.ingredientNameHere);
        prep = findViewById(R.id.prepTimeOutput);
        bakingTime = findViewById(R.id.totalPriceOutput);
        servingSize = findViewById(R.id.cookTimeOutput);
        costOutput = findViewById(R.id.ingredientCostOuput);
        scaleAmount = findViewById(R.id.scaleAmount);
        ingredientsOutput = findViewById(R.id.atomicPriceOutput);
        methodsOutput = findViewById(R.id.unitLabelOutput2);

        // CATEGORY SPINNER
        categorySpinner = findViewById(R.id.categorySpinner2);
        // Category Spinner click listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                categorySpinnerSelection = parent.getItemAtPosition(position).toString();
                //Log.d("Dropdown value", categorySpinnerSelection); // Debugging
                onCategorySelected();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Category spinner
        ArrayAdapter<String> categoryDataAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, RecipeManager.getCategoryList());
        // Drop down layout style
        categoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        categorySpinner.setAdapter(categoryDataAdapter);

        // RECIPE SPINNER
        recipeSpinner = findViewById(R.id.ingredientSpinner2);
        // Ingredient Spinner click listener
        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String recipeSpinnerSelection = parent.getItemAtPosition(position).toString();
                recipe = RecipeManager.getRecipe(recipeSpinnerSelection);
                name.setText(recipe.getRecipeName());
                //outputs
                displayRecipe();
                scaleAmount.setText("1");
//                Log.d("Before loop", "Loop 1"); // Debugging
//                Log.d("Check for RecipeItems", recipe.getRecipeItems().toString()); // Debugging
                // Set outputs for Recipe
                displayRecipe();
                scaleAmount.setText("1");
                //ingredientsOutput.setText(ingredientsOutputString.toString());
                displayIngredients();
                methodsOutput.setText(recipe.getMethod());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    /**
     * Set the list of recipes, for the recipe spinner, according to the selection made on the
     * category spinner
     */
    public void onCategorySelected() {
        // Spinner Drop down elements
        List<String> recipesNamesListPerCategory = RecipeManager
                .getRecipeListPerCategory(categorySpinnerSelection);
//        Log.d("recipesNamesListPerCat", recipesNamesListPerCategory.toString()); // Debugging
        // Creating adapter for recipe names spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, recipesNamesListPerCategory);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to unit spinner
        recipeSpinner.setAdapter(dataAdapter);
    }

    /**
     * Triggered by clicking the scale recipe button on the activity_view_recipe layout. Scales the
     * recipe according to the scale entered by the user.
     * @param view the current view
     */
    public void scaleRecipe(View view) {
        String recipeScaleString = scaleAmount.getText().toString();
        recipeScale = Float.parseFloat(recipeScaleString);
        Log.d("scale", String.valueOf(recipeScale));
        displayRecipe();
        displayIngredients();
    }

    /**
     * Set all the display outputs
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void displayRecipe() {
        prep.setText(recipe.getPrepTime() + " minutes");
        bakingTime.setText(recipe.getCookTime() + " minutes");
        servingSize.setText(recipe.getNumberServings() * recipeScale
                + " - " + recipe.getTypeServing());
        roundCost = String.format("%.2f", Math.round(recipe.getCost() * recipeScale * 100.0) / 100.0);
        costOutput.setText(" £" + roundCost);
    }

    /**
     * Set the display outputs for the list of ingredients
     */
    public void displayIngredients() {
        ingredientsOutputString = new StringBuilder();
        for (RecipeIngredient item : recipe.getRecipeIngredients()) {
            Log.d("Check ingredients", item.getIngredient().getIngredientName()); // Debugging
            ingredientsOutputString.append("")
                    .append(item.getQuantity() * recipeScale).append(" ")
                    .append(item.getUnit().getUnitLabel()).append(" ")
                    .append(item.getIngredient().getIngredientName())
                    .append("\n");
        }
        ingredientsOutput.setText(ingredientsOutputString.toString());
    }


    /**
     * Triggered by the edit button - goes to the edit recipe activity, passing the recipe name in.
     * @param view the current view
     */
    public void editRecipe(View view) {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra("recipeName", recipe.getRecipeName());
        startActivity(intent);
    }

    /**
     * Triggered by back button - go back to previous activity layout
     */
    public void goBack(View view) {
        this.finish();
    }

    /**
     * Resets all the outputs when the user returns to this activity after going to the edit
     * activity.
     */
    protected void onResume() {
        super.onResume();

        //get all textView output names
        name = findViewById(R.id.ingredientNameHere);
        prep = findViewById(R.id.prepTimeOutput);
        bakingTime = findViewById(R.id.totalPriceOutput);
        servingSize = findViewById(R.id.cookTimeOutput);
        costOutput = findViewById(R.id.ingredientCostOuput);
        scaleAmount = findViewById(R.id.scaleAmount);
        ingredientsOutput = findViewById(R.id.atomicPriceOutput);
        methodsOutput = findViewById(R.id.unitLabelOutput2);

        // CATEGORY SPINNER
        categorySpinner = findViewById(R.id.categorySpinner2);
        // Category Spinner click listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                categorySpinnerSelection = parent.getItemAtPosition(position).toString();
                //Log.d("Dropdown value", categorySpinnerSelection); // Debugging
                onCategorySelected();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Category spinner
        ArrayAdapter<String> categoryDataAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, RecipeManager.getCategoryList());
        // Drop down layout style
        categoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        categorySpinner.setAdapter(categoryDataAdapter);

        // RECIPE SPINNER
        recipeSpinner = findViewById(R.id.ingredientSpinner2);
        // Ingredient Spinner click listener
        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String recipeSpinnerSelection = parent.getItemAtPosition(position).toString();
                recipe = RecipeManager.getRecipe(recipeSpinnerSelection);
                name.setText(recipe.getRecipeName());
                //outputs
                displayRecipe();
                scaleAmount.setText("1");
//                Log.d("Before loop", "Loop 1"); // Debugging
//                Log.d("Check for RecipeItems", recipe.getRecipeItems().toString()); // Debugging
                // Set outputs for Recipe
                displayRecipe();
                scaleAmount.setText("1");
                //ingredientsOutput.setText(ingredientsOutputString.toString());
                displayIngredients();
                methodsOutput.setText(recipe.getMethod());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}