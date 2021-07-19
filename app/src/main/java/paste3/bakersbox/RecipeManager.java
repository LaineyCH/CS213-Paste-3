package paste3.bakersbox;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SINGLETON
 * The Recipe Manager object creates recipes and maintains the recipeMap, the categoryMap, and the
 * shoppingList.
 */
public class RecipeManager {
    private static final ConcurrentMap<String, Recipe> recipeMap = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, List<String>> categoryMap = new ConcurrentHashMap<>();
    private static OnListChangedListener onShoppingListLoaded = null;
    public static List<ShoppingListItem> shoppingList = new ArrayList<>();
    public static DatabaseReference dbRefRecipe = null;
    public static DatabaseReference dbRefCategory = null;
    public static DatabaseReference dbRefShoppingList = null;

    /**
     * Ensures sequential execution of processes
     */
    public static void setOnShoppingListLoaded(OnListChangedListener onShoppingListLoaded) {
        RecipeManager.onShoppingListLoaded = onShoppingListLoaded;
    }

    /**
     * Sets the Database reference and populates the Recipe Map from the Database
     * @param dbRef the database reference
     * @param onInitialised ensures sequential execution of processes
     */
    public static void setDbRefRecipe(DatabaseReference dbRef, OnInitialised onInitialised) {
        RecipeManager.dbRefRecipe = dbRef;
        populateRecipeMap(onInitialised);
    }

    /**
     * Sets the Database reference and populates the Categories Map from the Database
     * @param dbRef the database reference
     */
    public static void setDbRefCategory(DatabaseReference dbRef) {
        RecipeManager.dbRefCategory = dbRef;
//        Log.d("dbRefCategory", "Created"); // Debugging
        populateCategoryMap();

//        Debugging - print a list of all the ingredient keys in the ingredient map
//        for (ConcurrentMap.Entry<String, Recipe> recipeEntry : recipeMap.entrySet())  {
//            Log.d("Recipe Map", recipeEntry.getKey());
//        } // end Debugging
    }

    /**
     * Sets the Database reference and populates the Shopping List from the Database
     * @param dbRef the database reference
     */
    public static void setDbRefShoppingList(DatabaseReference dbRef) {
        RecipeManager.dbRefShoppingList = dbRef;
//        Log.d("dbRefShoppingList", "Created"); // Debugging
        populateShoppingList();
    }

    /*
     * Make sure the recipeMap contains the Recipe objects. If not, fetch them from the database.
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



    /**
     * Called by the create recipe activity and the edit recipe activity, when a new recipe is to
     * be created, or and existing one edited. Creates a Recipe from the data passed in,
     * provided and adds it to the Recipe Map
     * @param recipeName the recipe name
     * @param recipeIngredientList a list of recipeIngredients
     * @param prepTime the recipe's preparation time
     * @param cookTime how long the recipe is cooked / baked - a measure of appliance and/or
     *      *                 electricity / fue use
     * @param numberServings the number of servings produced by the recipe
     * @param typeServing the type of serving (slice, bar, cake, serving, etc)
     * @param method the recipe's method / instructions
     */
    public static void addRecipe(String recipeName, List<RecipeIngredient> recipeIngredientList,
                                 float prepTime, float cookTime, float numberServings,
                                 String typeServing, String method) {

        // Check whether recipe already exists in the recipeMap
        if (recipeMap.get(recipeName) == null) {
            // Create a new recipe if it doesn't already exist in the recipeMap
            Recipe recipe = new Recipe(recipeName, recipeIngredientList, prepTime, cookTime,
                    numberServings, typeServing, method);
            // Adds new Recipe to the Recipe Map
            recipeMap.put(recipe.getRecipeName(), recipe);
        } else {
            // Otherwise, update the recipe already in the recipeMap.
            editRecipe(recipeName, recipeIngredientList, prepTime, cookTime, numberServings,
                    typeServing, method);
        }
        // Save to database
        saveRecipes();
    }

    /**
     * Called when a recipe is to be edited. Updates the attributes of an existing Recipe.
     * @param recipeName the name of the recipe (can't be changed)
     * @param recipeIngredientList the list of recipeIngredient
     * @param prepTime the recipe's prep time
     * @param cookTime the recipe's cook/bake time
     * @param numberServings the number of servings the recipe produces
     * @param typeServing the type of serving (slice, bar, cake, serving, etc)
     * @param method the recipe's method / instructions
     */
    public static void editRecipe(String recipeName, List<RecipeIngredient> recipeIngredientList,
                                  float prepTime, float cookTime, float numberServings,
                                  String typeServing, String method) {
        Recipe editedRecipe = recipeMap.get(recipeName);
        assert editedRecipe != null;
        editedRecipe.setRecipeIngredients(recipeIngredientList);
        editedRecipe.setPrepTime(prepTime);
        editedRecipe.setCookTime(cookTime);
        editedRecipe.setNumberServings(numberServings);
        editedRecipe.setTypeServing(typeServing);
        editedRecipe.setMethod(method);
        editedRecipe.setCost();
    }


    /**
     *  Creates a simplified Map of the Recipe Objects (so that there are no Ingredient, nor Unit
     *  Objects within the Map).
     */
    public static Map<String, Object> getSimplifiedRecipeMap() {
        Map<String, Object> myMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, Recipe> entry: recipeMap.entrySet()) {
            myMap.put(entry.getKey(), entry.getValue().toMap());
        }
        return myMap;
    }

    /**
     * Sends the simplified recipe map to the Firebase Cloud Database
     */
    public static void saveRecipes() {
        if (dbRefRecipe == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        dbRefRecipe.setValue(getSimplifiedRecipeMap());
    }

    /**
     * Returns the Recipe that matches the parameter, from the Recipe Map.
     * @param recipeName the recipe name of the recipe wanted
     * @return a recipe object
     */
    public static Recipe getRecipe(String recipeName) {
        Recipe recipe = recipeMap.get(recipeName);
        if(recipe == null){
            return RecipeManager.getRecipe("Brownies");
            //throw new RuntimeException(String.format("Missing recipe: '%s'", recipeName));
        }
        return recipe;
    }

    /**
     * Returns a list of all the recipes by their recipeName (strings)
     * @return a list of recipe name strings
     */
    public static List<String> getRecipeNameList() {
        List<String> recipeNameList = new ArrayList<>();
        for (Map.Entry<String, Recipe> entry: recipeMap.entrySet()) {
            recipeNameList.add(entry.getValue().getRecipeName());
        }
        Collections.sort(recipeNameList);
        return recipeNameList;
    }

    /**
     * Make sure the categoryMap contains the categories. If not, fetch them from the database.
     */
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
//                    Log.d("Category Map", categoryMap.toString()); // Debugging
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Make sure the shoppingList contains the shopping list. If not, fetch it from the database.
     */
    public static void populateShoppingList() {
        // Fetches the Shopping List form the Firebase Cloud Database.
        if (dbRefShoppingList == null) {
            return;
        }
        dbRefShoppingList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingList.clear();
                //  Loops through the data retrieved from the Database
                for (DataSnapshot shoppingListSnap : snapshot.getChildren()) {
                    ShoppingListItem item = shoppingListSnap.getValue(ShoppingListItem.class);
                    if (item == null) {
                        Log.d("Shopping list loaded null", "");
                        continue;
                    }
                    // Adds the item to the shopping list
                    shoppingList.add(item);
                }
                if (onShoppingListLoaded != null) {
                    onShoppingListLoaded.onListChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Returns a list of the Categories in categoryMap (the key values of categoryMap).
     * @return categoryList
     */
    public static List<String> getCategoryList() {
        List<String> categoryList = new ArrayList<>();
        for (Map.Entry<String, List<String>> pair : categoryMap.entrySet()) {
            categoryList.add(pair.getKey());
        }
        Collections.sort(categoryList);
//        Log.d("Category List", categoryList.toString()); // Debugging
        return categoryList;
    }

    /**
     * Returns a list of the recipes in a category from the categoryMap.
     * @param category the category for which a list of recipes is wanted
     * @return recipeListPerCategory
     */
    public static List<String> getRecipeListPerCategory(String category) {
        List<String> recipeListPerCategory = categoryMap.get(category);
        assert recipeListPerCategory != null;
        Collections.sort(recipeListPerCategory);
//        Log.d("Recipe List Per Cat", recipeListPerCategory.toString()); // Debugging
        return recipeListPerCategory;
    }

    /**
     * Adds the new recipe name to the relevant category list in the categoryMap.
     * @param recipeName the recipe name to be added to the category list
     * @param categorySpinnerSelection the category it should be added to
     */
    public static void addRecipeToCategory(String recipeName, String categorySpinnerSelection) {
        List<String> recipeNames = categoryMap.get(categorySpinnerSelection);

        // if list does not exist create it
        if(recipeNames == null) {
            recipeNames = new ArrayList<>();
            recipeNames.add(recipeName);
            categoryMap.put(categorySpinnerSelection, recipeNames);
        } else {
            // add if item is not already in list
            if(!recipeNames.contains(recipeName)) recipeNames.add(recipeName);
        }
    }

    /**
     * Saves the Category Map to the Database.
     */
    public static void saveCategories() {
//        Log.d("Saving Categories", "To Database");
        if (dbRefCategory == null) {
            Log.d("dbRefCategory", "Database reference not available.");
            return;
        }
        dbRefCategory.setValue(categoryMap);
    }

    /**
     * Returns the Shopping List
     * @return the shoppingList
     */
    public static List<ShoppingListItem> getShoppingList() {
        return shoppingList;
    }

    /**
     * Saves the Shopping List to the Database.
     */
    public static void saveShoppingList() {
        if (dbRefShoppingList == null) {
            Log.d("dbRefCategory", "Database reference not available.");
            return;
        }
        dbRefShoppingList.setValue(shoppingList);
    }

    // Removes the Shopping List from the Database.
    public static void clearShoppingList() {
//        Log.d("Saving Shopping List", "To Database");
        if (dbRefShoppingList == null) {
            Log.d("dbRefCategory", "Database reference not available.");
            return;
        }
        dbRefShoppingList.removeValue();
    }

    /**
     * Saves Initial Categories to the Firebase Cloud Database - NOT IN USE
     */
    public static void saveCategoriesToDatabase() {
//        Log.d("Saving Categories", "To Database");
        Map<String, Object> initialCategoryMap = new HashMap<>();
        if (dbRefCategory == null) {
            Log.d("dbRefCategory", "Database reference not available.");
            return;
        }
//        List<String> bars = new ArrayList<>();
//        bars.add(" ");
//        bars.add("Oat Crunchy Bars");
//        List<String> cakes = new ArrayList<>();
//        cakes.add(" ");
//        cakes.add("Chocolate Cake");
        initialCategoryMap.put("Bars", Collections.singletonList(" "));
        initialCategoryMap.put("Breads", Collections.singletonList(" "));
        initialCategoryMap.put("Cakes", Collections.singletonList(" "));
        initialCategoryMap.put("Cookies", Collections.singletonList(" "));
        initialCategoryMap.put("Cup Cakes", Collections.singletonList(" "));
        initialCategoryMap.put("Desserts", Collections.singletonList(" "));
        initialCategoryMap.put("Loaves", Collections.singletonList(" "));
        initialCategoryMap.put("Muffins", Collections.singletonList(" "));
        initialCategoryMap.put("Pies", Collections.singletonList(" "));
        initialCategoryMap.put("Pastries", Collections.singletonList(" "));
        initialCategoryMap.put("Savoury Snacks", Collections.singletonList(" "));
        initialCategoryMap.put("Tray Bakes", Collections.singletonList(" "));

        Log.d("Category Map", initialCategoryMap.toString());

        dbRefCategory.setValue(initialCategoryMap);
    }
}