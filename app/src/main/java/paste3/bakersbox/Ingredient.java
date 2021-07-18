package paste3.bakersbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Ingredient Class. Created Ingredient Objects that have a name, an atomic price (the cost of a
 * single unit of its measurement - 1 g, 1 ml, or 1 count) and a Unit Object (this 'base' Unit
 *  will have a unit label of "ml" for wet ingredients, "g" for dry ingredients, or "count" for
 *  counted ingredients).
 */
public class Ingredient {

    private String _ingredientName;
    private float _atomicPrice;
    private Unit _unit;

    // Main Constructor for creation of new ingredient objects
    public Ingredient(String ingredientName, String thisUnitLabel, String userUnitLabel,
                      float quantity, float price) {
        this._ingredientName = ingredientName;
        // the base unit for this ingredient
        this._unit = UnitManager.getUnit(thisUnitLabel);
        // the unit the ingredient was purchased as, or that is required for a specific recipe.
        Unit userUnit = UnitManager.getUnit(userUnitLabel);
        // conversion needed to calculate the ingredient's atomic price, based on it's base unit.
        float convertedQuantity = this._unit.convertTo(userUnit, quantity);
        // the cost of a single unit of the ingredient (1 g, 1 ml or 1 count)
        _atomicPrice = price / convertedQuantity;
    }

    /**
     *  Blank Constructor, creates ingredient with 'empty' attributes
     */
    public Ingredient() {
        this._ingredientName = "";
        this._atomicPrice = 0;
        this._unit = null;
    }

    /**
     * "From Database" Constructor, creates ingredient objects from the data retrieved
     * @param ingredientMap
     */
    public Ingredient(Map<String, Object> ingredientMap) {
        this._ingredientName = (String) ingredientMap.get("ingredientName");
        this._atomicPrice = ((Number) ingredientMap.get("atomicPrice")).floatValue();
        // The Unit Object is added back using its unitLabel
        this._unit = UnitManager.getUnit((String) ingredientMap.get("unit"));
    }

    /**
     * Turns an ingredient object into a simplified map that doesn't contain any unit objects,
     * to be saved to the Database.
     * @return
     */
    public Map<String, Object> toMap() {
        Map<String, Object> ingredientMap = new HashMap<>();
        ingredientMap.put("ingredientName", _ingredientName);
        ingredientMap.put("atomicPrice", _atomicPrice);
        // The Unit Object is 'stored' by it's unit label
        ingredientMap.put("unit", _unit.getUnitLabel());
        // Returns the simplified ingredient map
        return ingredientMap;
    }

    public String getIngredientName() {
        return _ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this._ingredientName = ingredientName;
    }

    public Float getAtomicPrice() {
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
