package vn.edu.ptithcm.bankmanagement.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.ptithcm.bankmanagement.R;
import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;
import vn.edu.ptithcm.bankmanagement.utility.Helper;

public class RecentTransactionAdapter extends RecyclerView.Adapter<RecentTransactionViewHolder> {
    private List<ThongKeGD> items;
    private Context context;

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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecentTransactionViewHolder holder, int position) {
        ImageView isUserImage = holder.isUserImage;
        TextView tvTransactionTime = holder.tvTransactionTime;
        TextView tvTransactionValue = holder.tvTransactionValue;
        TextView tvTransactionTitle = holder.tvTransactionTitle;

        ThongKeGD tk = items.get(position);

        String action = tk.getLoaiGD();

        if (action.equals("Gửi tiền")) {
            action = "Nạp tiền";
        }

        tvTransactionTitle.setText(action);
        tvTransactionValue.setText(String.valueOf(Helper.showGia(tk.getSoTien()) + "đ"));
        tvTransactionTime.setText(Helper.getNgayFromEpoch(tk.getNgayGD()));

        switch (items.get(position).getLoaiGD()){
            case "Chuyển tiền":
                isUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tranfer));
                break;
            case "Gửi tiền":
                isUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_recharge));
                break;
            case "Rút tiền":
                isUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_withdraw));
                break;
        }
//        holder.itemView.setOnClickListener(view -> {
//        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

}