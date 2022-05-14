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

public class RecentTransactionAdapter extends RecyclerView.Adapter<RecentTransactionViewHolder> {

    private List<ThongKeGD> items;

    public RecentTransactionAdapter(List<ThongKeGD> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecentTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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

        tvTransactionTitle.setText(items.get(position).getLoaiGD());
        tvTransactionValue.setText(String.valueOf(items.get(position).getSoTien())); //Todo: format lại tiền dùm e
        tvTransactionTime.setText(""); //Todo: ngay giao dich
        switch (items.get(position).getLoaiGD()){
            case "Chuyển tiền":
                isUserImage.setImageResource(R.drawable.ic_tranfer);
                break;
            case "Gửi tiền":
                isUserImage.setImageResource(R.drawable.ic_recharge);
                break;
            case "Rút tiền":
                isUserImage.setImageResource(R.drawable.ic_withdraw);
                break;
        }
//        holder.itemView.setOnClickListener(view -> { //Todo: click vào chuyển qua chi tiết giao dịch
//        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

}
