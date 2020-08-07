package ir.taxi1880.operatormanagement.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.helper.PaintView;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;

public class AssignmentFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.paintView)
    PaintView paintView;

    @OnClick(R.id.imgBack)
    void onBack() {
        MyApplication.currentActivity.onBackPressed();
    }

    @OnClick(R.id.btnClearAssignment)
    void btnClearAssignment() {
        if (paintView != null)
            paintView.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        TypefaceUtil.overrideFonts(view);
        unbinder = ButterKnife.bind(this, view);
        DisplayMetrics metrics = new DisplayMetrics();
        MyApplication.currentActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
        paintView.normal();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
