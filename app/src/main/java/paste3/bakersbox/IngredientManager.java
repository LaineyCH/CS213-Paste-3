package paste3.bakersbox;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Ingredient Manager creates ingredients, and maintains the Ingredient Map
 */
public class IngredientManager {
    private static final ConcurrentMap<String, Ingredient> ingredientsMap = new ConcurrentHashMap<>();
    public static DatabaseReference dbRefIngredient = null;

    public static ConcurrentMap<String, Ingredient> getIngredientsMap() {
        return ingredientsMap;
    }

    public static void setDbRefIngredient(DatabaseReference dbRef, OnInitialised onInitialised) {
        IngredientManager.dbRefIngredient = dbRef;
        populateIngredientMap(onInitialised);
    }

    /*
     * Make sure the Unit Map contains the Unit object. If not, fetch them from the database.
     */
    public static void populateIngredientMap(OnInitialised callback) {
        Log.d("Fetch", "fetching ingredients"); // Debugging

        // Fetches the Unit objects form the Firebase Cloud Database.
        if (dbRefIngredient == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        dbRefIngredient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot unitSnap : snapshot.getChildren()) {
                    Map<String, Object> ingredientMap = (Map<String, Object>) unitSnap.getValue();
                    Ingredient ingredient = new Ingredient(ingredientMap);

                    Log.d("Ingredient", ingredient.getIngredientName()); // Debugging

                    ingredientsMap.put(ingredient.getIngredientName(), ingredient);
                }
                callback.onInitialised();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Triggered by the submit button of the Ingredient Activity.
    public static void addIngredient(String ingredientName, String thisUnitLabel, String userUnitLabel, Float quantity, Float price) {
        // if ingredient doesn't already exist in the ingredient map, add the new ingredient.
        if (ingredientsMap.get(ingredientName) == null) {
            Ingredient ingredient = new Ingredient(ingredientName, thisUnitLabel, userUnitLabel, quantity, price);
            ingredientsMap.put(ingredientName, ingredient);
        } else {
            // otherwise, if the ingredient already exists in the ingredient map, edit it.
            editIngredient(ingredientName, thisUnitLabel, userUnitLabel, quantity, price);
        }
    }

    // Returns the Ingredient that matches the key, from the Ingredient Map.
    public static Ingredient getIngredient(String ingredientLabel) {
        Ingredient ingredient = ingredientsMap.get(ingredientLabel);
        if(ingredient == null){
            throw new RuntimeException(String.format("Missing ingredient: '%s'", ingredientLabel));
        }
        return ingredient;
    }

    // Used when an Ingredient already exists, but values need to be changed.
    public static void editIngredient(String ingredientName, String thisUnitLabel, String userUnitLabel, Float quantity, Float price) {
        Ingredient ingredient = ingredientsMap.get(ingredientName);
        Unit thisUnit = UnitManager.getUnit(thisUnitLabel);
        assert ingredient != null; // Debugging
        float convertedQuantity = ingredient.setUnit(thisUnitLabel, userUnitLabel, quantity);
        ingredient.setAtomicPrice(price, convertedQuantity);
    }

    public static Map<String, Object> getSimplifiedIngredientMap() {
        Map<String, Object> myMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, Ingredient> entry: ingredientsMap.entrySet()) {
            myMap.put(entry.getKey(), entry.getValue().toMap());
        }
        return myMap;
    }

    // Sends ingredient map to the Firebase Cloud Database
    public static void saveIngredients() {
        if (dbRefIngredient == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        dbRefIngredient.setValue(getSimplifiedIngredientMap());
    }

}
