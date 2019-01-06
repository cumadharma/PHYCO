package gmmre.phyco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PengaturanActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    private ProgressDialog mProgess;

    Button mTentang, mKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        mKeluar = findViewById(R.id.btnKeluar);

        mKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }

        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {




            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {


                    Intent loginIntent = new Intent(PengaturanActivity.this, MainActivity.class);
                    startActivity(loginIntent);

                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListner);

    }



    private void logout() {
        mAuth.signOut();

    }
}
