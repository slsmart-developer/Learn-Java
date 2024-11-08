package com.example.learnjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DrawerLayout drawerLayout;
    MainActivity mainActivity = new MainActivity();

    Intent intentJavaBeginner, intentJavaIntermediate, intentJavaAdvanced, intentJavaMore;

    LinearLayout layoutBeginner, layoutIntermediate, layoutAdvanced, layoutMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        drawerLayout = findViewById(R.id.drawer_layout_n);

        mainActivity.spin = (Spinner)findViewById(R.id.spinner);
        mainActivity.spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mainActivity.languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mainActivity.spin.setAdapter(aa);

        intentJavaBeginner = new Intent(this, JavaBeginner.class);
        intentJavaIntermediate = new Intent(this, JavaIntermediate.class);
        intentJavaAdvanced = new Intent(this, JavaAdvanced.class);
        intentJavaMore = new Intent(this, JavaMore.class);

        layoutBeginner = (LinearLayout) findViewById(R.id.btnBeginner);
        layoutIntermediate = (LinearLayout) findViewById(R.id.btnIntermediate);
        layoutAdvanced = (LinearLayout) findViewById(R.id.btnAdvanced);
        layoutMore = (LinearLayout) findViewById(R.id.btnMore);


        layoutBeginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentJavaBeginner);
            }
        });

        layoutIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentJavaIntermediate);
            }
        });

        layoutAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentJavaAdvanced);
            }
        });

        layoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentJavaMore);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),mainActivity.languages[position] + " is under development", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void ClickMenu(View view){

        MainActivity.openDrawer(drawerLayout);
    }
    public void ClickProfile(View view){

        MainActivity.redirectActivity(this,Profile.class);
    }

    public void ClickDashboard(View view){
        MainActivity.redirectActivity(this,Home.class);
    }

    public void ClickSettings(View view){
        mainActivity.underDevelopment(this);
    }
    public void ClickFeedBack(View view){
        mainActivity.underDevelopment(this);
    }
    public void ClickRate(View view){
        mainActivity.underDevelopment(this);
    }

    public void ClickAboutUs(View view){
        MainActivity.redirectActivity(this,AboutUs.class);
    }

    public void ClickLogout(View view){
        MainActivity.logout(this);
    }

}