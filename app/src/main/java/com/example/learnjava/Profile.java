package com.example.learnjava;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learnjava.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DrawerLayout drawerLayout;
    MainActivity mainActivity = new MainActivity();
    String  email_global,password_global,fname_global,lname_global,mobile_global;
    private Manager_Cache managerCache;
    private EditText etemail,etfname,etlname,etmobileno,etpassword;
    private ImageView iv_P_Profile;
    private String img_filePath;

    // Uri indicates, where the image will be picked from
    private Uri filePath;
    private Uri image_uri;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    /**
     * Global image
     */
    public String global_Img;

    ImageView ivProfilePic;
    Button btn_P_ProfilePic;

    private Button btnUpdate;
    private String email,fname,lname,mobileno,password;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USERS = "users";
    private UserInfo user;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onStart() {
        super.onStart();

        showUserImage();
        showUserEmail();
        showUserFName();
        showUserLName();
        showUserMobile();
        showUserPassword();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initUI();

        initFunctions();

        managerCache = Manager_Cache.getPreferences(this);

        drawerLayout = findViewById(R.id.drawer_layout_n);

        mainActivity.spin = (Spinner)findViewById(R.id.spinner);
        mainActivity.spin.setOnItemSelectedListener(this);

        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        DatabaseReference getImage = databaseReference.child("image");
        DatabaseReference getUser = databaseReference.child(USERS);
        getUser.setValue("tset user");
        mDatabase = database.getReference(USERS);
        mAuth = FirebaseAuth.getInstance();

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // getting a DataSnapshot for the location at the specified
                // relative path and getting in the link variable
                String link = dataSnapshot.getValue(String.class);

                // loading that data into rImage
                // variable which is ImageView
                Glide.with(Profile.this).load(link).circleCrop().into(iv_P_Profile);
            }

            // this will called when any problem
            // occurs in getting data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(Profile.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });




        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mainActivity.languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mainActivity.spin.setAdapter(aa);

        btn_P_ProfilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Profile.this, btn_P_ProfilePic);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(Profile.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        switch (item.getItemId()) {
                            case R.id.id_camera:
                                // do your code

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            == PackageManager.PERMISSION_DENIED){
                                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                        requestPermissions(permission, 112);
                                    }
                                    else {
                                        openCamera();
                                    }
                                }

                                else {
                                    openCamera();
                                }
                                return false;

                            case R.id.id_files:
                                // do your code

                                SelectImage();

                                return true;

                            default:
                                return false;
                        }

                    }
                });

                popup.show();//showing popup menu
            }
        });

    }


    private void initUI() {
        /*
         * initButton
         * */

        btnUpdate = findViewById(R.id.btnUpdate);
        btn_P_ProfilePic = (Button) findViewById(R.id.btn_P_ProfilePic);
        iv_P_Profile = findViewById(R.id.iv_P_Profile);
        etemail = findViewById(R.id.txt_p_email);
        etfname = findViewById(R.id.txt_p_f_name);
        etlname = findViewById(R.id.txt_p_l_name);
        etmobileno = findViewById(R.id.txt_p_mobile);
        etpassword = findViewById(R.id.txt_p_password);

    }

    private static final int RESULT_LOAD_IMAGE = 123;
    public static final int IMAGE_CAPTURE_CODE = 654;
    //TODO opens camera so that user can capture image
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),mainActivity.languages[position] + " is under development", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void ClickMenu(View view){ MainActivity.openDrawer(drawerLayout); }

    public void ClickProfile(View view){ MainActivity.redirectActivity(this,Profile.class); }

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

    public void ClickLogout(View view){ MainActivity.logout(this); }


    private void initFunctions() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckNetworkStatus.isNetworkStatusAvailable(getBaseContext())) {

                    /*
                     * getting input from fields
                     * */
                    String _email = etemail.getText().toString().trim();
                    String _fname = etfname.getText().toString().trim();
                    String _lname = etlname.getText().toString().trim();
                    String _mobileno = etmobileno.getText().toString().trim();
                    String _password = etpassword.getText().toString().trim();

                    if(filePath == null){
                        Log.d("imgfile", String.valueOf(filePath));
                        Toast.makeText(getApplicationContext(), "Image is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
                        etemail.setError("Enter a valid email address");
                        etemail.requestFocus();
                        return;

                    } if (_fname.equals("")) {
                        etfname.setError("First Name fields is empty");
                        etfname.requestFocus();
                        return;

                    } if (_lname.equals("")) {
                        etlname.setError("Last Name fields is empty");
                        etlname.requestFocus();
                        return;

                    } if (_mobileno.equals("")) {
                        etmobileno.setError("Mobile no fields is empty");
                        etmobileno.requestFocus();
                        return;

                    } if (_password.equals("")) {
                        etpassword.setError("Password fields is empty");
                        etpassword.requestFocus();
                        return;

                    }

                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                    // Get auth credentials from the user for re-authentication
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email_global, password_global); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    fuser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                    //Now change your email address \\
                                    //----------------Code for Changing Email Address----------\\
                                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                                    fuser.updateEmail(_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                    }
                                                }
                                            });
                                    fuser.updatePassword(_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Profile.this,"User successfully Updated", Toast.LENGTH_SHORT).show();
                                                saveUserImage();
                                                saveUserEmail();
                                                saveUserPassword();
                                                saveUserFName();
                                                saveUserLName();
                                                saveUserMobile();
                                                uploadImage();
                                                goToHome();
                                            } else {
                                                Log.d(TAG, "Error password not updated");
                                            }
                                        }
                                    });

                                    //----------------------------------------------------------\\
                                }
                            });

                } else {

                    Intent intent = new Intent(Profile.this, AlertNoInternet.class);
                    startActivity(intent);
                }

            }
        });

    }

    /**
     * Activity result method will be called after closing the camera
     */
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            Log.d("imgfile", String.valueOf("On Activity "+ filePath));

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Glide.with(this).load(bitmap).circleCrop().into(iv_P_Profile);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK){
            //imageView.setImageURI(image_uri);
            Bitmap bitmap = uriToBitmap(image_uri);
//            ivProfilePic.setImageBitmap(bitmap);
            Glide.with(this).load(bitmap).circleCrop().into(iv_P_Profile);
        }

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
//            filePath = data.getData();
//            Log.d("imgfile", String.valueOf(filePath));
            try {

                filePath = data.getData();
                Log.d("imgfile", String.valueOf("pick image "+filePath));
                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

//                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(filePath);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                Glide.with(this).load(selectedImage).circleCrop().into(iv_P_Profile);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    //TODO takes URI of the image and returns bitmap
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    // UploadImage method
    private void uploadImage()
    {
        try {
            if (image_uri != null) {

                Log.d("imgfile", String.valueOf("Upload image "+filePath));

                // Code for showing progressDialog while uploading
//                ProgressDialog progressDialog = new ProgressDialog(this);
//                progressDialog.setTitle("Uploading...");
//                progressDialog.show();

                // Defining the child of storageReference
                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

                // adding listeners on upload
                // or failure of image
                ref.putFile(image_uri).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {

                                // Image uploaded successfully
                                // Dismiss dialog
//                                        progressDialog.dismiss();
                                Toast.makeText(Profile.this,"Image Uploaded!!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                                // Error, Image not uploaded
//                                progressDialog.dismiss();
                                Toast.makeText(Profile.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                    }
                                });
            }
        }catch (Exception e)
        {

        }

    }

    public void updateUI(FirebaseUser currentUser) {
        String keyid = mDatabase.push().getKey();
        mDatabase.child(keyid).setValue(user); //adding user info to database
        Intent loginIntent = new Intent(this, Home.class);
        startActivity(loginIntent);
    }

    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult( Intent.createChooser( intent,"Select Image from here..."), PICK_IMAGE_REQUEST);
    }


    private void saveUserEmail() {
        email_global = etemail.getText().toString();
        managerCache.setUserEmail(email_global);
    }

    private void saveUserPassword() {
        password_global = etpassword.getText().toString();
        managerCache.setUserPassword(password_global);
    }

    private void saveUserFName() {
        fname_global = etfname.getText().toString();
        managerCache.setUserFName(fname_global);
    }

    private void saveUserLName() {
        lname_global = etlname.getText().toString();
        managerCache.setUserLName(lname_global);
    }

    private void saveUserMobile() {
        mobile_global = etmobileno.getText().toString();
        managerCache.setUserMobile(mobile_global);
    }

    private void saveUserImage() {
        managerCache.setUserImage(filePath);
        Log.d("imgfile", String.valueOf("Save image in cache "+filePath));

    }

    /*
     * navigation to home
     * */
    private void goToHome() {
        Intent intent=new Intent(Profile.this,Home.class);
        startActivity(intent);
        finish();
    }

    private void showUserImage() {
        filePath = managerCache.getUserImage();
        Log.d("imgfile", String.valueOf("Show cache image "+filePath));
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , filePath);
            Glide.with(this).load(bitmap).circleCrop().into(iv_P_Profile);
        }
        catch (Exception e)
        {
            //handle exception
        }
    }


    private void showUserEmail() {
        email_global = managerCache.getUserEmail();
        etemail.setText(email_global);
    }

    private void showUserFName() {
        fname_global = managerCache.getUserFName();
        etfname.setText(fname_global);
    }

    private void showUserLName() {
        lname_global = managerCache.getUserLName();
        etlname.setText(lname_global);
    }

    private void showUserMobile() {
        mobile_global = managerCache.getUserMobile();
        etmobileno.setText(mobile_global);
    }

    private void showUserPassword() {
        password_global = managerCache.getUserPassword();
        etpassword.setText(password_global);
    }
}