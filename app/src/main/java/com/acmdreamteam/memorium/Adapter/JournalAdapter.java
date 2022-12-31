package com.acmdreamteam.memorium.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acmdreamteam.memorium.Model.Journal;
import com.acmdreamteam.memorium.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    private final Context mContext;

    private final FirebaseUser firebaseUser;



    private ArrayList<Journal> mJournal;



    public JournalAdapter(Context mContext, FirebaseUser firebaseUser,ArrayList<Journal> mJournal) {
       this.mContext = mContext;
       this.firebaseUser = firebaseUser;
       this.mJournal = mJournal;
    }


    @NonNull
    @Override
    public JournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.journel_item, parent, false);
        return new JournalAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.ViewHolder holder, int position) {


        Journal journal = mJournal.get(position);
    




        holder.date.setText(journal.getDate());
        holder.description.setText(journal.getThings());









    }


    @Override
    public int getItemCount() {
        return mJournal.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView title = itemView.findViewById(R.id.title);
        TextView date = itemView.findViewById(R.id.date);
        TextView description = itemView.findViewById(R.id.description);



        public ViewHolder(View view) {
            super(view);



        }
    }
}
