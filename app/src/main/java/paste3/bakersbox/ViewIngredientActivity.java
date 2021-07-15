package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewIngredientActivity extends AppCompatActivity {

    Spinner ingredientSpinner;
    Ingredient ingredient;
    TextView name;
    TextView unitLabel;
    TextView atomicPrice;
    TextView pricePer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ingredient);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewIngredient);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));

        name = findViewById(R.id.ingredientNameHere);
        atomicPrice = findViewById(R.id.atomicPriceOutput);
        unitLabel = findViewById(R.id.unitLabelOutput2);
        pricePer = findViewById(R.id.pricePer);

        // ingredient spinner
        ingredientSpinner = findViewById(R.id.ingredientSpinner2);
        // Ingredient Spinner click listener
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String IngredientSpinnerSelection = parent.getItemAtPosition(position).toString();
                ingredient = IngredientManager.getIngredient(IngredientSpinnerSelection);
                name.setText(ingredient.getIngredientName());
                String atomicP = "£ " + String.valueOf(ingredient.get_atomicPrice());
                atomicPrice.setText(atomicP);
                String unitL = ingredient.getUnit().getUnitLabel();
                unitLabel.setText(unitL);
                String countPrice = "£ " + Math.round(ingredient.get_atomicPrice() * 100.0) / 100.0 + " each";
                String mlPrice = "£ " + Math.round((ingredient.get_atomicPrice() * 100) * 100.0) / 100.0 + " per 100ml";
                String gPrice = "£ " + Math.round((ingredient.get_atomicPrice() * 100) * 100.0) / 100.0 + " per 100g";
                if (unitL.equals("count")) {
                    pricePer.setText(countPrice);
                } else if (unitL.equals("ml")) {
                    pricePer.setText(mlPrice);
                } else if (unitL.equals("g")) {
                    pricePer.setText(gPrice);
                }
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