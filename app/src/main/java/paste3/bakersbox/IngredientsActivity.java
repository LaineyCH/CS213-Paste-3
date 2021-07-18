package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.ingredients);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }
    public void goToPurchase(View view){
        Intent intent = new Intent(this, CreateIngredientActivity.class);
        startActivity(intent);
    }

    public void goToViewIngredient(View view) {
        Intent intent = new Intent(this,ViewIngredientActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by back button - go back to previous activity layout
     */
    public void goBack(View view) {
        this.finish();
    }
}