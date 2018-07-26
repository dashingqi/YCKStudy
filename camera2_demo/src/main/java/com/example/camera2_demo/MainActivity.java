package com.example.camera2_demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private CameraManager mCameraManager;
    private CameraCharacteristics mCameraManagerCameraCharacteristics;
    private String mCameraId;
    private boolean mFlashSuppored;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mCaptureRequestPreview;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceViewHolder;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest mCaptureRequest;
    private Button btnStartPreview;
    private int mState;
    private int STATE_PREVIEW;
    private int STATE_WAITING_LOCK;
    private int STATE_PICTURE_TAKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartPreview = findViewById(R.id.btn_start_preview);
        mSurfaceView = findViewById(R.id.mSurfaceView);
        mSurfaceViewHolder = mSurfaceView.getHolder();
        btnStartPreview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                getCameraManager();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getCameraManager() {


        mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        try {
            //获取到摄像头列表
            String[] cameraIdList = mCameraManager.getCameraIdList();
            for (String cameraId : cameraIdList) {
                //根据摄像头id获取到参数
                mCameraManagerCameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer id = mCameraManagerCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);

                //不准使用前置摄像头
                if (id != null && id == CameraCharacteristics.LENS_FACING_FRONT) {
                    Toast.makeText(MainActivity.this, "不支持前置摄像头", Toast.LENGTH_LONG).show();
                    continue;
                }

                //获取摄像头支持的配置属性
                StreamConfigurationMap map = mCameraManagerCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null)
                    continue;

                //检查相机是否支持闪光灯；
                Boolean available = mCameraManagerCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSuppored = available == null ? false : available;

                mCameraId = cameraId;

                Log.d("Tag", "相机可用");

                //拿到相机id 就去开启相机
                openCamera();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启相机
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() {

        try {
            checkPermission();
            mCameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkPermission() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            //创建 CameraCaptureSeeesion;
            //createCameraPreviewSession();
            createCameraCaptureSession();
        }


        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

            mCameraDevice.close();
            mCameraDevice = null;

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            mCameraDevice.close();
            mCameraDevice = null;

        }
    };


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createCameraPreviewSession() {
        try {
            mCaptureRequestPreview = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);//发送预览的请求
            mCaptureRequestPreview.addTarget(mSurfaceViewHolder.getSurface());

            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceViewHolder.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {

                    try {
                        if (null == mCameraDevice)
                            //相机已经关闭了
                            return;

                        mCameraCaptureSession = session;

                        mCaptureRequestPreview.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        mCaptureRequest = mCaptureRequestPreview.build();
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.d("Tag", "开启预览失败");

                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createCameraCaptureSession() {

        try {
            mCaptureRequestPreview = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            mCaptureRequestPreview.addTarget(mSurfaceViewHolder.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceViewHolder.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mCameraCaptureSession = session;
                    mCaptureRequest = mCaptureRequestPreview.build();
                    try {
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequest,null,null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.d("Tag", "开启预览失败");

                }
            }, null);
        } catch (CameraAccessException e) {

            e.printStackTrace();
        }
    }


    public CameraCaptureSession.CaptureCallback mSessionCallback = new CameraCaptureSession.CaptureCallback() {


        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void lockFocus(){

        mCaptureRequestPreview.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);


    }
}
