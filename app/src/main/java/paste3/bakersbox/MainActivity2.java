package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Create an instance of the Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://baker-s-box-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("BBStorage");
        UnitManager.setDbRefUnit(myRef.child("unit"));
        //IngredientManager.setDbRefIngredient(myRef.child("ingredients"));
    }

    // Sends data to the Firebase Cloud Database
    public void writeToCloud(View view) {

        //IngredientManager.saveIngredients();

        DatabaseReference ingredientRef = myRef.child("ingredients");
        DatabaseReference recipeRef = myRef.child("recipe");
        DatabaseReference unitRef = myRef.child("unit");

        // The 10 Unit Objects needed.
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

        IngredientManager.addIngredient("Plain Flour", "g", "kg", (float) 1.5, (float)1.80 );
        IngredientManager.addIngredient("Milk", "ml", "l", (float) 2.27, (float)1.10 );
        IngredientManager.addIngredient("Butter", "g", "g", (float) 150, (float)1.80 );
        IngredientManager.addIngredient("Baking Powder", "g", "g", (float) 50, (float)1.00 );
        IngredientManager.addIngredient("Eggs", "count", "count", (float) 12, (float)2.05 );

        ingredientRef.setValue(IngredientManager.getIngredientMap());
        recipeRef.setValue("RecipesList");
    }

}
