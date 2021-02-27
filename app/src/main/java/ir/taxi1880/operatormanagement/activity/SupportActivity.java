package ir.taxi1880.operatormanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.adapter.SupportViewPagerAdapter;
import ir.taxi1880.operatormanagement.app.Constant;
import ir.taxi1880.operatormanagement.app.DataHolder;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.dialog.GeneralDialog;
import ir.taxi1880.operatormanagement.fragment.MessageFragment;
import ir.taxi1880.operatormanagement.fragment.NotificationFragment;
import ir.taxi1880.operatormanagement.helper.FragmentHelper;
import ir.taxi1880.operatormanagement.helper.KeyBoardHelper;
import ir.taxi1880.operatormanagement.helper.PhoneNumberValidation;
import ir.taxi1880.operatormanagement.helper.StringHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;
import ir.taxi1880.operatormanagement.services.LinphoneService;

public class SupportActivity extends AppCompatActivity {
    public static final String TAG = SupportActivity.class.getSimpleName();
    Unbinder unbinder;
    SupportViewPagerAdapter supportViewPagerAdapter;
    RipplePulseLayout mRipplePulseLayout;
    private Runnable mCallQualityUpdater = null;
    private int mDisplayedQuality = -1;
    public static boolean supportActivityIsRunning = false;
    String voipId = "0";
    Core core;
    Call call;

    @BindView(R.id.vpSupport)
    ViewPager2 vpSupport;

    @BindView(R.id.tbLayout)
    TabLayout tbLayout;

    @OnClick(R.id.imgBack)
    void onBack() {
        MyApplication.currentActivity.onBackPressed();
    }

    @BindView(R.id.btnActivate)
    Button btnActivate;

    @BindView(R.id.btnDeActivate)
    Button btnDeActivate;

    @OnClick(R.id.btnActivate)
    void onActivePress() {
        KeyBoardHelper.hideKeyboard();
        new GeneralDialog()
                .title("هشدار")
                .cancelable(false)
                .message("مطمئنی میخوای وارد صف بشی؟")
                .firstButton("مطمئنم", () -> {
                    setActivate();
//                MyApplication.Toast("activated",Toast.LENGTH_SHORT);
                })
                .secondButton("نیستم", null)
                .show();

    }

    @OnClick(R.id.btnDeActivate)
    void onDeActivePress() {
        KeyBoardHelper.hideKeyboard();
        new GeneralDialog()
                .title("هشدار")
                .cancelable(false)
                .message("مطمئنی میخوای خارج بشی؟")
                .firstButton("مطمئنم", () -> {
                    if (MyApplication.prefManager.isCallIncoming()) {
                        MyApplication.Toast(getString(R.string.exit), Toast.LENGTH_SHORT);
                    } else {
                        setDeActivate();
                    }
                })
                .secondButton("نیستم", null)
                .show();
    }

    @BindView(R.id.rlNewInComingCall)
    RelativeLayout rlNewInComingCall;

    @BindView(R.id.rlActionBar)
    RelativeLayout rlActionBar;

    @BindView(R.id.txtCallerNum)
    TextView txtCallerNum;

    @BindView(R.id.imgEndCall)
    ImageView imgEndCall;

    @BindView(R.id.imgCallQuality)
    ImageView imgCallQuality;

    @OnClick(R.id.imgAccept)
    void onAcceptPress() {
        call = core.getCurrentCall();
        Call[] calls = core.getCalls();
        int i = calls.length;
        Log.i(TAG, "onRejectPress: " + i);
        if (call != null) {
            call.accept();
//      if (getMobileNumber().isEmpty() && isTellValidable)
//        MyApplication.handler.postDelayed(() -> onPressDownload(), 400);
        } else if (calls.length > 0) {
            calls[0].accept();
        }
    }

    @OnClick(R.id.imgReject)
    void onRejectPress() {
        Core mCore = LinphoneService.getCore();
        Call currentCall = mCore.getCurrentCall();
        for (Call call : mCore.getCalls()) {
            if (call != null && call.getConference() != null) {
//        if (mCore.isInConference()) {
//          displayConferenceCall(call);
//          conferenceDisplayed = true;
//        } else if (!pausedConferenceDisplayed) {
//          displayPausedConference();
//          pausedConferenceDisplayed = true;
//        }
            } else if (call != null && call != currentCall) {
                Call.State state = call.getState();
                if (state == Call.State.Paused
                        || state == Call.State.PausedByRemote
                        || state == Call.State.Pausing) {
                    call.terminate();
                }
            } else if (call != null && call == currentCall) {
                call.terminate();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
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
        mRipplePulseLayout = findViewById(R.id.layout_ripplepulse);

        supportViewPagerAdapter = new SupportViewPagerAdapter(this);
        vpSupport.setAdapter(supportViewPagerAdapter);

        new TabLayoutMediator(tbLayout, vpSupport, (tab, position) -> {
            vpSupport.setCurrentItem(tab.getPosition(), true);
            if (position == 0) {
                tab.setText("جدید");
            } else {
                tab.setText("در حال بررسی");
            }
        }).attach();

        if (MyApplication.prefManager.getActivateStatus()) {
            btnActivate.setBackgroundResource(R.drawable.bg_green_edge);
            btnDeActivate.setBackgroundColor(Color.parseColor("#00FFB2B2"));
            btnDeActivate.setTextColor(Color.parseColor("#ffffff"));
        } else {
            btnDeActivate.setBackgroundResource(R.drawable.bg_pink_edge);
            btnActivate.setBackgroundColor(Color.parseColor("#00FFB2B2"));
            btnDeActivate.setTextColor(Color.parseColor("#ffffff"));
        }

    }

    public void setActivate() {
        if (btnActivate != null)
            btnActivate.setBackgroundResource(R.drawable.bg_green_edge);
        MyApplication.prefManager.setActivateStatus(true);
        if (btnDeActivate != null) {
            btnDeActivate.setBackgroundColor(Color.parseColor("#00FFB2B2"));
            btnDeActivate.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void setDeActivate() {
        MyApplication.prefManager.setActivateStatus(false);
        if (btnActivate != null)
            btnActivate.setBackgroundColor(Color.parseColor("#00FFB2B2"));
        if (btnDeActivate != null) {
            btnDeActivate.setBackgroundResource(R.drawable.bg_pink_edge);
            btnDeActivate.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void showCallIncoming() {
        mRipplePulseLayout.startRippleAnimation();
        call = core.getCurrentCall();
        Address address = call.getRemoteAddress();
        txtCallerNum.setText(address.getUsername());
        rlNewInComingCall.setVisibility(View.VISIBLE);
        rlActionBar.setVisibility(View.GONE);
    }

    private void showTitleBar() {
        mRipplePulseLayout.stopRippleAnimation();
        rlNewInComingCall.setVisibility(View.GONE);
        rlActionBar.setVisibility(View.VISIBLE);
    }

    CoreListenerStub mCoreListener = new CoreListenerStub() {
        @Override
        public void onCallStateChanged(Core core, final Call call, Call.State state, String message) {
            SupportActivity.this.call = call;

            if (state == Call.State.IncomingReceived) {
                showCallIncoming();
            } else if (state == Call.State.Released) {
                if (imgEndCall != null)
                    imgEndCall.setColorFilter(ContextCompat.getColor(MyApplication.context, R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
                showTitleBar();
                if (mCallQualityUpdater != null) {
                    LinphoneService.removeFromUIThreadDispatcher(mCallQualityUpdater);
                    mCallQualityUpdater = null;
                }
            } else if (state == Call.State.Connected) {
                startCallQuality();
                if (imgEndCall != null)
                    imgEndCall.setColorFilter(ContextCompat.getColor(MyApplication.context, R.color.colorRed), android.graphics.PorterDuff.Mode.MULTIPLY);
                Address address = call.getRemoteAddress();
//                if (voipId.equals("0")) {  //TODO
//                    edtTell.setText(PhoneNumberValidation.removePrefix(address.getUsername()));
//                }
                showTitleBar();
            } else if (state == Call.State.Error) {
                showTitleBar();
            } else if (state == Call.State.End) {
                if (imgCallQuality != null)
                    imgCallQuality.setVisibility(View.INVISIBLE);
                showTitleBar();
                if (mCallQualityUpdater != null) {
                    LinphoneService.removeFromUIThreadDispatcher(mCallQualityUpdater);
                    mCallQualityUpdater = null;
                }
            }
        }
    };

    private void startCallQuality() {
        if (mCallQualityUpdater == null)
            LinphoneService.dispatchOnUIThreadAfter(
                    mCallQualityUpdater =
                            new Runnable() {
                                final Call mCurrentCall = LinphoneService.getCore().getCurrentCall();

                                public void run() {
                                    if (mCurrentCall == null) {
                                        mCallQualityUpdater = null;
                                        return;
                                    }
                                    float newQuality = mCurrentCall.getCurrentQuality();
                                    updateQualityOfSignalIcon(newQuality);

                                    if (MyApplication.prefManager.getConnectedCall())
                                        LinphoneService.dispatchOnUIThreadAfter(this, 1000);
                                }
                            },
                    1000);
    }

    private void updateQualityOfSignalIcon(float quality) {
        Log.d(TAG, "updateQualityOfSignalIcon: " + quality);
        int iQuality = (int) quality;

        int imageRes = R.drawable.ic_quality_0;

        if (iQuality == mDisplayedQuality) return;
        if (quality >= 4) { // Good Quality
            imageRes = R.drawable.ic_quality_4;
        } else if (quality >= 3) {// Average quality
            imageRes = (R.drawable.ic_quality_3);
        } else if (quality >= 2) { // Low quality
            imageRes = (R.drawable.ic_quality_2);
        } else if (quality >= 1) { // Very low quality
            imageRes = (R.drawable.ic_quality_1);
        }
        if (imgCallQuality != null) {
            imgCallQuality.setVisibility(View.VISIBLE);
            imgCallQuality.setImageResource(imageRes);
        }
        mDisplayedQuality = iQuality;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.currentActivity = this;
        MyApplication.prefManager.setAppRun(true);
        showTitleBar();
        if (MyApplication.prefManager.getConnectedCall()) {
            startCallQuality();
            imgEndCall.setColorFilter(ContextCompat.getColor(MyApplication.context, R.color.colorRed), android.graphics.PorterDuff.Mode.MULTIPLY);

            Call[] calls = core.getCalls();
            for (Call call : calls) {
                if (call != null && call.getState() == Call.State.StreamsRunning) {
//                    if (voipId.equals("0")) {
//                        Address address = call.getRemoteAddress();
//                        edtTell.setText(PhoneNumberValidation.removePrefix(address.getUsername()));
//                    }
                }
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
        supportActivityIsRunning = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.currentActivity = this;
        supportActivityIsRunning = true;

        core = LinphoneService.getCore();
        core.addListener(mCoreListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        core.removeListener(mCoreListener);
//    MyApplication.prefManager.setLastCallerId("");// set empty, because I don't want save this permanently .
        core = null;
    }

    @Override
    public void onBackPressed() {
        try {
            KeyBoardHelper.hideKeyboard();
            if (getFragmentManager().getBackStackEntryCount() > 0 || getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                new GeneralDialog()
                        .title("خروج")
                        .message("آیا از خروج خود اطمینان دارید؟")
                        .firstButton("بله", new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Intent intent = new Intent(MyApplication.context, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    AvaCrashReporter.send(e, "SupportActivity class, onBackPressed method");
                                }
                            }
                        })
                        .secondButton("خیر", null)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AvaCrashReporter.send(e, "TripRegister class, onBackPressed method");
        }
    }

}