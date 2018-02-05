package com.example.milos.creitive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.milos.creitive.models.Token;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEditTextMail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextMail = (EditText) findViewById(R.id.editTextMail);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonLogin = (Button) findViewById(R.id.buttonLogin);

        SharedPreferenceUtils.saveValue(getApplicationContext(), "testVariable", "no token");
        if (tokenFromMemory()){
            Log.e(TAG, "------load second activity from staring point: " + SharedPreferenceUtils.getStringValue(getApplicationContext(), "testVariable", "no token"));
            loadSecondActivity();
        } else {
            Log.e(TAG, "-----no token we do not load second activity: "  + SharedPreferenceUtils.getStringValue(getApplicationContext(), "testVariable", "no token"));
        }


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(mEditTextMail.getText());
                String password = String.valueOf(mEditTextPassword.getText());

                Log.d(TAG, "email value - " + email + " password value = " + password);

                if (!validateEmailAdress(email)) {
                    Log.e(TAG, "mail adress is not walid");
                    Toast.makeText(getApplicationContext(), "Mail adress is not in valid format.", Toast.LENGTH_LONG).show();
                }
                if (!validatePassword(password)) {
                    Log.e(TAG, "you should enter at least 6 characters");
                    Toast.makeText(getApplicationContext(), "password is too short,it needs to be at least 6 characters.", Toast.LENGTH_LONG).show();
                }

                //if user manage to login
                if (validateEmailAdress(email) && validatePassword(password)){
                    //Log.e(TAG, "username and password are OK so we can now go and login to wabpage");
                    loggingIn(email, password);

                }

            }
        });
    }

    public boolean validateEmailAdress(String email){
        String emailPattern = "^[+A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean validatePassword(String password){
        if (password.length() >= 6) return true;
        return false;
    }

    public void loggingIn(String email, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerConfiguration.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CreitiveAPI creitiveAPI = retrofit.create(CreitiveAPI.class);

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Host", "blogsdemo.creitiveapps.com");
        headerMap.put("Accept", "application/json");
        headerMap.put("Content-type", "application/json");
        headerMap.put("Content-Length", "68");

        HashMap<String, String> bodyMap = new HashMap<String, String>();
        bodyMap.put("email", email);
        bodyMap.put("password", password);

        Call<Token> call = creitiveAPI.login(headerMap, bodyMap);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.e(TAG, "onResponse: ServerResponse: " + response.toString());

                if (response.isSuccessful()){
                    Log.e(TAG, "onResponse: SUCCESSFUL  LOGGING!!!!");
                    Token token = response.body();
                    Log.e(TAG, "onResponse: token                       value: " + token);
                    Log.e(TAG, "onResponse: token.getToken()            value: " + token.getToken());
                    Log.e(TAG, "onResponse: token.getToken().toString() value: " + token.getToken().toString());

                    SharedPreferenceUtils.saveValue(getApplicationContext(), "testVariable", token.getToken());
                    loadSecondActivity();
                } else {
                    if (response.code() == StatusCodes.BAD_REQUEST){
                        Log.e(TAG, "onResponse: BAD_REQUEST ");
                    }else if (response.code() == StatusCodes.UNAUTHORIZED){
                        Log.e(TAG, "onResponse: UNAUTHORIZED ");
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e(TAG, "onFailure: Some problem occurs: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Some problem occurs:", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     *
     *
     */
    public void loadSecondActivity(){
        Log.e(TAG, "loadSecondActivity() " + SharedPreferenceUtils.getStringValue(getApplicationContext(), "testVariable", "default value"));
        Intent intent = new Intent(getApplicationContext(), BlogListActivity.class);
        startActivity(intent);

    }


    /*
     *
     * If returns true: token exists in memory and he is saved
     * If returns false: token do not exists in memory there are no saved token value in memory
     */
    public boolean tokenFromMemory(){
        if (SharedPreferenceUtils.getStringValue(getApplicationContext(), "testVariable", "no token").equals("no token")){
            Log.e(TAG, "tokenFromMemory() message: there are no token in memory: "
                    + SharedPreferenceUtils.getStringValue(getApplicationContext(), "testVariable", "no token"));
            return false;
        }else {
            Log.e(TAG, "tokenFromMemory() message: token saved in memory: "
                    + SharedPreferenceUtils.getStringValue(getApplicationContext(), "testVariable", "no token"));
            return true;
        }
    }


}
