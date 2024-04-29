package com.example.imagepro

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import java.io.IOException

class CameraActivity : Activity(), CameraBridgeViewBase.CvCameraViewListener2 {
    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var mOpenCvCameraView: CameraBridgeViewBase? = null

    // call java class
    private var facialExpressionRecognition: facialExpressionRecognition? = null
    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    run {
                        Log.i(TAG, "OpenCv Is loaded")
                        mOpenCvCameraView.enableView()
                    }
                    run { super.onManagerConnected(status) }
                }

                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    init {
        Log.i(TAG, "Instantiated new " + this.javaClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val MY_PERMISSIONS_REQUEST_CAMERA = 0
        // if camera permission is not given it will ask for it on device
        if (ContextCompat.checkSelfPermission(this@CameraActivity, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this@CameraActivity,
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA
            )
        }
        setContentView(R.layout.activity_camera)
        mOpenCvCameraView = findViewById<View>(R.id.frame_Surface) as CameraBridgeViewBase
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE)
        mOpenCvCameraView.setCvCameraViewListener(this)
        // this will load cascade classifier and model
        // this only happen one time when you start CameraActivity
        try {
            // input size of model is 48
            val inputSize = 48
            facialExpressionRecognition = facialExpressionRecognition(
                assets, this@CameraActivity,
                "model300.tflite", inputSize
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (OpenCVLoader.initDebug()) {
            //if load success
            Log.d(TAG, "Opencv initialization is done")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        } else {
            //if not loaded
            Log.d(TAG, "Opencv is not loaded. try again")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView()
        }
    }

    fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mGray = Mat(height, width, CvType.CV_8UC1)
    }

    fun onCameraViewStopped() {
        mRgba.release()
    }

    fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat? {
        mRgba = inputFrame.rgba()
        mGray = inputFrame.gray()
        //output                                         input
        mRgba = facialExpressionRecognition.recognizeImage(mRgba)
        return mRgba
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}