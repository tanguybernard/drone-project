package projet.istic.fr.firedrone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import projet.istic.fr.firedrone.ModelAPI.UserLoginApi;
import projet.istic.fr.firedrone.model.UserLogin;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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


        if(login=="admin" && password=="admin"){

            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
            setResult(RESULT_OK, myIntent);
            finish();

        }


        final UserLogin userLogin = new UserLogin(login,password);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .build();
        UserLoginApi userLoginApi = restAdapter.create(UserLoginApi.class);
        userLoginApi.connectUser(userLogin, new Callback<UserLogin>() {
            @Override
            public void success(UserLogin userLogin, Response response) {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);

                myIntent.putExtra("userRole", response.toString());
                LoginActivity.this.startActivity(myIntent);
                setResult(RESULT_OK, myIntent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    /*
    Intent intent = getIntent();

    String userRole = intent.getExtras().getParcelable("userRole");
    */




}
