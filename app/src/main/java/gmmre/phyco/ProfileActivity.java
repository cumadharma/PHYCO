package gmmre.phyco;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView mNamaUser, mTglLahirUser, mPekerjaanUser, mGender;
    private  FirebaseAuth mAuth;
    private DatabaseReference mDatabase;;

    FirebaseUser user;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mAuth = FirebaseAuth.getInstance();

        mNamaUser = (TextView) findViewById(R.id.namaUser);
        mTglLahirUser = (TextView) findViewById(R.id.tglLahirUser);
        mPekerjaanUser = (TextView) findViewById(R.id.pekerjaanUser);
        mGender = (TextView) findViewById(R.id.genderUser);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String user_nama = dataSnapshot.child(uid).child("nama").getValue(String.class);
                    String user_tgl = dataSnapshot.child(uid).child("tanggal_lahir").getValue(String.class);
                    String user_gender = dataSnapshot.child(uid).child("jeniskelamin").getValue(String.class);
                    String user_pekerjaan = dataSnapshot.child(uid).child("pekerjaan").getValue(String.class);



                    mNamaUser.setText(user_nama);
                    mTglLahirUser.setText(user_tgl);
                    mPekerjaanUser.setText(user_pekerjaan);
                    mGender.setText(user_gender);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
