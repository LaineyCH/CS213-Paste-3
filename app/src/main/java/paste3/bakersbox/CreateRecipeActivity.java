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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity {

    // Initialise recipe ingredient list
    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

    Spinner categorySpinner;
    Spinner ingredientSpinner;
    Spinner unitSpinner;
    String categorySpinnerSelection;
    String unitSpinnerSelection;
    String unitLabel;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        LinearLayout li=(LinearLayout)findViewById(R.id.createRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // CATEGORY SPINNER
        categorySpinner = findViewById(R.id.categorySpinner);
        // Category Spinner click listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                categorySpinnerSelection = parent.getItemAtPosition(position).toString();
                Log.d("Dropdown value", categorySpinnerSelection); // Debugging
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Category spinner
        ArrayAdapter<String> categoryDataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, RecipeManager.getCategoryList());
        // Drop down layout style
        categoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        categorySpinner.setAdapter(categoryDataAdapter);


        // UNIT SPINNER
        unitSpinner = findViewById(R.id.unitSpinner);
        // Unit Spinner click listener
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                unitSpinnerSelection = parent.getItemAtPosition(position).toString();
                Log.d("Dropdown value", unitSpinnerSelection); // Debugging
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // INGREDIENT SPINNER
        ingredientSpinner = findViewById(R.id.ingredientSpinner);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onIngredientItemSelected(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> ingredientDataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, IngredientManager.getIngredientNameList());
        // Drop down layout style
        ingredientDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(ingredientDataAdapter);

    }

    public void onIngredientItemSelected(AdapterView<?> parent, int position) {

        String ingredientName = ingredientSpinner.getSelectedItem().toString();
        String ingredientLabel = IngredientManager.getIngredient(ingredientName).getUnit().getUnitLabel();

        // Spinner Drop down elements
        List<String> units = new ArrayList<String>();

        switch(ingredientLabel) {
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

    // Triggered by the add ingredient button in the edit ingredient activity
    public void addRecipeIngredient(View view) {

        // Get all EditText inputs and create Ingredient and Unit objects

        //EditText inputIngredientName = findViewById(R.id.ingredientNameInput2);
        //String ingredientName  = inputIngredientName.getText().toString();
        //Log.d("Add I to RecipeIngred", ingredientName); // Debugging
        String ingredientName = ingredientSpinner.getSelectedItem().toString();
        Ingredient ingredient = IngredientManager.getIngredient(ingredientName);

        EditText inputQuantity = findViewById(R.id.quantityInput);
        float quantity  = Float.parseFloat(inputQuantity.getText().toString());

        String unitLabel = unitSpinner.getSelectedItem().toString();
        Unit unit = UnitManager.getUnit(unitLabel);

        Log.d("PrintUnitLabel",unitLabel); // Debugging
        Log.d("Add U to RecipeIngred", unit.getUnitLabel());  // Debugging

        // Create a new RecipeIngredient
        RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, quantity, unit);

        // Add recipe ingredient to the recipe ingredient list
        recipeIngredientList.add(recipeIngredient);
        TextView ingredientsInput = findViewById(R.id.ingredientsInput);
        String ingredientsOutputString = "";
        for(int i = 0; i < recipeIngredientList.size(); i++){
            ingredientsOutputString += "" + String.valueOf(recipeIngredientList.get(i)._quantity) + " " + recipeIngredientList.get(i).getUnit().getUnitLabel() + " " +
                    recipeIngredientList.get(i)._ingredient._ingredientName + "\n";
            Log.d("Check ingredients",ingredientsOutputString);
                }

        ingredientsInput.setText(ingredientsOutputString);

        // Clear fields after "add ingredient" button is clicked, ready for next ingredient to be added
        //inputIngredientName.setText("");
        inputQuantity.setText("");
    }

    // Triggered by the submit button in the edit ingredient activity
    public void submitRecipe(View view) throws InterruptedException {

        // Get all EditText inputs and parse to string or float as needed

        EditText inputRecipeName = findViewById(R.id.recipeName);
        String recipeName  = inputRecipeName.getText().toString();
        Log.d("Recipe Name", recipeName);

        // Add the recipe name to the categoryMap (categorySpinnerSelection being the key).
        RecipeManager.addRecipeToCategory(recipeName, categorySpinnerSelection);
        RecipeManager.saveCategories();

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

        Toast toast = Toast.makeText(this,"Recipe Created",Toast.LENGTH_SHORT);
        toast.show();
        Thread.sleep(2000);
        this.finish();
    }

    public void goBack(View view) {
        this.finish();
    }
    //add code for the spinners - activity_edit_recipe2.xml
    // use ingredientNameList to populate the ingredient Spinner on the edit page activity_edit_recepie.2

}