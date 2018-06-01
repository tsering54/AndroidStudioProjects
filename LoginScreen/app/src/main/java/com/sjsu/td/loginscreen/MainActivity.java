package com.sjsu.td.loginscreen;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Button cancel;
    private CheckBox showPassword;
    private TextView message;
    private int wrongPasswordCount;
    private  boolean locked;
    private long lockedAt;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        showPassword = (CheckBox) findViewById(R.id.showPassword);
        message = (TextView) findViewById(R.id.message);
        login = (Button) findViewById(R.id.loginButton);
        cancel = (Button) findViewById(R.id.cancelButton);
        wrongPasswordCount = 0;
        locked = false;
        lockedAt = 0;
        currentTime = 0;

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("");
                if (locked){
                    currentTime = System.currentTimeMillis( );
                    long diff = currentTime - lockedAt;
                    if (diff > 30000){
                        locked = false;
                        validate(username.getText().toString(), password.getText().toString());
                    }
                    else{
                        message.setText("Account Locked! Please try after 30 seconds.");
                    }
                }
                else {
                    validate(username.getText().toString(), password.getText().toString());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText("");
                password.setText("");
                message.setText("");
            }
        });
    }

    private void validate(String userName, String password){
        if((userName.equals("006101355")) && (password.equals("CMPE#137"))){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if (!(userName.equals("006101355")) && !(password.equals("CMPE#137"))){

            //message invalid username
            message.setText("Invalid Username and Password!");
        }
        else if (!(userName.equals("006101355")) && (password.equals("CMPE#137"))){

            //message invalid username
            message.setText("Invalid Username!");
        }
        else if ((userName.equals("011215477")) && (password.toLowerCase().equals("CMPE#137"))){

            //message use upper case
            message.setText("Try Using upper-case!");
        }
        else if ((userName.equals("006101355")) && !(password.equals("CMPE#137"))){

            wrongPasswordCount ++;
            if (wrongPasswordCount == 3)
            {
                locked = true;
                wrongPasswordCount = 0;
                lockedAt = System.currentTimeMillis( );
                message.setText("Three Invalid Password Attempts! Account Locked for 30 seconds!");
            }
            else {
                //message invalid password
                message.setText("Invalid Password!");
            }
        }

    }
}

