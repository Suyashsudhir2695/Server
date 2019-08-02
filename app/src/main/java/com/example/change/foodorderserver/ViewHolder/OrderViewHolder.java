package com.example.change.foodorderserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.change.foodorderserver.Common.Common;
import com.example.change.foodorderserver.Interface.ItemClickListener;
import com.example.change.foodorderserver.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class OrderViewHolder extends RecyclerView.ViewHolder  {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;
    public FancyButton btnEdit,btnRmv,btnDirect,btnDetails;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.text_order_id);
        txtOrderAddress = itemView.findViewById(R.id.text_order_add);
        txtOrderPhone = itemView.findViewById(R.id.text_order_phone);
        txtOrderStatus = itemView.findViewById(R.id.text_order_status);

        btnDetails = itemView.findViewById(R.id.btnDetails);
        btnRmv = itemView.findViewById(R.id.btnRmv);
        btnDirect = itemView.findViewById(R.id.btnDirect);
        btnEdit = itemView.findViewById(R.id.btnEdit);


    }


}
