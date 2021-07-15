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
        DatabaseReference unitRef = myRef.child("unit");
        DatabaseReference ingredientRef = myRef.child("ingredients");
        DatabaseReference recipeRef = myRef.child("recipes");
        DatabaseReference categoryRef =  myRef.child("categories");
        DatabaseReference shoppingListRef = myRef.child("shoppingList");

        // Set Database reference for Unit storage. The OnInitialised interface ensures that the
        // Unit map is populated before the Ingredient map begins populating.
        UnitManager.setDbRefUnit(unitRef, new OnInitialised() {
            @Override
            public void onInitialised() {

                // Set Database reference for Ingredient storage. The OnInitialised interface ensures
                // that the Ingredient map is populated before the Recipe map begins populating.
                IngredientManager.setDbRefIngredient(ingredientRef, new OnInitialised() {
                    @Override
                    public void onInitialised() {

                        // Set Database reference for Recipe storage
                        RecipeManager.setDbRefRecipe(recipeRef, new OnInitialised() {
                            @Override
                            public void onInitialised() {

                                // Set Database reference for Category storage
                                RecipeManager.setDbRefCategory(categoryRef);
                                RecipeManager.setDbRefShoppingList(shoppingListRef);
                            }
                        });
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

    public void goToShoppingList(View view){
        Intent intent = new Intent(this,ShoppingListActivity.class);
        startActivity(intent);
    }

//    public void goToPicture(View view) {
//        Intent intent = new Intent(this,LoadPictureActivity.class);
//        startActivity(intent);
//        }
}