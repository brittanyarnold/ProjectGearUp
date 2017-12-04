package edu.scd.csumb.projectgearup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.params.HttpParams;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UserAreaActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageToUpload, imageUpload;
    Button bUploadImage, btnChoose, btnUpload;
    EditText uploadImageName;
    final int CODE_GALLERY_REQUEST = 999;
    String urlUpload = "https://obtuse-angular-over.000webhostapp.com/upload.php";
    Bitmap bitmap;
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
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.bUploadImage);
        imageUpload = (ImageView) findViewById(R.id.imageUpload);

        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ActivityCompat.requestPermissions(
                        UserAreaActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST

                );
            }
        });

       btnUpload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                        Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();

                   }

               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(),"Error: " + volleyError.toString(), Toast.LENGTH_LONG).show();
                   }
               }){
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String imageData = imageToString(bitmap);
                            params.put("image", imageData);

                       return params;
                   }
               };
               RequestQueue requestQueue = Volley.newRequestQueue(UserAreaActivity.this);
               requestQueue.add(stringRequest);
           }


       });
//        imageToUpload = (ImageView) findViewById(R.id.imageToUpload) ;
//        bUploadImage = (Button) findViewById(R.id.bUploadImage);
//
//        uploadImageName = (EditText) findViewById(R.id.etUploadName);
//        imageToUpload.setOnClickListener(this);
//        bUploadImage.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){

        if(requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);

            } else {
                Toast.makeText(getApplicationContext(), "YOU DONT HAAVE ACCESS TO GALLERY", Toast.LENGTH_LONG).show();
            }
            return;

        }

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
           Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageUpload.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }
}
