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

public class JavaIntermediate extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DrawerLayout drawerLayout;
    MainActivity mainActivity = new MainActivity();
    CardView i1, i2, i3;
    ImageView ic1, ic2, ic3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_intermediate);

        drawerLayout = findViewById(R.id.drawer_layout_n);

        mainActivity.spin = (Spinner)findViewById(R.id.spinner);
        mainActivity.spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mainActivity.languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mainActivity.spin.setAdapter(aa);

        loadCardItem();
        setVisibilityCardItem();


        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ic1.isShown()) {
                    ic1.setVisibility(View.GONE);
                }
                else{
                    ic1.setVisibility(View.VISIBLE);
                }

            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ic2.isShown()) {
                    ic2.setVisibility(View.GONE);
                }
                else{
                    ic2.setVisibility(View.VISIBLE);
                }

            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ic3.isShown()) {
                    ic3.setVisibility(View.GONE);
                }
                else{
                    ic3.setVisibility(View.VISIBLE);
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
        i1 = (CardView) findViewById(R.id.i1);
        ic1 = (ImageView) findViewById(R.id.ic1);

        i2 = (CardView) findViewById(R.id.i2);
        ic2 = (ImageView) findViewById(R.id.ic2);

        i3 = (CardView) findViewById(R.id.i3);
        ic3 = (ImageView) findViewById(R.id.ic3);

    }

    private void setVisibilityCardItem()
    {
        ic1.setVisibility(View.GONE);
        ic2.setVisibility(View.GONE);
        ic3.setVisibility(View.GONE);

    }
}