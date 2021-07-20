package com.example.dropshipping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dropshipping.adapter.AddressAdapter;
import com.example.dropshipping.domain.Address;
import com.example.dropshipping.domain.BestSell;
import com.example.dropshipping.domain.Feature;
import com.example.dropshipping.domain.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {
    private RecyclerView mAddressRecyclerView;
    private AddressAdapter mAddressAdapter;
    private Button paymentBtn;
    private Button mAddAddressBtn;
    private List<Address> mAddressList;

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    private androidx.appcompat.widget.Toolbar mToolbar;

    String address ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        mToolbar=findViewById(R.id.address_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Object obj = getIntent().getSerializableExtra("item");

        mAddressRecyclerView = findViewById(R.id.address_recyclerView);
        paymentBtn = findViewById(R.id.payment_btn);
        mAddAddressBtn = findViewById(R.id.add_address_btn);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        mAddressList = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(getApplicationContext(),mAddressList,this);

        mAddressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAddressRecyclerView.setAdapter(mAddressAdapter);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult()){
                        Address address =doc.toObject(Address.class);
                        mAddressList.add(address);
                        mAddressAdapter.notifyDataSetChanged();
                    }

                }
            }
        });

        mAddAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
                startActivity(intent);
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double amount = 0.0;
                String url ="";
                String name ="";

                if(obj instanceof Feature){
                    Feature f = (Feature) obj;
                    amount =f.getPrice();
                    url=f.getImage_url();
                    name=f.getName();
                }
                if(obj instanceof BestSell){
                    BestSell b = (BestSell) obj;
                    amount =b.getPrice();
                    url=b.getImg_url();
                    name=b.getName();
                }
                if(obj instanceof Items){
                    Items i = (Items) obj;
                    amount =i.getPrice();
                    url=i.getImg_url();
                    name=i.getName();
                }




                Intent intent = new Intent(AddressActivity.this,PaymentActivity.class);
                intent.putExtra("amount",amount);
                intent.putExtra("img_url",url);
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                startActivity(intent);

            }
        });
    }


    @Override
    public void setAddress(String s) {
        address=s;

    }
}