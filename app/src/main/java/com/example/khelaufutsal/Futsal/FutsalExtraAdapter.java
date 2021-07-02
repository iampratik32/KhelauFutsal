package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.DialogChangePassword;
import com.example.khelaufutsal.Player.FutsalGroundHolder;
import com.example.khelaufutsal.Player.Player_FutsalClass;
import com.example.khelaufutsal.R;

import java.util.List;

public class FutsalExtraAdapter extends RecyclerView.Adapter<FutsalExtraHolder>{
    private List<String> extrasList;
    private Context context;

    public FutsalExtraAdapter(List<String> extrasList, Context context) {
        this.extrasList = extrasList;
        this.context = context;
    }

    @NonNull
    @Override
    public FutsalExtraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_extras_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutView.setLayoutParams(layoutParams);
        FutsalExtraHolder holder = new FutsalExtraHolder(layoutView);
        return holder;
    }

    @Override
    public int getItemCount() {
        int s = extrasList.size()+1;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull FutsalExtraHolder holder, final int position) {
        if(position==0){
            holder.extraName.setText("Add New");
            holder.addButton.setImageResource(R.drawable.add_button);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogAddExtra dialogAddExtra = new DialogAddExtra(context,extrasList,"Add",FutsalExtraAdapter.this,position);
                    dialogAddExtra.show(((AppCompatActivity)context).getSupportFragmentManager(),"Add");
                }
            });
        }
        else {
            if(!extrasList.get(position-1).equals("NullValue")){
                holder.extraName.setText(extrasList.get(position-1));
                holder.addButton.setImageResource(R.drawable.edit_button);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogAddExtra dialogAddExtra = new DialogAddExtra(context,extrasList,extrasList.get(position-1),FutsalExtraAdapter.this,position);
                        dialogAddExtra.show(((AppCompatActivity)context).getSupportFragmentManager(),"Edit");
                    }
                });
            }
            else {
                extrasList.remove(position-1);
            }
        }
    }
}
