package vn.edu.ptithcm.bankmanagement.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.ptithcm.bankmanagement.R;

public class RecentTransactionViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTransactionTime, tvTransactionValue, tvTransactionTitle;
    public ImageView isUserImage;

    public RecentTransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        isUserImage = itemView.findViewById(R.id.iv_item_recent_transaction_img);
        tvTransactionTime = itemView.findViewById(R.id.tv_item_recent_transaction_description);
        tvTransactionValue = itemView.findViewById(R.id.tv_item_recent_transaction_value);
        tvTransactionTitle = itemView.findViewById(R.id.tv_item_recent_transaction_title);
    }
}
