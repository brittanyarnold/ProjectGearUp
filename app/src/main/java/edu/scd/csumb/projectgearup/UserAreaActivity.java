package edu.scd.csumb.projectgearup;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageToUpload;
    Button bUploadImage;
    EditText uploadImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);


        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView welcomeMess = (TextView) findViewById(R.id.tvWelcome);

        Intent intent = getIntent();
        String name =  intent.getStringExtra("name");
        String username = intent.getStringExtra("username");

        String message = name + " welcome to your user area";

        welcomeMess.setText(message);
        etUsername.setText(username);

        //imageToUpload.setOnClickListener(this);
        //bUploadImage.setOnClickListener(this); not work
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.imageToUpload:
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            break;
        case R.id.bUploadImage:

            break;

    }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);

        }
    }
}
