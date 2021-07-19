package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The Main Activity class for Baker's Box. It sets up all the Database references for data storage.
 * It also serves as the main menu for the app, offering 4 buttons for the user to choose from.
 */
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

        // Set the Database references for Baker's Box
        DatabaseReference myRef = database.getReference("BBStorage");
        DatabaseReference unitRef = myRef.child("unit");
        DatabaseReference ingredientRef = myRef.child("ingredients");
        DatabaseReference recipeRef = myRef.child("recipes");
        DatabaseReference categoryRef =  myRef.child("categories");
        DatabaseReference shoppingListRef = myRef.child("shoppingList");


         // Set Database reference for Unit storage. The OnInitialised interface ensures sequential
         // execution of processes, so that the Unit map is populated before the Ingredient map
         // begins populating.
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

    /**
     * Triggered by the "recipes" button in the activity_main layout, takes the user to the
     * activity_recipe layout.
     * @param view the current view
     */
    public void goToRecipes(View view){
        Intent intent = new Intent(this,RecipeActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by the "ingredients" button in the activity_main layout, takes the user to the
     * activity_ingredients layout.
     * @param view the current view
     */
    public void goToIngredients(View view){
        Intent intent = new Intent(this,IngredientsActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by the "quote" button in the activity_main layout, takes the user to the
     * activity_quote layout.
     * @param view the current view
     */
    public void goToQuote(View view) {
        Intent intent = new Intent(this, QuoteActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by the "shopping list" button in the activity_main layout, takes the user to the
     * activity_shopping_list layout.
     * @param view the current view
     */
    public void goToShoppingList(View view){
        Intent intent = new Intent(this,ShoppingListActivity.class);
        startActivity(intent);
    }
}