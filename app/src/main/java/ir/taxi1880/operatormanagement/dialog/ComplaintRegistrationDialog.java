package ir.taxi1880.operatormanagement.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.adapter.SpinnerAdapter;
import ir.taxi1880.operatormanagement.app.EndPoints;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.helper.KeyBoardHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.model.TypeServiceModel;
import ir.taxi1880.operatormanagement.okHttp.RequestHelper;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;

public class ComplaintRegistrationDialog {

  private static final String TAG = ComplaintRegistrationDialog.class.getSimpleName();

  private Spinner spComplaintType;
  private int complaintType;

  static Dialog dialog;

  public void show(String serviceId, String voipId) {
    if (MyApplication.currentActivity == null || MyApplication.currentActivity.isFinishing())
      return;
    dialog = new Dialog(MyApplication.currentActivity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().getAttributes().windowAnimations = R.style.ExpandAnimation;
    dialog.setContentView(R.layout.dialog_complaint_registreation);
    TypefaceUtil.overrideFonts(dialog.getWindow().getDecorView());
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
    wlp.gravity = Gravity.CENTER;
    wlp.windowAnimations = R.style.ExpandAnimation;
    dialog.getWindow().setAttributes(wlp);
    dialog.setCancelable(true);

    ImageView imgClose = dialog.findViewById(R.id.imgClose);
    Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
    EditText edtComment = dialog.findViewById(R.id.edtComment);
    spComplaintType = dialog.findViewById(R.id.spComplaintType);

    initSpinner();

    imgClose.setOnClickListener(view -> dismiss());

    btnSubmit.setOnClickListener(view -> {
      String description = edtComment.getText().toString();

      if (description.isEmpty()) {
        edtComment.setError("لطفا توضیحات را وارد کنید");
        return;
      }

      setComplaint(serviceId, description, voipId);
    });

    dialog.show();
  }

  private void setComplaint(String serviceId, String description, String voipId) {

    RequestHelper.builder(EndPoints.INSERT_COMPLAINT)
            .addParam("userId", MyApplication.prefManager.getUserCode())
            .addParam("serviceId", 1)
            .addParam("complaintType", complaintType)
            .addParam("description", 1)
            .addParam("voipId", 1)
            .listener(onSetComplaint)
            .post();
  }

  RequestHelper.Callback onSetComplaint = new RequestHelper.Callback() {
    @Override
    public void onResponse(Runnable reCall, Object... args) {
      MyApplication.handler.post(new Runnable() {
        @Override
        public void run() {
          try {
            Log.i("TripDetailsFragment", "run: " + args[0].toString());

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }

    @Override
    public void onFailure(Runnable reCall, Exception e) {
      MyApplication.handler.post(() -> {

      });
    }
  };

  private void initSpinner() {
    ArrayList<TypeServiceModel> typeServiceModels = new ArrayList<>();
    ArrayList<String> serviceList = new ArrayList<String>();
    try {
      JSONArray serviceArr = new JSONArray(MyApplication.prefManager.getComplaint());
      for (int i = 0; i < serviceArr.length(); i++) {
        JSONObject serviceObj = serviceArr.getJSONObject(i);
        TypeServiceModel typeServiceModel = new TypeServiceModel();
        typeServiceModel.setName(serviceObj.getString("ShektypeSharh"));
        typeServiceModel.setId(serviceObj.getInt("sheKtypeId"));
        typeServiceModels.add(typeServiceModel);
        serviceList.add(serviceObj.getString("ShektypeSharh"));
      }
      if (spComplaintType == null)
        return;

      spComplaintType.setEnabled(true);

      spComplaintType.setAdapter(new SpinnerAdapter(MyApplication.currentActivity, R.layout.item_spinner, serviceList));

      spComplaintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          complaintType = typeServiceModels.get(position).getId();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
      });
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private static void dismiss() {
    try {
      if (dialog != null) {
        dialog.dismiss();
        KeyBoardHelper.hideKeyboard();
      }
    } catch (Exception e) {
      Log.e("TAG", "dismiss: " + e.getMessage());
      AvaCrashReporter.send(e, "ComplaintRegistrationDialog class, dismiss method");
    }
    dialog = null;
  }

}
