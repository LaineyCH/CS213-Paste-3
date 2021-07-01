package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity implements OnItemSelectedListener {

    Spinner spinner;
    String spinnerSelection;
    String unitLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe2);
        LinearLayout li=(LinearLayout)findViewById(R.id.editRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        spinner = findViewById(R.id.unitSpinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        unitLabel = spinnerSelection;

        // Spinner Drop down elements
        List<String> units = new ArrayList<String>();
        //fluids
        units.add("ml");
        units.add("l");
        units.add("cup");
        units.add("Tbsp");
        units.add("tsp");

        //weight
        units.add("g");
        units.add("kg");
        units.add("lb");
        units.add("oz");

        //count
        units.add("g");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        spinnerSelection = parent.getItemAtPosition(position).toString();
        Log.d("Dropdown value",spinnerSelection);
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }
    // Initialise recipe ingredient list
    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();


    // Triggered by the add ingredient button in the edit ingredient activity
    public void addRecipeIngredient(View view) {

        // Get all EditText inputs and create Ingredient and Unit objects

        EditText inputIngredientName = findViewById(R.id.ingredientNameInput2);
        String ingredientName  = inputIngredientName.getText().toString();
        Log.d("Add I to RecipeIngred", ingredientName); // Debugging
        Ingredient ingredient = IngredientManager.getIngredient(ingredientName);

        EditText inputQuantity = findViewById(R.id.quantityInput);
        //String unitLabel = spinner.getSelectedItem().toString();
        float quantity  = Float.parseFloat(inputQuantity.getText().toString());
        //EditText inputUnit = findViewById(R.id.unitInput);
        //String unitLabel  = inputUnit.getText().toString();
        Log.d("PrintUnitLabel",unitLabel);
        Unit unit = UnitManager.getUnit(unitLabel);
        Log.d("Add U to RecipeIngred", unit.getUnitLabel());  // Debugging

        // Create a new RecipeIngredient
        RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, quantity, unit);

        // Add recipe ingredient to the recipe ingredient list
        recipeIngredientList.add(recipeIngredient);
    }

    // Triggered by the submit button in the edit ingredient activity
    public void submitRecipe(View view) {

        // Get all EditText inputs and parse to string or float as needed

        EditText inputRecipeName = findViewById(R.id.recipeName);
        String recipeName  = inputRecipeName.getText().toString();
        Log.d("Recipe Name", recipeName);

        EditText inputPrepTime = findViewById(R.id.prepTimeInput);
        float prepTime  = Float.parseFloat(inputPrepTime.getText().toString());
        Log.d("Prep Time", String.format("%f",prepTime));

        EditText inputCookTime = findViewById(R.id.cookTimeInput);
        float cookTime  = Float.parseFloat(inputCookTime.getText().toString());
        Log.d("Cook Time", String.format("%f",cookTime));

        EditText inputServings = findViewById(R.id.servingsInput);
        float numberServings  = Float.parseFloat(inputServings.getText().toString());
        Log.d("Servings", String.format("%f",numberServings));

        EditText inputTypeServing = findViewById(R.id.servingTypeInput);
        String typeServing  = inputTypeServing.getText().toString();
        Log.d("Type of Serving", typeServing);

        EditText inputMethod = findViewById(R.id.methodInput);
        String method  = inputMethod.getText().toString();
        Log.d("Method", method);

        // Send retrieved data to the Recipe Manager, including the recipeIngredientList, to create
        // the recipe and add it to the Recipe Map
        RecipeManager.addRecipe(recipeName, recipeIngredientList, prepTime, cookTime,
                numberServings, typeServing, method);
    }
    public void goBack(View view) {
        super.onBackPressed();
    }

    //add code for the spinners - activity_edit_recipe2.xml
    // use ingredientNameList to populate the ingredient Spinner on the edit page activity_edit_recepie.2

}