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
    Spinner recipeSpinner;
    float roundCost;
    StringBuilder ingredientsOutputString;

    Recipe recipe = new Recipe();

    Recipe recipeCopy = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ConstraintLayout li = (ConstraintLayout) findViewById(R.id.viewCategory);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Context context;
        context = this;
        //get all textView output names
        prep = findViewById(R.id.prepTimeOutput2);
        bakingTime = findViewById(R.id.bakingTimeOutput);
        servingSize = findViewById(R.id.servingSizeOutput);
        costOutput = findViewById(R.id.costOutput);
        scaleAmount = findViewById(R.id.scaleAmount);
        ingredientsOutput = findViewById(R.id.ingredientsOuput);
        methodsOutput = findViewById(R.id.methodOutput);


        // RECIPE SPINNER
        recipeSpinner = findViewById(R.id.recipeSpinner);
        // Ingredient Spinner click listener
        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String recipeSpinnerSelection = parent.getItemAtPosition(position).toString();
                recipe = RecipeManager.getRecipe(recipeSpinnerSelection);

                //outputs
                //StringBuilder ingredientsOutputString = new StringBuilder();

                prep.setText(String.valueOf(recipe.prepTime) + " minutes");
                bakingTime.setText(String.valueOf(recipe.cookTime) + " minutes");
                servingSize.setText(String.valueOf(recipe.numberServings) + " " + recipe.typeServing);
                roundCost = (float) (Math.round(recipe.cost * 100.0) / 100.0);
                costOutput.setText(" £" + String.valueOf(roundCost));
                recipeCopy = recipe;
                scaleAmount.setText("1");

                Log.d("Before loop", "Loop 1"); // Debugging
                Log.d("Check for RecipeItems", recipe.getRecipeItems().toString()); // Debugging
                //put ingredients in one string
                /*for (RecipeIngredient item : recipe.getRecipeItems()) {
                    Log.d("Check ingredients", item.getIngredient().getIngredientName()); // Debugging
                    ingredientsOutputString.append("")
                            .append(item.getQuantity()).append(" ")
                            .append(item.getUnit().getUnitLabel()).append(" ")
                            .append(item.getIngredient().getIngredientName())
                            .append("\n");
                }*/
//                for(int i = 0; i < recipe.recipeItems.size(); i++){
//                    ingredientsOutputString += "" + String.valueOf(recipe.recipeItems.get(i)._quantity) + " " + recipe.recipeItems.get(i)._unit + " " +
//                            recipe.recipeItems.get(i)._ingredient._ingredientName + "\n";
//                    Log.d("Check ingredients",ingredientsOutputString);
//                }

                //ingredientsOutput.setText(ingredientsOutputString.toString());
                displayIngredients(recipe, 1);
                methodsOutput.setText(recipe.method);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, RecipeManager.getRecipeNameList());
        // Drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        recipeSpinner.setAdapter(recipeDataAdapter);

    }

    public void scaleRecipe(View view) {
        String recipeScaleString = scaleAmount.getText().toString();
        float recipeScale = Float.parseFloat(recipeScaleString);
        Log.d("scale", String.valueOf(recipeScale));
        //Recipe scaledRecipe = recipe;
        float recipeCost = recipeCopy.cost;
        Log.d("Cost", String.valueOf(recipe.cost));
        recipeCost *= recipeScale;
        float servings = recipeCopy.numberServings;
        servings *= recipeScale;
        List<RecipeIngredient> ingredients = recipe.recipeItems;
        //the scaleRecipe() function returns a new Recipe object that is scaled.
        //Recipe scaledRecipe = recipe.scaleRecipe(recipeScale);
        //scaledRecipe = scaledRecipe.scaleRecipe(recipeScale);
        servingSize.setText(String.valueOf(servings));
        roundCost = (float) (Math.round((recipe.cost * recipeScale) * 100.0) / 100.0);
        costOutput.setText("£" + String.valueOf(roundCost));
        Recipe scaledRecipe = recipe;
        scaledRecipe.cost = recipeCost;
        scaledRecipe.numberServings = servings;
        displayIngredients(scaledRecipe, recipeScale);
        //costOutput.setText(String.valueOf(scaledRecipe.cost));
    }

    public void displayIngredients(Recipe recipeDisplay, float recipeScale) {
        ingredientsOutputString = new StringBuilder();
        for (RecipeIngredient item : recipeDisplay.getRecipeItems()) {
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
        intent.putExtra("recipeName", recipe._recipeName);
        startActivity(intent);
    }
}