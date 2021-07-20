package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity {

    // Initialise variables
    EditText inputPrepTime;
    EditText inputCookTime;
    EditText inputServings;
    EditText inputTypeServing;
    EditText inputMethod;
    TextView ingredientsInput;
    TextView recipeName;
    Spinner ingredientSpinner;
    Spinner unitSpinner;
    String unitSpinnerSelection;
    Recipe recipe = new Recipe();

    // Initialise recipe ingredient list
    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        LinearLayout li = (LinearLayout) findViewById(R.id.editRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // Saving passed parameter for the recipe name
        Intent intent = getIntent();
        String recipeNameInput = intent.getStringExtra("recipeName");
        recipe = RecipeManager.getRecipe(recipeNameInput);

        // Find and cache inputs
        inputPrepTime = findViewById(R.id.prepTimeInput);
        inputCookTime = findViewById(R.id.cookTimeInput);
        inputServings = findViewById(R.id.servingsInput);
        inputTypeServing = findViewById(R.id.servingTypeInput);
        inputMethod = findViewById(R.id.methodInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        recipeName = findViewById(R.id.ingredientNameHere);
        recipeName.setText(recipe.getRecipeName());
        inputPrepTime.setText(String.valueOf(recipe.getPrepTime()));
        inputCookTime.setText(String.valueOf(recipe.getCookTime()));
        inputServings.setText(String.valueOf(recipe.getNumberServings()));
        inputTypeServing.setText(recipe.getTypeServing());
        inputMethod.setText(recipe.getMethod());
        StringBuilder ingredientsOutputString = new StringBuilder();

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
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // INGREDIENT SPINNER
        ingredientSpinner = findViewById(R.id.ingredientSpinner);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onIngredientItemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> ingredientDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, IngredientManager.getIngredientNameList());
        // Drop down layout style
        ingredientDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(ingredientDataAdapter);

        // Loop through the recipeItem list for the given recipe
        for (int i = 0; i < recipe.getRecipeIngredients().size(); i++) {
            ingredientsOutputString.append("").append(recipe.getRecipeIngredients()
                    .get(i).getQuantity()).append(" ").append(recipe.getRecipeIngredients().get(i)
                    .getUnit().getUnitLabel()).append(" ").append(recipe.getRecipeIngredients()
                    .get(i).getIngredient().getIngredientName()).append("\n");
            recipeIngredientList.add(recipe.getRecipeIngredients().get(i));
        }
        ingredientsInput.setText(ingredientsOutputString.toString());
    }

    /**
     * Called when a selection is made on the ingredient spinner. Sets the string list for the
     * unit spinner based on the ingredient selection made.
     */
    public void onIngredientItemSelected() {
        // Capture ingredient spinner selection
        String ingredientName = ingredientSpinner.getSelectedItem().toString();
        // Get the ingredients unit label
        String ingredientLabel = IngredientManager.getIngredient(ingredientName)
                .getUnit().getUnitLabel();

        // String list for spinner drop down elements
        List<String> units = new ArrayList<>();
        // Set the list according to the unit label of ingredient chosen
        switch (ingredientLabel) {
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attaching data adapter to unit spinner
        unitSpinner.setAdapter(dataAdapter);
    }

    /**
     * Triggered by the add ingredient button in the edit ingredient activity
     *
     * @param view the current layout view
     */
    public void addRecipeIngredient(View view) {

//        EditText inputIngredientName = findViewById(R.id.ingredientNameInput2); // Debugging
//        String ingredientName  = inputIngredientName.getText().toString(); // Debugging
//        Log.d("Add RecipeIngredient", ingredientName); // Debugging

        // Capture ingredient spinner selection (an ingredient name)
        String ingredientName = ingredientSpinner.getSelectedItem().toString();
        // Fetch ingredient object using the ingredient name
        Ingredient ingredient = IngredientManager.getIngredient(ingredientName);

        // Capture unit spinner selection (a unit label)
        String unitLabel = unitSpinner.getSelectedItem().toString();
        // Fetch unit object using the unit label
        Unit unit = UnitManager.getUnit(unitLabel);

        // Find and cache inputs
        EditText inputQuantity = findViewById(R.id.quantityInput);
        float quantity = Float.parseFloat(inputQuantity.getText().toString());

//        Log.d("PrintUnitLabel",unitLabel); // Debugging
//        Log.d("Add RecipeIngredient", unit.getUnitLabel());  // Debugging

        // Create a new RecipeIngredient, passing in the ingredient object, quantity and unit object
        RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, quantity, unit);

        // Remove old ingredient if it's already in the list (prevent duplication)
        recipeIngredientList.removeIf(item -> item.getIngredient().getIngredientName()
                .equals(ingredientName));

        // Add new or edited recipe ingredient to the recipe ingredient list
        recipeIngredientList.add(recipeIngredient);

        // Display list of all ingredients added to this point
        TextView ingredientsInput = findViewById(R.id.ingredientsInput);
        // Initialise list to be displayed
        StringBuilder ingredientsOutputString = new StringBuilder();
        // Loop through the recipeIngredientList, adding each ingredient and details to the list
        for (int i = 0; i < recipeIngredientList.size(); i++) {
            ingredientsOutputString.append("").append(recipeIngredientList.get(i)
                    .getQuantity()).append(" ").append(recipeIngredientList.get(i).getUnit()
                    .getUnitLabel()).append(" ").append(recipeIngredientList.get(i).getIngredient()
                    .getIngredientName()).append("\n");
//            Log.d("Check ingredients",ingredientsOutputString);
        }
        // Send list to output
        ingredientsInput.setText(ingredientsOutputString.toString());

        // Clear quantity field after "add ingredient" button is clicked
        inputQuantity.setText("");
    }

    /**
     * Triggered by the remove ingredient button in the activity_edit_recipe layout. Removes
     * an ingredient from the recipeIngredientList.
     *
     * @param view the current layout view
     */
    public void removeRecipeIngredient(View view) {

        // Capture ingredient spinner selection - the ingredient name
        String ingredientName = ingredientSpinner.getSelectedItem().toString();

        // REMOVE ingredient to the recipe ingredient list
        recipeIngredientList.removeIf(item -> item.getIngredient().getIngredientName()
                .equals(ingredientName));

        // Update displayed list of ingredients (removes deleted ones from display)
        TextView ingredientsInput = findViewById(R.id.ingredientsInput);
        // Initialise list to be displayed
        StringBuilder ingredientsOutputString = new StringBuilder();
        // Loop through the recipeIngredientList, adding each ingredient and details to the list
        for (int i = 0; i < recipeIngredientList.size(); i++) {
            ingredientsOutputString.append("").append(recipeIngredientList.get(i).getQuantity())
                    .append(" ").append(recipeIngredientList.get(i).getUnit().getUnitLabel())
                    .append(" ").append(recipeIngredientList.get(i).getIngredient()
                    .getIngredientName()).append("\n");
//            Log.d("Check ingredients",ingredientsOutputString);
        }
        // Send list to output
        ingredientsInput.setText(ingredientsOutputString.toString());

        // Clear field after "add ingredient" button is clicked
        EditText inputQuantity = findViewById(R.id.quantityInput);
        inputQuantity.setText("");
    }

    /**
     * Triggered by the save changes button in the activity_edit_recipe layout. Collects all the
     * input data and sends it to the Recipe Manager (including the recipeIngredientList) to create
     * the new recipe object and add it to the Recipe Map.
     *
     * @param view the current layout view
     */
    public void submitRecipe(View view) {

        // Find and cache inputs
        EditText inputPrepTime = findViewById(R.id.prepTimeInput);
        EditText inputCookTime = findViewById(R.id.cookTimeInput);
        EditText inputServings = findViewById(R.id.servingsInput);
        EditText inputTypeServing = findViewById(R.id.servingTypeInput);
        EditText inputMethod = findViewById(R.id.methodInput);
        editRecipe(inputPrepTime, inputCookTime, inputServings, inputTypeServing, inputMethod);
    }

    /**
     * Called by submitRecipe() in response the save changes button being clicked. Parses, then
     * sends the data to the Recipe Manager (including the recipeIngredientList) to create
     * the new recipe object and add it to the Recipe Map.
     * @param inputPrepTime the prep time
     * @param inputCookTime the cook time
     * @param inputServings the number of servings
     * @param inputMethod the recipe method
     */
    public void editRecipe(EditText inputPrepTime, EditText inputCookTime,
                           EditText inputServings, EditText inputTypeServing,
                           EditText inputMethod) {
        if (!inputPrepTime.getText().toString().isEmpty()
                && !inputCookTime.getText().toString().isEmpty()
                && !inputServings.getText().toString().isEmpty()
                && !inputTypeServing.getText().toString().isEmpty()
                && !inputMethod.getText().toString().isEmpty()
                && recipeIngredientList.size() > 0) {

            String recipeName = recipe.getRecipeName();
            float prepTime = Float.parseFloat(inputPrepTime.getText().toString());
            float cookTime = Float.parseFloat(inputCookTime.getText().toString());
            float numberServings = Float.parseFloat(inputServings.getText().toString());
            String typeServing = inputTypeServing.getText().toString();
            String method = inputMethod.getText().toString();

            // Send retrieved data to the Recipe Manager
            RecipeManager.addRecipe(recipeName, recipeIngredientList, prepTime, cookTime,
                    numberServings, typeServing, method);

            // Go back to previous activity layout - view recipe
            this.finish();
            startActivity(getIntent());
        }
    }

    /**
     * Triggered by back button in the activity_edit_recipe layout - go back to previous activity
     * layout
     */
    public void goBack(View view) {
        this.finish();
    }
}