package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.mainActivity);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // Create an instance of the Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://baker-s-box-default-rtdb.europe-west1.firebasedatabase.app/");

        // Set the Database reference for Baker's Box
        DatabaseReference myRef = database.getReference("BBStorage");

        // Set Database reference for Unit storage. The OnInitialised interface ensures that the
        // Unit map is populated before the Ingredient map begins populating.
        UnitManager.setDbRefUnit(myRef.child("unit"), new OnInitialised() {
            @Override
            public void onInitialised() {

                  // Debugging - print a list of all the unit keys in the unit map
//                Map<String, Unit> unitMap = UnitManager.getUnitMap();
//                Log.d("UnitMapSize", String.format("%d",unitMap.size()));
//                for (Map.Entry<String, Unit> unitEntry : unitMap.entrySet())  {
//                    Log.d("Unit Map", unitEntry.getKey());
//                } // end Debugging

                // Set Database reference for Ingredient storage. The OnInitialised interface ensures
                // that the Ingredient map is populated before the Recipe map begins populating.
                IngredientManager.setDbRefIngredient(myRef.child("ingredients"), new OnInitialised() {
                    @Override
                    public void onInitialised() {

                          // Debugging - print a list of all the ingredient keys in the ingredient map
//                        Map<String, Ingredient> ingredientsMap = IngredientManager.getIngredientsMap();
//                        for (Map.Entry<String, Ingredient> recipeEntry : ingredientsMap.entrySet())  {
//                            Log.d("Ingredient Map", recipeEntry.getKey());
//                        } // end Debugging

                        // Set Database reference for Recipe storage
                        RecipeManager.setDbRefRecipe(myRef.child("recipes"));

                        // Debugging - print a list of all the recipe keys in the recipe map
//                      Map<String, Recipe> recipeMap = RecipeManager.getRecipeMap();
//                      for (Map.Entry<String, Recipe> recipeEntry : recipeMap.entrySet())  {
//                      Log.d("Recipe Map", unitEntry.getKey());
//                      } // end Debugging
                    }
                });
            }
        });
    }

    public void goToRecipes(View view){
        Intent intent = new Intent(this,RecipeActivity.class);
        startActivity(intent);
    }

    public void goToIngredients(View view){
        Intent intent = new Intent(this,IngredientsActivity.class);
        startActivity(intent);
    }
}