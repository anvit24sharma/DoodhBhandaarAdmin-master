package com.doodhbhandaar.dbadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListOfAllDeliveryBoysAdapter extends RecyclerView.Adapter<ViewHolderDeliveryBoys> {

    ArrayList<DeliveryBoyReference> deliveryBoyItems;
    LayoutInflater inflater;
    ListOfAllDeliveryBoyInterface deliveryBoyInterface;


    public ListOfAllDeliveryBoysAdapter(Context context, ArrayList<DeliveryBoyReference> deliveryAreasItems, ListOfAllDeliveryBoyInterface deliveryBoyInterface){
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.deliveryBoyItems=deliveryAreasItems;
        this.deliveryBoyInterface = deliveryBoyInterface;
    }

    @NonNull
    @Override
    public ViewHolderDeliveryBoys onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View output=inflater.inflate(R.layout.view_holder_deliverboy,parent,false);
        return new ViewHolderDeliveryBoys(output);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDeliveryBoys holder, final int position) {
        final DeliveryBoyReference deliveryBoyReference = deliveryBoyItems.get(position);
        holder.deliveryboyName.setText(deliveryBoyReference.name);
        holder.deliveryboyPhonenumber.setText(deliveryBoyReference.contactNo);
        holder.deliveryboyAddress.setText(deliveryBoyReference.address);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryBoyInterface.onViewSingleClick(v,position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deliveryBoyInterface.onViewLongClick(v,position);
                return false;
            }
        });


        holder.deliveryboy_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryBoyInterface.onStatsClick(view,position);
            }
        });
        Picasso.get().load(String.valueOf(deliveryBoyReference.photoUrl))
                .fit().placeholder(R.color.colorPrimary).into(holder.deliveryboyImage);

    }

    @Override
    public int getItemCount() {
        return deliveryBoyItems.size();
    }
}