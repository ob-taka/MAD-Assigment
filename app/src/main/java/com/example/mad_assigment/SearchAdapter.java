package com.example.mad_assigment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_assigment.AddMedicinePage;
import com.example.mad_assigment.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> med_name_list;
    ArrayList<String>med_id_list;


    class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView med_name;
        TextView med_id;

        public SearchViewHolder(View v)
        {

            super(v);
            med_name=v.findViewById(R.id.med_name);
            med_id=v.findViewById(R.id.med_id);

        }
    }

    public SearchAdapter(Context context, ArrayList<String> med_name_list,ArrayList<String> med_id_list) {
        this.context = context;
        this.med_name_list = med_name_list;
        this.med_id_list = med_id_list;

    }


    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_row,parent,false);

        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.med_name.setText(med_name_list.get(position));
        holder.med_id.setText(med_id_list.get(position));
        final String med_name_2 = holder.med_name.getText().toString();
        //click on row of data and it will update the edit text for medicine
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicinePage.searchMed.setText(med_name_2);



            }
        });
    }



    @Override
    public int getItemCount() {
        return med_name_list.size();
    }
}
