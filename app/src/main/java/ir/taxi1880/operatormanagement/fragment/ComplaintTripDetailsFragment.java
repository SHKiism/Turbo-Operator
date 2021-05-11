package ir.taxi1880.operatormanagement.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.warkiz.widget.IndicatorSeekBar;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.EndPoints;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.helper.DateHelper;
import ir.taxi1880.operatormanagement.helper.FileHelper;
import ir.taxi1880.operatormanagement.helper.StringHelper;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.okHttp.AuthenticationInterceptor;

public class ComplaintTripDetailsFragment extends Fragment {
    Unbinder unbinder;
    static MediaPlayer mediaPlayer;
    static IndicatorSeekBar skbTimer;
    static ViewFlipper vfPlayPause;

    @BindView(R.id.txtComplaintType)
    TextView txtComplaintType;

    @BindView(R.id.txtServiceDate)
    TextView txtServiceDate;

    @BindView(R.id.txtOrigin)
    TextView txtOrigin;

    @BindView(R.id.txtPrice)
    TextView txtPrice;

    @BindView(R.id.vfVoiceStatus)
    ViewFlipper vfVoiceStatus;

    @OnClick(R.id.imgPlay)
    void onPlay() {
        if (vfPlayPause != null)
            vfPlayPause.setDisplayedChild(1);

        Log.i("URL", "show: " + EndPoints.CALL_VOICE + ComplaintDetailFragment.complaintDetailsModel.getComplaintVoipId());
        String voiceName = ComplaintDetailFragment.complaintDetailsModel.getComplaintId() + ".mp3";
        File file = new File(MyApplication.DIR_ROOT + MyApplication.VOICE_FOLDER_NAME + "/" + voiceName);
        String voipId = ComplaintDetailFragment.complaintDetailsModel.getComplaintVoipId();
        if (file.exists()) {
            initVoice(Uri.fromFile(file));
            playVoice();
        } else if (voipId.equals("0")) {
            if (vfVoiceStatus != null)
                vfVoiceStatus.setDisplayedChild(1);
            if (vfPlayPause != null)
                vfPlayPause.setDisplayedChild(0);
        } else {
            startDownload(EndPoints.CALL_VOICE + ComplaintDetailFragment.complaintDetailsModel.getComplaintVoipId(), voiceName);
        }
    }

    @OnClick(R.id.imgPause)
    void onImgPause() {
        pauseVoice();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint_trip_details, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        unbinder = ButterKnife.bind(this, view);
        TypefaceUtil.overrideFonts(view);

        skbTimer = view.findViewById(R.id.skbTimer);
        vfPlayPause = view.findViewById(R.id.vfPlayPause);

        txtComplaintType.setText(StringHelper.toPersianDigits(ComplaintDetailFragment.complaintDetailsModel.getComplaintType()));

        String date = DateHelper.strPersianTree(DateHelper.parseDate(ComplaintDetailFragment.complaintDetailsModel.getServiceDate()));
        txtServiceDate.setText(StringHelper.toPersianDigits(date));
        txtOrigin.setText(StringHelper.toPersianDigits(ComplaintDetailFragment.complaintDetailsModel.getAddress()));
        txtPrice.setText(StringHelper.toPersianDigits(StringHelper.setComma(ComplaintDetailFragment.complaintDetailsModel.getPrice() + "") + " تومان"));

        return view;
    }

    long lastTime = 0;

    private void startDownload(final String urlString, final String fileName) {
        try {
            URL url = new URL(urlString);

            String dirPath = MyApplication.DIR_ROOT + "voice/";
            String dirPathTemp = MyApplication.DIR_ROOT + "temp/";

            new File(dirPath).mkdirs();
            File file = new File(dirPath);
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    new File(file, children[i]).delete();
                }
            }

//      File file = new File(dirPathTemp + fileName);
//      int downloadId = FindDownloadId.execte(urlString);
//      if (file.exists() && downloadId != -1) {
//        PRDownloader.resume(downloadId);
//      } else {
//        downloadId =
            PRDownloader.download(url.toString(), dirPathTemp, fileName)
                    .setHeader("Authorization", MyApplication.prefManager.getAuthorization())
                    .setHeader("id_token", MyApplication.prefManager.getIdToken())
                    .build()
                    .setOnStartOrResumeListener(() -> {
                    })
                    .setOnPauseListener(() -> {
                    })
                    .setOnCancelListener(() -> {
                    })
                    .start(new OnDownloadListener() {

                        @Override
                        public void onDownloadComplete() {
//                    FinishedDownload.execute(urlString);
                            FileHelper.moveFile(dirPathTemp, fileName, dirPath);
                            File file = new File(dirPath + fileName);

                            MyApplication.handler.postDelayed(() -> {
                                initVoice(Uri.fromFile(file));
                                playVoice();
                            }, 500);
                        }

                        @Override
                        public void onError(Error error) {
                            Log.e("PlayConversationDialog", "onError: " + error.getResponseCode() + "");
                            Log.e("PlayConversationDialog", "onError: " + error.getServerErrorMessage() + "");
                            FileHelper.deleteFile(dirPathTemp, fileName);
                            if (error.getResponseCode() == 401)
                                new RefreshTokenAsyncTask().execute();
                            if (error.getResponseCode() == 404)
                                vfVoiceStatus.setDisplayedChild(1);
                        }
                    });

//        StartDownload.execute(downloadId, url.toString(), dirPathTemp + fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVoice(Uri uri) {
        try {
            mediaPlayer = MediaPlayer.create(MyApplication.context, uri);
            mediaPlayer.setOnCompletionListener(mp -> {
                if (vfPlayPause != null) {
                    vfPlayPause.setDisplayedChild(0);
                }
            });
            TOTAL_VOICE_DURATION = mediaPlayer.getDuration();

            skbTimer.setMax(TOTAL_VOICE_DURATION);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playVoice() {
        try {
            if (mediaPlayer != null)
                mediaPlayer.start();
            if (vfPlayPause != null)
                vfPlayPause.setDisplayedChild(2);
        } catch (Exception e) {
        }

        startTimer();
    }

    public static void pauseVoice() {
        try {
            if (mediaPlayer != null)
                mediaPlayer.pause();

            skbTimer.setProgress(0);

            if (vfPlayPause != null)
                vfPlayPause.setDisplayedChild(0);
        } catch (Exception e) {
        }
        cancelTimer();
    }

    private int TOTAL_VOICE_DURATION;

    private static Timer timer;

    private void startTimer() {
        Log.i("PlayConversationDialog", "startTimer: ");
        if (timer != null) {
            return;
        }
        timer = new Timer();
        UpdateSeekBar task = new UpdateSeekBar();
        timer.scheduleAtFixedRate(task, 500, 1000);

    }

    @Override
    public void onDestroyView() {
        try {
            pauseVoice();
            cancelTimer();
        } catch (Exception e) {
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    private static void cancelTimer() {
        try {
            if (timer == null) return;
            timer.cancel();
            timer = null;
        } catch (Exception e) {
        }

    }

    private class UpdateSeekBar extends TimerTask {
        public void run() {
            if (mediaPlayer != null) {
                try {
                    MyApplication.handler.post(() -> {
                        Log.i("PlayConversationDialog", "onStopTrackingTouch run: " + mediaPlayer.getCurrentPosition());
                        skbTimer.setProgress(mediaPlayer.getCurrentPosition());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class RefreshTokenAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            new AuthenticationInterceptor().refreshToken();
            return null;
        }
    }
}