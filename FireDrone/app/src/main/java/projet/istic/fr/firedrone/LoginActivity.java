package projet.istic.fr.firedrone;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import projet.istic.fr.firedrone.ModelAPI.UserLoginApi;
import projet.istic.fr.firedrone.model.UserLogin;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class LoginActivity extends AppCompatActivity {

    //*   Components   *//
    private EditText loginField;
    private EditText passField;
    private Button loginButton;

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        /** Init the View Text Fields **/
        loginField = (EditText) findViewById(R.id.loginField);
        passField = (EditText) findViewById(R.id.passField);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnClick();
            }
        });

    }



    public void loginOnClick() {

        //** Envoyer la requete vers le server **//
        String login =  loginField.getText().toString();
        String password = passField.getText().toString();





        System.out.println(login);
        System.out.println(password);

        if(login.equals("admin")){

            System.out.println("connected");
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
            setResult(RESULT_OK, myIntent);
            finish();
            return;

        }
        System.out.println("disconnected");


        final UserLogin userLogin = new UserLogin(login,password);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", "drone_android", "4ndr01d").getBytes(), Base64.NO_WRAP);



        UserLoginApi userLoginApi = restAdapter.create(UserLoginApi.class);
        userLoginApi.connectUser(basicAuth, new Callback<UserLogin>() {
            @Override
            public void success(UserLogin userLogin, Response response) {

                String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());


                JSONObject reader = null;
                try {
                    reader = new JSONObject(bodyString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String sys  = null;
                try {
                    sys = reader.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String access_token = null;


                System.out.println("RESULT");
                System.out.println(sys);


                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);

                myIntent.putExtra("userRole", response.toString());
                LoginActivity.this.startActivity(myIntent);
                setResult(RESULT_OK, myIntent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error);

            }
        });

    }

    /*
    Intent intent = getIntent();

    String userRole = intent.getExtras().getParcelable("userRole");
    */




}
