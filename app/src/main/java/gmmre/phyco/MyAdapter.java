package gmmre.phyco;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Base> mDataBase;
    public ArrayList<Base> mBase = new ArrayList<>();

    public MyAdapter(Context context,ArrayList<Base> modelArrayList) {
        this.context = context;
        this.mDataBase = modelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mDataBase.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataBase.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checkbox, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.tvMasalah = (TextView) convertView.findViewById(R.id.tvMasalah);
            holder.tvKode = (TextView) convertView.findViewById(R.id.tvKode);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMasalah.setText(mDataBase.get(position).getMasalah());
        holder.checkBox.setChecked(mDataBase.get(position).getSelected());

        holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tempview = (View) holder.checkBox.getTag(R.integer.btnplusview);
                TextView tvMasalah = (TextView) tempview.findViewById(R.id.tvMasalah);
                TextView tvKode = (TextView) tempview.findViewById(R.id.tvKode);
                Integer pos = (Integer) holder.checkBox.getTag();
                Toast.makeText(context, mDataBase.get(pos).getKode() + " dipilih", Toast.LENGTH_SHORT).show();

                if (mDataBase.get(pos).getSelected()) {
                    mDataBase.get(pos).setSelected(false);
                } else {
                    mDataBase.get(pos).setSelected(true);
                    mBase.add(mDataBase.get(pos));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        protected CheckBox checkBox;
        private TextView tvMasalah;
        private TextView tvKode;
    }
}
