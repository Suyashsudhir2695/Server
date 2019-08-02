package com.example.change.foodorderserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.change.foodorderserver.Common.Common;
import com.example.change.foodorderserver.Model.FBResponse;
import com.example.change.foodorderserver.Model.Notification;
import com.example.change.foodorderserver.Model.Request;
import com.example.change.foodorderserver.Model.Sender;
import com.example.change.foodorderserver.Model.Token;
import com.example.change.foodorderserver.Remote.APIService;
import com.example.change.foodorderserver.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatus extends AppCompatActivity {
    private static final String TAG = "OrderStatus";
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;
    MaterialSpinner spinner,spinerSupp;
    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        mService = Common.getApiService();


        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();

    }
    private void loadOrders() {

        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests,Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, final int position, @NonNull final Request model) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.codeToStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getEmail());

                viewHolder.txtOrderAddress.setText(model.getAddress());

                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(),adapter.getItem(position));

                    }
                });

                viewHolder.btnRmv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });
                viewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent orderDetails = new Intent(OrderStatus.this,OrderDetails.class);
                        Common.currentRequest = model;
                        orderDetails.putExtra("orderId",adapter.getRef(position).getKey());
                        startActivity(orderDetails);
                    }
                });
                viewHolder.btnDirect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentTrackOrders = new Intent(OrderStatus.this,TrackOrders.class);
                        Common.currentRequest = model;
                        startActivity(intentTrackOrders);

                    }
                });


            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_status_layout,viewGroup,false);
                return new OrderViewHolder(view);
            }
        };
        adapter.startListening();

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }



    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();

    }

    private void showUpdateDialog(String key, final Request item) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(OrderStatus.this);
        builder.setTitle("Update an Order");
        builder.setMessage("Choose a Status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.update_order_layout,null);
        spinner = v.findViewById(R.id.updateSpinner);
        spinerSupp = v.findViewById(R.id.supplierSpinner);

        final List<String> listSupplier = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("supplier")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot suppSnapshot : dataSnapshot.getChildren())
                            listSupplier.add(suppSnapshot.getKey());
                        spinerSupp.setItems(listSupplier);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        spinner.setItems("Placed","Preparing","Shipped","Delivered");
        final String itemIndex = String.valueOf(spinner.getSelectedIndex());

        builder.setView(v);

        final String localKey = key;
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                String itemStatus = item.getStatus();

                if (item.getStatus().equals("2")){
                    item.setSupplierEmail(spinerSupp.getItems().get(spinerSupp.getSelectedIndex()).toString());
                    FirebaseDatabase.getInstance().getReference("pendingShipment")
                            .child(spinerSupp.getItems().get(spinerSupp.getSelectedIndex()).toString())
                            .child(localKey)
                            .setValue(item);
                    requests.child(localKey).setValue(item);

                    adapter.notifyDataSetChanged();
                    sendOrderStatus(localKey, item, itemStatus);
                    sendOrderToSupplier(spinerSupp.getItems().get(spinerSupp.getSelectedIndex()).toString(),item);


                }
                else {

                    requests.child(localKey).setValue(item);

                    adapter.notifyDataSetChanged();
                    sendOrderStatus(localKey, item, itemStatus);
                }
            }
        });
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();



    }

    private void sendOrderToSupplier(String localKey, Request item) {
        DatabaseReference tokens = database.getReference("Tokens");
        tokens.child(localKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Token token = dataSnapshot.getValue(Token.class);
                            Notification notification = new Notification("Simply Food","You have a new Order shipment");
                            Sender content = new Sender(token.getToken(),notification);
                            mService.sendNotification(content)
                                    .enqueue(new Callback<FBResponse>() {
                                        @Override
                                        public void onResponse(@NonNull Call<FBResponse> call, @NonNull Response<FBResponse> response) {

                                            if (response.body().success == 1){
                                                Log.i(TAG, "onResponse: " + response.body().success);
                                                Toast.makeText(OrderStatus.this, "Order Sent to Supplier", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(OrderStatus.this, "Error Updating Order", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<FBResponse> call, Throwable t) {
                                            Log.e("Error Updating Order",t.getMessage());


                                        }
                                    });



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void sendOrderStatus(final String key, Request item, final String itemIndex) {
        DatabaseReference tokens = database.getReference("Tokens");
        tokens.child(item.getEmail().replace(".","_"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Token token = dataSnapshot.getValue(Token.class);
                            Notification notification = new Notification("Simply Food","Your Order #" + key + " was Updated to " + Common.codeToStatus(itemIndex));
                            Sender content = new Sender(token.getToken(),notification);
                            mService.sendNotification(content)
                                    .enqueue(new Callback<FBResponse>() {
                                        @Override
                                        public void onResponse(@NonNull Call<FBResponse> call, @NonNull Response<FBResponse> response) {

                                            if (response.body().success == 1){
                                                Log.i(TAG, "onResponse: " + response.body().success);
                                                Toast.makeText(OrderStatus.this, "Order was Updated", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(OrderStatus.this, "Error Updating Order", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<FBResponse> call, Throwable t) {
                                            Log.e("Error Updating Order",t.getMessage());


                                        }
                                    });

                        }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
