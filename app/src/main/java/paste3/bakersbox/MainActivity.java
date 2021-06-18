package paste3.bakersbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of the Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://baker-s-box-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("BBStorage");
    }

    public void writeToCloud(View view) {

        DatabaseReference ingredientRef = myRef.child("ingredients");
        DatabaseReference recipeRef = myRef.child("recipe");
        DatabaseReference unitRef = myRef.child("unit");

        new Unit("ml", 1000000).upload(unitRef);
        new Unit("l", 1000).upload(unitRef);
        new Unit("cup", 4000).upload(unitRef);
        new Unit("Tbsp", (float) 66666.67).upload(unitRef);
        new Unit("tsp", 200000).upload(unitRef);
        new Unit("g", 1000).upload(unitRef);
        new Unit("kg", 1).upload(unitRef);
        new Unit("lb", (float) 2.205).upload(unitRef);
        new Unit("oz", (float) 35.27).upload(unitRef);
        new Unit("count", 1).upload(unitRef);

        ingredientRef.setValue("IngredientList");
        recipeRef.setValue("RecipesList");
    }

    public void fetchUnits(View view) {
        Log.d("Fetch", "fetching units");
        myRef.child("unit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot unitSnap : snapshot.getChildren()) {
                    Unit unit = unitSnap.getValue(Unit.class);
                    Log.d("Unit", unit.getUnitLabel());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void basicRead(View view) {

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

}
