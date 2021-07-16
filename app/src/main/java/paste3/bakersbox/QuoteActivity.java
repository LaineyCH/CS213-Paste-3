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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class QuoteActivity extends AppCompatActivity {
    //get all textView output names
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
    Spinner recipeSpinner;
    float roundCost;
    float recipeScale = 1;

    Recipe recipe = new Recipe();

    Recipe recipeCopy = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.quoteActivity);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Context context;
        context = this;
        //get all textView output names
        name = findViewById(R.id.ingredientNameHere);
        prepTime = findViewById(R.id.prepTimeOutput);
        bakingTime = findViewById(R.id.cookTimeOutput);
        servingSize = findViewById(R.id.servingsOutput);
        batchNumber = findViewById(R.id.batchesInput);
        personalRatePerHour = findViewById(R.id.personalRatePerHour);
        personalRatePerHour.setText("10.00");
        ovenRatePerHour = findViewById(R.id.ovenRatePerHour);
        ovenRatePerHour.setText("0.14");
        costOutput = findViewById(R.id.ingredientCostOuput);
        totalCostOutput = findViewById(R.id.totalCostOutput);
        scaleAmount = findViewById(R.id.scaleAmount);

        // RECIPE SPINNER
        recipeSpinner = findViewById(R.id.ingredientSpinner2);
        // Ingredient Spinner click listener
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
    }

    public void displayRecipe() {
        String personalRateString = personalRatePerHour.getText().toString();
        personalRate = Float.parseFloat(personalRateString);
        String ovenRateString = ovenRatePerHour.getText().toString();
        ovenRate = Float.parseFloat(ovenRateString);
        String batchesString = batchNumber.getText().toString();
        batches = Float.parseFloat(batchesString);
        prepTime.setText(String.valueOf((recipe.prepTime)) + " minutes");
        bakingTime.setText(String.valueOf((recipe.cookTime * batches) * 100.0/100.0) + " minutes");
        servingSize.setText(String.valueOf(recipe.numberServings * recipeScale) + " " + recipe.typeServing);
        roundCost = (float) (Math.round(recipe.cost * recipeScale * 100.00) / 100.00);
        totalCost = (roundCost + (ovenRate * ((recipe.cookTime * batches) / 60.0f)) + (personalRate * ((recipe.prepTime * batches) / 60.0f)));
        costOutput.setText(" £" + String.valueOf(roundCost));
        BigDecimal bigDecimal = new BigDecimal(Float.toString(totalCost));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        totalCostOutput.setText(" £" + String.valueOf(bigDecimal.floatValue()));
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