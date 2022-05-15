package vn.edu.ptithcm.bankmanagement.ui.statistic;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.ptithcm.bankmanagement.R;

public class StatisticActivity extends AppCompatActivity {

    SectionsPagerAdapter pagerAdapter;
    ViewPager2 pager;
    public String[] tabTitles = {"Tháng", "Tuần", "Ngày"};
    boolean runResume = false;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        findViewById(R.id.b_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
    }
}