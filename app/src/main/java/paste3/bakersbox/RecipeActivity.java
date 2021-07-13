package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe2);
        ConstraintLayout li=(ConstraintLayout) findViewById(R.id.recipeActivity);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }

    public void createRecipe(View view) {
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }

    public void viewRecipe(View view) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        startActivity(intent);
    }

    public void viewRecipeCatagory(View view) {
        Intent intent = new Intent(this, ViewRecipeByCategoryActivity.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        this.finish();
    }
}