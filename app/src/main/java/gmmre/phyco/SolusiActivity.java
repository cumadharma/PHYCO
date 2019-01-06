package gmmre.phyco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gmmre.phyco.Pasien;

public class SolusiActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseHasil;
    private ListView listViewHasil;
    private TextView mSolusi;

    List<Pasien> pasienList;
    List<Solusi> solusiList;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solusi);

        Intent mainIntent = getIntent();
        final String solusi = mainIntent.getStringExtra("solusiPasien");


        mSolusi = findViewById(R.id.tvSolusi);
        mSolusi.setText(solusi);

    }

    public void selesaiSolusi(View view) {
        Intent intent = new Intent(SolusiActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void konsultasikanKembali(View view) {
        Intent intent = new Intent(SolusiActivity.this, TambahPasienActivity.class);
        startActivity(intent);
    }
}
