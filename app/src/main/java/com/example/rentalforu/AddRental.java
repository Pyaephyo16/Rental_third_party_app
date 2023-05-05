package com.example.rentalforu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.rentalforu.Adapters.SpinAdapter;
import com.example.rentalforu.Models.RentalModel;
import com.example.rentalforu.Utils.Util;
import com.example.rentalforu.db.DBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddRental extends AppCompatActivity implements View.OnClickListener {

    Spinner propertyTypeSpinner,bedroomsSpinner,furnitureSpinner;
    SpinAdapter propertyTypeSpinAdapter,bedroomsSpinnerAdapter,furnitureSpinnerAdapter;
    Button edtDatePicker,btnAdd,btnBack;
    EditText edtMonthlyRentPrice,edtRemark;
    TextView txtReferenceNo,edtReporter;

    ImageView rentalImage;

    DatePickerDialog datePickerDialog;

    List<String> propertyTypeList,bedroomsList,furnitureList;

    Uri filePath = null;
    Bitmap bitmap = null;
    String realFilePath = null;
    String date = null;
    String property = "";
    String bedroom = "";
    String furniture ="";

    int CODE = 222;

    DBHelper dbHelper =new DBHelper(AddRental.this);
    List<RentalModel> rentalList = new ArrayList<RentalModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rental);

        getSupportActionBar().hide();
        setupUI();
    }
    private void setupUI(){
        propertyTypeSpinner = findViewById(R.id.propertyTypeSpinner);
        bedroomsSpinner = findViewById(R.id.bedroomsSpinner);
        furnitureSpinner = findViewById(R.id.furnitureSpinner);
        edtDatePicker = findViewById(R.id.edtDatePicker);
        edtMonthlyRentPrice = findViewById(R.id.edtMonthlyRentPrice);
        txtReferenceNo = findViewById(R.id.txtReferenceNo);
        edtRemark = findViewById(R.id.edtRemark);
        edtReporter = findViewById(R.id.edtReporter);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        rentalImage = findViewById(R.id.rentalImage);

        initialDatePick();
        edtRemark.setText("");
        edtReporter.setText(Util.getData(AddRental.this,"name"));
        edtDatePicker.setText(getTodayDate());
        edtDatePicker.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        rentalImage.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    prepareForPropertyType();
    propertyTypeSpinner.setAdapter(propertyTypeSpinAdapter);

    prepareForBedrooms();
    bedroomsSpinner.setAdapter(bedroomsSpinnerAdapter);

    prepareForFurniture();
    furnitureSpinner.setAdapter(furnitureSpinnerAdapter);


        propertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(i>0){
                  // Util.showToast(AddRental.this,adapterView.getSelectedItem().toString());
                   property = adapterView.getSelectedItem().toString();
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bedroomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    //Util.showToast(AddRental.this,adapterView.getSelectedItem().toString());
                    bedroom = adapterView.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        furnitureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    //Util.showToast(AddRental.this,adapterView.getSelectedItem().toString());
                    furniture = adapterView.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepareForPropertyType(){
        propertyTypeList = new ArrayList<>();
        propertyTypeList.add("Select Property Type");
        propertyTypeList.add("Flag");
        propertyTypeList.add("House");
        propertyTypeList.add("Bungalow");
        propertyTypeSpinAdapter = new SpinAdapter(AddRental.this, android.R.layout.simple_spinner_item,propertyTypeList);
        propertyTypeSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void prepareForBedrooms(){
        bedroomsList = new ArrayList<>();
        bedroomsList.add("Select  Bedrooms Type");
        bedroomsList.add("Studio");
        bedroomsList.add("One");
        bedroomsList.add("Two");
        bedroomsList.add("Family");
        bedroomsSpinnerAdapter = new SpinAdapter(AddRental.this, android.R.layout.simple_spinner_item,bedroomsList);
        bedroomsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void prepareForFurniture(){
        furnitureList = new ArrayList<>();
        furnitureList.add("Select Furniture Type (Optional)");
        furnitureList.add("Furnished");
        furnitureList.add("Unfurnished");
        furnitureList.add("Part Furnished");
        furnitureSpinnerAdapter = new SpinAdapter(AddRental.this, android.R.layout.simple_spinner_item,furnitureList);
        furnitureSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edtDatePicker:startPickingDate();break;
            case R.id.btnAdd:insertPost();break;
            case R.id.rentalImage:requestForPermission();break;
            case R.id.btnBack:finish();break;
        }
    }

    public void insertPost(){

        if(realFilePath!=null){
            if(property!="" && bedroom!="" && date!=null && edtMonthlyRentPrice.getText().length()>0){
                System.out.println("rental image "+realFilePath);
                System.out.println("property "+property);
                System.out.println("bedroom "+bedroom);
                System.out.println("date "+date);
                System.out.println("price "+edtMonthlyRentPrice.getText().toString());
                System.out.println("furniture "+furniture);
                System.out.println("Remark "+edtRemark.getText().toString());
                System.out.println("reporter name "+edtReporter.getText().toString());
                System.out.println("reporter email "+Util.getData(AddRental.this,"token"));
                System.out.println("reporter profile "+Util.getData(AddRental.this,"profile"));

                dbHelper.addRental(
                        AddRental.this,
                        realFilePath,
                        property,
                        date,
                        bedroom,
                        edtMonthlyRentPrice.getText().toString(),
                        furniture,
                        edtRemark.getText().toString()
                );
                finish();
            }else{
                ////fill all fields
                Util.showToast(AddRental.this,"You need to fill all fields");
            }
        }else{
            ///need image
            Util.showToast(AddRental.this,"You need to add Image");
        }
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
                    date = makeDateString(day,month,year);
                    edtDatePicker.setText(date);
                }
            };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(AddRental.this,style,dateSetListener,year,month,day);
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

    ///Image Pick Start
    public void requestForPermission(){
        ActivityCompat.requestPermissions(AddRental.this,
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
            Util.showToast(AddRental.this,"Need Permission to pick photo");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE && resultCode == RESULT_OK && data!=null){
            filePath = data.getData();
            System.out.println("profile file path "+filePath.toString());
            System.out.println("real path "+getRealPathFromURI(AddRental.this,filePath));

            realFilePath = getRealPathFromURI(AddRental.this,filePath);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(realFilePath,opt);
            rentalImage.setImageBitmap(bm);

//            try {
//                InputStream input = getContentResolver().openInputStream(filePath);
//
//                bitmap = BitmapFactory.decodeStream(input);
//                rentalImage.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            }

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
    ///Image Pick End

}