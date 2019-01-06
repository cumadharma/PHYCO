package gmmre.phyco;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import gmmre.phyco.R;
import gmmre.phyco.Hasil;

public class Informasi extends ArrayAdapter<Hasil> {
    private Activity context;
    private List<Hasil> hasilList;

    public Informasi(Activity context, List<Hasil> hasilList){
        super(context, R.layout.list, hasilList);
        this.context = context;
        this.hasilList = hasilList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list,null,true);

        TextView tvSolusi = (TextView) listViewItem.findViewById(R.id.tvSolusi);
        TextView tvTanggal = (TextView) listViewItem.findViewById(R.id.tvTanggal);

        Hasil hasil = hasilList.get(position);

        tvSolusi.setText(hasil.getSolusi());
        tvTanggal.setText(hasil.getTanggal());
        tvTanggal.setText(hasil.getJam());

        return listViewItem;
    }
}
