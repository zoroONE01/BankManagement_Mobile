package vn.edu.ptithcm.bankmanagement.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.utility.Helper;

public class RecentTransactionAdapter extends RecyclerView.Adapter<RecentTransactionViewHolder> {
    Context context;
    private List<ThongKeGD> items;

    public RecentTransactionAdapter(List<ThongKeGD> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecentTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item_recent_transaction, parent, false);
        return new RecentTransactionViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTransactionViewHolder holder, int position) {
        ImageSwitcher isUserImage = holder.isUserImage;
        TextView tvTransactionTime = holder.tvTransactionTime;
        TextView tvTransactionValue = holder.tvTransactionValue;
        TextView tvTransactionTitle = holder.tvTransactionTitle;

        ThongKeGD tk = items.get(position);

        tvTransactionTitle.setText(tk.getLoaiGD());
        tvTransactionValue.setText(String.valueOf(Helper.showGia(tk.getSoTien()) + "đ"));
        tvTransactionTime.setText(Helper.getNgayFromEpoch(tk.getNgayGD()));

//        switch (items.get(position).getLoaiGD()){
//            case "Chuyển tiền":
//                isUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tranfer));
//                break;
//            case "Gửi tiền":
//                isUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_recharge));
//
//                break;
//            case "Rút tiền":
//                isUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_withdraw));
//                break;
//        }
//        holder.itemView.setOnClickListener(view -> { //Todo: click vào chuyển qua chi tiết giao dịch
//        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

}