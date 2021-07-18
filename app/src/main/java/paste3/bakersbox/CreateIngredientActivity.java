package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * This Activity Class allows a user to enter a new ingredient into the app, including adding a
 * recipe name
 */
public class CreateIngredientActivity extends AppCompatActivity {

    Spinner unitSpinner;
    String unitSpinnerSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredient);
        ConstraintLayout li = (ConstraintLayout) findViewById(R.id.createIngredient);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        //UNIT SPINNER
        unitSpinner = findViewById(R.id.unitSpinner2);
        // Unit Spinner click listener
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                unitSpinnerSelection = parent.getItemAtPosition(position).toString();
                Log.d("Dropdown value", unitSpinnerSelection); // Debugging
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    // Initialise variable for radio button selected
    String radioButton = "";
    // Finds out which radio button was checked
    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Is a button checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.dryButton:
                if (checked)
                    radioButton = "g";
                break;
            case R.id.wetButton:
                if (checked)
                    radioButton = "ml";
                break;
            case R.id.countedButton:
                if (checked)
                    radioButton = "count";
                break;
        }
        // Call onRadioButtonSelected() function to set list for the unit spinner
        onRadiobuttonSelected();
    }

    /**
     * Called after selection of radio button. Radio button selection determines the
     * unit measures that will be active in the unit spinner.
     */
    public void onRadiobuttonSelected() {
        // Spinner Drop down elements
        List<String> units = new ArrayList<String>();
        switch(radioButton) {
            case "ml":
                // Wet ingredients selection
                units.add("ml");
                units.add("l");
                units.add("cup");
                units.add("Tbsp");
                units.add("tsp");
                break;
            case "g":
                // Dry ingredients selection
                units.add("g");
                units.add("kg");
                units.add("lb");
                units.add("oz");
                break;
            case "count":
                // Counted ingredients selection
                units.add("count");
                break;
        }
        // Creating adapter for unit spinner, using "units" List<String>
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, units);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to unit spinner (displays content of "units" List<String>)
        unitSpinner.setAdapter(dataAdapter);
    }

    /**
     * Triggered by the add ingredient button in the activity_create_ingredient layout.
     * Collects all the user input data then sends it to the Ingredient Manager to add
     * the new ingredient to the ingredientMap.
     * @param view
     */
    public void submitIngredient(View view) {
        // Get all EditText inputs and parse to string or float as needed
        EditText inputIngredientName = findViewById(R.id.ingredientNameInput);
        String ingredientName = inputIngredientName.getText().toString();
        EditText inputQuantity = findViewById(R.id.quantityNumber1);
        float quantity = Float.parseFloat(inputQuantity.getText().toString());
        EditText inputPrice = findViewById(R.id.priceInput);
        float price = Float.parseFloat(inputPrice.getText().toString());
        //Send retrieved data to the Ingredient Manager to create the new ingredient and add it to
        // the Ingredient Map
        IngredientManager.addIngredient(ingredientName, radioButton, unitSpinnerSelection,
                quantity, price);
        // Go back to previous activity layout
        this.finish();
    }

    /**
     * Triggered by back button activity_create_ingredient layout - go back to previous activity
     * layout
     */
    public void goBack(View view) {
        this.finish();
    }
}