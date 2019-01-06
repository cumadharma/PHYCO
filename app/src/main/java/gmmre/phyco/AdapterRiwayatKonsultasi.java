package gmmre.phyco;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterRiwayatKonsultasi extends RecyclerView.Adapter<AdapterRiwayatKonsultasi.MyViewHolder> {

    Context context;
    ArrayList<ListRiwayatKonsultasi> ListRiwayatKonsultasi;
    public AdapterRiwayatKonsultasi(Context c, ArrayList<ListRiwayatKonsultasi> p) {
        context = c;
        ListRiwayatKonsultasi = p;
    }
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    FirebaseUser user;

    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    String uid;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.jam.setText(ListRiwayatKonsultasi.get(position).getJam());
        holder.tanggal.setText(ListRiwayatKonsultasi.get(position).getTanggal());
        holder.solusi.setText(ListRiwayatKonsultasi.get(position).getSolusi());
        holder.nama.setText(ListRiwayatKonsultasi.get(position).getNama());
        holder.usia.setText(ListRiwayatKonsultasi.get(position).getUsia());


    }

    @Override
    public int getItemCount() {
        return ListRiwayatKonsultasi.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView jam, tanggal, solusi, nama, usia;
        public MyViewHolder(View itemView) {
            super(itemView);
            jam = (TextView) itemView.findViewById(R.id.tvJam);
            tanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
            solusi = (TextView) itemView.findViewById(R.id.tvSolusi);
            nama = (TextView) itemView.findViewById(R.id.tvNama);
            usia = (TextView) itemView.findViewById(R.id.tvUsia);
        }


    }
}
