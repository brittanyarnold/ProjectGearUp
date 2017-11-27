package edu.scd.csumb.projectgearup;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView downloadedImage;
    Button loginMButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginMButton = (Button) findViewById(R.id.loginMButton);
        downloadedImage = (ImageView) findViewById(R.id.downloadedImage);

        loginMButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

                Intent k = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(k);

    }
}
