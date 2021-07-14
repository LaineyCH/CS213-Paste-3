package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewIngredientActivity extends AppCompatActivity {

    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
    Spinner ingredientSpinner;
    Ingredient ingredient;
    TextView name;
    TextView totalQuantity;
    TextView totalPrice;
    TextView unitLabel;
    TextView atomicPrice;
    TextView convertedUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ingredient);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewIngredient);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        name = findViewById(R.id.ingredientNameHere);
        totalQuantity = findViewById(R.id.totalQuantityOutput);
        totalPrice = findViewById(R.id.totalPriceOutput);
        unitLabel = findViewById(R.id.unitLabelOutput);
        atomicPrice = findViewById(R.id.atomicPriceOutput);
        convertedUnit = findViewById(R.id.convertedUnitOutput);

        // ingredient spinner
        ingredientSpinner = findViewById(R.id.ingredientSpinner2);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String IngredientSpinnerSelection = parent.getItemAtPosition(position).toString();
                ingredient = IngredientManager.getIngredient(IngredientSpinnerSelection);
                name.setText(ingredient.getIngredientName());
                totalQuantity.setText(String.valueOf(ingredient.totalQuantity) + " " + ingredient.inputLabel);
                totalPrice.setText(String.valueOf(ingredient.totalPrice * 100 / 100));
                atomicPrice.setText(String.valueOf(ingredient.get_atomicPrice()));
                convertedUnit.setText(ingredient.getUnit().getUnitLabel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Creating adapter for Ingredient spinner
        ArrayAdapter<String> recipeDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, IngredientManager.getIngredientNameList());
        // Drop down layout style
        recipeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to ingredient spinner
        ingredientSpinner.setAdapter(recipeDataAdapter);

    }
    public void goBack(View view) {
        this.finish();
    }

    public void goToEditIngredient(View view) {
        Intent intent = new Intent(this,EditIngredientActivity.class);
        intent.putExtra("ingredientName",ingredient.getIngredientName());
        startActivity(intent);
    }
}