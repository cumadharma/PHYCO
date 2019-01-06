package gmmre.phyco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RiwayatKonsultasiActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    ArrayList<ListRiwayatKonsultasi> list;
    AdapterRiwayatKonsultasi adapterRiwayatKonsultasi;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseRecyclerAdapter adapter;
    private ImageView mImage;



    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_konsultasi);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mAuth = FirebaseAuth.getInstance();


        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ListRiwayatKonsultasi>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Hasil");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    ListRiwayatKonsultasi p = dataSnapshot1.getValue(ListRiwayatKonsultasi.class);
                    list.add(p);
                }
                adapterRiwayatKonsultasi = new AdapterRiwayatKonsultasi(RiwayatKonsultasiActivity.this, list);
                Collections.reverse(list);
                recyclerView.setAdapter(adapterRiwayatKonsultasi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RiwayatKonsultasiActivity.this, "Salah", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void backHome(View view) {
        Intent intent = new Intent(RiwayatKonsultasiActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
