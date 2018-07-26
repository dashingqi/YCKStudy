package com.example.camera1and2;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void useCamera1() {

        int mFaceBankCameraId =0;
        int mFaceFrontCameraId;
        int mFaceBackOriection;
        int mFaceFrontOriection;

        int numberOfCameras = Camera.getNumberOfCameras();//获取到相机的数量

        for (int i = 0; i < numberOfCameras; i++) {

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            //后置摄像头
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mFaceBankCameraId = i;
                mFaceBackOriection = cameraInfo.orientation;
            }
            //前置摄像头
            else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mFaceFrontCameraId = i;
                mFaceFrontOriection = cameraInfo.orientation;

            }

        }
        Camera camera = Camera.open(mFaceBankCameraId);

        //获取参数
        Camera.Parameters parameters = camera.getParameters();
        String flashMode = parameters.getFlashMode();
        //camera.setParameters();

    }
}
