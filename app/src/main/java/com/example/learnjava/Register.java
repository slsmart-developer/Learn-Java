package com.example.learnjava;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learnjava.model.ImageUploadInfo;
import com.example.learnjava.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class Register extends AppCompatActivity {


    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";

    // Creating button.
    Button ChooseButton, UploadButton;

    // Creating EditText.
    EditText ImageName ;

    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    int Image_Request_Code = 7;

//    ProgressDialog progressDialog ;

    Boolean CheckImageViewEditText;


    /**
     * ---------------
     */


    /**
     * ---------------
     */

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
    String  email_global,password_global,fname_global,lname_global,mobile_global;

    ImageView ivProfilePic;
    Button  btnProfilePic;

    private Manager_Cache managerCache;

    private Button btnRegister;
    private EditText etemail,etfname,etlname,etmobileno,etpassword;
    private String email,fname,lname,mobileno,password;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USERS = "users";
    private UserInfo user;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        managerCache = Manager_Cache.getPreferences(this);

        initUI();

        initFunctions();


        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USERS);
        mAuth = FirebaseAuth.getInstance();

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        /**
         * ---------------
         */

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        /**
         * ---------------
         */

        btnProfilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Register.this, btnProfilePic);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(Register.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

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

//                                return true;
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


        //TODO ask for permission of camera upon first launch of application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 112);
            }
        }

        //TODO captue image using camera
        ivProfilePic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
            }
        });


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






    /*
     * UI elements
     * */
    private void initUI() {
        /*
         * initButton
         * */
        btnRegister = findViewById(R.id.btnRegister);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfile);

        btnProfilePic = (Button) findViewById(R.id.btnProfilePic);

        /*
         * initEditText
         * */
        etemail = findViewById(R.id.txt_r_email);
        etfname = findViewById(R.id.txt_r_f_name);
        etlname = findViewById(R.id.txt_r_l_name);
        etmobileno = findViewById(R.id.txt_r_mobile);
        etpassword = findViewById(R.id.txt_r_password);

        /*
         * Firebase init
         * */
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(USERS);
    }

    /*
     * functions
     * */
    private void initFunctions() {

        btnRegister.setOnClickListener(new View.OnClickListener() {
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

                    if(image_uri == null){
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

                    mAuth.createUserWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Register.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                                user = new UserInfo(email, fname , lname, mobileno);
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                saveUserImage();
                                saveUserEmail();
                                saveUserPassword();
                                saveUserFName();
                                saveUserLName();
                                saveUserMobile();
                                uploadImage();
                                goToHome();
                                UploadImageFileToFirebaseStorage();
                            }
                            else
                            {
                                Toast.makeText(Register.this,"You are not Registered! Try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {

                    Intent intent = new Intent(Register.this, AlertNoInternet.class);
                    startActivity(intent);
                }

            }
        });

    }




    public void updateUI(FirebaseUser currentUser) {
        String keyid = mDatabase.push().getKey();
        mDatabase.child(keyid).setValue(user); //adding user info to database
        Intent loginIntent = new Intent(this, Home.class);
        startActivity(loginIntent);
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (image_uri != null) {

            // Setting progressDialog Title.
//            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
//            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(image_uri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
//                            String TempImageName = ImageName.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.
//                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo("TempImageName", "taskSnapshot.getDownloadUrl().toString()");

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
//                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(Register.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
//                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(Register.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult( Intent.createChooser( intent,"Select Image from here..."), PICK_IMAGE_REQUEST);
    }


    //TODO takes URI of the image and returns bitmap
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
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
            Log.d("imgfile", String.valueOf(filePath));

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Glide.with(this).load(bitmap).circleCrop().into(ivProfilePic);
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
            Glide.with(this).load(bitmap).circleCrop().into(ivProfilePic);
        }

//        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK){
//            innerImage.setImageURI(image_uri);
//        }

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            Log.d("imgfile", String.valueOf(filePath));
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Glide.with(this).load(bitmap).circleCrop().into(ivProfilePic);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    // UploadImage method
    private void uploadImage()
    {
        try {
            if (image_uri != null) {

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
                                        Toast.makeText(Register.this,"Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                                // Error, Image not uploaded
//                                progressDialog.dismiss();
                                Toast.makeText(Register.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
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




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
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

    }

    /*
     * navigation to home
     * */
    private void goToHome() {
        Intent intent=new Intent(Register.this,Home.class);
        startActivity(intent);
        finish();
    }

}