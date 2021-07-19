package paste3.bakersbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Ingredient Class. Creates Ingredient Objects that have a name, an atomic price (the cost of a
 * single unit of its measurement - 1 g, 1 ml, or 1 count) and a Unit Object (this 'base' Unit
 *  will have a unit label of "ml" for wet ingredients, "g" for dry ingredients, or "count" for
 *  counted ingredients).
 */
public class Ingredient {

    private final String _ingredientName;
    private float _atomicPrice;
    private Unit _unit;

    /**
     * Main Constructor for creation of new ingredient objects
     * @param ingredientName the ingredient's name
     * @param thisUnitLabel the ingredient's base unit label
     * @param userUnitLabel the unit that the user purchased the ingredient as
     * @param quantity the quantity of the ingredient purchased
     * @param price how much was paid for the ingredient
     */
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
     * @param ingredientMap the simplified unit map received from the database
     */
    public Ingredient(Map<String, Object> ingredientMap) {
        this._ingredientName = (String) ingredientMap.get("ingredientName");
        this._atomicPrice = ((Number) Objects.requireNonNull(ingredientMap.get("atomicPrice")))
                .floatValue();
        // The Unit Object is added back using its unitLabel
        this._unit = UnitManager.getUnit((String) ingredientMap.get("unit"));
    }

    /**
     * Turns an ingredient object into a simplified map that doesn't contain any unit objects,
     * to be saved to the Database.
     * @return teh ingredientMap
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

    /**
     * Getter for the ingredient's name
     * @return _ingredientName
     */
    public String getIngredientName() {
        return _ingredientName;
    }

    /**
     * Getter for the ingredient's atomic price
     * @return _atomicPrice
     */
    public Float getAtomicPrice() {
        return _atomicPrice;
    }

    /**
     * Setter for the ingredient's atomic price. Takes the parameters and calculates the correct
     * atomic price to be set
     * @param price the price payed
     * @param convertedQuantity the converted quantity (calculated by the Unit.convertTo() function)
     */
    public void setAtomicPrice(Float price, Float convertedQuantity) {
        this._atomicPrice = price / convertedQuantity;
    }

    /**
     * Getter for the ingredient's associated Unit object
     * @return the unit object
     */
    public Unit getUnit() {
        return _unit;
    }

    // Sets the Unit for this Ingredient, and returns the conversion needed to calculate the
    // atomic cost.

    /**
     * Setter for the ingredient's unit object
     * @param thisUnitLabel the unit label for the unit object
     * @param userUnitLabel the unit that the user entered for their purchase
     * @param quantity how much of the ingredient that was purchased
     * @return the quantity converted to the unit's base unit label
     */
    public float setUnit(String thisUnitLabel, String userUnitLabel, Float quantity) {
        this._unit = UnitManager.getUnit(thisUnitLabel);
        Unit userUnit = UnitManager.getUnit(userUnitLabel);
        return this._unit.convertTo(userUnit, quantity);
    }
}
