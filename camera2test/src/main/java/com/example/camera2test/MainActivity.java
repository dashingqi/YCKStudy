package com.example.camera2test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private CameraManager cameraManager;
    private SurfaceHolder holder;
    private Handler handler;
    private HandlerThread handlerThread;
    private String mCameraId;
    private ImageReader mImageReader;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder captureRequest;
    private CameraCaptureSession mSession;
    private CaptureRequest.Builder mPreviewBuilder;
    private CaptureRequest.Builder mCaptureRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mSurfaceView = findViewById(R.id.surface_view);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        holder = mSurfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initViewAndCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }


    private void initViewAndCamera() {

        //开启线程
        handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        try {

            //设置摄像头
            mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;

            mImageReader = ImageReader.newInstance(mSurfaceView.getWidth(), mSurfaceView.getHeight(), ImageFormat.JPEG, 7);

            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, handler);

            //检查和申请相机的权限
            checkPermission();

            cameraManager.openCamera(mCameraId, mStateCallback, handler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {

            mCameraDevice = camera;
            try {
                takePreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Toast.makeText(MainActivity.this, "打开摄像头失败", Toast.LENGTH_LONG).show();

        }
    };


    /**
     * 开启预览
     */
    private void takePreview() throws CameraAccessException {
        captureRequest = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequest.setTag(mSurfaceView.getHolder());
        mCameraDevice.createCaptureSession(Arrays.asList(holder.getSurface(), mImageReader.getSurface()), mSessionStateCallback, handler);


    }

    private CameraCaptureSession.CaptureCallback sessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            mSession = session;
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            mSession = session;
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };


    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //在这里进行图片的存储；
            mCameraDevice.close();
            Image image = mImageReader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            if (bitmap!=null){
                //将bitmap设置到ImageView中;
            }

        }
    };

    private CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mSession = session;
            //配置完毕开始预览


            try {
                //自动对焦
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                //开启闪光灯
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                //无限次循环捕捉图像
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, handler);
            }catch (Exception e){
                e.printStackTrace();
            }




        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(MainActivity.this, "配置失败", Toast.LENGTH_LONG).show();
        }
    };


    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }


    public void takePicture(){
        try {
            mCaptureRequest = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            mCaptureRequest.addTarget(mImageReader.getSurface());
            //自动对焦
            mCaptureRequest.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            //自动曝光
            mCaptureRequest.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(mCameraId);
            mCaptureRequest.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(cameraCharacteristics, rotation));//使图片做顺时针旋转
            //captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,getOrientaion(rotation))两种方法都可以。
            CaptureRequest mCaptureRequest = captureRequest.build();
            mSession.capture(mCaptureRequest, null, handler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    //获取图片应该旋转的角度，使图片竖直
    public int getOrientation(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0:
                return 90;
            case Surface.ROTATION_90:
                return 0;
            case Surface.ROTATION_180:
                return 270;
            case Surface.ROTATION_270:
                return 180;
            default:
                return 0;
        }
    }


    //获取图片应该旋转的角度，使图片竖直
    private int getJpegOrientation(CameraCharacteristics c, int deviceOrientation) {
        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0;
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        // LENS_FACING相对于设备屏幕的方向,LENS_FACING_FRONT相机设备面向与设备屏幕相同的方向
        boolean facingFront = c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;

        return jpegOrientation;
    }
}
