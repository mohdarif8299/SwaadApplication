package com.restaurent.swaadapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import static com.restaurent.swaadapplication.CartFragment.finalPrice;
import static com.restaurent.swaadapplication.CartFragment.price;
import static com.restaurent.swaadapplication.CartFragment.viewTotalPrice;
public class Cart_ItemAdapter extends RecyclerView.Adapter<Cart_ItemAdapter.ViewHolder> {
    private Context ctx;
    private List<Cart_Items> list;
    private int previousQuantity=1;
    int i=0;
    int final_price;
    int calculatePrice=0;
    public Cart_ItemAdapter(){}
    public Cart_ItemAdapter(List<Cart_Items> listFeed, Context context) {
        this.ctx = context;
        this.list = listFeed;
    }
    @NonNull
    @Override
    public Cart_ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cart_items, parent, false);
        Cart_ItemAdapter.ViewHolder viewHolder = new Cart_ItemAdapter.ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull Cart_ItemAdapter.ViewHolder holder, int position) {
        Cart_Items items = list.get(position);
        int counter[] = new int[list.size()];
        int totalPrice[] = new int[list.size()];
        Glide.with(ctx).load(items.getItem_image()).fitCenter().centerCrop().into(holder.item_image);
        holder.item_name.setText(items.getItem_name());
        holder.item_category.setText(items.getItem_category());
        holder.item_price.setText("\u20B9 "+items.getPrice());
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(items.getItem_name());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    previousQuantity = dataSnapshot.child("quantity").getValue(Integer.class);
                    totalPrice[position] = dataSnapshot.child("totalPrice").getValue(Integer.class);
                    counter[position] = previousQuantity;
                    final_price = totalPrice[position];
                    holder.counter.setText(""+previousQuantity);
                    price.setText("\u20B9 "+totalPrice[position]);
                    finalPrice.setText("\u20B9 "+(80+totalPrice[position]));
                    viewTotalPrice.setText("\u20B9 "+(80+totalPrice[position]));
                }
                catch (Exception e){ e.printStackTrace(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { databaseError.toException().printStackTrace(); }
        });
        holder.increment.setOnClickListener(v-> {
                    if (counter[position] < 2 || counter[position] > 10) {
                    }
                    counter[position]++;
                    items.setQuantity(counter[position]);
                    holder.counter.setText("" + items.getQuantity());
                    totalPrice[position] = counter[position] * items.getPrice();
                    final_price += totalPrice[position];
                    price.setText("\u20B9 " + totalPrice[position]);
                    finalPrice.setText("\u20B9 " + (80 + findTotalPrice()));
                    viewTotalPrice.setText("\u20B9 " + (80 + findTotalPrice()));
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(holder.item_name.getText().toString()).child("quantity").setValue(items.getQuantity());
                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(holder.item_name.getText().toString()).child("totalPrice").setValue(totalPrice[position]);
                });
            holder.decrement.setOnClickListener(v->{
            if(counter[position]<=1) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(holder.item_name.getText().toString()).removeValue();
                   new Cart_ItemAdapter();
            }
            else {
                counter[position]--;
                items.setQuantity(counter[position]);
                holder.counter.setText(""+items.getQuantity());
                totalPrice[position] = items.getPrice()*counter[position];
                final_price-=totalPrice[position];
                price.setText("\u20B9 "+totalPrice[position]);
                finalPrice.setText("\u20B9 "+(80+findTotalPrice()));
                viewTotalPrice.setText("\u20B9 "+(80+findTotalPrice()));
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(holder.item_name.getText().toString()).child("quantity").setValue(items.getQuantity());
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems").child(holder.item_name.getText().toString()).child("totalPrice").setValue(totalPrice[position]);
            }
        });
    }
    @Override
    public int getItemCount() { return list.size(); }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_image;
        public TextView item_name,item_category,item_price,counter;
        public LinearLayout linearLayout;
        public RelativeLayout control_cart;
        public ImageView increment,decrement;
        public ViewHolder(View itemView) {
            super(itemView);
            this.item_image = itemView.findViewById(R.id.item_image);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.item_category = itemView.findViewById(R.id.item_category);
            this.item_price = itemView.findViewById(R.id.item_price);
            this.linearLayout = itemView.findViewById(R.id.linearLayout);
            control_cart = itemView.findViewById(R.id.control_cart);
            counter = itemView.findViewById(R.id.counter);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);
        }
    }
 public int  findTotalPrice() {
     FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
     DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userAccount").child("cartItems");
     databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                 calculatePrice += dataSnapshot1.child("totalPrice").getValue(Integer.class);
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {
         }
     });
     return calculatePrice;
 }
}
