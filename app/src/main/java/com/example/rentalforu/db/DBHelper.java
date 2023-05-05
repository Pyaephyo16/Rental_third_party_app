package com.example.rentalforu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rentalforu.Models.RentalModel;
import com.example.rentalforu.Models.UserModel;
import com.example.rentalforu.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Renting.db";

    public static final String userTable = "user";
    public static final String rentalTable = "rentalU";
    //public static final String rental = "rental";

    ///UserTable Field Names
    private String userId = "id";
    private String userEmail = "email";
    private String userName = "name";
    private String userPassword = "password";
    private String userProfile = "profile";

    ///Rental U Field Names
    private String referenceNo = "refNo";

    private String rentalProfile = "rentalProfile";
    private String propertyType = "propertyType";
    private String bedroomsType = "bedroomsType";
    private String date = "date";
    private String price = "price";
    private String furnitureType = "furnitureType";
    private String remarks = "remarks";
    private String reporterName = "reporterName";
    private String reporterEmail = "reporterEmail";
    private String reporterProfile = "reporterProfile";

    public DBHelper(Context context){
        super(context,DB_NAME,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String userCreate = "CREATE TABLE "+userTable+"("+userName+" TEXT,"+userEmail+" TEXT PRIMARY KEY,"+userPassword+" TEXT,"+userProfile+" TEXT)";
        String rentalUCreate = "CREATE TABLE "+rentalTable+"("+referenceNo+" INTEGER PRIMARY KEY AUTOINCREMENT,"+rentalProfile+" TEXT,"+propertyType+" TEXT,"+bedroomsType+" TEXT,"+date+" TEXT,"+price+" TEXT,"+furnitureType+" TEXT,"+remarks+" TEXT,"+reporterName+" TEXT,"+reporterEmail+" TEXT,"+reporterProfile+" TEXT)";
               db.execSQL(userCreate);
               db.execSQL(rentalUCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+userTable);
        db.execSQL("DROP TABLE IF EXISTS "+rentalTable);
    }

    ///Delete
    public void deleteRental(String refNo){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(rentalTable,"refNo=?",new String[]{refNo});
        database.close();
    }

    ///Update
    public void updateRental(String refNo,String rProfile,String property,String bedrooms,String dateData,String priceData,String furniture,String remarkData,String name,String email,String profile){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(rentalProfile,rProfile);
        content.put(propertyType,property);
        content.put(bedroomsType,bedrooms);
        content.put(date,dateData);
        content.put(price,priceData);
        content.put(furnitureType,furniture);
        content.put(remarks,remarkData);
        content.put(reporterName,name);
        content.put(reporterEmail,email);
        content.put(reporterProfile,profile);

        db.update(rentalTable,content,"refNo=?",new String[]{refNo});
        db.close();
    }

    public void updateUser(String updateUserName,String updateUserEmail,String updateUserPassword,String updateUserProfile){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(userName,updateUserName);
        content.put(userEmail,updateUserEmail);
        content.put(userPassword,updateUserPassword);
        content.put(userProfile,updateUserProfile);

        db.update(userTable,content,"email=?",new String[]{updateUserEmail});
        db.close();
    }

    public void registerUser(String name,String email,String password,String profile){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        System.out.println("db ======================>"+name+"  "+email+"  "+password+"  "+profile);

        contentValues.put(userName,name);
        contentValues.put(userEmail,email);
        contentValues.put(userPassword,password);
        contentValues.put(userProfile,profile);

        database.insert(userTable,null,contentValues);
        database.close();
    }

    public void addRental(Context context,String rProfile,String property,String dateData,String bedrooms,String priceData,String furniture,String remarkData){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        String email = Util.getData(context,"token");
        String name = Util.getData(context,"name");
        String profile = Util.getData(context,"profile");

        content.put(rentalProfile,rProfile);
        content.put(propertyType,property);
        content.put(bedroomsType,bedrooms);
        content.put(date,dateData);
        content.put(price,priceData);
        content.put(furnitureType,furniture);
        content.put(remarks,remarkData);
        content.put(reporterName,name);
        content.put(reporterEmail,email);
        content.put(reporterProfile,profile);

        database.insert(rentalTable,null,content);
        database.close();
    }

    public List<UserModel> getUser(){

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor userCursor = database.rawQuery("SELECT * FROM "+userTable,null);

        List<UserModel> userList = new ArrayList<UserModel>();

        if(userCursor.moveToFirst()){
            do{
                userList.add(new UserModel(
                   //userCursor.getInt(0),
                   userCursor.getString(0),
                   userCursor.getString(1),
                   userCursor.getString(2),
                   userCursor.getString(3)
                ));
            }while (userCursor.moveToNext());
        }
        return userList;
    }

    ///Search By Id
    public RentalModel getDetailRental(String refNo){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+rentalTable+" WHERE refNo="+refNo,null);

        RentalModel model = new RentalModel();
        if(cursor.moveToFirst()){
            model = new RentalModel(
                cursor.getInt(0),
                cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10)
            );
        }
        return model;
    }

    public List<RentalModel> searchRental(String property){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from rentalU where propertyType='"+property+"'",null);

        List<RentalModel> searchList = new ArrayList<RentalModel>();
        if(cursor.moveToFirst()){
            do{
                searchList.add(new RentalModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10)
                ));

            }while (cursor.moveToNext());
        }
        return searchList;
    }

    public UserModel getDetailUser(String userEmail){
        String arr = userEmail.split("@")[0];
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM "+userTable+" WHERE email="+userEmail,null);
        Cursor cursor = db.rawQuery("select * from user where email='"+userEmail+"'",null);
        UserModel user = new UserModel();
        if(cursor.moveToFirst()){
            user = new UserModel(
                    //cursor.getInt(0),
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
        }
        return user;
    }

    public List<RentalModel> getAllPosts(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor rentalCursor = database.rawQuery("SELECT * FROM "+rentalTable,null);

        List<RentalModel> rentalList = new ArrayList<RentalModel>();

        if(rentalCursor.moveToFirst()){
            do{

                rentalList.add(new RentalModel(
                   rentalCursor.getInt(0),
                   rentalCursor.getString(1),
                   rentalCursor.getString(2),
                   rentalCursor.getString(3),
                   rentalCursor.getString(4),
                   rentalCursor.getString(5),
                   rentalCursor.getString(6),
                   rentalCursor.getString(7),
                   rentalCursor.getString(8),
                   rentalCursor.getString(9),
                   rentalCursor.getString(10)
                ));

            }while (rentalCursor.moveToNext());
        }
        return rentalList;
    }
}
