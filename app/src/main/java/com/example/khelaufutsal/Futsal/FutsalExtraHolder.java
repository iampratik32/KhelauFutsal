package com.example.khelaufutsal.Futsal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class FutsalExtraHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView extraName;
    public CardView cardView;
    public ImageView addButton;

    public FutsalExtraHolder(@NonNull final View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        extraName = itemView.findViewById(R.id.extraName_extrasList);
        cardView = itemView.findViewById(R.id.cardView_extrasList);
        addButton = itemView.findViewById(R.id.addButton_extrasList);
    }

    @Override
    public void onClick(View v) {

    }
}
