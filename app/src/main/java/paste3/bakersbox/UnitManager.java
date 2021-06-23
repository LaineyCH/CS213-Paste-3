package paste3.bakersbox;

import java.util.HashMap;
import java.util.Map;

/*
 * Unit Manager manages the Unit Map
 */
public class UnitManager {
    private static final Map<String, Unit> unitMap = new HashMap<>();
    private static boolean isPopulated = false;

    public static Unit getUnit(String unitLabel) {
        populateUnitMap();
        return unitMap.get(unitLabel);
    }

    public static void addUnit(String unitLabel, Unit unit) {
        unitMap.put(unitLabel, unit);
    }

    public static void populateUnitMap() {
        if (isPopulated) {
            return;
        }
        isPopulated = true;
        // go to firebase database and download units and add all to unitMap.
    }
}
