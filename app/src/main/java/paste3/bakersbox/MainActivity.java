package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.layout1);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }

    public void goToRecipes(View view){
        Intent intent = new Intent(this,RecipeActivity.class);
        startActivity(intent);
    }

    public void goToIngredients(View view){
        Intent intent = new Intent(this,IngredientsActivity.class);
        startActivity(intent);
    }
}