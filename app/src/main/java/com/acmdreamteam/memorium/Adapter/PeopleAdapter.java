package com.acmdreamteam.memorium.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.acmdreamteam.memorium.Model.Journal;
import com.acmdreamteam.memorium.Model.People;
import com.acmdreamteam.memorium.PeopleViewActivity;
import com.acmdreamteam.memorium.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {


    Context mContext;
    FirebaseUser firebaseUser;

    ArrayList<People> mPeople;


    public PeopleAdapter(Context mContext, FirebaseUser firebaseUser, ArrayList<People> mPeople) {
        this.mContext = mContext;
        this.firebaseUser = firebaseUser;
        this.mPeople = mPeople;
    }


    @NonNull
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.people_item, parent, false);
        return new PeopleAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.ViewHolder holder, int position) {

        People people = mPeople.get(position);


        holder.name.setText(people.getName());


        Glide.with(mContext).load(people.getImageURL()).into(holder.face_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFace(people.getName());
            }
        });




    }

    private void openFace(String name) {

        Intent intent = new Intent(mContext, PeopleViewActivity.class);
        intent.putExtra("name",name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mPeople.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView name = itemView.findViewById(R.id.name_txt);

        ImageView face_image = itemView.findViewById(R.id.face_view);




        public ViewHolder(View view) {
            super(view);


        }
    }
}
