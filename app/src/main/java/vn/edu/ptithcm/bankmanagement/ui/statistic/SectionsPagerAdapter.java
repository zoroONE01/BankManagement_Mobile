package vn.edu.ptithcm.bankmanagement.ui.statistic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionsPagerAdapter extends FragmentStateAdapter {
    public SectionsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StatisticFragment(0);
            case 1:
                return new StatisticFragment(1);
            case 2:
                return new StatisticFragment(2);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
