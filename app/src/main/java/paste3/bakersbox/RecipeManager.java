package paste3.bakersbox;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RecipeManager {
    private static final ConcurrentMap<String, Recipe> recipeMap = new ConcurrentHashMap<>();
    public static DatabaseReference dbRefRecipe = null;

    // Sets the Database reference and populates the Recipe Map from the Database
    public static void setDbRefRecipe(DatabaseReference dbRef) {
        RecipeManager.dbRefRecipe = dbRef;
        populateRecipeMap();

        // Debugging - print a list of all the ingredient keys in the ingredient map
//        for (ConcurrentMap.Entry<String, Recipe> recipeEntry : recipeMap.entrySet())  {
//            Log.d("Recipe Map", recipeEntry.getKey());
//        } // end Debugging
    }

    public static ConcurrentMap<String, Recipe> getRecipeMap() {
        return recipeMap;
    }

    /*
     * Make sure the Recipe Map contains the Recipe objects. If not, fetch them from the database.
     */
    public static void populateRecipeMap() {
//        Log.d("Fetch", "fetching recipes"); // Debugging

        // Fetches the Unit objects form the Firebase Cloud Database.
        if (dbRefRecipe == null) {
            Log.d("dbRefRecipe", "Database reference not available.");
            return;
        }
        dbRefRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  Loops through the data retrieved from the Database
                for (DataSnapshot unitSnap : snapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) unitSnap.getValue();
                    // If  not null, creates a Recipe object from the data
                    if (value == null) {
                        Log.d("Recipe loaded null", "");
                        continue;
                    }
                    Recipe recipe = new Recipe(value);

//                    assert recipe != null; // Debugging
//                    Log.d("Recipe", recipe.getRecipeName()); // Debugging

                    // Adds the Recipe to the Recipe Map
                    recipeMap.put(recipe.getRecipeName(), recipe);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Triggered by the submit button of the Recipe Activity. Creates a Recipe from the data provided
    // and adds it to the Recipe Map
    public static void addRecipe(String recipeName, List<RecipeIngredient> recipeIngredientList,
                                 float prepTime, float cookTime, float numberServings,
                                 String typeServing, String method) {

        Log.d("New Recipe", "about to create");  // Debugging

        // Creates a new recipe
        Recipe recipe = new Recipe(recipeName, recipeIngredientList, prepTime, cookTime,
                numberServings, typeServing, method);

//        Log.d("New Recipe created", recipe.getMethod());  // Debugging

        // Adds Recipe to the Recipe Map
        recipeMap.put(recipe.getRecipeName(), recipe);

        saveRecipes();  // Debugging - need to decide when we want to save

    }

    // Creates a simplified Map of the Recipe Objects (so that there are no Ingredient, nor Unit
    // Objects within the Map).
    public static Map<String, Object> getSimplifiedRecipeMap() {
        Map<String, Object> myMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, Recipe> entry: recipeMap.entrySet()) {
            myMap.put(entry.getKey(), entry.getValue().toMap());
        }
        return myMap;
    }

    // Sends the simplified recipe map to the Firebase Cloud Database
    public static void saveRecipes() {
        if (dbRefRecipe == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        dbRefRecipe.setValue(getSimplifiedRecipeMap());
    }

    // Returns the Recipe that matches the key, from the Recipe Map.
    public static Recipe getRecipe(String recipeName) {
        Recipe recipe = recipeMap.get(recipeName);
        if(recipe == null){
            throw new RuntimeException(String.format("Missing recipe: '%s'", recipeName));
        }
        return recipe;
    }

    // Returns a list of all the recipes by their recipeName
    public static List<String> getRecipeNameList() {
        List<String> recipeNameList = new ArrayList<>();
        for (Map.Entry<String, Recipe> entry: recipeMap.entrySet()) {
            recipeNameList.add(entry.getValue().getRecipeName());
        }
        return recipeNameList;
    }
}