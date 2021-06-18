package paste3.bakersbox;

public class Ingredient {

    private String _ingredientName;
    private Float _atomicPrice;
    private Unit _unit;

    public Ingredient(String ingredientName, String thisUnitLabel, String userUnitLabel, Float quantity, Float price) {
        this._ingredientName = ingredientName;
        this._unit = UnitManager.getUnit(thisUnitLabel);
        Unit userUnit = UnitManager.getUnit(userUnitLabel);
        float convertedQuantity = this._unit.convertTo(userUnit, quantity);
        _atomicPrice = price / convertedQuantity;
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
        this._atomicPrice = price / quantity;
    }

    public Unit get_unit() {
        return _unit;
    }

    public void set_unit(Unit _unit) {
        this._unit = _unit;
    }
}
