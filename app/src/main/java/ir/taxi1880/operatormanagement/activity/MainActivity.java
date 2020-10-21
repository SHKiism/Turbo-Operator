package ir.taxi1880.operatormanagement.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.Constant;
import ir.taxi1880.operatormanagement.app.DataHolder;
import ir.taxi1880.operatormanagement.app.EndPoints;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.dialog.GeneralDialog;
import ir.taxi1880.operatormanagement.fragment.AccountFragment;
import ir.taxi1880.operatormanagement.fragment.MessageFragment;
import ir.taxi1880.operatormanagement.fragment.NotificationFragment;
import ir.taxi1880.operatormanagement.fragment.DeterminationPageFragment;
import ir.taxi1880.operatormanagement.fragment.ScoresFragment;
import ir.taxi1880.operatormanagement.fragment.ShiftFragment;
import ir.taxi1880.operatormanagement.helper.FragmentHelper;
import ir.taxi1880.operatormanagement.helper.StringHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.okHttp.RequestHelper;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;

public class MainActivity extends AppCompatActivity implements NotificationFragment.RefreshNotificationCount {

  public static final String TAG = MainActivity.class.getSimpleName();
  Unbinder unbinder;
  boolean doubleBackToExitPressedOnce = false;

  @OnClick(R.id.llNotification)
  void onNotification() {
    FragmentHelper
            .toFragment(MyApplication.currentActivity, new NotificationFragment())
            .replace();
  }

  @OnClick(R.id.llDeterminationPage)
  void onRange() {
    if (MyApplication.prefManager.getAccessStationDeterminationPage() == 0) {
      new GeneralDialog()
              .title("هشدار")
              .message("شما اجازه دسترسی به این بخش از برنامه را ندارید")
              .firstButton("باشه", null)
              .show();
    } else {
      FragmentHelper
              .toFragment(MyApplication.currentActivity, new DeterminationPageFragment())
              .replace();
    }
  }

  @OnClick(R.id.llShifts)
  void onShifts() {
    FragmentHelper
            .toFragment(MyApplication.currentActivity, new ShiftFragment())
            .setAddToBackStack(true)
            .replace();
  }

  @OnClick(R.id.llTripRegister)
  void onTripRegister() {

    if (MyApplication.prefManager.getAccessInsertService() == 0) {
      new GeneralDialog()
              .title("هشدار")
              .message("شما اجازه دسترسی به این بخش از برنامه را ندارید")
              .firstButton("باشه", null)
              .show();
    } else {
      startActivity(new Intent(MyApplication.context, TripRegisterActivity.class));
      finish();
    }
  }

  @OnClick(R.id.llScores)
  void onScores() {
    FragmentHelper
            .toFragment(MyApplication.currentActivity, new ScoresFragment())
            .replace();
  }

  @OnClick(R.id.llMessage)
  void onMessage() {
    FragmentHelper
            .toFragment(MyApplication.currentActivity, new MessageFragment())
            .replace();
  }

  @BindView(R.id.txtBadgeCount)
  TextView txtBadgeCount;

  @BindView(R.id.txtRequestCount)
  TextView txtRequestCount;

  @BindView(R.id.txtOperatorName)
  TextView txtOperatorName;

  @BindView(R.id.txtOperatorCharge)
  TextView txtOperatorCharge;

  @BindView(R.id.vfBalance)
  ViewFlipper vfBalance;

  @OnClick(R.id.llProfile)
  void onPressProfile() {

    FragmentHelper
            .toFragment(MyApplication.currentActivity, new AccountFragment())
            .replace();

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    View view = getWindow().getDecorView();
    getSupportActionBar().hide();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryLighter));
      window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
      window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    MyApplication.configureAccount();

    unbinder = ButterKnife.bind(this, view);
    TypefaceUtil.overrideFonts(view);

    if (MyApplication.prefManager.getCountNotification() == 0) {
      txtBadgeCount.setVisibility(View.GONE);
    } else {
      txtBadgeCount.setVisibility(View.VISIBLE);
      txtBadgeCount.setText(StringHelper.toPersianDigits(MyApplication.prefManager.getCountNotification() + ""));
    }

    if (MyApplication.prefManager.getCountRequest() == 0) {
      txtRequestCount.setVisibility(View.GONE);
    } else {
      txtRequestCount.setVisibility(View.VISIBLE);
      txtRequestCount.setText(StringHelper.toPersianDigits(MyApplication.prefManager.getCountRequest() + ""));
    }

    txtOperatorName.setText(MyApplication.prefManager.getOperatorName());
//    txtOperatorCharge.setText(StringHelper.toPersianDigits(MyApplication.prefManager.getBalance() + " تومان "));

    /*TODO :this is for test*/
//    new NotificationHelper()
//            .channelId("elahe")
//            .channelName("elahe")
//            .notificationId(123)
//            .autoCancel(true)
//            .notificationMessage(R.id.txtValue, "elahe")
//            .collapsedView(R.layout.notification_collapsed)
//            .clickableView(R.id.linearNotif)
//            .startActivity("TripRegisterActivity")
//            .smallIcon(R.drawable.ic_baseline_message_24)
//            .show();
  }

  private void getBalance(int userId) {
    if (vfBalance != null)
      vfBalance.setDisplayedChild(0);

    RequestHelper.builder(EndPoints.BALANCE)
            .addPath(userId + "")
            .listener(getBalance)
            .get();

  }

  RequestHelper.Callback getBalance = new RequestHelper.Callback() {
    @Override
    public void onResponse(Runnable reCall, Object... args) {
      MyApplication.handler.post(new Runnable() {
        @Override
        public void run() {
          try {
            Log.i(TAG, "run: " + args[0].toString());
            JSONObject obj = new JSONObject(args[0].toString());
            boolean success = obj.getBoolean("success");
            String message = obj.getString("message");
            JSONObject dataObj = obj.getJSONObject("data");
            int accountBalance = dataObj.getInt("accountBalance");

            if (txtOperatorCharge != null)
              txtOperatorCharge.setText(StringHelper.toPersianDigits(accountBalance + " تومان "));
            if (vfBalance != null)
              vfBalance.setDisplayedChild(1);

          } catch (Exception e) {
            e.printStackTrace();
            AvaCrashReporter.send(e, "MainActivity class, getBalance onResponse method");
          }
        }
      });
    }

    @Override
    public void onFailure(Runnable reCall, Exception e) {
      MyApplication.handler.post(() -> {
        if (vfBalance != null)
          vfBalance.setDisplayedChild(1);
      });
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    MyApplication.currentActivity = this;
    MyApplication.prefManager.setAppRun(true);
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

    getBalance(MyApplication.prefManager.getUserCode());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  @Override
  public void onBackPressed() {

    try {
      if (getFragmentManager().getBackStackEntryCount() > 0 || getSupportFragmentManager().getBackStackEntryCount() > 0) {
        super.onBackPressed();
      } else {
        if (doubleBackToExitPressedOnce) {
//          super.onBackPressed();
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
