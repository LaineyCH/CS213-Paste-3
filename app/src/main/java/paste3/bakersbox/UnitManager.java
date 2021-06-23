package paste3.bakersbox;

import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/*
 * Unit Manager manages the Unit Map
 */
public class UnitManager {
    private static final Map<String, Unit> unitMap = new HashMap<>();
    private static WeakReference<DatabaseReference> dbRef = new WeakReference<>(null);


    public static WeakReference<DatabaseReference> getDbRef() {
        return dbRef;
    }

    public static void setDbRef(DatabaseReference dbRef) {
        UnitManager.dbRef = new WeakReference<>(dbRef);
        populateUnitMap();
    }

    public static Unit getUnit(String unitLabel) {
        return unitMap.get(unitLabel);
    }

    public static void addUnit(Unit unit) {
        unitMap.put(unit.getUnitLabel(), unit);
    }


    /*
     * Make sure the Unit Map contains the Unit object. If not, fetch them from the database.
     */
    public static void populateUnitMap() {
        Log.d("Fetch", "fetching units"); // Debugging

        // Fetches the Unit objects form the Firebase Cloud Database.
        DatabaseReference _dbRef = dbRef.get();
        if (dbRef == null) {
            Log.d("dbRef", "Database reference not available.");
            return;
        }
        _dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot unitSnap : snapshot.getChildren()) {
                    Unit unit = unitSnap.getValue(Unit.class);

                    Log.d("Unit", unit.getUnitLabel()); // Debugging

                    addUnit(unit);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
