package com.example.change.foodorderserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.change.foodorderserver.Model.Order;
import com.example.change.foodorderserver.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name,price,quantity,discount;
    public MyViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.prdName);
        price = itemView.findViewById(R.id.prdTotal);
        quantity = itemView.findViewById(R.id.prdQty);
        discount = itemView.findViewById(R.id.prdDisc);

    }
}


public class OrderDetailsAdapter extends RecyclerView.Adapter<MyViewHolder>{
    public OrderDetailsAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    List<Order> myOrders;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name : %s",order.getProductName()));
        holder.price.setText(String.format("Price : %s",order.getPrice()));
        holder.quantity.setText(String.format("Quantity : %s",order.getQuantity()));
        holder.discount.setText(String.format("Discount : %s",order.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
