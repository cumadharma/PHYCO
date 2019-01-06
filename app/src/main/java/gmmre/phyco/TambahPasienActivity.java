package gmmre.phyco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahPasienActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabasePasien;
    EditText mNamaPasien, mPekerjaanPasien, mUsiaPasien;
    Button mTambah;
    private RadioGroup mGender;
    private RadioButton mGenderOption;
    ProgressDialog mProgress;

    FirebaseUser user;
    String uid;
    String gender;
    String pasienID;
    String userID;
    String namaPasien;
    String usiaPasien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pasien);

        mNamaPasien = (EditText) findViewById(R.id.inputNamaPasien);
        mGender = findViewById(R.id.rb_kelamin);
        mUsiaPasien = (EditText) findViewById(R.id.inputUsiaPasien);
        mPekerjaanPasien = (EditText) findViewById(R.id.inputPerkejaanPasien);
        mTambah = (Button) findViewById(R.id.btnTambahPasien);

        mTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahPasien();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        userID = uid;
        mAuth = FirebaseAuth.getInstance();
        mDatabasePasien = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pasien");
        mProgress = new ProgressDialog(this);


        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                mGenderOption = mGender.findViewById(i);
                switch (i) {
                    case R.id.rbPria:
                        gender = mGenderOption.getText().toString();
                        break;
                    case R.id.rbWanita:
                        gender = mGenderOption.getText().toString();
                        break;
                    default:
                }
            }
        });
    }

    private void tambahPasien() {
        final String nama = mNamaPasien.getText().toString().trim();
        final String pekerjaan = mPekerjaanPasien.getText().toString().trim();
        final String usia =  mUsiaPasien.getText().toString().trim();

        namaPasien = nama;
        usiaPasien = usia;

        mProgress.setMessage("Menambahkan Pasien");
        mProgress.show();
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(pekerjaan) && !TextUtils.isEmpty(usia)) {



            String key = mDatabasePasien.child("Pasien").push().getKey();
            pasienID = key;




            DatabaseReference cureent_user_db = mDatabasePasien.child(key);

            cureent_user_db.child("nama").setValue(nama);
            cureent_user_db.child("jeniskelamin").setValue(gender);
            cureent_user_db.child("pekerjaan").setValue(pekerjaan);
            cureent_user_db.child("usia").setValue(usia);

            mProgress.dismiss();

            Intent intent = new Intent(this, KonsultasiPasienActivity.class);
            intent.putExtra("keyPasien", pasienID);
            intent.putExtra("keyUser", userID);
            intent.putExtra("nmPasien", namaPasien);
            intent.putExtra("usPasien", usiaPasien);
            startActivity(intent);


        } else {
            Toast.makeText(TambahPasienActivity.this, "Gagal Menambahkan Pasien", Toast.LENGTH_LONG).show();
            mProgress.dismiss();
        }


    }

}
