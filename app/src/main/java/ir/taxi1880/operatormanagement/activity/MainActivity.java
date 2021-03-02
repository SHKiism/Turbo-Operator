package ir.taxi1880.operatormanagement.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.adapter.MainViewPagerAdapter;
import ir.taxi1880.operatormanagement.app.Constant;
import ir.taxi1880.operatormanagement.app.DataHolder;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.dialog.GeneralDialog;
import ir.taxi1880.operatormanagement.fragment.AccountFragment;
import ir.taxi1880.operatormanagement.fragment.MessageFragment;
import ir.taxi1880.operatormanagement.fragment.NotificationFragment;
import ir.taxi1880.operatormanagement.helper.FragmentHelper;
import ir.taxi1880.operatormanagement.helper.KeyBoardHelper;
import ir.taxi1880.operatormanagement.helper.StringHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;

public class MainActivity extends AppCompatActivity implements NotificationFragment.RefreshNotificationCount {

    public static final String TAG = MainActivity.class.getSimpleName();
    Unbinder unbinder;
    MainViewPagerAdapter mainViewPagerAdapter;
    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.vpMain)
    ViewPager2 vpMain;

    @BindView(R.id.tabMain)
    TabLayout tabLayout;

    @BindView(R.id.txtBadgeCount)
    TextView txtBadgeCount;

    @OnClick(R.id.imgNotification)
    void onNotification() {
        FragmentHelper
                .toFragment(MyApplication.currentActivity, new NotificationFragment())
                .replace();
    }

    @OnClick(R.id.imgMessage)
    void onMessage() {
        FragmentHelper
                .toFragment(MyApplication.currentActivity, new MessageFragment())
                .replace();
    }

    @OnClick(R.id.imgProfile)
    void onProfile(){
        FragmentHelper
                .toFragment(MyApplication.currentActivity, new AccountFragment())
                .replace();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        MyApplication.configureAccount();
        unbinder = ButterKnife.bind(this, view);
        TypefaceUtil.overrideFonts(view);

        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        vpMain.setAdapter(mainViewPagerAdapter);

        new TabLayoutMediator(tabLayout, vpMain, (tab, position) -> {
            tab.setCustomView(mainViewPagerAdapter.getTabView(position));
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewPagerAdapter.setSelectView(tabLayout, tab.getPosition(), "select");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mainViewPagerAdapter.setSelectView(tabLayout, tab.getPosition(), "unSelect");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.currentActivity = this;
        MyApplication.prefManager.setAppRun(true);

        if (MyApplication.prefManager.getCountNotification() == 0) {
            txtBadgeCount.setVisibility(View.GONE);
        } else {
            String message = " شما " + MyApplication.prefManager.getCountNotification() + " اطلاعیه جدید دارید. ";
            new GeneralDialog()
                    .title("هشدار")
                    .message(message)
                    .firstButton("باشه", () -> FragmentHelper.toFragment(MyApplication.currentActivity, new NotificationFragment()).replace())
                    .cancelable(false)
                    .show();
            txtBadgeCount.setVisibility(View.VISIBLE);
            txtBadgeCount.setText(StringHelper.toPersianDigits(MyApplication.prefManager.getCountNotification() + ""));
        }

        if (DataHolder.getInstance().getPushType() != null) {
            if (DataHolder.getInstance().getPushType().equals(Constant.PUSH_NOTIFICATION_MESSAGE_TYPE)) {
                FragmentHelper.toFragment(this, new MessageFragment())
                        .replace();
                DataHolder.getInstance().setPushType(null);
            } else if (DataHolder.getInstance().getPushType().equals(Constant.PUSH_NOTIFICATION_ANNOUNCEMENT_TYPE)) {
                FragmentHelper.toFragment(this, new NotificationFragment())
                        .replace();
                DataHolder.getInstance().setPushType(null);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.prefManager.setAppRun(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.currentActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {

        try {
            KeyBoardHelper.hideKeyboard();
            if (getFragmentManager().getBackStackEntryCount() > 0 || getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                if (doubleBackToExitPressedOnce) {
                    MyApplication.prefManager.setStartGettingAddress(false);
                    finish();
                } else {
                    this.doubleBackToExitPressedOnce = true;
                    MyApplication.Toast(getString(R.string.txt_please_for_exit_reenter_back), Toast.LENGTH_SHORT);
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AvaCrashReporter.send(e, "MainActivity class, onBackPressed method");
        }
    }

    @Override
    public void refreshNotification() {
        if (MyApplication.prefManager.getCountNotification() == 0) {
            txtBadgeCount.setVisibility(View.GONE);
        } else {
            txtBadgeCount.setVisibility(View.VISIBLE);
            txtBadgeCount.setText(StringHelper.toPersianDigits(MyApplication.prefManager.getCountNotification() + ""));
        }
    }
}
