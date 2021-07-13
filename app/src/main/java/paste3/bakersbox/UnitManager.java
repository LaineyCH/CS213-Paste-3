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

    // Sets the Database reference and populates the Unit Map from the Database
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
        //Log.d("Adding Unit", unit.getUnitLabel()); // Debugging
        unitMap.put(unit.getUnitLabel(), unit);
    }


    /*
     * Make sure the Unit Map contains Unit objects. If not, fetch them from the database and add to
     * the Unit Map.
     */
    public static void populateUnitMap(OnInitialised callback) {
        Log.d("Fetch", "fetching units"); // Debugging

        // Fetches the Unit objects form the Firebase Cloud Database.
        if (dbRefUnit == null) {
            Log.d("dbRefUnit", "Database reference not available.");
            return;
        }
        dbRefUnit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Loops through the data from the Database
                for (DataSnapshot unitSnap : snapshot.getChildren()) {
                    // Create a Unit from the data
                    Unit unit = unitSnap.getValue(Unit.class);

//                    Log.d("Unit", unit.getUnitLabel()); // Debugging

                    // Add the Unit to the Unit Map
                    addUnit(unit);

                    // Debugging
                    for (Map.Entry<String, Unit> unitEntry : unitMap.entrySet())  {
                        //Log.d("Unit Map after Add", unitEntry.getKey()); // Debugging
                    }
                }
                callback.onInitialised();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Sends the specific unit map to the Firebase Cloud Database (only used to initialise the
    // Units into the Database)
    public static void saveUnits() {
        ConcurrentMap<String, Unit> initialUnitMap = new ConcurrentHashMap<>();

        if (dbRefUnit == null) {
            Log.d("dbRefIngredient", "Database reference not available.");
            return;
        }
        // The 10 Unit Objects needed.
        Unit ml = new Unit("ml", 1000000);
        initialUnitMap.put(ml.getUnitLabel(), ml);
        Unit l = new Unit("l", 1000);
        initialUnitMap.put(l.getUnitLabel(), l);
        Unit cup = new Unit("cup", 4000);
        initialUnitMap.put(cup.getUnitLabel(), cup);
        Unit tbsp = new Unit("Tbsp", (float) 66666.67);
        initialUnitMap.put(tbsp.getUnitLabel(), tbsp);
        Unit tsp = new Unit("tsp", 200000);
        initialUnitMap.put(tsp.getUnitLabel(), tsp);
        Unit g = new Unit("g", 1000);
        initialUnitMap.put(g.getUnitLabel(), g);
        Unit kg = new Unit("kg", 1);
        initialUnitMap.put(kg.getUnitLabel(), kg);
        Unit lb = new Unit("lb", (float) 2.205);
        initialUnitMap.put(lb.getUnitLabel(), lb);
        Unit oz = new Unit("oz", (float) 35.27);
        initialUnitMap.put(oz.getUnitLabel(), oz);
        Unit count = new Unit("count", 1);
        initialUnitMap.put(count.getUnitLabel(), count);

        dbRefUnit.setValue(initialUnitMap);
    }
}
