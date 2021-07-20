package com.example.dropshipping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.dropshipping.adapter.ItemsRecyclerAdapter;
import com.example.dropshipping.domain.Items;
import com.example.dropshipping.fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Fragment homeFragment;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;

    private EditText mSearchtext;
    private FirebaseFirestore mStore;

    private List<Items> mItemsList;

    private RecyclerView mItemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth =FirebaseAuth.getInstance();

        homeFragment = new HomeFragment();
        loadFragment(homeFragment);

        mToolbar= findViewById(R.id.home_toolbar);
        mSearchtext = findViewById(R.id.search_text);
        setSupportActionBar(mToolbar);

        mStore = FirebaseFirestore.getInstance();

        mItemsList = new ArrayList<>();
        mItemRecyclerView = findViewById(R.id.search_recycler);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this,mItemsList);

        mItemRecyclerView.setAdapter(itemsRecyclerAdapter);




        mSearchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().isEmpty()){
                    mItemsList.clear();
                    itemsRecyclerAdapter.notifyDataSetChanged();
                }
                else{
                    searchItem(s.toString());

                }

                // Toast.makeText(HomeActivity.this, ""+s.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void searchItem(String text) {
        if(!text.isEmpty()){
            mStore.collection("All").whereGreaterThanOrEqualTo("name",text).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()&& task.getResult()!=null){
                                for (DocumentSnapshot doc:task.getResult().getDocuments()){
                                    Items items = doc.toObject(Items.class);
                                    mItemsList.add(items);
                                    itemsRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void logOut(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout_btn){
            mAuth.signOut();

            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();

        }

        return true;
    }
}