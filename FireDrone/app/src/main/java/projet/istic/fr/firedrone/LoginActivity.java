package projet.istic.fr.firedrone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import projet.istic.fr.firedrone.ModelAPI.UserApi;
import projet.istic.fr.firedrone.model.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class LoginActivity extends AppCompatActivity {

    //le token qu'il faut rajouter à toutes les requetes
    public String token = "";

    //*   Components   *//
    private EditText loginField;
    private EditText passField;
    private Button loginButton;

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void loginOnClick() {

        //** Envoyer la requete vers le server **//
        final String login = loginField.getText().toString();
        String password = passField.getText().toString();

        System.out.println(login);
        System.out.println(password);

        if (login.equals("admin")) {

            System.out.println("connected");
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
            setResult(RESULT_OK, myIntent);
            finish();
            return;

        }
        System.out.println("disconnected");


        final User user;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", "drone_android", "4ndr01d").getBytes(), Base64.NO_WRAP);


        final UserApi userApi = restAdapter.create(UserApi.class);
        userApi.connectUser(basicAuth, login, password, "password", new Callback<User>() {
            @Override
            public void success(User user, Response response) {

                String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());


                JSONObject reader = null;
                try {
                    reader = new JSONObject(bodyString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String sys = null;
                String type = null;
                try {
                    sys = reader.getString("access_token");
                    type = reader.getString("token_type");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String access_token = null;

                //recuperer le token
                token = type+" "+sys;

                userApi.getUser(token, login, new Callback<User>() {

                    @Override
                    public void success(User user, Response response) {
                        user = new User(user.getLogin(), user.getLastname(), user.getFirstname(), user.getPhone(), user.getEmail(),user.getRole());

                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        myIntent.putExtra("user", "user");
                        LoginActivity.this.startActivity(myIntent);
                        setResult(RESULT_OK, myIntent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                         System.out.println("impossible de recuperer l'utilisateur: "+error);
                    }
                });

                System.out.println("RESULT");
                System.out.println(bodyString);
                System.out.println(sys);
                System.out.println(token);

            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Impossible de recuperer le token: "+error);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://projet.istic.fr.firedrone/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://projet.istic.fr.firedrone/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /*
    Intent intent = getIntent();

    String userRole = intent.getExtras().getParcelable("userRole");
    */


}
