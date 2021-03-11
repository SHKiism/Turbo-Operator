package ir.taxi1880.operatormanagement.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.fragment.DriverTripsDetailsFragment;
import ir.taxi1880.operatormanagement.fragment.TripDetailsFragment;
import ir.taxi1880.operatormanagement.helper.FragmentHelper;
import ir.taxi1880.operatormanagement.helper.KeyBoardHelper;
import ir.taxi1880.operatormanagement.helper.StringHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.model.TripModel;

public class DriverTripsAdapter extends RecyclerView.Adapter<DriverTripsAdapter.TripViewHolder> {

  ArrayList<TripModel> tripModels;

  public DriverTripsAdapter(ArrayList<TripModel> tripModels) {
    this.tripModels = tripModels;
  }

  @NonNull
  @Override
  public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
    TypefaceUtil.overrideFonts(view, MyApplication.IraSanSMedume);
    return new TripViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
    final TripModel tripModel = tripModels.get(position);
    holder.txtCallTime.setText(StringHelper.toPersianDigits(tripModel.getCallTime()));
    holder.txtSendTime.setText(tripModel.getSendTime().equals("null") ? " " : StringHelper.toPersianDigits(tripModel.getSendTime()));
    holder.txtCarType.setText(tripModel.getCarType().equals("null") ? " " : StringHelper.toPersianDigits(tripModel.getCarType()));
    holder.txtCity.setText(StringHelper.toPersianDigits(tripModel.getCity()));
    holder.txtCustomerAddress.setText(StringHelper.toPersianDigits(tripModel.getAddress()));
    holder.txtCustomerMobile.setText(StringHelper.toPersianDigits(tripModel.getCustomerMob()));
    holder.txtCustomerName.setText(StringHelper.toPersianDigits(tripModel.getCustomerName()));
    holder.txtCustomerTell.setText(StringHelper.toPersianDigits(tripModel.getCustomerTell()));
    holder.txtDriverMobile.setText(tripModel.getDriverMobile().equals("null") ? " " : StringHelper.toPersianDigits(tripModel.getDriverMobile()));
    holder.txtCallDate.setText(tripModel.getCallDate().equals("null") ? " " : StringHelper.toPersianDigits(tripModel.getCallDate()));
    holder.txtStationCode.setText(StringHelper.toPersianDigits(tripModel.getStationCode()+""));

//    holder.llHeaderStatus.setBackgroundColor(Color.parseColor(tripModel.getStatusColor()));
    holder.llHeaderStatus.setBackgroundColor(Color.parseColor(tripModel.getStatusColor()));
    holder.txtStatus.setText(tripModel.getStatusText());

    holder.itemView.setOnClickListener(view -> {
      Bundle bundle = new Bundle();
      bundle.putString("id", tripModel.getServiceId());
      FragmentHelper.toFragment(MyApplication.currentActivity, new DriverTripsDetailsFragment()).setArguments(bundle).add();
      KeyBoardHelper.hideKeyboard();
    });
  }

  @Override
  public int getItemCount() {
    return tripModels.size();
  }

  public class TripViewHolder extends RecyclerView.ViewHolder {
    TextView txtCallTime;
    TextView txtSendTime;
    TextView txtStatus;
    TextView txtCustomerName;
    TextView txtCustomerTell;
    TextView txtCustomerMobile;
    TextView txtCity;
    TextView txtCustomerAddress;
    TextView txtCarType;
    TextView txtDriverMobile;
    TextView txtCallDate;
    TextView txtStationCode;
    LinearLayout llHeaderStatus;

    public TripViewHolder(@NonNull View itemView) {
      super(itemView);
      txtCallTime = itemView.findViewById(R.id.txtCallTime);
      txtStatus = itemView.findViewById(R.id.txtStatus);
      txtSendTime = itemView.findViewById(R.id.txtSendTime);
      txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
      txtCustomerTell = itemView.findViewById(R.id.txtCustomerTell);
      txtCustomerMobile = itemView.findViewById(R.id.txtCustomerMobile);
      txtCity = itemView.findViewById(R.id.txtCity);
      txtCustomerAddress = itemView.findViewById(R.id.txtCustomerAddress);
      txtCarType = itemView.findViewById(R.id.txtCarType);
      txtDriverMobile = itemView.findViewById(R.id.txtDriverMobile);
      txtCallDate = itemView.findViewById(R.id.txtCallDate);
      txtStationCode = itemView.findViewById(R.id.txtStationCode);
      llHeaderStatus = itemView.findViewById(R.id.llHeaderStatus);
    }
  }
}