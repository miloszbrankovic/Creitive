package com.example.milos.creitive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEditTextMail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    //candidate@creitive.com

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextMail = (EditText) findViewById(R.id.editTextMail);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonLogin = (Button) findViewById(R.id.buttonLogin);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(mEditTextMail.getText());
                String password = String.valueOf(mEditTextPassword.getText());

                Log.d(TAG, "email value - " + email + " password vale = " + password);

                if (!validateEmailAdress(email)) {
                    Log.e("MILOS", "mail adress is not walid");
                    Toast.makeText(getApplicationContext(), "Mail adress is not in valid format.", Toast.LENGTH_LONG).show();
                }
                if (!validatePassword(password)) {
                    Log.e("MILOS", "you should enter at least 6 characters");
                    Toast.makeText(getApplicationContext(), "password is too short,it needs to be at least 6 characters.", Toast.LENGTH_LONG).show();
                }

                //if user manage to login
                if (validateEmailAdress(email) && validatePassword(password)){
                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    startActivity(intent);
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


}
