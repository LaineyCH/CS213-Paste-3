package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This Activity Class displays the cost of recipes, including personal time and appliance time used.
 * It allows the user to specify their owm rates for personal and appliance time, and they can both
 * scale the recipe and increase the number of batches (increasing appliance time).
 */
public class QuoteActivity extends AppCompatActivity {
    // Declare variables
    Spinner recipeSpinner;
    TextView name;
    TextView prepTime;
    TextView bakingTime;
    TextView servingSize;
    TextView costOutput;
    TextView totalCostOutput;
    TextView scaleAmount;
    EditText batchNumber;
    EditText personalRatePerHour;
    EditText ovenRatePerHour;
    float personalRate;
    float ovenRate;
    float batches;
    float totalCost;
    float roundCost;
    float recipeScale = 1;
    Recipe recipe = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.quoteActivity);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // Find and cache inputs
        name = findViewById(R.id.ingredientNameHere);
        prepTime = findViewById(R.id.prepTimeOutput);
        bakingTime = findViewById(R.id.cookTimeOutput);
        servingSize = findViewById(R.id.servingsOutput);
        batchNumber = findViewById(R.id.batchesInput);
        personalRatePerHour = findViewById(R.id.personalRatePerHour);
        // Set personal £ / hour rate to suggested value
        String personalRate = "10.00";
        personalRatePerHour.setText(personalRate);
        ovenRatePerHour = findViewById(R.id.ovenRatePerHour);
        // Set bake/cook £ / hour rate to suggested value
        String bakeRate = "1.00";
        ovenRatePerHour.setText(bakeRate);
        costOutput = findViewById(R.id.ingredientCostOuput);
        totalCostOutput = findViewById(R.id.totalCostOutput);
        scaleAmount = findViewById(R.id.scaleAmount);

        // RECIPE SPINNER
        recipeSpinner = findViewById(R.id.ingredientSpinner2);
        // Recipe Spinner click listener
        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // Triggered by selection on the recipe spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // get selected value from spinner
                String recipeSpinnerSelection = parent.getItemAtPosition(position).toString();
                // fetch the recipe object matching teh selection
                recipe = RecipeManager.getRecipe(recipeSpinnerSelection);
                // set Name Title to Recipe Name selected
                name.setText(recipe.getRecipeName());

                // Set outputs for Recipe
                displayRecipe();
                scaleAmount.setText("1");

//                Log.d("Before loop","Loop 1"); // Debugging
//                Log.d("Check for RecipeItems", recipe.getRecipeItems().toString()); // Debugging

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Recipe spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RecipeManager.getRecipeNameList());
        // Set drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attach data adapter to recipe spinner
        recipeSpinner.setAdapter(recipeDataAdapter);
    }

    /**
     * Triggered by the update scale/batches button - scales the recipe and resets the
     * display values
     * @param view the current view
     */
    public void scaleRecipe(View view) {
        String recipeScaleString = scaleAmount.getText().toString();
        recipeScale = Float.parseFloat(recipeScaleString);
//        Log.d("scale", String.valueOf(recipeScale));
        displayRecipe();
    }

    /**
     * Display recipe by setting output values
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void displayRecipe() {
        // Set variable for personal £ / hour rate
        String personalRateString = personalRatePerHour.getText().toString();
        personalRate = Float.parseFloat(personalRateString);
        // Set variable bake/cook £ / hour rate
        String ovenRateString = ovenRatePerHour.getText().toString();
        ovenRate = Float.parseFloat(ovenRateString);
        // Set variavle for batch number
        String batchesString = batchNumber.getText().toString();
        batches = Float.parseFloat(batchesString);
        // Display recipe's prep time
        String prepareTime = recipe.getPrepTime() + " minutes";
        prepTime.setText(prepareTime);
        // Display recipe's cook/bake time
        String bakeTime = recipe.getCookTime() * batches + " minutes";
        bakingTime.setText(bakeTime);
        // Display recipe's serving size and type
        String serveSize = recipe.getNumberServings() * recipeScale + " - " + recipe.getTypeServing();
        servingSize.setText(serveSize);
        // Calculate recipe's cost by ingredient, taking scale into account
        roundCost = (float) (Math.round(recipe.getCost() * recipeScale * 100.00) / 100.00);
        // Include preparation and cook/bake time in Total cost, also accounting for batch numbers
        totalCost = (roundCost + (ovenRate * ((recipe.getCookTime() * batches) / 60.0f)) +
                (personalRate * ((recipe.getPrepTime() * batches) / 60.0f)));
        costOutput.setText(" £" + String.format("%.2f", roundCost));
        totalCostOutput.setText(" £" + String.format("%.2f", totalCost));
    }

    /**
     * Triggered by the edit button - goes to the edit recipe activity, passing the recipe name in.
     * @param view the current view
     */
    public void editRecipe(View view) {
        Intent intent = new Intent(this,EditRecipeActivity.class);
        intent.putExtra("recipeName",recipe.getRecipeName());
        startActivity(intent);
    }

    /**
     * Triggered by back button - go back to previous activity layout
     */
    public void goBack(View view) {
        this.finish();
    }
}