package paste3.bakersbox;

/*
 * The Unit Class created new unit objects. Each has a unit label and an siQuantity (the number of
 * units, represented by the unit label, in the internationally recognised unit for that type of
 * measure - eg: if the unit label is "g", the SI unit would be "kg", and there are 1000g in a
 * kilogram, so the siQuantity would be 1000).
 */
public class Unit {

    private String _unitLabel;
    private float _siQuantity;

    /**
     * Main constructor for Unit
     * @param unitLabel the unit label
     * @param siQuantity the quantity as described above
     */
    public Unit(String unitLabel, float siQuantity) {
        this._unitLabel = unitLabel;
        this._siQuantity = siQuantity;
    }

    /**
     * Secondary constructor that creates a "blank" Unit object
     */
    public Unit() {
        this._unitLabel = "";
        this._siQuantity = 1;
    }

    /**
     * Conversion needed to go from the units as purchased or specified by a recipe, to the base
     * unit.
     */
    public float convertTo(Unit unit, float quantity){
        return this.getSiQuantity() / unit.getSiQuantity() * quantity;
    }

    /**
     * GETTERS AND SETTERS
     */

    public String getUnitLabel() {
        return _unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this._unitLabel = unitLabel;
    }

    public float getSiQuantity() {
        return _siQuantity;
    }

    public void setSiQuantity(float siQuantity) {
        this._siQuantity = siQuantity;
    }
}
