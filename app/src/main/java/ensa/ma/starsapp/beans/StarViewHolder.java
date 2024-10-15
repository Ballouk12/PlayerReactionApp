package ensa.ma.starsapp.beans;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ensa.ma.starsapp.R;

public class StarViewHolder  extends RecyclerView.ViewHolder {
    TextView idss;
    ImageView img;
    TextView name;
    RatingBar stars;
    RelativeLayout parent;
    public StarViewHolder(@NonNull View itemView) {
        super(itemView);
        idss = itemView.findViewById(R.id.ids);
        img = itemView.findViewById(R.id.img);
        name = itemView.findViewById(R.id.name);
        stars = itemView.findViewById(R.id.stars);
        parent = itemView.findViewById(R.id.parent);
    }
}

