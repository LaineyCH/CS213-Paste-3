package paste3.bakersbox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class viewRecipeActivity extends AppCompatActivity {
    //get all textView output names
    TextView name = findViewById(R.id.recipeNameTitle);
    TextView prep = findViewById(R.id.prepTimeOutput2);
    TextView bakingTime = findViewById(R.id.bakingTimeOutput);
    TextView servingSize = findViewById(R.id.servingSizeOutput);
    TextView costOutput = findViewById(R.id.costOutput);
    TextView scaleAmount = findViewById(R.id.scaleAmount);
    TextView ingredientsOutput = findViewById(R.id.ingredientsOuput);
    TextView methodsOutput = findViewById(R.id.methodOutput);

    //get recipe
    //Recipe recipe = new Recipe();
    Recipe recipe = RecipeManager.getRecipe("Test Recipe");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        //outputs
        String ingredientsOutputString = "";
        name.setText(recipe._recipeName);
        prep.setText(String.valueOf(recipe.prepTime));
        bakingTime.setText(String.valueOf(recipe.cookTime));
        servingSize.setText(String.valueOf(recipe.numberServings));
        costOutput.setText(String.valueOf(recipe.cost));
        scaleAmount.setText("1");

        //put ingredients in one string
        for(int i = 0; i < recipe.recipeItems.size(); i++){
            ingredientsOutputString += String.valueOf(recipe.recipeItems.get(i)._quantity) + " " + recipe.recipeItems.get(i)._unit + " " +
                    recipe.recipeItems.get(i)._ingredient + "\n";
        }
        ingredientsOutput.setText(ingredientsOutputString);
        methodsOutput.setText(recipe.method);
    }

    public void scaleRecipe(View view) {
        String recipeScaleString = scaleAmount.getText().toString();
        float recipeScale = Float.parseFloat(recipeScaleString);
        // the scaleRecipe() function returns a new Recipe object that is scaled.
        Recipe scaledRecipe = recipe.scaleRecipe(recipeScale);
        servingSize.setText(String.valueOf(scaledRecipe.numberServings));
        costOutput.setText(String.valueOf(scaledRecipe.cost));
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}