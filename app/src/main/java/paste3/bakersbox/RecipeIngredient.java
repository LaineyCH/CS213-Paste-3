package paste3.bakersbox;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The RecipeIngredient class, creates new RecipeIngredient objects. Each recipeIngredient object
 * has an Ingredient object, a quantity, and a Unit object.
 */
public class RecipeIngredient {

    private Ingredient _ingredient;
    private float _quantity;
    private Unit _unit;

    /**
     * Main constructor for creating a new recipeIngredient object
     * @param ingredient an ingredient object
     * @param quantity the quantity of the ingredient needed for the recipe
     * @param unit the Unit specified by the recipe
     */
    public RecipeIngredient(Ingredient ingredient, float quantity, Unit unit){
        this._ingredient = ingredient;
        this._quantity = quantity;
        this._unit = unit;
    }

    /**
     * "From Database" Constructor - creates a new recipeIngredient from the data retrieved from
     * the database
     * @param recipeIngredientMap the map of data retrieved from the database
     */
    public RecipeIngredient(Map<String, Object> recipeIngredientMap) {
        this._ingredient = IngredientManager.getIngredient((String) recipeIngredientMap
                .get("ingredientName"));
        this._quantity = ((Number) Objects.requireNonNull(recipeIngredientMap.get("quantity")))
                .floatValue();
        this._unit = UnitManager.getUnit((String) recipeIngredientMap.get("unit"));
    }

    /**
     * Turns each recipeIngredient object into a simplified map, so that they don't contain any
     * ingredient or unit objects, for saving to the Database.
     * @return recipeIngredientMap
     */
    public Map<String, Object> toMap() {
        Map<String, Object> recipeIngredientMap = new HashMap<>();
        //The Ingredient Object is recorded in the map's key : value structure, with its value
        // being the string "ingredientName"
        recipeIngredientMap.put("ingredientName", _ingredient.getIngredientName());
        recipeIngredientMap.put("quantity", _quantity);
        //The Unit Object is recorded in the map's key : value structure, with its value
        // being the string "unitLabel"
        recipeIngredientMap.put("unit", _unit.getUnitLabel());
        return recipeIngredientMap;
    }

    /**
     * Turns a list of object that are maps (received from the database) into a list of
     * recipeIngredient object
     * @param recipeIngredientMaps a list of recipeIngredient maps (no ingredient and unit objects)
     * @return a list of recipeIngredient objects
     */
    public static List<RecipeIngredient> getRecipeItemList(List<Map<String, Object>> recipeIngredientMaps) {
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        if (recipeIngredientMaps == null) {
            Log.d("RecipeItemList is null", "");
        } else {
            for (Object item : recipeIngredientMaps) {
                // Loops through the list of recipeItem names
                Map<String, Object> itemMap = (Map<String, Object>) item;
                recipeIngredientList.add(new RecipeIngredient(itemMap));
            }
        }
        return recipeIngredientList;
    }

    /**
     * Calculates the price of the recipeIngredient.
     * @return the price of the recipeIngredient.
     */
    public float calcPrice(){
        float convertedQuantity = _ingredient.getUnit().convertTo(_unit, _quantity);
        return convertedQuantity * _ingredient.getAtomicPrice();
    }

    /**
     * GETTERS AND SETTERS
     */

    public Ingredient getIngredient() {
        return _ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this._ingredient = ingredient;
    }

    public float getQuantity() {
        return _quantity;
    }

    public void setQuantity(float quantity) {
        this._quantity *= quantity;
    }

    public Unit getUnit() {
        return _unit;
    }

    public void setUnit(Unit unit) {
        this._unit = unit;
    }

}