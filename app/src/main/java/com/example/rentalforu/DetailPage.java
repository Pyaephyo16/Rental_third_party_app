package com.example.rentalforu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.rentalforu.Adapters.PostAdapter;
import com.example.rentalforu.Adapters.SpinAdapter;
import com.example.rentalforu.Models.RentalModel;
import com.example.rentalforu.Utils.Util;
import com.example.rentalforu.db.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailPage extends AppCompatActivity implements View.OnClickListener {

    ImageView postImage;
    Button btnUpdate,btnDelete;

    TextView txtRefData,txtReporterName;

    Button detailDate;

    DatePickerDialog datePickerDialog;

    Spinner detailPropertyTypeSpinner,detailBedroomsSpinner,detailFurnitureSpinner;
    SpinAdapter detailPropertyTypeSpinnerAdapter,detailBedroomsSpinnerAdapter,detailFurnitureSpinnerAdapter;
    List<String> propertyTypeList,bedroomsList,furnitureList;

    EditText edtDetailPrice,edtDetailRemark;

    Drawable customTextfield;

    String refNo;
    boolean isEditModeOn = false;

    String updateProperty = "";
    String updateBedroom = "";
    String updateFurniture = "";

    RentalModel model;
    DBHelper db = new DBHelper(DetailPage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        refNo = getIntent().getStringExtra("refNo");
        setupUI();
    }

    private void setupUI(){
        postImage = findViewById(R.id.postImage);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        txtRefData = findViewById(R.id.txtRefData);
        detailPropertyTypeSpinner = findViewById(R.id.detailPropertyTypeSpinner);
        detailBedroomsSpinner = findViewById(R.id.detailBedroomsSpinner);
        detailFurnitureSpinner = findViewById(R.id.detailFurnitureSpinner);
        detailDate = findViewById(R.id.detailDate);
        edtDetailPrice = findViewById(R.id.edtDetailPrice);
        edtDetailRemark = findViewById(R.id.edtDetailRemark);
        txtReporterName = findViewById(R.id.txtReporterName);

        customTextfield = ResourcesCompat.getDrawable(getResources(),
                R.drawable.custom_inputtextfield, null);

        model = db.getDetailRental(refNo);
        bindingSelectedData();
        disableSpinners();

        prepareForPropertyType();
        detailPropertyTypeSpinner.setAdapter(detailPropertyTypeSpinnerAdapter);
        //insertPropertyTypeData("House");

        prepareForBedrooms();
        detailBedroomsSpinner.setAdapter(detailBedroomsSpinnerAdapter);
        //insertBedroomsData("Family");

        prepareForFurniture();
        detailFurnitureSpinner.setAdapter(detailFurnitureSpinnerAdapter);
        //insertFurnitureData("Part Furnished");

        detailBedroomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isEditModeOn==false){
                    adapterView.setSelection(setBedroom(model.getBedroomsType()));
                }
                updateBedroom = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        detailFurnitureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isEditModeOn==false){
                    adapterView.setSelection(setFurniture(model.getFurnitureType()));
                }
                updateFurniture = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        detailPropertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              if(isEditModeOn==false){
                  adapterView.setSelection(setPropertyType(model.getPropertyType()));
              }
                updateProperty = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initialDatePick();
        detailDate.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

    }

    private void disableSpinners(){
        detailPropertyTypeSpinner.setEnabled(false);
        detailPropertyTypeSpinner.setClickable(false);
        detailBedroomsSpinner.setEnabled(false);
        detailBedroomsSpinner.setClickable(false);
        detailFurnitureSpinner.setEnabled(false);
        detailFurnitureSpinner.setClickable(false);
    }

    private void prepareForPropertyType(){
        propertyTypeList = new ArrayList<>();
        propertyTypeList.add("House");
        propertyTypeList.add("Flat");
        propertyTypeList.add("Bungalow");
        detailPropertyTypeSpinnerAdapter = new SpinAdapter(DetailPage.this, android.R.layout.simple_spinner_item,propertyTypeList);
        detailPropertyTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void prepareForBedrooms(){
        bedroomsList = new ArrayList<>();
        bedroomsList.add("Studio");
        bedroomsList.add("One");
        bedroomsList.add("Two");
        bedroomsList.add("Family");
        detailBedroomsSpinnerAdapter = new SpinAdapter(DetailPage.this, android.R.layout.simple_spinner_item,bedroomsList);
        detailBedroomsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void prepareForFurniture(){
        furnitureList = new ArrayList<>();
        furnitureList.add("Furnished");
        furnitureList.add("Unfurnished");
        furnitureList.add("Part Furnished");
        detailFurnitureSpinnerAdapter = new SpinAdapter(DetailPage.this, android.R.layout.simple_spinner_item,furnitureList);
        detailFurnitureSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

//    private void insertPropertyTypeData(String s){
//        for (int i=0;i<propertyTypeList.size();i++){
//            if(s.equalsIgnoreCase(propertyTypeList.get(i))){
//                detailPropertyTypeSpinner.setSelection(i);
//            }
//        }
//    }
//
//    private void insertBedroomsData(String s){
//        for (int i=0;i<bedroomsList.size();i++){
//            if(s.equalsIgnoreCase(bedroomsList.get(i))){
//                detailBedroomsSpinner.setSelection(i);
//            }
//        }
//    }
//
//    private void insertFurnitureData(String s){
//        for (int i=0;i<furnitureList.size();i++){
//            if(s.equalsIgnoreCase(furnitureList.get(i))){
//                detailFurnitureSpinner.setSelection(i);
//            }
//        }
//    }

    public void bindingSelectedData(){
        String image = model.getRentalProfile();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(image,opt);
        postImage.setImageBitmap(bm);
        txtRefData.setText(refNo.toString());
        detailDate.setText(model.getDate());
        edtDetailPrice.setText(model.getPrice());
        edtDetailRemark.setText(model.getRemarks());
        txtReporterName.setText(model.getReporterName());
    }

    public int setPropertyType(String data){
        if(data.equals("House")){
            return 0;
        }else if(data.equals("Flat")){
            return 1;
        }else{
            return 2;
        }
    }

    public int setBedroom(String data){
        if(data.equals("Studio")){
            return 0;
        }else if(data.equals("One")){
            return 1;
        }else if(data.equals("Two")){
            return 2;
        }else{
            return 3;
        }
    }

    public int setFurniture(String data){
        if(data.equals("Furnished")){
            return 0;
        }else if(data.equals("Unfurnished")){
            return 1;
        }else{
            return 2;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String currentUser = Util.getData(DetailPage.this,"token");
        if(currentUser!="null" && currentUser.equals(model.getReporterEmail())) {
            getMenuInflater().inflate(R.menu.detail_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.editTab:activeEditMode();break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void activeEditMode(){
        isEditModeOn = true;
        detailPropertyTypeSpinner.setEnabled(true);
        detailPropertyTypeSpinner.setClickable(true);
        btnDelete.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.VISIBLE);
        detailBedroomsSpinner.setEnabled(true);
        detailBedroomsSpinner.setClickable(true);
        detailDate.setEnabled(true);
        edtDetailPrice.setEnabled(true);
        edtDetailPrice.setBackground(customTextfield);
        //edtDetailPrice.setBackgroundColor(Color.parseColor("#8B000000"));
        detailFurnitureSpinner.setEnabled(true);
        detailFurnitureSpinner.setClickable(true);
        edtDetailRemark.setEnabled(true);
        edtDetailRemark.setBackground(customTextfield);
        //edtDetailRemark.setBackgroundColor(Color.parseColor("#8B000000"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detailDate:startPickingDate();break;
            case R.id.btnDelete:getDialog(DetailPage.this,"Delete","Delete Post!","Are you sure want to delete this post?",true);break;
            case R.id.btnUpdate:getDialog(DetailPage.this,"Update","Update Post!","Are you sure want to update this post?",false);break;
        }
    }

    private void deletePost(){
        db.deleteRental(refNo);
        finish();
    }

    private void updatePost(){
        String image = model.getRentalProfile();
        //refNo is already get
        //get update property
        //get update bedroom
        String updateDate = detailDate.getText().toString();
        String updatePrice = edtDetailPrice.getText().toString();
        //get update fruniture
        String updateRemark = edtDetailRemark.getText().toString();
        String updateReporterName = txtReporterName.getText().toString();
        String updateReporterEmail = Util.getData(DetailPage.this,"token");
        String updateReporterProfile = Util.getData(DetailPage.this,"profile");

        if(updatePrice==null && updatePrice.equals("") && updateBedroom.equals("") && updateFurniture.equals("") ){
            Util.showToast(DetailPage.this,"You need to fill all fields to update post");
        }else{
                System.out.println("update image "+image);
                System.out.println("update refno "+refNo);
                System.out.println("update property "+updateProperty);
                System.out.println("update bedroom "+updateBedroom);
                System.out.println("update date "+updateDate);
                System.out.println("update price "+updatePrice);
                System.out.println("update furniture "+updateFurniture);
                System.out.println("update remark "+updateRemark);
                System.out.println("update name "+updateReporterName);
                System.out.println("update email "+updateReporterEmail);
                System.out.println("update profile "+updateReporterProfile);

                db.updateRental(
                        refNo,
                        image,
                        updateProperty,
                        updateBedroom,
                        updateDate,
                        updatePrice,
                        updateFurniture,
                        updateRemark,
                        updateReporterName,
                        updateReporterEmail,
                        updateReporterProfile
                );
                finish();
        }
    }

    public void getDialog(Context context,String btnText,String title,String msg,boolean isDelete){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(isDelete==true) {
                    deletePost();
                }else{
                    updatePost();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

    ///Date Pick Start
    private void startPickingDate(){
        datePickerDialog.show();
    }

    private void initialDatePick(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                detailDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(DetailPage.this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month)+" "+day+" "+year;
    }

    private String getMonthFormat(int month){
        String data;
        switch (month){
            case 1: data = "JAN";break;
            case 2: data = "FEB";break;
            case 3: data = "MAR";break;
            case 4: data = "APR";break;
            case 5: data = "MAY";break;
            case 6: data = "JUN";break;
            case 7: data = "JUL";break;
            case 8: data = "AUG";break;
            case 9: data = "SEP";break;
            case 10: data = "OCT";break;
            case 11: data = "NOV";break;
            case 12: data = "DEC";break;
            default:data = "JAN";break;
        }
        return data;
    }

    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }
    ///Date Pick End
}