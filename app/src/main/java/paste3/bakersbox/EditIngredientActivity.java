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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * The Edit Ingredient Activity Class allows the user to make changes to ingredients that already
 * exist in the ingredientMap. The user can enter a new cost and quantity for the ingredient, to
 * update it, and change the ingredient type. The ingredient name can not be changed, nor can the
 * ingredient be deleted.
 */
public class EditIngredientActivity extends AppCompatActivity {

    // Initialise variables
    Ingredient ingredient = new Ingredient();
    Spinner unitSpinner;
    String unitSpinnerSelection;
    String radioButton = "";
    TextView name;
    EditText quantity;
    EditText price;
    RadioButton dryRadioButton;
    RadioButton wetradioButton;
    RadioButton countRadioButton;
    List<String> units = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);
        ConstraintLayout li = (ConstraintLayout) findViewById(R.id.editIngredient);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // Saving passed parameter for the ingredient name
        Intent intent = getIntent();
        String ingredientNameInput = intent.getStringExtra("ingredientName");
        ingredient = IngredientManager.getIngredient(ingredientNameInput);

        // Find and cache inputs
        name = findViewById(R.id.ingredientNameInput3);
        quantity = findViewById(R.id.quantityNumber1);
        price = findViewById(R.id.priceInput);
        RadioButton dryRadioButton = findViewById(R.id.dryButton);
        RadioButton wetRadioButton = findViewById(R.id.wetButton);
        RadioButton countRadioButton = findViewById(R.id.countedButton);

        //UNIT SPINNER
        unitSpinner = findViewById(R.id.unitSpinner2);
        // Unit Spinner click listener
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Set "unitSpinnerSelection" variable on selection of a spinner item
                unitSpinnerSelection = parent.getItemAtPosition(position).toString();
//                Log.d("Dropdown value", unitSpinnerSelection); // Debugging
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set input values
        name.setText(ingredient.getIngredientName());
        String hundred = "100";
        String unitLabel = ingredient.getUnit().getUnitLabel();
        switch (unitLabel) {
            case "g" :
                quantity.setText(hundred);
                @SuppressLint("DefaultLocale") String gPrice = String.format("%.2f",
                        (Math.round((ingredient.getAtomicPrice() * 100) * 100.0)) / 100.0);
                price.setText(gPrice);
                dryRadioButton.setChecked(true);
                wetRadioButton.setChecked(false);
                countRadioButton.setChecked(false);
                radioButton = "g";
                break;
            case "ml" :
                quantity.setText(hundred);
                @SuppressLint("DefaultLocale") String mlPrice = String.format("%.2f",
                        (Math.round((ingredient.getAtomicPrice() * 100) * 100.0)) / 100.0);
                price.setText(mlPrice);
                dryRadioButton.setChecked(false);
                wetRadioButton.setChecked(true);
                countRadioButton.setChecked(false);
                radioButton = "ml";
                break;
            case "count" :
                quantity.setText("1");
                @SuppressLint("DefaultLocale") String countPrice = String.format("%.2f",
                        (Math.round(ingredient.getAtomicPrice() * 100.0) / 100.0));
                price.setText(countPrice);
                dryRadioButton.setChecked(false);
                wetRadioButton.setChecked(false);
                countRadioButton.setChecked(true);
                radioButton = "count";
                break;
        }
        onRadiobuttonSelected();
    }

    /**
     * Thriggered by a radio button selection
     * @param view the current layout view
     */
    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Set the variable "checked" to whichever radio button is checked
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
        onRadiobuttonSelected();
    }

    /**
     * Sets the string list for the unit spinner in response to a radio button selection
     */
    public void onRadiobuttonSelected() {
        // Spinner Drop down elements
        switch(radioButton) {
            case "ml":
                // Wet ingredients
                units.add("ml");
                units.add("l");
                units.add("cup");
                units.add("Tbsp");
                units.add("tsp");
                break;
            case "g":
                // Dry ingredients
                units.add("g");
                units.add("kg");
                units.add("lb");
                units.add("oz");
                break;
            case "count":
                // Counted ingredients
                units.add("count");
                break;
        }
        // Creating adapter for unit spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, units);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to unit spinner
        unitSpinner.setAdapter(dataAdapter);
    }

    /**
     * Triggered by the "save changes" button activity_edit_ingredient layout. Sends retrieved data
     * to the Ingredient Manager to create the new ingredient and add it to the Ingredient Map.
     * @param view the current layout view
     */
    public void editIngredientButton(View view) {
        // Get all EditText inputs and parse to string or float as needed
        EditText inputIngredientName = findViewById(R.id.ingredientNameInput);
        String ingredientName = ingredient.getIngredientName();
        EditText inputQuantity = findViewById(R.id.quantityNumber1);
        float quantity = Float.parseFloat(inputQuantity.getText().toString());
        EditText inputPrice = findViewById(R.id.priceInput);
        float price = Float.parseFloat(inputPrice.getText().toString());
        // Send data to IngredientManager
        IngredientManager.addIngredient(ingredientName, radioButton, unitSpinnerSelection,
                quantity, price);
        // Go back to previous activity layout
        this.finish();
    }

    /**
     * Triggered by back button activity_edit_ingredient layout - go back to previous activity
     * layout
     */
    public void goBack(View view) {
        this.finish();
    }
}