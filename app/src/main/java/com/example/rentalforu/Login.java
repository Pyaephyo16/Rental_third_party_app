package com.example.rentalforu;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.example.rentalforu.Models.UserModel;
import com.example.rentalforu.Utils.Util;
import com.example.rentalforu.db.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Login extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    CheckBox checkboxShowPswd;
    EditText edtName, edtPassword;

    TextView txtLogin,mqText,txtForget;
    Button btnLogin, btnRegister;
    ImageView loginLogo;
    Animation zoom_in;

    DBHelper dbHelper = new DBHelper(Login.this);
    List<UserModel> userList = new ArrayList<UserModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getSupportActionBar().hide();
        zoom_in = AnimationUtils.loadAnimation(Login.this, R.anim.zoom_in);
        zoom_in.setAnimationListener(this);

        setupUI();
    }

    private void setupUI() {
        checkboxShowPswd = findViewById(R.id.checkboxShowPswd);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loginLogo = findViewById(R.id.loginLogo);
        txtLogin = findViewById(R.id.txtLogin);
        mqText = findViewById(R.id.mqText);
        txtForget = findViewById(R.id.txtForget);

        mqText.setSelected(true);
        userList = dbHelper.getUser();

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtForget.setOnClickListener(this);
        loginLogo.startAnimation(zoom_in);

        checkboxShowPswd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    checkboxShowPswd.setText("Hide Password");
                } else {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    checkboxShowPswd.setText("Show Password");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:login();break;
            case R.id.btnRegister:startActivity(new Intent(Login.this,Register.class));break;
            case R.id.txtForget:checkForForgetPassword();break;
        }
    }

    private void checkForForgetPassword(){
        String inputEmail = edtName.getText().toString();
        boolean isUser = false;
        if(inputEmail.length()>0){
            if(Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
                for (int i=0;i<userList.size();i++){
                    UserModel model = userList.get(i);
                    if(model.getEmail().equals(inputEmail)){
                       Intent intent = new Intent(Login.this,OTP.class);
                       intent.putExtra("inputEmail",inputEmail);
                       startActivity(intent);
                        isUser = true;
                        break;
                    }
                }
                if(isUser==false){
                    Util.showToast(Login.this,"This Email Address has not match account");
                }
            }else{
                Util.showToast(Login.this,"Invalid Email Format");
            }
        }else{
            Util.showToast(Login.this,"You need to fill Email");
        }
        //
    }

    private void login(){
        boolean isUser = false;

        if(edtName.getText().length() > 0 && edtPassword.getText().length() > 0){
            if(Patterns.EMAIL_ADDRESS.matcher(edtName.getText().toString()).matches()) {

                for (int i=0;i<userList.size();i++){
                    UserModel model = userList.get(i);
                    System.out.println("data "+ userList.get(0).getEmail());
                    System.out.println("data input "+ edtName.getText().toString());
                    if(model.getEmail().equals(edtName.getText().toString()) && model.getPassword().equals(edtPassword.getText().toString())){
                            isUser = true;
                        System.out.println("login data name ========================="+model.getName());
                        System.out.println("login data pswd ========================="+model.getPassword());
                        System.out.println("login data profile ======================="+model.getProfile());
                        System.out.println("login data email =========================="+model.getEmail());
                        startActivity(new Intent(Login.this,HomePage.class));
                        Util.saveData(Login.this,"token",model.getEmail());
                        Util.saveData(Login.this,"name",model.getName());
                        Util.saveData(Login.this,"profile",model.getProfile());
                            break;
                    }
                }
             if(isUser==true){

                }else{
                Util.showToast(this,"This Email has not an account. You can sign up!");
           }
            }else{
                Util.showToast(this,"Invalid Email Address");
            }
        }else{
            Util.showToast(this,"You need to fill All Fields");
        }
    }

//    public void navigate(Intent i) {
//        startActivity(i);
//    }

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