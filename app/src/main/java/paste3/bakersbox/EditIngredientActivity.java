package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class EditIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.layout4);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }

    // Triggered by the submit button in the edit ingredient activity
    public void submitIngredient(View view) {

        // Get all EditText inputs and parse to string or float as needed

        EditText inputIngredientName = findViewById(R.id.ingredientNameInput);
        String ingredientName = inputIngredientName.getText().toString();

        EditText inputQuantity = findViewById(R.id.quantityNumber1);
        float quantity = Float.parseFloat(inputQuantity.getText().toString());

        EditText inputPrice = findViewById(R.id.priceInput);
        float price = Float.parseFloat(inputQuantity.getText().toString());

        //RadioButton - needs to be worked out

        // Send retrieved data to the Ingredient Manager to create the new ingredient and add it to
        // the Ingredient Map
        IngredientManager.addIngredient(ingredientName, "ml", "l", quantity, price);
    }
}