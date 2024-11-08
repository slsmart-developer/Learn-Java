package com.example.learnjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Manager_Cache managerCache;
    private Button btnLogin;
    private EditText edmail,edpassword;
    private TextView tv;


    private FirebaseAuth mAuth;
    String  user_id,email_global,password_global;
    @Override
    public void onStart() {
        super.onStart();

        requestPermission();

        showUserEmail();
        showUserPassword();

//        if(email_global != null && password_global != null)
//        {
//            goToHome();
//        }


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // No user is signed in
        } else {
            user_id = currentUser.getUid();
            // User logged in
            saveUserID();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        managerCache = Manager_Cache.getPreferences(this);

        /**
         * for check db-----------------------------------------------------------------------------
         */

        DebugDB.getAddressLog();

        /**
         * db check over----------------------------------------------------------------------------
         */

        initUI();

        initFunction();
    }

    /*
     * init ui
     * */
    private void initUI() {
        btnLogin=findViewById(R.id.btn_login);
        edmail=findViewById(R.id.enter_email);

        edpassword=findViewById(R.id.enter_password);

        tv=findViewById(R.id.txt_register);


        /*
         * Firebase init
         * */
        mAuth = FirebaseAuth.getInstance();
    }

    private void initFunction() {

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckNetworkStatus.isNetworkStatusAvailable(getBaseContext())) {

//                    Intent intent = new Intent(Login.this, Home.class);
//                    startActivity(intent);

                    String _email=edmail.getText().toString().trim();
                    String _password=edpassword.getText().toString().trim();

                    if(_email.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "E-mail fields is empty", Toast.LENGTH_SHORT).show();
                    }else if(_password.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Password fields is empty", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        mAuth.signInWithEmailAndPassword(_email,_password).addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Authentication Succeed", Toast.LENGTH_SHORT).show();
                                saveUserEmail();
                                saveUserPassword();
                                goToHome();
                            }
                            else
                            {
                                Toast.makeText(Login.this, "login failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {

                    Intent intent = new Intent(Login.this, AlertNoInternet.class);
                    startActivity(intent);
                }


            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Close all activites
        System.exit(0);  // Releasing resources
    }

    /*
     * navigation to home
     * */
    private void goToHome() {
        Intent intent=new Intent(Login.this,Home.class);
        startActivity(intent);
        finish();
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(Login.this, new String[] {

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_NETWORK_STATE,

        } , 1000);
    }

    /**
     * Cache Login data
     */
    private void saveUserID() {
        managerCache.setUserID(user_id);
    }

    private void saveUserEmail() {
        email_global = edmail.getText().toString();
        managerCache.setUserEmail(email_global);

    }

    private void showUserEmail() {
        email_global = managerCache.getUserEmail();
        edmail.setText(email_global);
    }

    private void saveUserPassword() {
        password_global = edpassword.getText().toString();
        managerCache.setUserPassword(password_global);

    }

    private void showUserPassword() {
        password_global = managerCache.getUserPassword();
        edpassword.setText(password_global);
    }
}