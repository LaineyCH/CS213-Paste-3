package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
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

public class ViewRecipeByCategoryActivity extends AppCompatActivity {
    //get all textView output names
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
    float roundCost;
    StringBuilder ingredientsOutputString;
    float recipeScale = 1;

    Recipe recipe = new Recipe();

    Recipe recipeCopy = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_by_category);
        ConstraintLayout li = (ConstraintLayout) findViewById(R.id.viewCategory);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Context context;
        context = this;
        //get all textView output names
        name = findViewById(R.id.ingredientNameHere);
        prep = findViewById(R.id.totalQuantityOutput);
        bakingTime = findViewById(R.id.totalPriceOutput);
        servingSize = findViewById(R.id.unitLabelOutput);
        costOutput = findViewById(R.id.costOutput);
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
                Log.d("Dropdown value", categorySpinnerSelection); // Debugging
                onCategorySelected(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Category spinner
        ArrayAdapter<String> categoryDataAdapter = new ArrayAdapter<String>
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
                //StringBuilder ingredientsOutputString = new StringBuilder();

                prep.setText(String.valueOf(recipe.prepTime) + " minutes");
                bakingTime.setText(String.valueOf(recipe.cookTime) + " minutes");
                servingSize.setText(String.valueOf(recipe.numberServings) + " " + recipe.typeServing);
                roundCost = (float) (Math.round(recipe.cost * 100.0) / 100.0);
                costOutput.setText(" £" + String.valueOf(roundCost));
                recipeCopy = recipe;
                scaleAmount.setText("1");

//                Log.d("Before loop", "Loop 1"); // Debugging
//                Log.d("Check for RecipeItems", recipe.getRecipeItems().toString()); // Debugging

                // Set outputs for Recipe
                displayRecipe();
                scaleAmount.setText("1");

                //ingredientsOutput.setText(ingredientsOutputString.toString());
                displayIngredients();
                methodsOutput.setText(recipe.method);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void onCategorySelected(AdapterView<?> parent, int position) {

        // Spinner Drop down elements
        List<String> recipesNamesListPerCategory = RecipeManager.getRecipeListPerCategory(categorySpinnerSelection);
        Log.d("recipesNamesListPerCat", recipesNamesListPerCategory.toString());

        // Creating adapter for recipe names spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipesNamesListPerCategory);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to unit spinner
        recipeSpinner.setAdapter(dataAdapter);
    }

    public void scaleRecipe(View view) {
        String recipeScaleString = scaleAmount.getText().toString();
        recipeScale = Float.parseFloat(recipeScaleString);
        Log.d("scale", String.valueOf(recipeScale));
        displayRecipe();
        displayIngredients();
    }

    public void displayRecipe() {
        prep.setText(String.valueOf(recipe.prepTime) + " minutes");
        bakingTime.setText(String.valueOf(recipe.cookTime) + " minutes");
        servingSize.setText(String.valueOf(recipe.numberServings * recipeScale) + " " + recipe.typeServing);
        roundCost = (float) (Math.round(recipe.cost * recipeScale * 100.0) / 100.0);
        costOutput.setText(" £" + String.valueOf(roundCost));
    }

    public void displayIngredients() {
        ingredientsOutputString = new StringBuilder();
        for (RecipeIngredient item : recipe.getRecipeItems()) {
            Log.d("Check ingredients", item.getIngredient().getIngredientName()); // Debugging
            ingredientsOutputString.append("")
                    .append(item.getQuantity() * recipeScale).append(" ")
                    .append(item.getUnit().getUnitLabel()).append(" ")
                    .append(item.getIngredient().getIngredientName())
                    .append("\n");
        }
        ingredientsOutput.setText(ingredientsOutputString.toString());
    }

    public void goBack(View view) {
        this.finish();
    }

    public void editRecipe(View view) {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra("recipeName", recipe.getRecipeName());
        startActivity(intent);
    }
}