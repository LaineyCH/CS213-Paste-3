package paste3.bakersbox;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RecipeManager {
    private static final ConcurrentMap<String, Recipe> recipeMap = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, List<String>> categoryMap = new ConcurrentHashMap<>();
    public static DatabaseReference dbRefRecipe = null;
    public static DatabaseReference dbRefCategory = null;


    // Sets the Database reference and populates the Recipe Map from the Database
    public static void setDbRefRecipe(DatabaseReference dbRef, OnInitialised onInitialised) {
        RecipeManager.dbRefRecipe = dbRef;
        populateRecipeMap(onInitialised);

        // Debugging - print a list of all the ingredient keys in the ingredient map
//        for (ConcurrentMap.Entry<String, Recipe> recipeEntry : recipeMap.entrySet())  {
//            Log.d("Recipe Map", recipeEntry.getKey());
//        } // end Debugging
    }

    // Sets the Database reference and populates the Categories Map from the Database
    public static void setDbRefCategory(DatabaseReference dbRef) {
        RecipeManager.dbRefCategory = dbRef;
        Log.d("dbRefCategory", "Created");
        populateCategoryMap();

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
    public static void populateRecipeMap(OnInitialised callback) {
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
                for (DataSnapshot recipeSnap : snapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) recipeSnap.getValue();
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
                callback.onInitialised();
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
            return RecipeManager.getRecipe("Pancakes");
            //throw new RuntimeException(String.format("Missing recipe: '%s'", recipeName));
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

    public static void populateCategoryMap() {
//        Log.d("Fetch", "fetching categories"); // Debugging

        // Fetches the Category lists form the Firebase Cloud Database.
        if (dbRefCategory == null) {
            Log.d("dbRefCategory", "Database reference not available.");
            return;
        }
        dbRefCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  Loops through the data retrieved from the Database
                for (DataSnapshot categorySnap : snapshot.getChildren()) {
                    String key = categorySnap.getKey();
                    List<String> value = (List<String>) categorySnap.getValue();
                    // If  not null, creates a Category and Recipe name list from the data
                    if (value == null) {
                        Log.d("Category loaded null", "");
                        continue;
                    }

                    // Adds the Category and list of Recipe Names to the Category Map
                    categoryMap.put(key, value);
                    // Log.d("Category Map", categoryMap.toString()); // Debugging
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Returns a list of the Categories in categoryMap (the key values of categoryMap).
    public static List<String> getCategoryList() {
        List<String> categoryList = new ArrayList<>();
        for (Map.Entry<String, List<String>> pair : categoryMap.entrySet()) {
            categoryList.add(pair.getKey());
        }
        Collections.sort(categoryList);
        Log.d("Category List", categoryList.toString());
        return categoryList;
    }

    // Returns a list of the Categories in categoryMap (the key values of categoryMap).
    public static List<String> getRecipeListPerCategory(String category) {
        List<String> recipeListPerCategory = categoryMap.get(category);
        Log.d("Recipe List Per Cat", recipeListPerCategory.toString());
        return recipeListPerCategory;
    }

    // Adds the new recipe name to the relevant category list in the categoryMap.
    public static void addRecipeToCategory(String recipeName, String categorySpinnerSelection) {
        List<String> recipeNames = categoryMap.get(categorySpinnerSelection);

        // if list does not exist create it
        if(recipeNames == null) {
            recipeNames = new ArrayList<String>();
            recipeNames.add(recipeName);
            categoryMap.put(categorySpinnerSelection, recipeNames);
        } else {
            // add if item is not already in list
            if(!recipeNames.contains(recipeName)) recipeNames.add(recipeName);
        }
    }

    // Saves the Category Map to the Database.
    public static void saveCategories() {
        Log.d("Saving Categories", "To Database");

        if (dbRefCategory == null) {
            Log.d("dbRefCategory", "Database reference not available.");
            return;
        }
        dbRefCategory.setValue(categoryMap);
    }


    // Saves Initial Categories to the Firebase Cloud Database
    public static void saveCategoriesToDatabase() {
        Log.d("Saving Categories", "To Database");
        Map<String, Object> initialCategoryMap = new HashMap<>();

        if (dbRefCategory == null) {
            Log.d("dbRefCategory", "Database reference not available.");
            return;
        }

        //HashMap<String, String> emptyList = new HashMap<>();
        initialCategoryMap.put("Bars", Arrays.asList(" "));
        initialCategoryMap.put("Breads", Arrays.asList(" "));
        initialCategoryMap.put("Cakes", Arrays.asList(" "));
        initialCategoryMap.put("Cookies", Arrays.asList(" "));
        initialCategoryMap.put("Cup Cakes", Arrays.asList(" "));
        initialCategoryMap.put("Desserts", Arrays.asList(" "));
        initialCategoryMap.put("Loaves", Arrays.asList(" "));
        initialCategoryMap.put("Muffins", Arrays.asList(" "));
        initialCategoryMap.put("Pies", Arrays.asList(" "));
        initialCategoryMap.put("Pastries", Arrays.asList(" "));
        initialCategoryMap.put("Savoury Snacks", Arrays.asList(" "));
        initialCategoryMap.put("Tray Bakes", Arrays.asList(" "));

        Log.d("Category Map", initialCategoryMap.toString());

        dbRefCategory.setValue(initialCategoryMap);
    }

}