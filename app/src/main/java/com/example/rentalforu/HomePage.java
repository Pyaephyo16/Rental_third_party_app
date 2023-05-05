package com.example.rentalforu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.rentalforu.Adapters.PostAdapter;
import com.example.rentalforu.Models.RentalModel;
import com.example.rentalforu.Utils.Util;
import com.example.rentalforu.db.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePage extends AppCompatActivity {

    RecyclerView recyclerList;
    FloatingActionButton btnFloat;
    PostAdapter postAdapter;

    int NAV_CODE = 999;

    DBHelper db = new DBHelper(HomePage.this);
    List<RentalModel> rentalList = new ArrayList<RentalModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        rentalList = db.getAllPosts();
        setupUI();
    }
    private void setupUI(){
        recyclerList = findViewById(R.id.recyclerList);
        btnFloat = findViewById(R.id.btnFloat);

        recyclerList.setHasFixedSize(true);
        recyclerList.setLayoutManager(new LinearLayoutManager(HomePage.this));
        getPost();




        btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,AddRental.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem searchTab = menu.findItem(R.id.searchTab);
        SearchView searchV = (SearchView) searchTab.getActionView();

        searchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<RentalModel> searchList = db.searchRental(s);

                postAdapter = new PostAdapter(HomePage.this,searchList);
                recyclerList.setAdapter(postAdapter);
                notifyAdapterDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()==0){
                    rentalList = db.getAllPosts();
                    getPost();
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutTab:getDialog(HomePage.this);break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void getPost(){
        postAdapter = new PostAdapter(HomePage.this,rentalList);
        recyclerList.setAdapter(postAdapter);
        notifyAdapterDataSetChanged();
    }

    private void notifyAdapterDataSetChanged() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rentalList = db.getAllPosts();
        postAdapter = new PostAdapter(HomePage.this,rentalList);
        recyclerList.setAdapter(postAdapter);
        notifyAdapterDataSetChanged();
    }

    public void getDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Util.saveData(HomePage.this,"token",null);
                Util.saveData(HomePage.this,"name",null);
                Util.saveData(HomePage.this,"profile",null);
               startActivity(new Intent(HomePage.this,Login.class));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setTitle("Exit Alert");
        dialog.setMessage("Are you sure want to exit?");
        dialog.show();
    }


}