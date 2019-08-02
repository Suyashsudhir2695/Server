package com.example.change.foodorderserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.change.foodorderserver.Interface.ItemClickListener;
import com.example.change.foodorderserver.R;

public class SupplierViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
   public TextView txtSupplierName,txtSupplierEmail,txtSupplierNumber;
    public Button supplierRemove,supplierEdit;
    private ItemClickListener itemClickListener;
    public SupplierViewHolder(@NonNull View itemView) {
        super(itemView);
        txtSupplierName = itemView.findViewById(R.id.txtSupplierName);
        txtSupplierEmail = itemView.findViewById(R.id.txtSupplierEmail);
        txtSupplierNumber = itemView.findViewById(R.id.txtSupplierNumber);

        supplierRemove = itemView.findViewById(R.id.supplierRemove);
        supplierEdit = itemView.findViewById(R.id.supplierEdit);




    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
