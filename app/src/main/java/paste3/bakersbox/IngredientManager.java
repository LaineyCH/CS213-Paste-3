package paste3.bakersbox;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/*
 * Ingredient Manager creates ingredients, and maintains the Ingredient Map
 */
public class IngredientManager {
    private static final Map<String, Ingredient> ingredientsMap = new HashMap<>();
    private static WeakReference<DatabaseReference> dbRefIngredient = new WeakReference<>(null);


    public static WeakReference<DatabaseReference> getDbRef() {
        return dbRefIngredient;
    }

    public static void setDbRefIngredient(DatabaseReference dbRef) {
        IngredientManager.dbRefIngredient = new WeakReference<>(dbRef);
        populateIngredientMap();
    }

    /*
     * Make sure the Unit Map contains the Unit object. If not, fetch them from the database.
     */
    public static void populateIngredientMap() {
        Log.d("Fetch", "fetching units"); // Debugging

        // Fetches the Unit objects form the Firebase Cloud Database.
        DatabaseReference _dbRefIngredient = dbRefIngredient.get();
        if (dbRefIngredient == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        _dbRefIngredient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot unitSnap : snapshot.getChildren()) {
                    Map<String, Object> ingredientMap = (Map<String, Object>) unitSnap.getValue();
                    Ingredient ingredient = new Ingredient(ingredientMap);

                    Log.d("Ingredient", ingredient.get_ingredientName()); // Debugging

                    ingredientsMap.put(ingredient.get_ingredientName(), ingredient);
                }
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
    public static Ingredient findIngredient(String ingredientLabel) {
        return ingredientsMap.get(ingredientLabel);
    }

    // Used when an Ingredient already exists, but values need to be changed.
    public static void editIngredient(String ingredientName, String thisUnitLabel, String userUnitLabel, Float quantity, Float price) {
        Ingredient ingredient = ingredientsMap.get(ingredientName);
        Unit thisUnit = UnitManager.getUnit(thisUnitLabel);
        assert ingredient != null; // Debugging
        float convertedQuantity = ingredient.set_unit(thisUnitLabel, userUnitLabel, quantity);
        ingredient.set_atomicPrice(price, convertedQuantity);
    }

    public static Map<String, Object> getIngredientMap() {
        Map<String, Object> myMap = new HashMap<>();
        for (Map.Entry<String, Ingredient> entry: ingredientsMap.entrySet()) {
            myMap.put(entry.getKey(), entry.getValue().toMap());
        }
        return myMap;
    }

    // Sends ingredient map to the Firebase Cloud Database
    public static void saveIngredients() {

        IngredientManager.addIngredient("Plain Flour", "g", "kg", (float) 1.5, (float)1.80 );
        IngredientManager.addIngredient("Milk", "ml", "l", (float) 2.27, (float)1.10 );
        IngredientManager.addIngredient("Butter", "g", "g", (float) 150, (float)1.80 );
        IngredientManager.addIngredient("Baking Powder", "g", "g", (float) 50, (float)1.00 );
        IngredientManager.addIngredient("Eggs", "count", "count", (float) 12, (float)2.05 );

        DatabaseReference _dbRefIngredient = dbRefIngredient.get();
        if (dbRefIngredient == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        _dbRefIngredient.setValue(getIngredientMap());
    }
}
