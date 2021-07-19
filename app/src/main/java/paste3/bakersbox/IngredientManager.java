package paste3.bakersbox;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* SINGLETON
 * The Ingredient Manager object creates ingredients, and maintains the Ingredient Map
 */
public class IngredientManager {
    // Create the empty ingredientMap
    private static final ConcurrentMap<String, Ingredient> ingredientsMap = new ConcurrentHashMap<>();
    // Create a DatabaseReference for the ingredient map storage
    public static DatabaseReference dbRefIngredient = null;

    /**
     * Sets the Database reference for the ingredient map storage, and populates the Ingredient Map
     * from the Database
     * @param dbRef the database reference
     * @param callback used to ensure sequential execution
     */
    public static void setDbRefIngredient(DatabaseReference dbRef, OnInitialised callback) {
        IngredientManager.dbRefIngredient = dbRef;
        populateIngredientMap(callback);
    }

    /*
     * Populates the ingredientMap from the database. Makes sure the ingredientMap doesn't already
     * contain the ingredient objects - if not, fetches them from the database.
     */
    public static void populateIngredientMap(OnInitialised callback) {
//        Log.d("Fetch", "fetching ingredients"); // Debugging
        if (dbRefIngredient == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        // Fetches the Ingredient data from the Firebase Cloud Database.
        dbRefIngredient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Loops through the data retrieved from the Database
                for (DataSnapshot ingredientSnap : snapshot.getChildren()) {
                    Map<String, Object> ingredientMap =
                            (Map<String, Object>) ingredientSnap.getValue();
                    assert ingredientMap != null;
                    // Creates a new Ingredient object from the ingredientSnap (single object)
                    Ingredient ingredient = new Ingredient(ingredientMap);
//                    Log.d("Ingredient", ingredient.getIngredientName()); // Debugging
                    // Adds the new Ingredient to the Ingredient Map
                    ingredientsMap.put(ingredient.getIngredientName(), ingredient);
                }
                // Once this is executed, the next precess can execute
                callback.onInitialised();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Called when a new ingredient needs to be created and added to the ingredientMap, or when
     * an existing ingredient needs to be edited.
     * @param ingredientName the ingredient's name
     * @param thisUnitLabel the ingredient's base unit
     * @param userUnitLabel the unit that the user purchased the ingredient as
     * @param quantity the quantity of the ingredient purchased
     * @param price how much was paid for the ingredient
     */
    public static void addIngredient(String ingredientName, String thisUnitLabel,
                                     String userUnitLabel, Float quantity, Float price) {
        // if ingredient doesn't already exist in the ingredient map, add the new ingredient.
        if (ingredientsMap.get(ingredientName) == null) {
            Ingredient ingredient = new Ingredient(ingredientName, thisUnitLabel, userUnitLabel,
                    quantity, price);
            ingredientsMap.put(ingredientName, ingredient);

//            Log.d("New Ingredient", ingredient.getIngredientName());  // Debugging
        } else {
            // otherwise, if the ingredient already exists in the ingredient map, edit it.
            editIngredient(ingredientName, thisUnitLabel, userUnitLabel, quantity, price);
        }
        // Save to the database
        saveIngredients();
    }

    /**
     * Returns the Ingredient object that matches the ingredientName, from the Ingredient Map
     * @param ingredientName the name of the ingredient wanted
     * @return the ingredient object
     */
    public static Ingredient getIngredient(String ingredientName) {
        // Fetches the ingredient that matches the
        Ingredient ingredient = ingredientsMap.get(ingredientName);
        if(ingredient == null){
            throw new RuntimeException(String.format("Missing ingredient: '%s'", ingredientName));
        }
        return ingredient;
    }

    /**
     * Called when an existing ingredient is being edited. Set the new values for the ingredient
     * object in the ingredientMap (the ingredient name can not be changed).
     * @param ingredientName the ingredient's name
     * @param thisUnitLabel the ingredient's base unit
     * @param userUnitLabel the unit of the ingredient purchased
     * @param quantity the quantity of the ingredient purchased
     * @param price the price paid for the ingredient
     */
    public static void editIngredient(String ingredientName, String thisUnitLabel,
                                      String userUnitLabel, Float quantity, Float price) {
        // Fetch the ingredient from the ingredientMap
        Ingredient ingredient = ingredientsMap.get(ingredientName);
        assert ingredient != null; // Debugging
        // Set the unit object
        ingredient.setUnit(thisUnitLabel, userUnitLabel, quantity);
        // Set new atomic price
        float convertedQuantity = ingredient.setUnit(thisUnitLabel, userUnitLabel, quantity);
        ingredient.setAtomicPrice(price, convertedQuantity);
    }

    /**
     * Coverts each Ingredient Object in the Ingredient Map to a simplified object for sending to
     * the database (a custom toMap() is called on Unit Objects within each Ingredient Object).
     * @return the simplified ingredient map
     */
    public static Map<String, Object> getSimplifiedIngredientMap() {
        Map<String, Object> myMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, Ingredient> entry: ingredientsMap.entrySet()) {
            myMap.put(entry.getKey(), entry.getValue().toMap());
        }
        return myMap;
    }

    /**
     * Sends the simplified ingredient map to the Firebase Cloud Database
     */
    public static void saveIngredients() {
        if (dbRefIngredient == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        dbRefIngredient.setValue(getSimplifiedIngredientMap());
    }

    /**
     * Creates and returns a string list of all the ingredient names in the ingredientMap
     * @return a list of strings of all the ingredient names
     */
    public static List<String> getIngredientNameList() {
        List<String> ingredientNameList = new ArrayList<>();
        for (Map.Entry<String, Ingredient> entry: ingredientsMap.entrySet()) {
            ingredientNameList.add(entry.getValue().getIngredientName());
        }
        Collections.sort(ingredientNameList);
        return ingredientNameList;
    }

}
