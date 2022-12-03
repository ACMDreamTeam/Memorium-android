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
    String things,date,type,priority;
    private final FirebaseUser firebaseUser;

    private final ArrayList<Journal> journalArrayList;



    public JournalAdapter(Context mContext, FirebaseUser firebaseUser, String things, String date, String type, String priority, ArrayList<Journal> journalArrayList) {
       this.mContext = mContext;
       this.firebaseUser = firebaseUser;
       this.things = things;
       this.date = date;
       this.type = type;
       this.priority = priority;
       this.journalArrayList = journalArrayList;
    }

    @NonNull
    @Override
    public JournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.journel_item, parent, false);
        return new JournalAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.ViewHolder holder, int position) {

        final Journal journal = journalArrayList.get(position);


        String date,description;
        date = journal.getThings();
        description = journal.getThings();

        holder.date.setText(date);
        holder.description.setText(description);







    }


    @Override
    public int getItemCount() {
        return journalArrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView date = itemView.findViewById(R.id.date);
        TextView description = itemView.findViewById(R.id.description);



        public ViewHolder(View view) {
            super(view);



        }
    }
}
