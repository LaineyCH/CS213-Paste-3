package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditRecipeActivity extends AppCompatActivity {

    EditText inputPrepTime;
    EditText inputCookTime;
    EditText inputServings;
    EditText inputTypeServing;
    EditText inputMethod;
    TextView ingredientsInput;
    TextView recipeName;
    Recipe recipe = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        LinearLayout li=(LinearLayout)findViewById(R.id.editRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        inputPrepTime = findViewById(R.id.prepTimeInput);
        inputCookTime = findViewById(R.id.cookTimeInput);
        inputServings = findViewById(R.id.servingsInput);
        inputTypeServing = findViewById(R.id.servingTypeInput);
        inputMethod = findViewById(R.id.methodInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        recipeName = findViewById(R.id.recipeName2);
        Intent intent = getIntent();
        String recipeNameInput = intent.getStringExtra("recipeName");
        recipe = RecipeManager.getRecipe(recipeNameInput);

        recipeName.setText(recipe._recipeName);
        inputPrepTime.setText(String.valueOf(recipe.prepTime));
        inputCookTime.setText(String.valueOf(recipe.cookTime));
        inputServings.setText(String.valueOf(recipe.numberServings));
        inputTypeServing.setText(recipe.typeServing);
        inputMethod.setText(recipe.method);
        String ingredientsOutputString = "";
        for(int i = 0; i < recipe.recipeItems.size(); i++){
            ingredientsOutputString += "" + String.valueOf(recipe.recipeItems.get(i)._quantity) + " " + recipe.recipeItems.get(i)._unit + " " +
                    recipe.recipeItems.get(i)._ingredient._ingredientName + "\n";
        }
        ingredientsInput.setText(ingredientsOutputString);
    }
}