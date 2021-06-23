package paste3.bakersbox;

import java.util.HashMap;
import java.util.Map;

/*
 * Ingredient class
 */
public class Ingredient {

    private String _ingredientName;
    private float _atomicPrice;
    private Unit _unit; //ml or g or count

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

    // "From Database" Constructor
    public Ingredient(Map<String, Object> ingredientMap) {
        this._ingredientName = (String) ingredientMap.get("ingredientName");
        this._atomicPrice = ((Double) ingredientMap.get("atomicPrice")).floatValue();
        this._unit = UnitManager.getUnit((String) ingredientMap.get("unit"));
    }

    public String get_ingredientName() {
        return _ingredientName;
    }

    public void set_ingredientName(String ingredientName) {
        this._ingredientName = ingredientName;
    }

    public Float get_atomicPrice() {
        return _atomicPrice;
    }

    public void set_atomicPrice(Float price, Float quantity) {
        this._atomicPrice = price / quantity; // do full calculation for atomic price.
    }

    public Unit get_unit() {
        return _unit;
    }

    public float set_unit(String thisUnitLabel, String userUnitLabel, Float quantity) {
        this._unit = UnitManager.getUnit(thisUnitLabel);
        Unit userUnit = UnitManager.getUnit(userUnitLabel);
        return this._unit.convertTo(userUnit, quantity);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> ingredientMap = new HashMap<>();
        ingredientMap.put("ingredientName", _ingredientName);
        ingredientMap.put("atomicPrice", _atomicPrice);
        ingredientMap.put("unit", _unit.getUnitLabel());
        return ingredientMap;
    }
}
