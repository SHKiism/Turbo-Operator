package ir.taxi1880.operatormanagement.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.helper.KeyBoardHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;

public class DriverInfoDialog {

    private static final String TAG = DriverInfoDialog.class.getSimpleName();
    static Dialog dialog;
    Unbinder unbinder;

    @BindView(R.id.txtFullName)
    TextView txtFullName;

    @BindView(R.id.txtFatherName)
    TextView txtFatherName;

    @BindView(R.id.txtNationalCode)
    TextView txtNationalCode;

    @BindView(R.id.txtCity)
    TextView txtCity;

    @BindView(R.id.txtGender)
    TextView txtGender;

    @BindView(R.id.txtBirthCertificate)
    TextView txtBirthCertificate;

    @BindView(R.id.txtDriverCode)
    TextView txtDriverCode;

    @BindView(R.id.txtStartDate)
    TextView txtStartDate;

    @BindView(R.id.txtCarClass)
    TextView txtCarClass;

    @BindView(R.id.txtIbenNo)
    TextView txtIbenNo;

    @BindView(R.id.txtVinNo)
    TextView txtVinNo;

    @BindView(R.id.imgSmartTaxiMeter)
    ImageView imgSmartTaxiMeter;

    @BindView(R.id.imgFuelQuota)
    ImageView imgFuelQuota;

    @BindView(R.id.imgConfirmInfo)
    ImageView imgConfirmInfo;

    @OnClick(R.id.imgClose)
    void onPressCLose(){
        dismiss();
    }

    public void show(String driverInfo) {
        if (MyApplication.currentActivity == null || MyApplication.currentActivity.isFinishing())
            return;
        dialog = new Dialog(MyApplication.currentActivity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpandAnimation;
        dialog.setContentView(R.layout.dialog_driver_info);
        unbinder = ButterKnife.bind(this, dialog);
        TypefaceUtil.overrideFonts(dialog.getWindow().getDecorView());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.style.ExpandAnimation;
        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(false);

        try {
            JSONObject driverInfoObj=new JSONObject(driverInfo);
            int cityCode = driverInfoObj.getInt("cityCode");
            txtDriverCode.setText(driverInfoObj.getInt("driverCode")+"");
            int carCode = driverInfoObj.getInt("carCode");
            int smartCode = driverInfoObj.getInt("smartCode");
            txtFullName.setText(driverInfoObj.getString("driverName"));
            int smartTaximeter = driverInfoObj.getInt("smartTaximeter");
            txtCarClass.setText( driverInfoObj.getInt("carClass")+"");
            txtGender.setText(driverInfoObj.getInt("gender")+"");
            int confirmation = driverInfoObj.getInt("confirmation");
            txtNationalCode.setText(driverInfoObj.getString("nationalCode"));
            txtFatherName.setText( driverInfoObj.getString("fatherName"));
            txtVinNo.setText(driverInfoObj.getString("vin"));
            txtIbenNo.setText( driverInfoObj.getString("sheba"));
            txtBirthCertificate.setText(driverInfoObj.getString("shenasname"));
            int fuelRationing = driverInfoObj.getInt("fuelRationing");
            int cancelFuel = driverInfoObj.getInt("cancelFuel");
            txtStartDate.setText( driverInfoObj.getString("startActiveDate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.show();
    }

    private static void dismiss() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
        }
        dialog = null;
    }

}