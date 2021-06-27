package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe2);
        ConstraintLayout li=(ConstraintLayout) findViewById(R.id.layout2);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }

    public void createRecipe(View view) {
        Intent intent = new Intent(this,EditRecipeActivity.class);
        startActivity(intent);
    }
}