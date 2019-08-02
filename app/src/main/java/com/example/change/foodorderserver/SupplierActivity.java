package com.example.change.foodorderserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.change.foodorderserver.Model.Shipper;
import com.example.change.foodorderserver.ViewHolder.SupplierViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SupplierActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fabAddSupplier;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference supplierRef;
    FirebaseRecyclerAdapter<Shipper, SupplierViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        fabAddSupplier = findViewById(R.id.fabAddSupplier);
        recyclerView = findViewById(R.id.listSuppliers);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance();
        supplierRef = database.getReference("supplier");
        fabAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSupplierAlert();
            }
        });

        loadSupplierList();
    }

    private void loadSupplierList() {
        FirebaseRecyclerOptions<Shipper> options = new FirebaseRecyclerOptions.Builder<Shipper>()
                .setQuery(supplierRef,Shipper.class).build();

        adapter = new FirebaseRecyclerAdapter<Shipper, SupplierViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SupplierViewHolder holder, final int position, @NonNull final Shipper model) {
                holder.txtSupplierEmail.setText(model.getEmail());
                holder.txtSupplierName.setText(model.getName());
                holder.txtSupplierNumber.setText(model.getPhone());
                holder.supplierEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSupplierEditDialog(adapter.getRef(position).getKey(),model);
                    }
                });
                holder.supplierRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            removeSupplier(Objects.requireNonNull(adapter.getRef(position).getKey()));
                        }
                    }
                });



            }

            @NonNull
            @Override
            public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supplier_layout,viewGroup,false);
                return new SupplierViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void removeSupplier(String ref) {

        supplierRef.child(ref.replace(".","_")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SupplierActivity.this, "Supplier Removed", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SupplierActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showSupplierEditDialog(String key, final Shipper model) {
        final BottomSheetDialog dialog = new BottomSheetDialog(SupplierActivity.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        dialog.setTitle("Add Supplier");
        View view = this.getLayoutInflater().inflate(R.layout.edit_supp_layout,null);
        final MaterialEditText addSupplierNameEdit,addSupplierEmailEdit,addSupplierNumberEdit;//,addSupplierPass;
        Button btn  = view.findViewById(R.id.btnAddSupplierEdit);
        addSupplierEmailEdit = view.findViewById(R.id.addSupplierEmailEdit);
        addSupplierNameEdit = view.findViewById(R.id.addSupplierNameEdit);
        //addSupplierPass = view.findViewById(R.id.addSupplierPass);
        addSupplierNumberEdit = view.findViewById(R.id.addSupplierNumberEdit);


        addSupplierEmailEdit.setText(model.getEmail());
        addSupplierNameEdit.setText(model.getName());
        addSupplierNumberEdit.setText(model.getPhone());






        dialog.setContentView(view);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SupplierActivity.this);
                progressDialog.setMessage("Updating Shipper.Please Wait!");
                progressDialog.show();
                final String name = addSupplierNameEdit.getText().toString();
                String email = addSupplierEmailEdit.getText().toString();
                String number = addSupplierNumberEdit.getText().toString();
               // String pass = addSupplierPass.getText().toString();
                Map<String,Object> map = new HashMap<>();
                map.put("name",name);
                map.put("email",email);
                map.put("phone",number);

                supplierRef.child(email.replace(".","_")).updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SupplierActivity.this, "Shipper " + model.getName() + " Updated", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SupplierActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });




            }
        });

        dialog.show();

    }

    private void showAddSupplierAlert() {
        final BottomSheetDialog dialog = new BottomSheetDialog(SupplierActivity.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        dialog.setTitle("Add Supplier");
        View view = this.getLayoutInflater().inflate(R.layout.add_supplier,null);
        final MaterialEditText addSupplierName,addSupplierEmail,addSupplierNumber,addSupplierPass;
        Button btn  = view.findViewById(R.id.btnAddSupplier);
        addSupplierEmail = view.findViewById(R.id.addSupplierEmail);
        addSupplierName = view.findViewById(R.id.addSupplierName);
        addSupplierPass = view.findViewById(R.id.addSupplierPass);
        addSupplierNumber = view.findViewById(R.id.addSupplierNumber);

        dialog.setContentView(view);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = addSupplierName.getText().toString();
                String email = addSupplierEmail.getText().toString();
                String number = addSupplierNumber.getText().toString();
                String pass = addSupplierPass.getText().toString();
                Shipper shipper = new Shipper(email,pass,number,name);

                supplierRef.child(email.replace(".","_")).setValue(shipper)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SupplierActivity.this, "Shipper " + name + " created", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SupplierActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        dialog.show();
    }
}
