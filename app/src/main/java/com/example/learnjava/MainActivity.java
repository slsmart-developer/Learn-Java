package com.example.learnjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static Manager_Cache managerCache;
    DrawerLayout drawerLayout;
    public Spinner spin;

    public String[] languages = { "Java", "Language 2", "Language 3", "Language 4", "Language 5"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        managerCache = Manager_Cache.getPreferences(this);
        drawerLayout = findViewById(R.id.drawer_layout_n);

        spin = (Spinner)findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), languages[position] + " is under development" , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){

//        redirectActivity(this,UpdateStudentProfile.class);

//        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){

        recreate();
    }

    public void ClickAboutUs(View view){

        redirectActivity(this,AboutUs.class);
    }

    public void ClickDashboard(View view){

        redirectActivity(this,Home.class);
    }

    public void ClickProfile(View view){

        redirectActivity(this,Profile.class);
    }

    public void ClickSettings(View view){

        underDevelopment(this);
    }
    public void ClickFeedBack(View view)
    {
        underDevelopment(this);
    }

    public void ClickRate(View view)
    {
        underDevelopment(this);
    }


    public static void redirectActivity(Activity activity, Class aClass) {

        Intent intent = new Intent(activity, aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    public void ClickLogout(View view){

        logout(this);
    }

    public static void logout(Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finishAffinity();

                trimCache(activity);
                System.exit(0);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }



    public void underDevelopment(Activity activity){
        Toast.makeText(activity,"This is under development",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                boolean cache = deleteDir(dir);
                Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" +
                        " DELETED *******************");
                Log.e("After pressing Exit ", "cache memory clear result is:: " +
                        cache);
            }
        } catch (Exception e) {
// TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }



}