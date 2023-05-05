package com.example.rentalforu.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalforu.DetailPage;
import com.example.rentalforu.Models.RentalModel;
import com.example.rentalforu.R;
import com.example.rentalforu.Register;
import com.example.rentalforu.Utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<RentalModel> list;
    private Context context;

    int NAV_CODE = 999;
    public PostAdapter(Context context,List<RentalModel> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_post,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RentalModel model = list.get(position);
            String current = Util.getData(context,"token");

            holder.txtUsername.setText(model.getReporterName());
            holder.txtDate.setText(model.getDate());
            holder.txtPropertyTypeData.setText(model.getPropertyType());
            holder.txtBedroomsData.setText(model.getBedroomsType());
            holder.txtPriceData.setText(model.getPrice());

            BitmapFactory.Options opt = new BitmapFactory.Options();
            Bitmap bmRent = BitmapFactory.decodeFile(model.getRentalProfile(),opt);
            Bitmap bmProfile = BitmapFactory.decodeFile(model.getReporterProfile(),opt);
            holder.imgRent.setImageBitmap(bmRent);
            holder.profile.setImageBitmap(bmProfile);

            if(current.equals(model.getReporterEmail())){
                holder.card.setVisibility(View.VISIBLE);
            }

            holder.postCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), DetailPage.class);
                    i.putExtra("refNo",String.valueOf(model.getReferenceNo()));
                    view.getContext().startActivity(i);
                }
            });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtUsername,txtDate,txtPropertyType,txtPropertyTypeData,txtBedrooms,txtBedroomsData,txtPrice,txtPriceData;
        ImageView imgRent,profile;

        CardView postCard,card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPropertyType = itemView.findViewById(R.id.txtPropertyType);
            txtPropertyTypeData = itemView.findViewById(R.id.txtPropertyTypeData);
            txtBedrooms = itemView.findViewById(R.id.txtBedrooms);
            txtBedroomsData = itemView.findViewById(R.id.txtBedroomsData);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtPriceData = itemView.findViewById(R.id.txtPriceData);
            imgRent = itemView.findViewById(R.id.imgRent);
            profile = itemView.findViewById(R.id.profile);
            postCard = itemView.findViewById(R.id.postCard);
            card = itemView.findViewById(R.id.card);
        }
    }
}
