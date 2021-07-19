package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

/**
 * This Activity Class creates the "Ingredients Menu", There are 3 buttons, allowing the user to
 * enter in a new purchased ingredient, view ingredients, or go back to the previous menu.
 */
public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.ingredients);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }

    /**
     * Triggered by the "enter purchased ingredients" button in the activity_ingredients layout.
     * Takes the user to the activity_create_ingredient layout.
     * @param view the current view
     */
    public void goToPurchase(View view){
        Intent intent = new Intent(this, CreateIngredientActivity.class);
        startActivity(intent);
    }
    /**
     * Triggered by the "find ingredient" button in the activity_ingredients layout.
     * Takes the user to the activity_view_ingredient layout.
     * @param view the current view
     */
    public void goToViewIngredient(View view) {
        Intent intent = new Intent(this,ViewIngredientActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by back button - go back to previous activity layout, the main menu
     */
    public void goBack(View view) {
        this.finish();
    }
}