package paste3.bakersbox;

import com.google.firebase.database.DatabaseReference;

public class Unit {

    private String _unitLabel;
    private float _siQuantity;

    public Unit() {
        this._unitLabel = "";
        this._siQuantity = 1;
    }

    public Unit(String unitLabel, float siQuantity) {
        this._unitLabel = unitLabel;
        this._siQuantity = siQuantity;
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

    public float convertTo(Unit unit, float quantity){
        return this.getSiQuantity() / unit.getSiQuantity() * quantity;
    }

    public void upload(DatabaseReference ref) {
        ref.child(_unitLabel).setValue(this);
    }
}
