package ir.taxi1880.operatormanagement.fragment.mistake;

import static ir.taxi1880.operatormanagement.app.Keys.KEY_NEW_MISTAKE_COUNT;
import static ir.taxi1880.operatormanagement.app.Keys.NEW_MISTAKE_COUNT;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.operatormanagement.adapter.AllMistakesAdapter;
import ir.taxi1880.operatormanagement.app.EndPoints;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.databinding.FragmentAllMistakesBinding;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.model.AllMistakesModel;
import ir.taxi1880.operatormanagement.okHttp.RequestHelper;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;

public class AllMistakesFragment extends Fragment {
    public static final String TAG = AllMistakesFragment.class.getSimpleName();
    FragmentAllMistakesBinding binding;
    LocalBroadcastManager broadcaster;
    AllMistakesAdapter mAdapter;
    ArrayList<AllMistakesModel> allMistakesModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllMistakesBinding.inflate(inflater, container, false);
        TypefaceUtil.overrideFonts(binding.getRoot());

        getListen();

        binding.refreshPage.setOnRefreshListener(this::getListen);

        binding.imgRefreshFail.setOnClickListener(view -> getListen());

        binding.imgRefresh.setOnClickListener(view -> getListen());

        return binding.getRoot();
    }

    private void getListen() {
        if (binding.vfAllMistake != null)
            binding.vfAllMistake.setDisplayedChild(0);
        RequestHelper.builder(EndPoints.LISTEN)
                .listener(listenCallBack)
                .get();
    }

    RequestHelper.Callback listenCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    if (binding.refreshPage != null)
                        binding.refreshPage.setRefreshing(false);
                    allMistakesModels = new ArrayList<>();
                    JSONObject listenObj = new JSONObject(args[0].toString());
                    boolean success = listenObj.getBoolean("success");
                    String message = listenObj.getString("message");
                    if (success) {
                        if (binding.vfAllMistake != null)
                            binding.vfAllMistake.setDisplayedChild(1);
                        JSONArray dataArr = listenObj.getJSONArray("data");
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject dataObj = dataArr.getJSONObject(i);
                            AllMistakesModel model = new AllMistakesModel();

                            model.setId(dataObj.getInt("id"));
                            model.setServiceCode(dataObj.getInt("serviceCode"));
                            model.setUserCode(dataObj.getInt("userCode"));
                            model.setDate(dataObj.getString("saveDate"));
                            model.setTime(dataObj.getString("saveTime"));
                            model.setDescription(dataObj.getString("Description"));
                            model.setTell(dataObj.getString("tell"));
                            model.setMobile(dataObj.getString("mobile"));
                            model.setUserCodeContact(dataObj.getInt("userCodeContact"));
                            model.setStationRegisterUser(dataObj.getInt("stationRegisterUser"));
                            model.setDestStationRegisterUser(dataObj.getInt("destStationRegisterUser"));
                            model.setAddress(dataObj.getString("address"));
                            model.setCustomerName(dataObj.getString("customerName"));
                            model.setConDate(dataObj.getString("conDate"));
                            model.setConTime(dataObj.getString("conTime"));
                            model.setSendTime(dataObj.getString("sendTime"));
                            model.setVoipId(dataObj.getString("VoipId"));
                            model.setStationCode(dataObj.getInt("stationCode"));
                            model.setCity(dataObj.getInt("cityId"));
                            model.setDestStation(dataObj.getString("destinationStation"));
                            model.setDestination(dataObj.getString("destinationAddress"));
                            model.setPrice(dataObj.getString("servicePrice"));

                            allMistakesModels.add(model);
                        }

                        if (allMistakesModels.size() == 0) {
                            if (binding.vfAllMistake != null)
                                binding.vfAllMistake.setDisplayedChild(3);
                        } else {
                            if (binding.vfAllMistake != null) {
                                binding.vfAllMistake.setDisplayedChild(1);
                                mAdapter = new AllMistakesAdapter(allMistakesModels);
                                binding.mistakesList.setAdapter(mAdapter);
                            }
                        }

                        broadcaster = LocalBroadcastManager.getInstance(MyApplication.context);
                        Intent broadcastIntent = new Intent(KEY_NEW_MISTAKE_COUNT);
                        broadcastIntent.putExtra(NEW_MISTAKE_COUNT, allMistakesModels.size());
                        broadcaster.sendBroadcast(broadcastIntent);

                    } else {
                        if (binding.vfAllMistake != null)
                            binding.vfAllMistake.setDisplayedChild(2);
                    }

                } catch (Exception e) {
                    if (binding.refreshPage != null)
                        binding.refreshPage.setRefreshing(false);
                    if (binding.vfAllMistake != null)
                        binding.vfAllMistake.setDisplayedChild(2);
                    e.printStackTrace();
                    AvaCrashReporter.send(e, TAG + " class, listenCallBack method");
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                if (binding.refreshPage != null)
                    binding.refreshPage.setRefreshing(false);
                if (binding.vfAllMistake != null)
                    binding.vfAllMistake.setDisplayedChild(2);
            });
        }
    };
}