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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Unit Manager manages the Unit Map
 */
public class UnitManager {
    private static final ConcurrentMap<String, Unit> unitMap = new ConcurrentHashMap<>();
    public static DatabaseReference dbRefUnit = null;

    public static Map<String, Unit> getUnitMap() {
        return unitMap;
    }

    public static void setDbRefUnit(DatabaseReference dbRef, OnInitialised onInitialised) {
        UnitManager.dbRefUnit = dbRef;
        populateUnitMap(onInitialised);
    }

    public static Unit getUnit(String unitLabel) {
        Unit unit =  unitMap.get(unitLabel);
        if (unit == null) {
            throw new RuntimeException(String.format("Missing Unit: '%s'", unitLabel));
        }
        return unit;
    }

    public static void addUnit(Unit unit) {
        Log.d("Adding Unit", unit.getUnitLabel()); // Debugging
        unitMap.put(unit.getUnitLabel(), unit);
    }


    /*
     * Make sure the Unit Map contains the Unit object. If not, fetch them from the database.
     */
    public static void populateUnitMap(OnInitialised onInitialised) {
        Log.d("Fetch", "fetching units"); // Debugging

        // Fetches the Unit objects form the Firebase Cloud Database.
        if (dbRefUnit == null) {
            Log.d("dbRefUnit", "Database reference not available.");
            return;
        }
        dbRefUnit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot unitSnap : snapshot.getChildren()) {
                    Unit unit = unitSnap.getValue(Unit.class);

                    Log.d("Unit", unit.getUnitLabel()); // Debugging

                    addUnit(unit);

                    for (Map.Entry<String, Unit> unitEntry : unitMap.entrySet())  {
                        Log.d("Unit Map after Add", unitEntry.getKey()); // Debugging
                    }
                }
                onInitialised.onInitialised();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
