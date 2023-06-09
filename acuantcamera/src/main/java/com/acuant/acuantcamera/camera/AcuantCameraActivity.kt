package com.acuant.acuantcamera.camera

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.acuant.acuantcamera.R
import com.acuant.acuantcamera.camera.barcode.AcuantBarcodeCameraFragment
import com.acuant.acuantcamera.camera.document.cameraone.DocumentCaptureActivity
import com.acuant.acuantcamera.camera.document.AcuantDocCameraFragment
import com.acuant.acuantcamera.camera.mrz.AcuantMrzCameraFragment
import com.acuant.acuantcamera.camera.AcuantCameraOptions.CameraMode
import com.acuant.acuantcamera.constant.*
import com.acuant.acuantcamera.helper.MrzResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


interface ICameraActivityFinish{
    fun onActivityFinish(imageUrl: String, barCodeString: String?)
    fun onActivityFinish(mrzResult: MrzResult)
    fun onActivityFinish(barCodeString: String?)
}

class AcuantCameraActivity : AppCompatActivity(), ICameraActivityFinish {

    companion object {
        const val RESULT_SUCCESS_CODE = 1
        const val BARCODE_REQUEST = 3
        const val MRZ_REQUEST = 2
        const val DOC_REQUEST = 1
    }

    override fun onActivityFinish(imageUrl: String, barCodeString: String?) {
        val intent = Intent()
        intent.putExtra(ACUANT_EXTRA_IMAGE_URL, imageUrl)
        intent.putExtra(ACUANT_EXTRA_PDF417_BARCODE, barCodeString)
        this@AcuantCameraActivity.setResult(RESULT_SUCCESS_CODE, intent)
        this@AcuantCameraActivity.finish()
    }

    override fun onActivityFinish(mrzResult: MrzResult) {
        val intent = Intent()
        intent.putExtra(ACUANT_EXTRA_MRZ_RESULT, mrzResult)
        this@AcuantCameraActivity.setResult(RESULT_SUCCESS_CODE, intent)
        this@AcuantCameraActivity.finish()
    }

    override fun onActivityFinish(barCodeString: String?) {
        val intent = Intent()
        intent.putExtra(ACUANT_EXTRA_PDF417_BARCODE, barCodeString)
        this@AcuantCameraActivity.setResult(RESULT_SUCCESS_CODE, intent)
        this@AcuantCameraActivity.finish()
    }

    private fun hideTopMenu() {
        @Suppress("DEPRECATION") //this needs android x to use the not deprecated code
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        hideTopMenu()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val unserializedOptions = intent.getSerializableExtra(ACUANT_EXTRA_CAMERA_OPTIONS)
        val isAutoCapture = intent.getBooleanExtra(ACUANT_EXTRA_IS_AUTO_CAPTURE, true)
        val isBorderAllowed = intent.getBooleanExtra(ACUANT_EXTRA_BORDER_ENABLED, true)

        val options: AcuantCameraOptions = if (unserializedOptions == null) {
            AcuantCameraOptions.DocumentCameraOptionsBuilder().setAutoCapture(isAutoCapture).setAllowBox(isBorderAllowed).build()
        } else {
            unserializedOptions as AcuantCameraOptions
        }

        if (options.useGMS) {
            val resultCode = try {
                val googleApi = GoogleApiAvailability.getInstance()
                googleApi.isGooglePlayServicesAvailable(this)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                ConnectionResult.SERVICE_INVALID
            }

            if (resultCode != ConnectionResult.SUCCESS) {
                options.useGMS = false
            }
        }

        if (supportCamera2(this)) {
            setContentView(R.layout.activity_acu_camera)
            hideTopMenu()

            if (null == savedInstanceState) {
                val cameraFragment: AcuantBaseCameraFragment = when (options.cameraMode) {
                    CameraMode.BarcodeOnly -> {
                        AcuantBarcodeCameraFragment.newInstance()
                    }
                    CameraMode.Mrz -> {
                        AcuantMrzCameraFragment.newInstance()
                    }
                    else -> { //Document
                        AcuantDocCameraFragment.newInstance()
                    }
                }
                cameraFragment.arguments = Bundle().apply {
                    putBoolean(ACUANT_EXTRA_IS_AUTO_CAPTURE, isAutoCapture)
                    putBoolean(ACUANT_EXTRA_BORDER_ENABLED, isBorderAllowed)
                    putSerializable(ACUANT_EXTRA_CAMERA_OPTIONS, options)
                }


                if (!options.useGMS) {
                    onActivityFinish(null)
                } else {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container, cameraFragment as Fragment)
                            .commit()
                }
            }
        } else {
            when (options.cameraMode) {
                CameraMode.BarcodeOnly -> {
                    if (options.useGMS) {
                        val cameraIntent = Intent(
                                this@AcuantCameraActivity,
                                com.acuant.acuantcamera.camera.barcode.cameraone.BarcodeCaptureActivity::class.java
                        )

                        cameraIntent.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS, options)

                        startActivityForResult(cameraIntent, BARCODE_REQUEST)
                    } else {
                        onActivityFinish(null)
                    }
                }
                CameraMode.Mrz -> {
                    val cameraIntent = Intent(
                            this@AcuantCameraActivity,
                            com.acuant.acuantcamera.camera.mrz.cameraone.MrzCaptureActivity::class.java
                    )

                    cameraIntent.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS, options)

                    startActivityForResult(cameraIntent, MRZ_REQUEST)
                }
                else -> { //Document
                    val cameraIntent = Intent(
                            this@AcuantCameraActivity,
                            DocumentCaptureActivity::class.java
                    )
                    cameraIntent.putExtra(ACUANT_EXTRA_IS_AUTO_CAPTURE, options.autoCapture)
                    cameraIntent.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS, options)

                    startActivityForResult(cameraIntent, DOC_REQUEST)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            DOC_REQUEST -> {
                if(resultCode == RESULT_SUCCESS_CODE && data != null){
                    onActivityFinish(data.getStringExtra(ACUANT_EXTRA_IMAGE_URL) ?: "-1", data.getStringExtra(ACUANT_EXTRA_PDF417_BARCODE))
                }
                else{
                    this@AcuantCameraActivity.finish()
                }
            }
            MRZ_REQUEST -> {
                if(resultCode == RESULT_SUCCESS_CODE && data != null){
                    onActivityFinish(data.getSerializableExtra(ACUANT_EXTRA_MRZ_RESULT) as MrzResult)
                }
                else{
                    this@AcuantCameraActivity.finish()
                }
            }
            BARCODE_REQUEST -> {
                if(resultCode == RESULT_SUCCESS_CODE && data != null){
                    onActivityFinish(data.getStringExtra(ACUANT_EXTRA_PDF417_BARCODE))
                }
                else{
                    this@AcuantCameraActivity.finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        this@AcuantCameraActivity.finish()
    }

    private fun supportCamera2(activity: AppCompatActivity): Boolean {
        // Check if we're running on Android 5.0 or higher
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.MODEL == "Nexus 9") {
                false
            } else hasSuitableCamera2Camera(activity)
        } else false
    }

    private fun hasSuitableCamera2Camera(activity: AppCompatActivity): Boolean {
        var foundCamera = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val manager = activity.applicationContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                // Find first back-facing camera that has necessary capability.
                val cameraIds = manager.cameraIdList
                for (id in cameraIds) {
                    val info = manager.getCameraCharacteristics(id)
                    val facing = info.get(CameraCharacteristics.LENS_FACING)!!

                    val level = info.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)!!
                    val hasFullLevel = level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL

                    val syncLatency = info.get(CameraCharacteristics.SYNC_MAX_LATENCY)!!
                    val hasEnoughCapability = syncLatency == CameraCharacteristics.SYNC_MAX_LATENCY_PER_FRAME_CONTROL

                    // All these are guaranteed by
                    // CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL, but checking
                    // for only the things we care about expands range of devices we can run on.
                    // We want:
                    //  - Back-facing camera
                    //  - Per-frame synchronization (so that exposure can be changed every frame)
                    if (facing == CameraCharacteristics.LENS_FACING_BACK && (hasFullLevel || hasEnoughCapability)) {
                        // Found suitable camera - get info, open, and set up outputs
                        foundCamera = true
                        break
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return foundCamera

    }
}
