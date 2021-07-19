package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

/**
 * This Activity Class forms the recipe menu of the app. Presents the user with 3 button choices.
 */
public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ConstraintLayout li=(ConstraintLayout) findViewById(R.id.recipeActivity);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }

    /**
     * Triggered by the "add new recipe" button in the activity_recipe layout, takes the user to
     * the activity_create_recipe layout.
     * @param view the current view
     */
    public void createRecipe(View view) {
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by the "find recipe by name" button in the activity_recipe layout, takes the user
     * to the activity_view_recipe layout.
     * @param view the current view
     */
    public void viewRecipe(View view) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by the "find recipe by category" button in the activity_recipe layout, takes the
     * user to the activity_view_recipe_by_category layout.
     * @param view the current view
     */
    public void viewRecipeCategory(View view) {
        Intent intent = new Intent(this, ViewRecipeByCategoryActivity.class);
        startActivity(intent);
    }

    /**
     * Triggered by back button - go back to previous activity layout, the main menu
     */
    public void goBack(View view) {
        this.finish();
    }
}