package paste3.bakersbox;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadPictureActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;

    //Variables from app
    ImageView imageView;
    Button btnPick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        //initalizing views
        imageView = findViewById(R.id.imgView);
        btnPick = findViewById(R.id.buttonLoadPicture);

        btnPick.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/^");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Pick an image"), GALLERY_REQUEST_CODE);
        });
    }
    //This is when it call the picture
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            imageView.setImageURI(imageData);
        }
    }
}

