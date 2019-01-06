package gmmre.phyco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    private ProgressDialog mProgess;

    private CardView mProfil, mKonsultasi, mRiwayat, mPengaturan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mPengaturan = findViewById(R.id.menuPengaturan);
        mProfil = findViewById(R.id.menuProfil);
        mKonsultasi = findViewById(R.id.menuKonsultasi);
        mRiwayat = findViewById(R.id.menuRiwayat);

        mPengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PengaturanActivity.class);
                startActivity(intent);
            }
        });

        mProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        mRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RiwayatKonsultasiActivity.class);
                startActivity(intent);
            }
        });

        mKonsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TambahPasienActivity.class);
                startActivity(intent);
            }
        });

    }

/*    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListner);

    }*/



/*    private void logout() {
        mAuth.signOut();

    }*/

}
