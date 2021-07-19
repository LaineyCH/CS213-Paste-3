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
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The View Ingredient Activity class allows the user to view any ingredient in the ingredientMap,
 * as selected by them. The ingredient can then be edited by clicking the edit button.
 */
public class ViewIngredientActivity extends AppCompatActivity {

    Spinner ingredientSpinner;
    String IngredientSpinnerSelection;
    Ingredient ingredient;
    TextView name;
    TextView unitLabel;
    TextView atomicPrice;
    TextView pricePer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ingredient);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewIngredient);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // Find and cache inputs
        name = findViewById(R.id.ingredientNameHere);
        atomicPrice = findViewById(R.id.atomicPriceOutput);
        unitLabel = findViewById(R.id.unitLabelOutput2);
        pricePer = findViewById(R.id.pricePer);

        // INGREDIENT SPINNER
        ingredientSpinner = findViewById(R.id.ingredientSpinner2);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Capture the selection made on the ingredient spinner
                IngredientSpinnerSelection = parent.getItemAtPosition(position).toString();
                // Fetch the ingredient object specified by the ingredient spinner selection
                ingredient = IngredientManager.getIngredient(IngredientSpinnerSelection);
                // Set the display outputs
                name.setText(ingredient.getIngredientName());
                @SuppressLint("DefaultLocale") String atomicP = "£ " +
                        String.format("%.4f",(ingredient.getAtomicPrice()));
                atomicPrice.setText(atomicP);
                String unitL = ingredient.getUnit().getUnitLabel();
                unitLabel.setText(unitL);
                @SuppressLint("DefaultLocale") String countPrice = "£ " + String.format("%.2f",
                        (Math.round(ingredient.getAtomicPrice() * 100.0) / 100.0)) + " each";
                @SuppressLint("DefaultLocale") String mlPrice = "£ " + String.format("%.2f",
                        (Math.round((ingredient.getAtomicPrice() * 100) * 100.0)) / 100.0)
                        + " per 100ml";
                @SuppressLint("DefaultLocale") String gPrice = "£ " + String.format("%.2f",
                        (Math.round((ingredient.getAtomicPrice() * 100) * 100.0)) / 100.0)
                        + " per 100g";
                switch (unitL) {
                    case "count":
                        pricePer.setText(countPrice);
                        break;
                    case "ml":
                        pricePer.setText(mlPrice);
                        break;
                    case "g":
                        pricePer.setText(gPrice);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, IngredientManager.getIngredientNameList());
        // Drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(recipeDataAdapter);
    }

    /**
     * Resets all the outputs when the user returns to this activity after going to the edit
     * activity.
     */
    protected void onResume() {
        super.onResume();

        // Find and cache inputs
        name = findViewById(R.id.ingredientNameHere);
        atomicPrice = findViewById(R.id.atomicPriceOutput);
        unitLabel = findViewById(R.id.unitLabelOutput2);
        pricePer = findViewById(R.id.pricePer);

        // INGREDIENT SPINNER
        ingredientSpinner = findViewById(R.id.ingredientSpinner2);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Capture the selection made on the ingredient spinner
                IngredientSpinnerSelection = parent.getItemAtPosition(position).toString();
                // Fetch the ingredient object specified by the ingredient spinner selection
                ingredient = IngredientManager.getIngredient(IngredientSpinnerSelection);
                // Set the display outputs
                name.setText(ingredient.getIngredientName());
                @SuppressLint("DefaultLocale") String atomicP = "£ " +
                        String.format("%.4f",(ingredient.getAtomicPrice()));
                atomicPrice.setText(atomicP);
                String unitL = ingredient.getUnit().getUnitLabel();
                unitLabel.setText(unitL);
                @SuppressLint("DefaultLocale") String countPrice = "£ " + String.format("%.2f",
                        (Math.round(ingredient.getAtomicPrice() * 100.0) / 100.0)) + " each";
                @SuppressLint("DefaultLocale") String mlPrice = "£ " + String.format("%.2f",
                        (Math.round((ingredient.getAtomicPrice() * 100) * 100.0)) / 100.0)
                        + " per 100ml";
                @SuppressLint("DefaultLocale") String gPrice = "£ " + String.format("%.2f",
                        (Math.round((ingredient.getAtomicPrice() * 100) * 100.0)) / 100.0)
                        + " per 100g";
                switch (unitL) {
                    case "count":
                        pricePer.setText(countPrice);
                        break;
                    case "ml":
                        pricePer.setText(mlPrice);
                        break;
                    case "g":
                        pricePer.setText(gPrice);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, IngredientManager.getIngredientNameList());
        // Drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(recipeDataAdapter);
    }

    public void goToEditIngredient(View view) {
        Intent intent = new Intent(this,EditIngredientActivity.class);
        intent.putExtra("ingredientName",ingredient.getIngredientName());
        startActivity(intent);
    }

    /**
     * Triggered by back button - go back to previous activity layout - the ingredient menu
     */
    public void goBack(View view) {
        this.finish();
    }
}