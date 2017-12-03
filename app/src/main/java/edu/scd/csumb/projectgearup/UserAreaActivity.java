package edu.scd.csumb.projectgearup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload) ;
        bUploadImage = (Button) findViewById(R.id.bUploadImage);

        uploadImageName = (EditText) findViewById(R.id.etUploadName);
        imageToUpload.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.imageToUpload:
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            break;
        case R.id.bUploadImage:
            Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            new UploadImage(image, uploadImageName.getText().toString()).execute();
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


    private class UploadImage extends AsyncTask<Void,Void, Void>{

        Bitmap image;
        String name;
        public UploadImage(Bitmap image, String name){
            this.image = image;
            this.name = name;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);


//            ArrayList<Pair> dataToSend = new ArrayList<Pair>();
//            Pair pair = new Pair();
//            pair.setX("image");
//            pair.setY(encodedImage);
//            dataToSend.add(pair);
//
//            pair.setX("name");
//            pair.setY(name);
//            dataToSend.add(pair);

            String urlParameters  =   "image="+ encodedImage + "&name="+ name;


            byte[] postData = urlParameters.getBytes( Charset.forName("UTF-8") );
            int postDataLength = postData.length;
            String request = "http://obtuse-angular-over.000webhostapp.com/SavePicture.php";
            URL url = null;
            try {
                url = new URL( request );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn= null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
            conn.setUseCaches(false);

            DataOutputStream wr = null;
            try {
                wr =  new DataOutputStream(conn.getOutputStream());
                wr.write( postData );
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }


    }

}
