package paste3.bakersbox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class viewRecipeActivity extends AppCompatActivity {
    //get all textView output names
    TextView name;
    TextView prep;
    TextView bakingTime;
    TextView servingSize;
    TextView costOutput;
    TextView scaleAmount;
    TextView ingredientsOutput;
    TextView methodsOutput;
    Spinner recipeSpinner;
    //get recipe
    //Recipe recipe = new Recipe();
    Recipe recipe = RecipeManager.getRecipe("New Recipe");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Context context;
        context = this;
        //get all textView output names
        name = findViewById(R.id.recipeNameTitle);
        prep = findViewById(R.id.prepTimeOutput2);
        bakingTime = findViewById(R.id.bakingTimeOutput);
        servingSize = findViewById(R.id.servingSizeOutput);
        costOutput = findViewById(R.id.costOutput);
        scaleAmount = findViewById(R.id.scaleAmount);
        ingredientsOutput = findViewById(R.id.ingredientsOuput);
        methodsOutput = findViewById(R.id.methodOutput);
/*
        // INGREDIENT SPINNER
        recipeSpinner = findViewById(R.id.recipeSpinner);
        // Ingredient Spinner click listener
        //recipeSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, RecipeManager.getRecipeNameList());
        // Drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        recipeSpinner.setAdapter(recipeDataAdapter);

 */
        //outputs
        String ingredientsOutputString = "";
        name.setText(recipe._recipeName);
        prep.setText(String.valueOf(recipe.prepTime));
        bakingTime.setText(String.valueOf(recipe.cookTime));
        servingSize.setText(String.valueOf(recipe.numberServings));
        costOutput.setText(String.valueOf(recipe.cost));
        scaleAmount.setText("1");

        Log.d("Before loop","Loop 1");
        //put ingredients in one string
        for(int i = 0; i < recipe.recipeItems.size(); i++){
            ingredientsOutputString += "" + String.valueOf(recipe.recipeItems.get(i)._quantity) + " " + recipe.recipeItems.get(i)._unit + " " +
                    recipe.recipeItems.get(i)._ingredient._ingredientName + "\n";
            Log.d("Check ingredients",ingredientsOutputString);
        }
        ingredientsOutput.setText(ingredientsOutputString);
        methodsOutput.setText(recipe.method);
    }

    public void scaleRecipe(View view) {
        String recipeScaleString = scaleAmount.getText().toString();
        float recipeScale = Float.parseFloat(recipeScaleString);
        //the scaleRecipe() function returns a new Recipe object that is scaled.
        Recipe scaledRecipe = recipe.scaleRecipe(recipeScale);
        servingSize.setText(String.valueOf(scaledRecipe.numberServings));
        costOutput.setText(String.valueOf(recipe.cost*recipeScale));

        //costOutput.setText(String.valueOf(scaledRecipe.cost));
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}