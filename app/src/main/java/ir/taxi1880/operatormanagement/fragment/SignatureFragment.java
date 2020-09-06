package ir.taxi1880.operatormanagement.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileOutputStream;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.operatormanagement.R;
import ir.taxi1880.operatormanagement.app.EndPoints;
import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.dialog.GeneralDialog;
import ir.taxi1880.operatormanagement.dialog.LoadingDialog;
import ir.taxi1880.operatormanagement.helper.TypefaceUtil;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;

public class SignatureFragment extends Fragment {

    public static final String TAG = SignatureFragment.class.getSimpleName();
    private Unbinder unbinder;

    @BindView(R.id.paintView)
    SignaturePad paintView;

    @OnClick(R.id.imgBack)
    void onBack() {
        MyApplication.currentActivity.onBackPressed();
    }

    @OnClick(R.id.btnClearSignature)
    void btnClearAssignment() {
        if (paintView != null)
            paintView.clear();
    }

    @OnClick(R.id.btnSubmitSignature)
    void btnSubmitSignature() {
        if (paintView != null) {
            GeneralDialog generalDialog = new GeneralDialog();
            generalDialog.title("تاییدیه");
            generalDialog.message("از ارسال امضاء خود برای قرارداد جدید، مطمئن هستید؟");
            generalDialog.cancelable(false);
            generalDialog.firstButton("بله", () -> {
                Bitmap bitmap = paintView.getSignatureBitmap();

                saveBitmap("ela.png", bitmap);

                checkPermission();

                uploadImage(Uri.parse(MyApplication.image_path_save + "ela.png"));

                /*TODO: for svg*/
//                String svgPath = paintView.getSignatureSvg();
//                svgPath = Pattern.compile("black").matcher(svgPath).replaceAll("blue");
//                Log.i("TAG", "btnSubmitSignature: " + svgPath);
//                /*TODO: add api for send svg*/
////                MyApplication.prefManager.isLoggedIn(true);
////                MyApplication.avaStart();
////                startActivity(new Intent(MyApplication.currentActivity, MainActivity.class));
////                MyApplication.currentActivity.finish();
            });
            generalDialog.secondButton("خیر", generalDialog::dismiss);
            generalDialog.show();
        }
    }

    public void saveBitmap(String bitName, Bitmap mBitmap) {

        File file = new File(MyApplication.image_path_save, bitName);
        file.mkdirs();
//        try {
//            f.createNewFile();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//        }
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            AvaCrashReporter.send(e, "SignatureFragment class, saveBitmap method");
        }

    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            int PERMISSION_CALLBACK_CONSTANT = 100;
            boolean externalStoragePermission = (ContextCompat.checkSelfPermission(MyApplication.currentActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
            if (externalStoragePermission) {
                ActivityCompat.requestPermissions(MyApplication.currentActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            } else {
                /*TODO(najafi): what now?*/

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
    }

    private void uploadImage(Uri uri) {
        try {
            String uploadNationalPathUrl = EndPoints.UPLOAD_NATIONAL_CARD;

            MultipartUploadRequest multipartUploadRequest = null;
            multipartUploadRequest = new MultipartUploadRequest(MyApplication.context, uploadNationalPathUrl);
            multipartUploadRequest.addFileToUpload(getRealPathFromUri(MyApplication.context, uri), "image", MyApplication.prefManager.getUserCode() + ".png");
            multipartUploadRequest.addParameter("userId", MyApplication.prefManager.getUserCode() + "");
            multipartUploadRequest.setMaxRetries(2);
            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "uploaderrrrrr => progress");
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception e) {
                    Log.i(TAG, "uploaderrrrrr => onError: Exception: " + e.getMessage() + " ,uploadInfo: " + uploadInfo);
                    LoadingDialog.dismiss();
                    new GeneralDialog()
                            .title("هشدار")
                            .message("ارسال تصویر با خطا روبه رو شد، میخوای دوباره تلاش کنی؟")
                            .firstButton("تلاش مجدد", () -> uploadImage(uri))
                            .secondButton("بستن", null)
                            .cancelable(false)
                            .show();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    Log.i(TAG, "uploaderrrrrr => onCompleted: uploadInfo: " + uploadInfo + " ,serverResponse: " + serverResponse);
                    LoadingDialog.dismiss();

                    new GeneralDialog()
                            .title("ارسال شد")
                            .message("تصویر با موفقیت ارسال شد")
                            .firstButton("باشه", null)
                            .cancelable(false)
                            .show();

                    MyApplication.Toast("done", Toast.LENGTH_SHORT);
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "uploaderrrrrr => onCancelled : uploadInfo: " + uploadInfo);
                    LoadingDialog.dismiss();
                    new GeneralDialog()
                            .title("هشدار")
                            .message("ارسال تصویر با خطا روبه رو شد، میخوای دوباره تلاش کنی؟")
                            .firstButton("تلاش مجدد", () -> uploadImage(uri))
                            .secondButton("بستن", null)
                            .cancelable(false)
                            .show();
                }
            });
            multipartUploadRequest.startUpload();
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e("AndroidUploadService", exc.getMessage(), exc);
            AvaCrashReporter.send(exc, "SignatureFragment class,uploadImage method");
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signature, container, false);
        TypefaceUtil.overrideFonts(view);
        unbinder = ButterKnife.bind(this, view);

        paintView.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
            }

            @Override
            public void onClear() {
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}