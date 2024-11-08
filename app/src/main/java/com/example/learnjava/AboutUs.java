package com.example.learnjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DrawerLayout drawerLayout;
    MainActivity mainActivity = new MainActivity();
    TextView linkLinkedIn, linkFacebook, linkWhatsApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        drawerLayout = findViewById(R.id.drawer_layout_n);

        mainActivity.spin = (Spinner)findViewById(R.id.spinner);
        mainActivity.spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mainActivity.languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mainActivity.spin.setAdapter(aa);


        setupHyperlink();
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


    private void setupHyperlink() {
        linkLinkedIn = findViewById(R.id.txt_linekedin);
        linkLinkedIn.setMovementMethod(LinkMovementMethod.getInstance());

        linkFacebook = findViewById(R.id.txt_facebook);
        linkFacebook.setMovementMethod(LinkMovementMethod.getInstance());

        linkWhatsApp = findViewById(R.id.txt_whatsapp);
        linkWhatsApp.setMovementMethod(LinkMovementMethod.getInstance());

    }
}