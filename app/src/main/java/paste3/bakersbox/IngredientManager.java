package paste3.bakersbox;

import java.util.HashMap;
import java.util.Map;

/*
 * Ingredient Manager creates ingredients, and maintains the Ingredient Map
 */
public class IngredientManager {
    private static final Map<String, Ingredient> ingredientsMap = new HashMap<>();

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
}
