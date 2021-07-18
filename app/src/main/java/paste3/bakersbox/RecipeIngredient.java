package paste3.bakersbox;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecipeIngredient {

    Ingredient _ingredient;
    float _quantity;
    Unit _unit;


    // Main Constructor
    public RecipeIngredient(Ingredient ingredient, float quantity, Unit unit){
        this._ingredient = ingredient;
        this._quantity = quantity;
        this._unit = unit;  // the Unit specified by the recipe
    }

    // "From Database" Constructor
    public RecipeIngredient(Map<String, Object> recipeIngredientMap) {
        this._ingredient = IngredientManager.getIngredient((String) recipeIngredientMap.get("ingredientName"));
        this._quantity = ((Number) recipeIngredientMap.get("quantity")).floatValue();
        this._unit = UnitManager.getUnit((String) recipeIngredientMap.get("unit"));
    }

    // Turns each recipeIngredient object into a map, so that they don't contain ingredient and
    // Unit objects, for saving to the Database.
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

    // Turns a list of recipeItems (simplified map )into a list of recipeIngredient Objects.
    public static List<RecipeIngredient> getRecipeItemList(List<Object> recipeItem) {
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        if (recipeItem == null) {
            Log.d("RecipeItemList is null", "");
        } else {
            for (Object item : recipeItem) {
                // Loops through the list of recipeItem names
                Map<String, Object> itemMap = (Map<String, Object>) item;
//                String ingredientName = (String) itemMap.get("ingredientName");
//                Ingredient ingredient = IngredientManager.getIngredient(ingredientName);
//
//                float quantity = ((Number) itemMap.get("quantity")).floatValue();
//
//                String unitLabel = (String) itemMap.get("unit");
//                Unit unit = UnitManager.getUnit(unitLabel);

                recipeIngredientList.add(new RecipeIngredient(itemMap));
            }
        }
        return recipeIngredientList;
    }

    // Calculates the price of the recipeIngredient.
    public float calcPrice(){
        float convertedQuantity = _ingredient.getUnit().convertTo(_unit, _quantity);
        return convertedQuantity * _ingredient.getAtomicPrice();
    }

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