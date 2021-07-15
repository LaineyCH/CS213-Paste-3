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

public class ViewRecipeActivity extends AppCompatActivity {
    //get all textView output names
    TextView name;
    TextView prep;
    TextView bakingTime;
    TextView servingSize;
    TextView costOutput;
    TextView scaleAmount;
    TextView ingredientsOutput;
    TextView methodsOutput;
    Spinner recipeSpinner;
    float roundCost;
    StringBuilder ingredientsOutputString;
    float recipeScale = 1;

    Recipe recipe = new Recipe();

    Recipe recipeCopy = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewCategory);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Context context;
        context = this;
        //get all textView output names
        name = findViewById(R.id.ingredientNameHere);
        prep = findViewById(R.id.prepTimeOutput);
        bakingTime = findViewById(R.id.totalPriceOutput);
        servingSize = findViewById(R.id.cookTimeOutput);
        costOutput = findViewById(R.id.ingredientCostOuput);
        scaleAmount = findViewById(R.id.scaleAmount);
        ingredientsOutput = findViewById(R.id.atomicPriceOutput);
        methodsOutput = findViewById(R.id.unitLabelOutput2);


        // RECIPE SPINNER
        recipeSpinner = findViewById(R.id.ingredientSpinner2);
        // Recipe Spinner click listener
        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String recipeSpinnerSelection = parent.getItemAtPosition(position).toString();
                recipe = RecipeManager.getRecipe(recipeSpinnerSelection);
                name.setText(recipe.getRecipeName());

                // Set outputs for Recipe
                displayRecipe();
                scaleAmount.setText("1");

                Log.d("Before loop","Loop 1"); // Debugging
                Log.d("Check for RecipeItems", recipe.getRecipeItems().toString()); // Debugging

                // Call function to put all recipe's ingredients into 1 string and display.
                displayIngredients();
                methodsOutput.setText(recipe.method);
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

    }

    public void scaleRecipe(View view) {
        String recipeScaleString = scaleAmount.getText().toString();
        recipeScale = Float.parseFloat(recipeScaleString);
        Log.d("scale", String.valueOf(recipeScale));
        displayRecipe();
        displayIngredients();

//        //Recipe scaledRecipe = recipe;
//        float recipeCost = recipeCopy.cost;
//        Log.d("Cost", String.valueOf(recipe.cost));
//        recipeCost *= recipeScale;
//        float servings = recipeCopy.numberServings;
//        servings *= recipeScale;
//        List<RecipeIngredient> ingredients = recipe.recipeItems;
//        //the scaleRecipe() function returns a new Recipe object that is scaled.
//        //Recipe scaledRecipe = recipe.scaleRecipe(recipeScale);
//        //scaledRecipe = scaledRecipe.scaleRecipe(recipeScale);
//        servingSize.setText(String.valueOf(servings));
//        roundCost = (float) (Math.round((recipe.cost*recipeScale) * 100.0) / 100.0);
//        costOutput.setText("£" + String.valueOf(roundCost));
//        Recipe scaledRecipe = recipe;
//        scaledRecipe.cost = recipeCost;
//        scaledRecipe.numberServings = servings;
//        displayIngredients(scaledRecipe);
//        //costOutput.setText(String.valueOf(scaledRecipe.cost));
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
        Intent intent = new Intent(this,EditRecipeActivity.class);
        intent.putExtra("recipeName",recipe.getRecipeName());
        startActivity(intent);
    }
}