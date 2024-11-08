package com.example.learnjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class JavaAdvanced extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    MainActivity mainActivity = new MainActivity();
    CardView a1, a2, a3;
    ImageView ac1, ac2, ac3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_advanced);

        drawerLayout = findViewById(R.id.drawer_layout_n);

        mainActivity.spin = (Spinner)findViewById(R.id.spinner);
        mainActivity.spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mainActivity.languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mainActivity.spin.setAdapter(aa);

        loadCardItem();
        setVisibilityCardItem();


        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ac1.isShown()) {
                    ac1.setVisibility(View.GONE);
                }
                else{
                    ac1.setVisibility(View.VISIBLE);
                }

            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ac2.isShown()) {
                    ac2.setVisibility(View.GONE);
                }
                else{
                    ac2.setVisibility(View.VISIBLE);
                }

            }
        });

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ac3.isShown()) {
                    ac3.setVisibility(View.GONE);
                }
                else{
                    ac3.setVisibility(View.VISIBLE);
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
        a1 = (CardView) findViewById(R.id.a1);
        ac1 = (ImageView) findViewById(R.id.ac1);

        a2 = (CardView) findViewById(R.id.a2);
        ac2 = (ImageView) findViewById(R.id.ac2);

        a3 = (CardView) findViewById(R.id.a3);
        ac3 = (ImageView) findViewById(R.id.ac3);

    }

    private void setVisibilityCardItem()
    {
        ac1.setVisibility(View.GONE);
        ac2.setVisibility(View.GONE);
        ac3.setVisibility(View.GONE);

    }
}