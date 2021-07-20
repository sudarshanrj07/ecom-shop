package com.example.dropshipping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dropshipping.domain.BestSell;
import com.example.dropshipping.domain.Feature;
import com.example.dropshipping.domain.Items;

public class DetailActivity extends AppCompatActivity {
    private ImageView mImage;
    private TextView mItemName;
    private TextView mPrice;
    private Button mItemRating;
    private TextView mItemRatDesc;
    private TextView mItemDesc;
    private Button mAddToCart;
    private Button mBuyBtn;

    private androidx.appcompat.widget.Toolbar mToolbar;


    Feature feature = null;
    BestSell bestSell = null;
    Items items =  null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        mToolbar=findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mImage = findViewById(R.id.item_img);
        mItemName = findViewById(R.id.item_name);
        mPrice = findViewById(R.id.item_price);
        mItemRating = findViewById(R.id.item_rating);
        mItemRatDesc = findViewById(R.id.item_rating_text);
        mItemDesc = findViewById(R.id.item_des);
        mAddToCart = findViewById(R.id.item_add_cart);
        mBuyBtn = findViewById(R.id.item_buy_now);


         final Object obj = getIntent().getSerializableExtra("detail");
        //Toast.makeText(this, ""+feature.getName(), Toast.LENGTH_SHORT).show();
        if (obj instanceof Feature){
            feature = (Feature) obj;
        }else if (obj instanceof BestSell){
            bestSell = (BestSell) obj;
        }
        else if (obj instanceof Items){
            items = (Items) obj;
        }

        if(feature!=null){
            Glide.with(getApplicationContext()).load(feature.getImage_url()).into(mImage);
            mItemName.setText(feature.getName());
            mPrice.setText(feature.getPrice()+" ₹");
            mItemRating.setText(feature.getRating()+"");
            if(feature.getRating()>3){
                mItemRatDesc.setText("very good");

            }else{
                mItemRatDesc.setText("Good");
            }
            mItemDesc.setText(feature.getDescription());

        }
        if(bestSell!=null){
            Glide.with(getApplicationContext()).load(bestSell.getImg_url()).into(mImage);
            mItemName.setText(bestSell.getName());
            mPrice.setText(bestSell.getPrice()+" ₹");
            mItemRating.setText(bestSell.getRating()+"");
            if(bestSell.getRating()>3){
                mItemRatDesc.setText("very good");

            }else{
                mItemRatDesc.setText("Good");
            }
            mItemDesc.setText(bestSell.getDescription());

        }


        if(items!=null){
            Glide.with(getApplicationContext()).load(items.getImg_url()).into(mImage);
            mItemName.setText(items.getName());
            mPrice.setText(items.getPrice()+" ₹");
            mItemRating.setText(items.getRating()+"");
            if(items.getRating()>3){
                mItemRatDesc.setText("very good");

            }else{
                mItemRatDesc.setText("Good");
            }
            mItemDesc.setText(items.getDescription());

        }







        mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,AddressActivity.class);
                if(feature!=null){
                    intent.putExtra("item",feature);
                }
                if(bestSell!=null){
                    intent.putExtra("item",bestSell);
                }
                if(items!=null){
                    intent.putExtra("item",items);
                }
                startActivity(intent);

            }
        });

    }
}