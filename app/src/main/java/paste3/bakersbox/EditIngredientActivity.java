package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditIngredientActivity extends AppCompatActivity {

    Ingredient ingredient = new Ingredient();
    Spinner unitSpinner;
    String unitSpinnerSelection;
    TextView name;
    EditText quantity;
    EditText price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);
        ConstraintLayout li = (ConstraintLayout) findViewById(R.id.editIngredient);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Intent intent = getIntent();
        String ingredientNameInput = intent.getStringExtra("ingredientName");
        name = findViewById(R.id.ingredientNameInput3);
        quantity = findViewById(R.id.quantityNumber1);
        price = findViewById(R.id.priceInput);
        ingredient = IngredientManager.getIngredient(ingredientNameInput);
        name.setText(ingredient.getIngredientName());
        quantity.setText(String.valueOf(ingredient.totalQuantity));
        price.setText(String.valueOf(ingredient.totalQuantity));
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
    String radioButton = "";
    // Finds out which radio button was checked
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
        onRadiobuttonSelected();
    }
    public void onRadiobuttonSelected() {
        // Spinner Drop down elements
        List<String> units = new ArrayList<String>();
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to unit spinner
        unitSpinner.setAdapter(dataAdapter);
    }

    // Triggered by the submit button in the edit ingredient activity
    public void editIngredientButton(View view) {
        // Get all EditText inputs and parse to string or float as needed
        EditText inputIngredientName = findViewById(R.id.ingredientNameInput);
        String ingredientName = ingredient.getIngredientName();
        EditText inputQuantity = findViewById(R.id.quantityNumber1);
        float quantity = Float.parseFloat(inputQuantity.getText().toString());
        EditText inputPrice = findViewById(R.id.priceInput);
        float price = Float.parseFloat(inputPrice.getText().toString());
        //Send retrieved data to the Ingredient Manager to create the new ingredient and add it to
        // the Ingredient Map
        IngredientManager.addIngredient(ingredientName, radioButton, unitSpinnerSelection, quantity, price);
        this.finish();
    }

    public void goBack(View view) {
        this.finish();
    }
}