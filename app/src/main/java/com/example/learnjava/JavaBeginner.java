package com.example.learnjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class JavaBeginner extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    MainActivity mainActivity = new MainActivity();
    CardView b1, b2, b3, b4, b5;
    ImageView bc1, bc2, bc3, bc4, bc5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_beginner);

        drawerLayout = findViewById(R.id.drawer_layout_n);

        mainActivity.spin = (Spinner)findViewById(R.id.spinner);
        mainActivity.spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mainActivity.languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mainActivity.spin.setAdapter(aa);

        loadCardItem();
        setVisibilityCardItem();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bc1.isShown()) {
                    bc1.setVisibility(View.GONE);
                }
                else{
                    bc1.setVisibility(View.VISIBLE);
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bc2.isShown()) {
                    bc2.setVisibility(View.GONE);
                }
                else{
                    bc2.setVisibility(View.VISIBLE);
                }

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bc3.isShown()) {
                    bc3.setVisibility(View.GONE);
                }
                else{
                    bc3.setVisibility(View.VISIBLE);
                }

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

    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
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

    private void loadCardItem()
    {
        b1 = (CardView) findViewById(R.id.b1);
        bc1 = (ImageView) findViewById(R.id.bc1);

        b2 = (CardView) findViewById(R.id.b2);
        bc2 = (ImageView) findViewById(R.id.bc2);

        b3 = (CardView) findViewById(R.id.b3);
        bc3 = (ImageView) findViewById(R.id.bc3);

    }

    private void setVisibilityCardItem()
    {
        bc1.setVisibility(View.GONE);
        bc2.setVisibility(View.GONE);
        bc3.setVisibility(View.GONE);

    }


}