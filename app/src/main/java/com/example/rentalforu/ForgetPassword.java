package com.example.rentalforu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

public class ForgetPassword extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {

    ImageView forgetlogo;
    Animation zoom_in;

    EditText edtPassword,edtConfirmPassword;
    CheckBox confirmPasswordCheckbox,passwordCheckbox;
    Button btnSubmit,btnLogin;

    String inputEmail = "";

    DBHelper db = new DBHelper(ForgetPassword.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        getSupportActionBar().hide();
        zoom_in = AnimationUtils.loadAnimation(ForgetPassword.this, R.anim.zoom_in);
        zoom_in.setAnimationListener(this);
        setupUI();
    }

    private void setupUI(){
        forgetlogo = findViewById(R.id.forgetlogo);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        passwordCheckbox = findViewById(R.id.passwordCheckbox);
        confirmPasswordCheckbox = findViewById(R.id.confirmPasswordCheckbox);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnLogin = findViewById(R.id.btnLogin);

        inputEmail = getIntent().getStringExtra("inputEmail");

        btnSubmit.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        forgetlogo.startAnimation(zoom_in);

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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:nav(new Intent(ForgetPassword.this,Login.class));break;
            case R.id.btnSubmit:updatePassword();break;
        }
    }

    private void updatePassword(){
        UserModel model = db.getDetailUser(inputEmail);
        //System.out.println("forget id "+model.getId());
        System.out.println("forget name "+model.getName());
        System.out.println("forget email "+model.getEmail());
        System.out.println("forget pswd "+model.getPassword());
        System.out.println("forget profile "+model.getProfile());
        String newPassword = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if(newPassword.length()>0 && confirmPassword.length()>0){
            if (newPassword.equals(confirmPassword)){
                db.updateUser(model.getName(),inputEmail,newPassword,model.getProfile());
                startActivity(new Intent(ForgetPassword.this,Login.class));
            }else{
                Util.showToast(ForgetPassword.this,"New Password and Confirm Password must same");
            }
        }else{
            Util.showToast(ForgetPassword.this,"You need to fill All Fields");
        }
    }

    private void nav(Intent i){
        startActivity(i);
    }
}