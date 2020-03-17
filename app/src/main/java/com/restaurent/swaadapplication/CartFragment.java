package com.restaurent.swaadapplication;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class CartFragment extends Fragment {
    private List<Cart_Items> list;
    private RecyclerView recyclerView;
    private Button order;
    private AlertDialog alertDialog1;
    private UserAccount userAccount;
    private String[] values = {"SELECT FROM EXISTING ADDRESS ","ADD NEW ADDRESS"};
    static TextView price,finalPrice,viewTotalPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);
        price = view.findViewById(R.id.price);
        finalPrice = view.findViewById(R.id.final_price);
        viewTotalPrice = view.findViewById(R.id.view_total_price);
        order = view.findViewById(R.id.order);
        list = new ArrayList<>();
        recyclerView =view.findViewById(R.id.recyclerView);
           try {
               FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
               DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("userAccount").child("cartItems");
               databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                           Cart_Items cart_items = dataSnapshot1.getValue(Cart_Items.class);
                           list.add(cart_items);
                           Cart_ItemAdapter adapter = new Cart_ItemAdapter(list, getActivity());
                           recyclerView.setHasFixedSize(true);
                           recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                           recyclerView.setAdapter(adapter);
                           recyclerView.setNestedScrollingEnabled(false);
                       }
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {}});
           }
           catch(Exception e){}
        order.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("MANAGE ADDRESS");
            builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch(item)
                    {
                        case 0:
                            Toast.makeText(getContext(), "First Item Clicked", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(getContext(), "Second Item Clicked", Toast.LENGTH_LONG).show();
                            break;
                    }
                    alertDialog1.dismiss();
                }
            });
            alertDialog1 = builder.create();
            alertDialog1.show();
        });
        return view;
    }
}
