package paste3.bakersbox;

import java.util.HashMap;
import java.util.Map;

/*
 * Ingredient class
 */
public class Ingredient {

    String _ingredientName;
    float _atomicPrice;
    Unit _unit; // "ml" for wet, "g" for dry, or "count"

    // Main Constructor
    public Ingredient(String ingredientName, String thisUnitLabel, String userUnitLabel, float quantity, float price) {
        this._ingredientName = ingredientName;
        // the base unit for this ingredient
        this._unit = UnitManager.getUnit(thisUnitLabel);
        // the unit the ingredient was purchased as, or that is required for a specific recipe.
        Unit userUnit = UnitManager.getUnit(userUnitLabel);
        // the conversion needed to calculate the atomic price for the ingredient, based on it's base unit.
        float convertedQuantity = this._unit.convertTo(userUnit, quantity);
        _atomicPrice = price / convertedQuantity;
    }

    // Blank Constructor
    public Ingredient() {
        this._ingredientName = "";
        this._atomicPrice = 0;
        this._unit = null;
    }

    // "From Database" Constructor
    public Ingredient(Map<String, Object> ingredientMap) {
        this._ingredientName = (String) ingredientMap.get("ingredientName");
        this._atomicPrice = ((Number) ingredientMap.get("atomicPrice")).floatValue();
        // The Unit Object is added back using its unitLabel
        this._unit = UnitManager.getUnit((String) ingredientMap.get("unit"));
    }

    // Turns an ingredient object into a map that doesn't contain unit objects, for saving to the
    // Database.
    public Map<String, Object> toMap() {
        Map<String, Object> ingredientMap = new HashMap<>();
        ingredientMap.put("ingredientName", _ingredientName);
        ingredientMap.put("atomicPrice", _atomicPrice);
        //The Unit Object is recorded in the map's key : value structure, with its value
        // being the string "unitLabel"
        ingredientMap.put("unit", _unit.getUnitLabel());
        return ingredientMap;
    }

    public String getIngredientName() {
        return _ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this._ingredientName = ingredientName;
    }

    public Float get_atomicPrice() {
        return _atomicPrice;
    }

    public void setAtomicPrice(Float price, Float quantity) {
        this._atomicPrice = price / quantity; // do full calculation for atomic price.
    }

    public Unit getUnit() {
        return _unit;
    }

    // Sets the Unit for this Ingredient, and returns the conversion needed to calculate the
    // atomic cost.
    public float setUnit(String thisUnitLabel, String userUnitLabel, Float quantity) {
        this._unit = UnitManager.getUnit(thisUnitLabel);
        Unit userUnit = UnitManager.getUnit(userUnitLabel);
        return this._unit.convertTo(userUnit, quantity);
    }

}
