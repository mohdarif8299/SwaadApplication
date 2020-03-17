package com.restaurent.swaadapplication;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class HomeFragment extends Fragment {
    private List<Items> list;
    RecyclerView recyclerView,recyclerView1;
    SwipeRefreshLayout swipeRefreshLayout;
    private List<Categroy_Items> category_images;
    private LinearLayout search_layout;
    private UserAccount userAccount;
    private TextView textView;
    int i= 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_home, container, false);
        list = new ArrayList<>();
        recyclerView =view.findViewById(R.id.recyclerView);
        recyclerView1 =view.findViewById(R.id.recyclerView1);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        category_images = new ArrayList<>();
        textView = view.findViewById(R.id.welcome_name);
     try {
         FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
         DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getUid());
         databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 userAccount = dataSnapshot.child("userAccount").getValue(UserAccount.class);
                 if (userAccount == null) {
                     return;
                 } else {
                     PersonalDetails personalDetails = userAccount.getPersonalDetails();
                     String userName = personalDetails.getName();
                     textView.setText("Welcome "+userName);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) { }
         });
     }
     catch(Exception e) {}
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference("MainCategoryImages");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                  String category_name = dataSnapshot1.getKey();
                  String category_image = dataSnapshot1.getValue(String.class);
                  Categroy_Items categroy_items = new Categroy_Items(category_image,category_name);
                  category_images.add(categroy_items);
                    Category_Adapter adapter = new Category_Adapter(category_images,getActivity());
                    recyclerView1.setHasFixedSize(true);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
                    recyclerView1.setAdapter(adapter);
                    recyclerView1.setNestedScrollingEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        FirebaseDatabase firebaseDatabase12 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference12 = firebaseDatabase12.getReference("home");
        databaseReference12.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Items myItem =  dataSnapshot.child("item"+i).getValue(Items.class);
//                    Items item1 = new Items(myItem.getItem_name(),myItem.getItem_category(),myItem.getItem_price(),myItem.getItem_image());
                    list.add(myItem);
                    ItemsAdapter adapter = new ItemsAdapter(list,getActivity());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(false);
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        swipeRefreshLayout.setOnRefreshListener(()->{
            Collections.shuffle(list);
            ItemsAdapter adapter1 = new ItemsAdapter(list,getActivity());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter1);
            recyclerView.setNestedScrollingEnabled(false);
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }
}
