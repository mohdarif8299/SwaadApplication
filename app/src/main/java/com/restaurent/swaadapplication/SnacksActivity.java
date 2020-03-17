package com.restaurent.swaadapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.danimahardhika.cafebar.CafeBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class SnacksActivity extends AppCompatActivity {
    private TextView item_name,item_category,item_price,item_quantity,item_status,category,title;
    private ImageView item_image,decrement,increment,back_button;
    private RelativeLayout addToCart,cartControl;
    private LinearLayout item_type,item_type2;
    private List<Items> list;
    private RecyclerView recyclerView;
    int i=0;
    private int previousQuantity=1,setCounter=1,counter=1,itemPrice=0,totalPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);
        Intent intent = getIntent();
        String set_item_name = intent.getStringExtra("item_name");
        String set_item_category = intent.getStringExtra("item_category");
        String set_item_image = intent.getStringExtra("item_image");
        String set_item_type = intent.getStringExtra("item_type");
        String set_item_status = intent.getStringExtra("item_status");
        int set_price = intent.getIntExtra("item_price",0);
        list = new ArrayList<>();
        recyclerView =findViewById(R.id.recyclerView);
        addToCart = findViewById(R.id.add_to_cart);
        cartControl = findViewById(R.id.cart_control);
        item_name = findViewById(R.id.item_name);
        title = findViewById(R.id.title);
        back_button = findViewById(R.id.back_button);
        item_category = findViewById(R.id.item_category);
//        item_status = findViewById(R.id.item_status);
        item_type = findViewById(R.id.item_type);
        item_type2 = findViewById(R.id.item_type2);
        item_image = findViewById(R.id.item_image);
        category = findViewById(R.id.category);
        item_price = findViewById(R.id.item_price);
        item_quantity = findViewById(R.id.item_quantity);
        decrement = findViewById(R.id.decrement);
        increment = findViewById(R.id.increment);
        item_name.setText(set_item_name);
        title.setText(set_item_name);
        item_category.setText(set_item_category);
        item_price.setText("\u20B9 "+set_price);
        if(Objects.equals(set_item_type,"veg")){
            item_type.setVisibility(View.VISIBLE);
            item_type2.setVisibility(View.GONE);
        }
        else {
            item_type.setVisibility(View.GONE);
            item_type2.setVisibility(View.VISIBLE);
        }
        back_button.setOnClickListener(v-> {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        });
        Glide.with(getApplicationContext()).load(set_item_image).fitCenter().centerCrop().into(item_image);
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(set_item_name);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    previousQuantity = dataSnapshot.child("quantity").getValue(Integer.class);
                    counter = previousQuantity;
                    cartControl.setVisibility(View.VISIBLE);
                    addToCart.setVisibility(View.GONE);
                    item_quantity.setText(""+previousQuantity);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
        addToCart.setOnClickListener(v-> {
            if(checkIsLoggedOrNot()) {
                cartControl.setVisibility(View.VISIBLE);
                addToCart.setVisibility(View.GONE);
                item_quantity.setText("" + setCounter);
                Cart_Items cart_items = new Cart_Items(set_item_name, set_item_category, "added", set_item_image, 1, set_price, set_price);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child("userAccount").child("cartItems").child(cart_items.getItem_name()).setValue(cart_items);
                CafeBar.make(SnacksActivity.this, "Items added to cart", CafeBar.Duration.INDEFINITE)
                        .setAction("Go to cart", Color.parseColor("#03A9F4"), cafe -> {
                            cafe.dismiss();
                        })
                        .show();
            }
            else logInFirst();
        });
        increment.setOnClickListener(inc->{
            if(checkIsLoggedOrNot()) {
                if (counter < 2 || counter > 10) {
                }
                counter++;
                item_quantity.setText("" + counter);
                totalPrice = counter * set_price;
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(set_item_name).child("quantity").setValue(counter);
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(set_item_name).child("totalPrice").setValue(totalPrice);
            }
            else logInFirst();
        });
        decrement.setOnClickListener(dec->{
                    if(checkIsLoggedOrNot()) {
                        if(counter<=1) {
                            cartControl.setVisibility(View.GONE);
                            addToCart.setVisibility(View.VISIBLE);
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(set_item_name).removeValue();
                        }
                        else {
                            counter--;
                            item_quantity.setText(""+counter);
                            totalPrice = counter*set_price;
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(set_item_name).child("quantity").setValue(counter);
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(set_item_name).child("totalPrice").setValue(totalPrice);
                        }
                    }
                    else logInFirst();
                }
        );
        category.setText("Most popular in "+set_item_category);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(set_item_category);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Items myItem =  dataSnapshot1.getValue(Items.class);
                    Items item1 = new Items(myItem.getItem_name(),myItem.getItem_category(),myItem.getItem_image(),myItem.getItem_type(),myItem.getItem_status(),myItem.getItem_price());
                    list.add(item1);
                    ItemsAdapter adapter = new ItemsAdapter(list,SnacksActivity.this);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SnacksActivity.this));
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(false);
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    catch (Exception e){}
    }
    boolean checkIsLoggedOrNot(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser==null) return false;
        else return true;
    }
    void logInFirst() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
