package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This Activity Class allows a user to enter a new recipe into the app, including adding a recipe
 * name, a list of ingredients and their quantities, a method (block of text), prep time,
 * cook/bake time, number of servings and type.
 */
public class CreateRecipeActivity extends AppCompatActivity {

    // Initialise variables
    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
    Spinner categorySpinner;
    Spinner ingredientSpinner;
    Spinner unitSpinner;
    String categorySpinnerSelection;
    String unitSpinnerSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        LinearLayout li=(LinearLayout)findViewById(R.id.createRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // CATEGORY SPINNER
        categorySpinner = findViewById(R.id.categorySpinner);
        // Create Category Spinner click listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Triggered by a selection on the category spinner - sets the categorySpinnerSelection
             * variable.
             * @param parent AdapterView
             * @param view the current layout view
             * @param position spinner position
             * @param id number
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySpinnerSelection = parent.getItemAtPosition(position).toString();
//                Log.d("Dropdown value", categorySpinnerSelection); // Debugging
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Category spinner using the list RecipeManager.getCategoryList()
        ArrayAdapter<String> categoryDataAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, RecipeManager.getCategoryList());
        // Drop down layout style
        categoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attach data adapter to the category spinner
        categorySpinner.setAdapter(categoryDataAdapter);

        // INGREDIENT SPINNER
        ingredientSpinner = findViewById(R.id.ingredientSpinner);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Triggered bt a selection on the ingredient spinner - calls the
             * onIngredientItemSelected() function which determines the unit spinner display list
             * @param parent AdapterView
             * @param view the current layout view
             * @param position spinner position
             * @param id number
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onIngredientItemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> ingredientDataAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, IngredientManager.getIngredientNameList());
        // Drop down layout style
        ingredientDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(ingredientDataAdapter);

        // UNIT SPINNER
        unitSpinner = findViewById(R.id.unitSpinner);
        // Unit Spinner click listener
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Triggered bt a selection on the unit spinner - sets the unitSpinnerSelection variable
             * @param parent AdapterView
             * @param view the current layout view
             * @param position spinner position
             * @param id number
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                unitSpinnerSelection = parent.getItemAtPosition(position).toString();
//                Log.d("Dropdown value", unitSpinnerSelection); // Debugging
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    /**
     * Called by the ingredient spinner when a selection is made.
     */
    public void onIngredientItemSelected() {
        // Set variables
        String ingredientName = ingredientSpinner.getSelectedItem().toString();
        String ingredientLabel = IngredientManager.getIngredient(ingredientName).getUnit().getUnitLabel();
        List<String> units = new ArrayList<>();

        // Set spinner Drop down elements according to the unitLabel of the ingredient selected
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
        // Creating adapter for unit spinner - using units list created above
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attach data adapter to unit spinner
        unitSpinner.setAdapter(dataAdapter);
    }

    /**
     * Triggered by the add ingredient button activity_create_recipe layout - creates a new
     * RecipeIngredient object from the user input data, and adds it to the list of ingredients
     * for the recipe being created
     * @param view the current layout view
     */
    public void addRecipeIngredient(View view) {

        // Get all ingredient name using ingredient spinner selection (ingredientSpinner variable)
        String ingredientName = ingredientSpinner.getSelectedItem().toString();
        // Fetch Ingredient object
        Ingredient ingredient = IngredientManager.getIngredient(ingredientName);
//        Log.d("Add RecipeIngredient", ingredientName); // Debugging
        // Get input quantity and assign to variable
        EditText inputQuantity = findViewById(R.id.quantityInput);
        float quantity  = Float.parseFloat(inputQuantity.getText().toString());
        // Get unitLabel using unit spinner selection (unitSpinner variable)
        String unitLabel = unitSpinner.getSelectedItem().toString();
        // Fetch Unit object
        Unit unit = UnitManager.getUnit(unitLabel);
//        Log.d("PrintUnitLabel",unitLabel); // Debugging
//        Log.d("Add RecipeIngredient", unit.getUnitLabel());  // Debugging

        // Create a new RecipeIngredient using the Ingredient and Unit objects and quantity
        RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, quantity, unit);
        // Add recipe ingredient to the recipe ingredient list
        recipeIngredientList.add(recipeIngredient);

        // Display list of all ingredients added to this point
        TextView ingredientsInput = findViewById(R.id.ingredientsInput);
        // Initialise list to be displayed
        StringBuilder ingredientsOutputString = new StringBuilder();
        // Loop through the recipeIngredientList, adding each ingredient and details to the list
        for(int i = 0; i < recipeIngredientList.size(); i++){
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
     * Triggered by the add recipe button activity_create_recipe layout - collects all the user
     * input data, and, along with the list of ingredients already created, sends the data to the
     * RecipeManager that will create a new Recipe object and add it to the recipeMap. The recipe
     * name is also added to the appropriate key in the categoryMap.
     * @param view the current layout view
     */
    public void submitRecipe(View view) {
        // Get all EditText inputs and call addNewRecipe()
        EditText inputRecipeName = findViewById(R.id.recipeName);
        EditText inputPrepTime = findViewById(R.id.prepTimeInput);
        EditText inputCookTime = findViewById(R.id.cookTimeInput);
        EditText inputServings = findViewById(R.id.servingsInput);
        EditText inputTypeServing = findViewById(R.id.servingTypeInput);
        EditText inputMethod = findViewById(R.id.methodInput);
        addNewRecipe(inputRecipeName, inputPrepTime, inputCookTime, inputServings, inputTypeServing,
                inputMethod);
    }

    /**
     *  Called by submitRecipe(), in response to the add recipe button being clicked.
     * Checks that inputs are valid, then parses them, fetches the  and sends them to the Recipe
     * manager to create the new Recipe.
     * @param inputRecipeName the recipe name
     * @param inputPrepTime teh preparation time
     * @param inputCookTime the cook /  bake time
     * @param inputServings number of servings
     * @param inputTypeServing type of servings
     * @param inputMethod the method
     */
    public void addNewRecipe(EditText inputRecipeName, EditText inputPrepTime,
                             EditText inputCookTime, EditText inputServings,
                             EditText inputTypeServing, EditText inputMethod) {
        // If all fields are filled out
        if (!inputRecipeName.getText().toString().isEmpty()
                && !inputRecipeName.getText().toString().isEmpty()
                && !inputPrepTime.getText().toString().isEmpty()
                && !inputCookTime.getText().toString().isEmpty()
                && !inputServings.getText().toString().isEmpty()
                && !inputTypeServing.getText().toString().isEmpty()
                && !inputMethod.getText().toString().isEmpty()
                && !categorySpinnerSelection.isEmpty()
                && recipeIngredientList.size() > 0 ) {

            // Parse the data, add recipe to the category list, then send data to the Recipe Manager
            String recipeName = inputRecipeName.getText().toString();

            // Add the recipe name to the categoryMap (categorySpinnerSelection being the key).
            RecipeManager.addRecipeToCategory(recipeName, categorySpinnerSelection);
            RecipeManager.saveCategories();

            // Collect, format and save all user entered data to variables.
            float prepTime = Float.parseFloat(inputPrepTime.getText().toString());
            float cookTime = Float.parseFloat(inputCookTime.getText().toString());
            float numberServings = Float.parseFloat(inputServings.getText().toString());
            String typeServing = inputTypeServing.getText().toString();
            String method = inputMethod.getText().toString();

            // Send data as well as the recipeIngredientList to the Recipe Manager
            RecipeManager.addRecipe(recipeName, recipeIngredientList, prepTime, cookTime,
                    numberServings, typeServing, method);

//            // Pop up message to give user feedback that the recipe has been added
//            Toast toast = Toast.makeText(this, "Recipe Created", Toast.LENGTH_SHORT);
//            toast.show();
//            Thread.sleep(2000);

            // Go back to previous activity layout
            this.finish();
        }
    }

    /**
     * Triggered by back button activity_create_recipe layout - go back to previous activity layout
     */
    public void goBack(View view) {
        this.finish();
    }

}