package gmmre.phyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DaftarActivity extends AppCompatActivity {

    private EditText inputNama, inputEmail, inputKatasandi, inputPekerjaan;
    private RadioGroup mGender;
    private RadioButton mGenderOption;
    Button btnDaftarDone;
    private ImageView mFotoUser;

    String gender;
    String umur;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    private static final String TAG = "DaftarActivity";


    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDataSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        inputNama = (EditText) findViewById(R.id.inputNama);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputKatasandi = (EditText) findViewById(R.id.inputKatasandi);
        mGender = findViewById(R.id.rb_kelamin);
        inputPekerjaan = (EditText) findViewById(R.id.inputPerkejaan);
        btnDaftarDone = (Button) findViewById(R.id.btnDaftarDone);
        mFotoUser = findViewById(R.id.fotoRegisterUser);

        btnDaftarDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

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




        // Pilih Tanggal Lahir
        mDisplayDate = (TextView) findViewById(R.id.tanggalLahir);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int tahun = cal.get(Calendar.YEAR);
                int bulan = cal.get(Calendar.MONTH);
                int tanggal = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DaftarActivity.this
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDataSetListener, tahun, bulan, tanggal);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int tahun, int bulan, int tanggal) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, tahun);
                cal.set(Calendar.MONTH, bulan);
                cal.set(Calendar.DAY_OF_MONTH, tanggal);
                String format = new SimpleDateFormat("dd MMM YYYY").format(cal.getTime());
                mDisplayDate.setText(format);
                umur = (Integer.toString(calculateAge(cal.getTimeInMillis())));
            }
        };

    }

    int calculateAge(long date) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();

        int umur = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            umur--;
        }

        return umur;
    }

    private void startRegister() {
        final String nama = inputNama.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String katasandi = inputKatasandi.getText().toString().trim();
        final String pekerjaan = inputPekerjaan.getText().toString().trim();
        final String tanggal_lahir = mDisplayDate.getText().toString().trim();

        if(!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(katasandi) && !TextUtils.isEmpty(pekerjaan)
                && !TextUtils.isEmpty(tanggal_lahir)) {

            mProgress.setMessage("Signing Up");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email,katasandi).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference cureent_user_db = mDatabase.child(user_id);

                        cureent_user_db.child("nama").setValue(nama);
                        cureent_user_db.child("email").setValue(email);
                        cureent_user_db.child("katasandi").setValue(katasandi);
                        cureent_user_db.child("jeniskelamin").setValue(gender);
                        cureent_user_db.child("pekerjaan").setValue(pekerjaan);
                        cureent_user_db.child("tanggal_lahir").setValue(tanggal_lahir);
                        cureent_user_db.child("usia").setValue(umur);

                        mProgress.dismiss();

                        Intent mainIntent = new Intent(DaftarActivity.this, HomeActivity.class);
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(DaftarActivity.this, "Pendaftaran Gagal", Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    }
                }
            });
        }
    }

}
