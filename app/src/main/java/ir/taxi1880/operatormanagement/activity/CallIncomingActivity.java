package ir.taxi1880.operatormanagement.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.tools.Log;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.services.LinphoneService;
import ir.taxi1880.operatormanagement.services.LinphoneUtils;
public class CallIncomingActivity extends AppCompatActivity {
  private CoreListenerStub mListener;
  private Call mCall;

  @OnClick(R.id.btnAccept)
  void onAcceptPress() {
//    CallParams params = LinphoneService.getCore().createCallParams(LinphoneService.getInstance().getCall());
//    LinphoneService.getInstance().getCall().acceptWithParams(params);
    acceptCall(mCall);
  }

  Unbinder unbinder;

  @Override
  protected void onResume() {
    Core core = LinphoneService.getCore();
    if (core != null) {
      core.addListener(mListener);
    }

    super.onResume();

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_call_incoming);
    View view = getWindow().getDecorView();
    getSupportActionBar().hide();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryLighter));
      window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
      window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    unbinder = ButterKnife.bind(this);

    mListener =
            new CoreListenerStub() {
              @Override
              public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                mCall = call;
                if (state == Call.State.End || state == Call.State.Released) {
                  mCall = null;
                  finish();
                }
              }
            };


  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }


  @Override
  protected void onPause() {
    Core core = LinphoneService.getCore();
    if (core != null) {
      core.removeListener(mListener);
    }
    super.onPause();
  }


  public boolean acceptCall(Call call) {

    android.util.Log.i("LOG", "acceptCall ");
    if (call == null) return false;

    Core core = LinphoneService.getCore();
    CallParams params = core.createCallParams(call);

    boolean isLowBandwidthConnection =
            !LinphoneUtils.isHighBandwidthConnection(MyApplication.context);

    if (params != null) {
      params.enableLowBandwidth(isLowBandwidthConnection);
    } else {
      Log.e("[Call Manager] Could not create call params for call");
      return false;
    }

    call.acceptWithParams(params);
    return true;
  }


}
