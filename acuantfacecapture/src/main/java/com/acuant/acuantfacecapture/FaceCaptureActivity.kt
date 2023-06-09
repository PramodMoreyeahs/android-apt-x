package com.acuant.acuantfacecapture

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.acuant.acuantfacecapture.detector.FaceDetector
import com.acuant.acuantfacecapture.detector.FaceListener
import com.acuant.acuantfacecapture.detector.FaceProcessor
import com.acuant.acuantfacecapture.model.FaceCaptureOptions
import com.acuant.acuantfacecapture.model.FaceDetailState
import com.acuant.acuantfacecapture.model.FaceDetails
import com.acuant.acuantfacecapture.overlays.CameraSourcePreview
import com.acuant.acuantfacecapture.overlays.FacialGraphic
import com.acuant.acuantfacecapture.overlays.FacialGraphicOverlay
import com.acuant.acuantimagepreparation.AcuantImagePreparation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class FaceCaptureActivity : AppCompatActivity(), FaceListener {

    private var mCameraSource: CameraSource? = null
    private lateinit var mPreview: CameraSourcePreview
    private lateinit var mFacialGraphicOverlay: FacialGraphicOverlay
    private var mFacialGraphic: FacialGraphic? = null
    private var faceCaptureStarted = false
    private var liveFaceDetector: FaceDetector? = null
    private lateinit var faceImage: ImageView
    private var options: FaceCaptureOptions? = null
    private var uri: Uri? = null
    private var photoFile: File? = null
    private var email: String? = null
    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_capture)

        options = if (intent.hasExtra(ACUANT_EXTRA_FACE_CAPTURE_OPTIONS)) {
            intent.getSerializableExtra(ACUANT_EXTRA_FACE_CAPTURE_OPTIONS) as FaceCaptureOptions
        } else {
            null
        }


        if (intent.getStringExtra("email") != null && !intent.getStringExtra("email").equals("") ){

            email=intent.getStringExtra("email").toString()
        }

            mPreview = findViewById(R.id.preview)
        mFacialGraphicOverlay = findViewById(R.id.faceOverlay)
        faceImage = findViewById(R.id.blank_face_image)
        faceImage.imageAlpha = 153

        mFacialGraphicOverlay.setOptions(options)

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            requestCameraPermission()
        }
    }

    private fun savenewResult(data: Bitmap?) {
        val result = Intent()
        if (data != null) {
            val file = File(applicationContext.cacheDir, "${UUID.randomUUID()}.jpg")
            result.putExtra(OUTPUT_URL, file.absolutePath)
            println("Capture Image Path ${file.absolutePath}")
          //  photoFile = createIm
            //  ageFileWith();
            saveFile(file, data)

            absolutePath = file.absolutePath


            var imgPath = file.absolutePath
            val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("capImgNew", imgPath)
            editor.commit()
        }

        val myIntent = Intent(this, Class.forName("com.apt_x.app.views.activity.signup.CaptureImageActivity"))
        startActivity(myIntent.putExtra("email",email))
        this@FaceCaptureActivity.finish()

    }


    @Throws(IOException::class)
    private fun createImageFileWith(): File? {
        val imageFileName = "JPEG_"
        val storageDir: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "pics"
        )
        storageDir.mkdirs()
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }


    fun setIntent(cObjection: Class<*>, isFrom: Int) {
        startActivity(Intent(this, cObjection))
        when (isFrom) {
            1 -> {
                //just no need to finish
            }
            2 ->
                //just finishing the single activity
                finish()
            3 ->
                //finishing all previous activity
                finishAffinity()
        }
    }

    private fun saveResult(data: Bitmap?) {
        val result = Intent()
        if (data != null) {
            val file = File(applicationContext.cacheDir, "${UUID.randomUUID()}.jpg")
            Log.e("Capture Image url", file.absolutePath)
            val bitmap = AcuantImagePreparation.resize(data, 720)
            if (bitmap != null) {
                saveFile(file, bitmap)
            } else {
                Log.e("Acuant", "resize error")

            }
            result.putExtra(OUTPUT_URL, file.absolutePath)
        }
        this@FaceCaptureActivity.setResult(RESPONSE_SUCCESS_CODE, result)
        this@FaceCaptureActivity.finish()

    }


    /**
     * Handles the requesting of the camera permission.  This includes showing a "Snackbar" message
     * of why the permission is needed then sending the request.
     */
    private fun requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission")

        val permissions = arrayOf(Manifest.permission.CAMERA)

        val builder = AlertDialog.Builder(this)
        builder.setMessage("The camera is required to capture faces. If the permission has been declined you will need to manually go to the app settings to enable it.")
            .setOnCancelListener {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
                }
            }
            .setPositiveButton(
                "OK"
            ) { dialog, _ ->
                dialog.dismiss()
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
                }
            }
        builder.create().show()
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        mFacialGraphic = FacialGraphic(mFacialGraphicOverlay)
        mFacialGraphic?.setOptions(options)
        startCameraSource()
    }


    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        mPreview.stop()
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mCameraSource != null) {
            mCameraSource!!.release()
        }
        if (liveFaceDetector != null) {
            liveFaceDetector!!.release()
        }
    }

    /**
     * Callback for the result from requesting permissions. This method is invoked for every call on
     * [.requestPermissions].
     *
     *
     *
     * **Note:** It is possible that the permissions request interaction with the user
     * is interrupted. In this case you will receive empty permissions and results arrays which
     * should be treated as a cancellation.
     *
     *
     *
     * @param requestCode  The request code passed in [.requestPermissions].
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [PackageManager.PERMISSION_GRANTED]
     * or [PackageManager.PERMISSION_DENIED]. Never null.
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source")
            // we have permission, so create the camera source
            createCameraSource()
            return
        }

        Log.e(
            TAG, "Permission not granted: results len = " + grantResults.size +
                    " Result code = " + if (grantResults.isNotEmpty()) grantResults[0] else "(empty)"
        )

        val listener = DialogInterface.OnClickListener { _, _ -> finish() }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.camera_load_error)
            .setMessage(R.string.no_camera_permission)
            .setPositiveButton(R.string.ok, listener)
            .show()
    }

    //==============================================================================================
    // UI
    //==============================================================================================

    /**
     * Saves the camera facing mode, so that it can be restored after the device is rotated.
     */
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
    }

    //==============================================================================================
    // Camera Source
    //==============================================================================================

    /**
     * Creates the face detector and the camera.
     */

    private fun createCameraSource() {
        val context = applicationContext
        liveFaceDetector =
            FaceProcessor.initLiveFaceDetector(context, this, options?.totalCaptureTime ?: 2)
        val facing = CameraSource.CAMERA_FACING_FRONT
        // The camera source is initialized to use either the front or rear facing camera.  We use a
        // relatively low resolution for the camera preview, since this is sufficient for this app
        // and the face detector will run faster at lower camera resolutions.
        //
        // However, note that there is a speed/accuracy trade-off with respect to choosing the
        // camera resolution.  The face detector will run faster with lower camera resolutions,
        // but may miss smaller faces, landmarks, or may not correctly detect eyes open/closed in
        // comparison to using higher camera resolutions.  If you have any of these issues, you may

        // want to increase the resolution.
        mCameraSource = CameraSource.Builder(context, liveFaceDetector)
            .setFacing(facing)
            .setRequestedFps(10.0f)
            .setAutoFocusEnabled(true)
            .build()
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        // check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
            applicationContext
        )
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            dlg?.show()
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource!!, mFacialGraphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                mCameraSource!!.release()
                mCameraSource = null
            }

        }
    }

    override fun faceCaptured(faceDetails: FaceDetails) {
        runOnUiThread {
            if (faceDetails.error == null) {
                if (faceDetails.state != FaceDetailState.NONE) {
                    faceImage.clearAnimation()
                    faceImage.visibility = View.INVISIBLE
                } else {
                    faceImage.clearAnimation()
                    faceImage.visibility = View.VISIBLE
                }
                mFacialGraphicOverlay.setState(faceDetails.state, faceDetails.countdownToCapture)
                mFacialGraphicOverlay.add(mFacialGraphic!!)
                mFacialGraphic!!.updateLiveFaceDetails(faceDetails)

                if (faceDetails.state == FaceDetailState.FACE_GOOD_DISTANCE) {
                    if (!faceCaptureStarted && faceDetails.countdownToCapture == 0) {
                        faceCaptureStarted = true
                        if (intent.getStringExtra("captureselfie") != null && intent.getStringExtra(
                                "captureselfie"
                            ).equals("true")
                        ) {
                            println("CAPTURE IMAGE TRUE")
                            savenewResult(faceDetails.image);


                        } else {
                            println("CAPTURE IMAGE false")
                            saveResult(faceDetails.image)
                        }
                        //saveResult(faceDetails.image)  // old flow without capture image
                    }
                } else {
                    faceCaptureStarted = false
                }
            } else {
                faceImage.clearAnimation()
                faceImage.visibility = View.VISIBLE
                mFacialGraphicOverlay.clear()
            }
        }
    }

    override fun onBackPressed() {
        val result = Intent()
        this@FaceCaptureActivity.setResult(RESPONSE_CANCEL_CODE, result)
        this@FaceCaptureActivity.finish()
    }


    private fun saveFile2(file: File, bitmap: Bitmap?) {
        if (bitmap != null) {
            var output: FileOutputStream? = null
            try {

                output = FileOutputStream(file)
                println("fddfgdffdfg" + output)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                output?.let {
                    try {
                        it.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun saveFile(file: File, bitmap: Bitmap?) {
        if (bitmap != null) {
            var output: FileOutputStream? = null
            try {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                bitmap.recycle()
                output = FileOutputStream(file).apply { write(byteArray) }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                output?.let {
                    try {
                        it.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun saveFile1(file: File) {
        var output: FileOutputStream? = null
        try {
            val stream = ByteArrayOutputStream()
            val byteArray = stream.toByteArray()
            output = FileOutputStream(file).apply { write(byteArray) }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            output?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        private const val TAG = "FaceCapture"

        private const val RC_HANDLE_GMS = 9001

        // permission request codes need to be < 256
        private const val RC_HANDLE_CAMERA_PERM = 2

        const val RESPONSE_SUCCESS_CODE = 2
        const val RESPONSE_SUCCESS_CODE_CAPTUREIMG = 5
        const val RESPONSE_CANCEL_CODE = 4
        var absolutePath = ""
        const val OUTPUT_URL = "outputUrl"
        private const val ACUANT_EXTRA_FACE_CAPTURE_OPTIONS = "faceCaptureOptions"
    }
}