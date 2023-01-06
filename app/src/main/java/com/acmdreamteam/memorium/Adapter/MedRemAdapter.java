package com.acmdreamteam.memorium.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.acmdreamteam.memorium.JournalReadActivity;
import com.acmdreamteam.memorium.Model.Journal;
import com.acmdreamteam.memorium.Model.MedRem;
import com.acmdreamteam.memorium.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class MedRemAdapter extends RecyclerView.Adapter<MedRemAdapter.ViewHolder> {

    private final Context mContext;

    private final FirebaseUser firebaseUser;


    private ArrayList<MedRem> medRems;


    public MedRemAdapter(Context mContext, FirebaseUser firebaseUser, ArrayList<MedRem> medRems) {
        this.mContext = mContext;
        this.firebaseUser = firebaseUser;
        this.medRems = medRems;
    }


    @NonNull
    @Override
    public MedRemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.med_rem_item, parent, false);
        return new MedRemAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull MedRemAdapter.ViewHolder holder, int position) {
        MedRem medRem = medRems.get(position);

        holder.name.setText(medRem.getName());
        holder.food.setText(medRem.getFood());
        holder.time.setText(medRem.getTime());

        holder.freq.setText(medRem.getFrequency());

        holder.weekday.setVisibility(View.INVISIBLE);

        if(Objects.equals(medRem.getFrequency(), "Weekly")){

            holder.weekday.setText(medRem.getWeekday());
            holder.weekday.setVisibility(View.VISIBLE);

        }


        if(medRem.getMed_type().equals("Tablet")){
            Glide.with(mContext).load(R.drawable.pills).into(holder.itype);
            holder.no.setText(medRem.getNumber() + " Pill");
        }
        if(medRem.getMed_type().equals("Syrup")){
            Glide.with(mContext).load(R.drawable.syrup).into(holder.itype);
            holder.no.setText(medRem.getNumber() + " ml");
        }
        if(medRem.getMed_type().equals("Powder")){
            Glide.with(mContext).load(R.drawable.powder).into(holder.itype);
            holder.no.setText(medRem.getNumber() + " Spoon");
        }
        if(medRem.getMed_type().equals("Injection")){
            Glide.with(mContext).load(R.drawable.injection).into(holder.itype);
            holder.no.setText("");
        }
        if(medRem.getMed_type().equals("Oinment")){
            Glide.with(mContext).load(R.drawable.ointment).into(holder.itype);
            holder.no.setText("");
        }
        if(medRem.getMed_type().equals("Other")){
            Glide.with(mContext).load(R.drawable.pills).into(holder.itype);
            holder.no.setText("");
        }


    }


    @Override
    public int getItemCount() {
        return medRems.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView name = itemView.findViewById(R.id.med_name);
        TextView food = itemView.findViewById(R.id.med_food);
        TextView time = itemView.findViewById(R.id.med_time);
        TextView no = itemView.findViewById(R.id.med_no);
        TextView freq = itemView.findViewById(R.id.med_frequency);
        TextView weekday = itemView.findViewById(R.id.med_weekday);
        ImageView itype = itemView.findViewById(R.id.type_img);



        CardView card = itemView.findViewById(R.id.card);


        public ViewHolder(View view) {
            super(view);


        }
    }

}