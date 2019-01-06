package gmmre.phyco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputKatasandi;
    Button btnMasuk;

    private  FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private ProgressDialog mProgess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgess = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputKatasandi = (EditText) findViewById(R.id.inputKatasandi);

        btnMasuk = (Button) findViewById(R.id.btnMasuk);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLogin();
            }
        });


    }

    private void checkLogin() {

        String email = inputEmail.getText().toString().trim();
        String katasandi = inputKatasandi.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(katasandi)){

            mProgess.setMessage("Signing In");
            mProgess.show();

            mAuth.signInWithEmailAndPassword(email, katasandi).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgess.dismiss();
                    if (task.isSuccessful()){
                        finish();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    }else {
                        Toast.makeText(MainActivity.this, "Login Gagal", Toast.LENGTH_LONG).show();
                    }

                    mProgess.dismiss();

                }
            });



        }

    }

    public void btnDaftar(View view) {
        Intent intent = new Intent(MainActivity.this, DaftarActivity.class);
        startActivity(intent);
    }
}
