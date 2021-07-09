package paste3.bakersbox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class viewRecipeActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Context context;
        context = this;
        //get all textView output names
        name = findViewById(R.id.recipeNameTitle);
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
                name.setText(recipe.getRecipeName());
                prep.setText(String.valueOf(recipe.prepTime));
                bakingTime.setText(String.valueOf(recipe.cookTime));
                servingSize.setText(String.valueOf(recipe.numberServings));
                roundCost = (float) (Math.round(recipe.cost * 100.0) / 100.0);
                costOutput.setText("£" + String.valueOf(roundCost));
                scaleAmount.setText("1");

                Log.d("Before loop","Loop 1"); // Debugging
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
                displayIngredients(recipe);
                methodsOutput.setText(recipe.method);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
        Recipe scaledRecipe = recipe;
        //the scaleRecipe() function returns a new Recipe object that is scaled.
        //Recipe scaledRecipe = recipe.scaleRecipe(recipeScale);
        scaledRecipe = scaledRecipe.scaleRecipe(recipeScale);
        servingSize.setText(String.valueOf(scaledRecipe.numberServings));
        roundCost = (float) (Math.round(scaledRecipe.cost * 100.0) / 100.0);
        costOutput.setText("£" + String.valueOf(roundCost));
        displayIngredients(scaledRecipe);
        //costOutput.setText(String.valueOf(scaledRecipe.cost));
    }

    public void displayIngredients(Recipe recipeDisplay){
        ingredientsOutputString = new StringBuilder();
        for (RecipeIngredient item : recipeDisplay.getRecipeItems()) {
            Log.d("Check ingredients", item.getIngredient().getIngredientName()); // Debugging
            ingredientsOutputString.append("")
                    .append(item.getQuantity()).append(" ")
                    .append(item.getUnit().getUnitLabel()).append(" ")
                    .append(item.getIngredient().getIngredientName())
                    .append("\n");
        }
        ingredientsOutput.setText(ingredientsOutputString.toString());
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}