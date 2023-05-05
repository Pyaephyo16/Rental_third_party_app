package com.example.rentalforu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.rentalforu.Models.UserModel;
import com.example.rentalforu.Utils.Util;
import com.example.rentalforu.db.DBHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    ImageView profile,registerlogo;
    Button btnSignUp,btnBack;
    EditText edtName,edtPassword,edtEmail,edtConfirmPassword;
    CheckBox passwordCheckbox,confirmPasswordCheckbox;

    Animation zoom_in;
    Uri filePath = null;
    Bitmap bitmap = null;
    String realFilePath = null;

    DBHelper dbHelper = new DBHelper(Register.this);
    List<UserModel> userList = new ArrayList<UserModel>();
    int CODE = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        getSupportActionBar().hide();
        zoom_in = AnimationUtils.loadAnimation(Register.this,R.anim.zoom_in);
        zoom_in.setAnimationListener(this);
        setupUI();
    }

    private void setupUI(){
        btnBack = findViewById(R.id.btnBack);
        profile = findViewById(R.id.profile);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        passwordCheckbox = findViewById(R.id.passwordCheckbox);
        confirmPasswordCheckbox = findViewById(R.id.confirmPasswordCheckbox);
        registerlogo = findViewById(R.id.registerlogo);

        btnBack.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        profile.setOnClickListener(this);
        registerlogo.startAnimation(zoom_in);
        passwordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordCheckbox.setText("Hide Password");
                    }else{
                        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordCheckbox.setText("Show Password");
                    }
            }
        });

        confirmPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordCheckbox.setText("Hide Password");
                }else{
                    edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordCheckbox.setText("Show Password");
                }
            }
        });
    }

    public void requestForPermission(){
        ActivityCompat.requestPermissions(Register.this,
               new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent,"Pick an image"),CODE);
            }
        }else{
            Util.showToast(Register.this,"Need Permission to pick photo");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE && resultCode == RESULT_OK && data!=null){
            filePath = data.getData();
            System.out.println("profile file path "+filePath.toString());
            System.out.println("real path "+getRealPathFromURI(Register.this,filePath));

            realFilePath = getRealPathFromURI(Register.this,filePath);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(realFilePath,opt);
            profile.setImageBitmap(bm);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getRealPathFromURI(Context context, Uri contentUri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String realPath = cursor.getString(column_index);
        cursor.close();
        return realPath;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:finish();break;
            case R.id.btnSignUp:signUp();break;
            case R.id.profile:requestForPermission();break;
        }
    }

    private void signUp(){
        int one = edtName.getText().length();
        int two = edtPassword.getText().length();
        int three = edtConfirmPassword.length();
        int four = edtEmail.getText().length();

        if(one > 0 && two > 0 && three > 0 && four > 0){
            if(realFilePath != null){
                if(Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
                        if(edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){

                            userList = dbHelper.getUser();
                            boolean alreadyAcc = false;

                            for (int i=0;i<userList.size();i++){
                                if (edtEmail.getText().toString().equals(userList.get(i).getEmail())){
                                    alreadyAcc = true;
                                    break;
                                }
                            }
                            if(alreadyAcc != true){
                                dbHelper.registerUser(
                                        edtName.getText().toString(),
                                        edtEmail.getText().toString(),
                                        edtPassword.getText().toString(),
                                        realFilePath
                                );
                                Util.saveData(Register.this,"token",edtEmail.getText().toString());
                                Util.saveData(Register.this,"name",edtName.getText().toString());
                                Util.saveData(Register.this,"profile",realFilePath);
                                Intent i = new Intent(Register.this,HomePage.class);
                                startActivity(i);
                            }else{
                              Util.showToast(this,"This Email already has an account");
                            }
                        }else{
                            Util.showToast(this,"Password and Confirm Password must same");
                        }
                }else{
                    Util.showToast(this,"Invalid Email");
                }
            }else{
                Util.showToast(this,"You need to add Profile Image");
            }
        }else{
            Util.showToast(this,"You need to fill All fields");
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}