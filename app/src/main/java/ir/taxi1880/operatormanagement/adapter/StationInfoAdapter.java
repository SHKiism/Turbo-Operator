package ir.taxi1880.operatormanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.helper.StringHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.model.StationInfoModel;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;

public class StationInfoAdapter extends BaseAdapter {

    public static final String TAG = StationInfoAdapter.class.getSimpleName();
    private ArrayList<StationInfoModel> stationInfoModels;
    private LayoutInflater layoutInflater;

    public StationInfoAdapter(ArrayList<StationInfoModel> stationInfoModels) {
        this.stationInfoModels = stationInfoModels;
        this.layoutInflater = LayoutInflater.from(MyApplication.currentActivity);
    }

    @Override
    public int getCount() {
        return stationInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return stationInfoModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView = convertView;

        try {
            final StationInfoModel stationInfoModel = stationInfoModels.get(position);
            if (myView == null) {
                myView = layoutInflater.inflate(R.layout.item_station_info, null);
                TypefaceUtil.overrideFonts(myView, MyApplication.IraSanSMedume);
            }

            TextView txtStreet = myView.findViewById(R.id.txtStreet);
            TextView txtEven = myView.findViewById(R.id.txtEven);
            TextView txtOdd = myView.findViewById(R.id.txtOdd);
            TextView txtStationCode = myView.findViewById(R.id.txtStationCode);
            LinearLayout llEven = myView.findViewById(R.id.llEven);
            LinearLayout llOdd = myView.findViewById(R.id.llOdd);
            LinearLayout llStreet = myView.findViewById(R.id.llStreet);
            LinearLayout llStationCode = myView.findViewById(R.id.llStationCode);

            txtStreet.setText(stationInfoModel.getStreet());
            txtEven.setText(StringHelper.toPersianDigits(stationInfoModel.getEven()));
            txtOdd.setText(StringHelper.toPersianDigits(stationInfoModel.getOdd()));
            txtStationCode.setText(StringHelper.toPersianDigits(stationInfoModel.getStcode() + ""));

            if (stationInfoModel.getOdd().equals("")) {
                llOdd.setVisibility(View.GONE);
            } else {
                llOdd.setVisibility(View.VISIBLE);
            }

            if (String.valueOf(stationInfoModel.getStcode()).equals("")) {
                llStationCode.setVisibility(View.GONE);
            } else {
                llStationCode.setVisibility(View.VISIBLE);
            }

            if (stationInfoModel.getStreet().equals("")) {
                llStreet.setVisibility(View.GONE);
            } else {
                llStreet.setVisibility(View.VISIBLE);
            }

            if (stationInfoModel.getEven().equals("")) {
                llEven.setVisibility(View.GONE);
            } else {
                llEven.setVisibility(View.VISIBLE);
            }

            if (stationInfoModel.getAddressOrNot().equals("stationCode")) {
                llStationCode.setVisibility(View.GONE);
            } else
                llStationCode.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
            AvaCrashReporter.send(e, TAG + " class,getView method");
        }

        return myView;
    }
}