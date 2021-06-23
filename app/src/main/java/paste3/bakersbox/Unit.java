package paste3.bakersbox;

import com.google.firebase.database.DatabaseReference;

/*
 * Unit Class
 */
public class Unit {

    private String _unitLabel;
    private float _siQuantity;

    // Main constructor for Unit
    public Unit(String unitLabel, float siQuantity) {
        this._unitLabel = unitLabel;
        this._siQuantity = siQuantity;
    }

    // Secondary constructor that creates a "blank" Unit object
    public Unit() {
        this._unitLabel = "";
        this._siQuantity = 1;
    }

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

    // Conversion needed to go from the units as purchased or specified by a recipe, to the base unit.
    public float convertTo(Unit unit, float quantity){
        return this.getSiQuantity() / unit.getSiQuantity() * quantity;
    }

    public void upload(DatabaseReference ref) {
        ref.child(_unitLabel).setValue(this);
    }
}
