package paste3.bakersbox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class viewRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.viewRecipe);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
        Recipe recipe = new Recipe();
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}