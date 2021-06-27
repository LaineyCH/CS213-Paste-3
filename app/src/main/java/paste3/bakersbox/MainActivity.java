package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.layout1);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        // Create an instance of the Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://baker-s-box-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("BBStorage");

        UnitManager.setDbRefUnit(myRef.child("unit"), new OnInitialised() {
            @Override
            public void onInitialised() {

                // Debugging - print a list of all the ingredient keys in the ingredient map
                Map<String, Unit> unitMap = UnitManager.getUnitMap();
                Log.d("UnitMapSize", String.format("%d",unitMap.size()));
                for (Map.Entry<String, Unit> unitEntry : unitMap.entrySet())  {
                    Log.d("Unit Map", unitEntry.getKey()); // Debugging
                }

                IngredientManager.setDbRefIngredient(myRef.child("ingredients"), new OnInitialised() {
                    @Override
                    public void onInitialised() {

                        // Debugging - print a list of all the ingredient keys in the ingredient map
                        Map<String, Ingredient> ingredientsMap = IngredientManager.getIngredientsMap();
                        for (Map.Entry<String, Ingredient> recipeEntry : ingredientsMap.entrySet())  {
                            Log.d("Ingredient Map", recipeEntry.getKey()); // Debugging
                        }

                        RecipeManager.setDbRefRecipe(myRef.child("recipes"));
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