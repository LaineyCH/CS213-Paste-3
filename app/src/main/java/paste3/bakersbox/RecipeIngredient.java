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
        this._unit = unit;
    }

    // "From Database" Constructor
    public RecipeIngredient(Map<String, Object> recipeIngredientMap) {
        this._ingredient = IngredientManager.getIngredient((String) recipeIngredientMap.get("ingredientName"));
        this._quantity = ((Double) recipeIngredientMap.get("quantity")).floatValue();
        this._unit = UnitManager.getUnit((String) recipeIngredientMap.get("unit"));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> recipeIngredientMap = new HashMap<>();
        recipeIngredientMap.put("ingredientName", _ingredient.getIngredientName());
        recipeIngredientMap.put("quantity", _quantity);
        recipeIngredientMap.put("unit", _unit.getUnitLabel());
        return recipeIngredientMap;
    }

    public static List<RecipeIngredient> getRecipeItemList(List<Object> recipeItem) {
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        if (recipeItem == null) {
            Log.d("RecipeItemList is null", "");
        } else {
            for (Object item : recipeItem) {
                Map<String, Object> itemMap = (Map<String, Object>) item;
                String ingredientName = (String) itemMap.get("ingredientName");
                Ingredient ingredient = IngredientManager.getIngredient(ingredientName);

                float quantity = ((Number) itemMap.get("quantity")).floatValue();

                String unitLabel = (String) itemMap.get("unit");
                Unit unit = UnitManager.getUnit(unitLabel);
            }
        }
        return recipeIngredientList;
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
        this._quantity = quantity;
    }

    public Unit getUnit() {
        return _unit;
    }

    public void setUnit(Unit unit) {
        this._unit = unit;
    }

    public float calcPrice(){
        float convertedQuantity = _ingredient.get_unit().convertTo(_unit, _quantity);
        convertedQuantity *= _ingredient.get_atomicPrice();
        return convertedQuantity;
    }

}