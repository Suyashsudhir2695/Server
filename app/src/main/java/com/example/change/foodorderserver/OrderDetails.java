package com.example.change.foodorderserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.change.foodorderserver.Common.Common;
import com.example.change.foodorderserver.ViewHolder.OrderDetailsAdapter;

public class OrderDetails extends AppCompatActivity {

    TextView order_id,order_total,order_add,order_comment,order_phone;
    String order_id_value = "";

    RecyclerView listFoods;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        setTitle("Order Details");
        order_phone = findViewById(R.id.text_order_phone_details);
        order_id = findViewById(R.id.text_order_id_details);
        order_total = findViewById(R.id.text_order_total_details);
        order_add = findViewById(R.id.text_order_add_details);
        order_comment = findViewById(R.id.text_order_comment_details);
        listFoods = findViewById(R.id.listFoodDetails);
        listFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listFoods.setLayoutManager(layoutManager);


        if (getIntent() != null)
            order_id_value = getIntent().getStringExtra("orderId");

        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getEmail());
        order_total.setText(Common.currentRequest.getTotal());
        order_add.setText(Common.currentRequest.getAddress());
        order_comment.setText(Common.currentRequest.getComment());

        OrderDetailsAdapter adapter  = new OrderDetailsAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        listFoods.setAdapter(adapter);




    }
}
