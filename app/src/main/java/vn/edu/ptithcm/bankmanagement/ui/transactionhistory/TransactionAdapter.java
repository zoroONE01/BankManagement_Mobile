package vn.edu.ptithcm.bankmanagement.ui.transactionhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.utility.Helper;

public class TransactionAdapter extends ArrayAdapter<ThongKeGD> {
    Context context;
    List<ThongKeGD> objects = new ArrayList<>();
    List<ThongKeGD> filteredObjects = new ArrayList<>();

    public TransactionAdapter(@NonNull Context context, @NonNull List<ThongKeGD> objects) {
        super(context, 0, objects);
        this.context = context;

        setList(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_transaction_item_view, parent, false);
        }

        TextView transTv = convertView.findViewById(R.id.transactionTv);
        TextView amountTv = convertView.findViewById(R.id.soTien);
        TextView ngayTv = convertView.findViewById(R.id.ngayGd);

        ThongKeGD gd = filteredObjects.get(position);

        transTv.setText(gd.getLoaiGD());
        amountTv.setText(Helper.showGia(gd.getSoTien()));
        ngayTv.setText(Helper.getNgayFromEpoch(gd.getNgayGD()));

        return convertView;
    }

    public void setList(List<ThongKeGD> list) {
        objects.clear();
        filteredObjects.clear();

        objects.addAll(list);
        filteredObjects.addAll(list);

        notifyDataSetChanged();
    }
}