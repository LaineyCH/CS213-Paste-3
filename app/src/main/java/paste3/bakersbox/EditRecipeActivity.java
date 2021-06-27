package paste3.bakersbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class EditRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe2);
        LinearLayout li=(LinearLayout)findViewById(R.id.layout5);
        li.setBackgroundColor(Color.parseColor("#7BEEE4"));
    }
}