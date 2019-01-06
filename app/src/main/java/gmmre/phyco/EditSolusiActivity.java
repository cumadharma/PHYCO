package gmmre.phyco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EditSolusiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_solusi);
    }

    public void ubahSolusiSelesai(View view) {
        Intent intent = new Intent(EditSolusiActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
