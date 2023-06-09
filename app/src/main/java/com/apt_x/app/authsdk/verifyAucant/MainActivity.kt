package com.apt_x.app.authsdk.verifyAucant

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.acuant.acuantcamera.camera.AcuantCameraActivity
import com.acuant.acuantcamera.camera.AcuantCameraOptions
import com.acuant.acuantcamera.constant.ACUANT_EXTRA_CAMERA_OPTIONS
import com.acuant.acuantcamera.constant.ACUANT_EXTRA_IMAGE_URL
import com.acuant.acuantcamera.constant.ACUANT_EXTRA_MRZ_RESULT
import com.acuant.acuantcamera.constant.ACUANT_EXTRA_PDF417_BARCODE
import com.acuant.acuantcamera.helper.MrzResult
import com.acuant.acuantcamera.initializer.MrzCameraInitializer
import com.acuant.acuantcommon.exception.AcuantException
import com.acuant.acuantcommon.helper.CredentialHelper
import com.acuant.acuantcommon.initializer.AcuantInitializer
import com.acuant.acuantcommon.initializer.IAcuantPackageCallback
import com.acuant.acuantcommon.model.*
import com.acuant.acuantcommon.type.CardSide
import com.acuant.acuantdocumentprocessing.AcuantDocumentProcessor
import com.acuant.acuantdocumentprocessing.model.*
import com.acuant.acuantdocumentprocessing.service.listener.CreateInstanceListener
import com.acuant.acuantdocumentprocessing.service.listener.DeleteListener
import com.acuant.acuantdocumentprocessing.service.listener.GetDataListener
import com.acuant.acuantdocumentprocessing.service.listener.UploadImageListener
import com.acuant.acuantechipreader.initializer.EchipInitializer
import com.acuant.acuantfacecapture.FaceCaptureActivity
import com.acuant.acuantfacematchsdk.AcuantFaceMatch
import com.acuant.acuantfacematchsdk.model.FacialMatchData
import com.acuant.acuantfacematchsdk.service.FacialMatchListener
import com.acuant.acuanthgliveness.model.FaceCapturedImage
import com.acuant.acuantimagepreparation.AcuantImagePreparation
import com.acuant.acuantimagepreparation.background.EvaluateImageListener
import com.acuant.acuantimagepreparation.initializer.ImageProcessorInitializer
import com.acuant.acuantimagepreparation.model.AcuantImage
import com.acuant.acuantimagepreparation.model.CroppingData
import com.acuant.acuantipliveness.AcuantIPLiveness
import com.acuant.acuantipliveness.IPLivenessListener
import com.acuant.acuantipliveness.facialcapture.model.FacialCaptureResult
import com.acuant.acuantipliveness.facialcapture.model.FacialSetupResult
import com.acuant.acuantipliveness.facialcapture.service.FacialCaptureCredentialListener
import com.acuant.acuantipliveness.facialcapture.service.FacialCaptureLisenter
import com.acuant.acuantipliveness.facialcapture.service.FacialSetupLisenter
import com.acuant.acuantpassiveliveness.AcuantPassiveLiveness
import com.acuant.acuantpassiveliveness.model.PassiveLivenessData
import com.acuant.acuantpassiveliveness.model.PassiveLivenessResult
import com.acuant.acuantpassiveliveness.service.PassiveLivenessListener
import com.acuant.sampleapp.backgroundtasks.AcuantTokenService
import com.acuant.sampleapp.backgroundtasks.AcuantTokenServiceListener
import com.apt_x.app.R
import com.apt_x.app.privacy.netcom.Keys
import com.apt_x.app.utils.CommonUtils
import com.apt_x.app.views.activity.kyc.KYCActivity
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, IPLivenessListener {
    private var progressDialog: LinearLayout? = null
    private var progressText: TextView? = null
    private var headerText: TextView? = null
    private var ivBacknew: ImageView? = null
    private var headerTitle: TextView? = null
    private var capturedFrontImage: AcuantImage? = null
    private var capturedBackImage: AcuantImage? = null
    private var capturedSelfieImage: Bitmap? = null
    private var capturedFaceImage: Bitmap? = null
    private var capturedBarcodeString: String? = null
    private var frontCaptured: Boolean = false
    private var isHealthCard: Boolean = false
    private var isRetrying: Boolean = false
//    private var insuranceButton: Button? = null
    private var idButton: Button? = null
    private var capturingImageData: Boolean = true
    private var capturingSelfieImage: Boolean = false
    private var capturingFacialMatch: Boolean = false
    private var facialResultString: String? = null
    private var facialLivelinessResultString: String? = null
    private var captureWaitTime: Int = 0
    private var documentInstanceID: String? = null
    private var autoCaptureEnabled: Boolean = false
    private var barcodeExpected: Boolean = false
    private var numberOfClassificationAttempts: Int = 0
    private var isInitialized = false
    private var livenessSelected = 1
    private var transactionId = ""
    private var isMatch = false
    private var isKeyless = false
    private var processingFacialLiveness = false
    private val useTokenInit = false
    private var recentImage: AcuantImage? = null
//    private lateinit var livenessSpinner : Spinner
//    private lateinit var livenessArrayAdapter: ArrayAdapter<String>

    fun cleanUpTransaction() {
        capturedFrontImage?.destroy()
        capturedBackImage?.destroy()
        facialResultString = null
        capturedFrontImage = null
        capturedBackImage = null
        capturedSelfieImage = null
        capturedFaceImage = null
        facialLivelinessResultString = null
        capturedBarcodeString = null
        isHealthCard = false
        processingFacialLiveness = false
        isRetrying = false
        capturingImageData = true
        documentInstanceID = null
        numberOfClassificationAttempts = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 99)
        }

        idButton = findViewById(R.id.main_id_passport)
        headerText = findViewById(R.id.tvHeader)
        headerTitle = findViewById(R.id.tvTitle)
        ivBacknew = findViewById(R.id.ivBack)


//        livenessSpinner = findViewById(R.id.livenessSpinner)
//        val list = mutableListOf("No liveness test/face match", "Liveness: Passive Liveness", "tmp")
//        livenessArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
//        livenessArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        livenessSpinner.adapter = livenessArrayAdapter
//        loadLastLiveness(true)
//        livenessSpinner.onItemSelectedListener = this


        progressDialog = findViewById(R.id.main_progress_layout)
        progressText = findViewById(R.id.pbText)

        setProgress(true, "Initializing...")

        initializeAcuantSdk(object: IAcuantPackageCallback{
            override fun onInitializeSuccess() {
                this@MainActivity.runOnUiThread {
                    setProgress(false)
                }
            }

            override fun onInitializeFailed(error: List<Error>) {
                this@MainActivity.runOnUiThread {
                    showAcuDialog("Could not initialize.\n"+error[0].errorDescription)
                }
            }
        })
        var type=intent.getStringExtra(Keys.detail)
        if(type.equals(Keys.passport)){
            idButton?.setText(getText(R.string.capture_id_passport))
            idButton?.setText(getText(R.string.capture_id_passport))
            headerText?.setText(getText(R.string.capture_id_passport))
            headerTitle?.setText(getText(R.string.capture_id))
        }
        else
        {
            idButton?.setText(getText(R.string.capture_id_licence))
            headerText?.setText(getText(R.string.capture_id_licence))
            headerTitle?.setText(getText(R.string.capture_id))

        }
    }

    private fun initializeAcuantSdk(callback:IAcuantPackageCallback){
        try{
            // Or, if required to initialize without a config file , then can be done the following way
            Credential.init("admin_merchants_resellers_and_affilliates_of_apt_pay@mra_aptpay.com",
                    "JR2rZ5^CVGtb4^DT",
                    "786f0886-bfcc-4951-8367-a3bc826078ea",
                    "https://frm.acuant.net",
                    "https://services.assureid.net",
                    "https://medicscan.acuant.net",
                    "https://us.passlive.acuant.net",
                    "https://acas.acuant.net",
                    "https://ozone.acuant.net")


            /*
            * ========================================README========================================
            *
            * The following demonstrates how to initialize with both token and credentials,
            * there is no reason to implement both, pick which one fits your needs and use that one.
            *
            * The token initialization workflow also contains a service showing how to get the
            * token. This should NOT be done in the app, this defeats the purpose of tokens. This
            * should be done on a separate server then securely passed to the app.
            *
            * ======================================================================================
            * */
            val initCallback = object: IAcuantPackageCallback{

                override fun onInitializeSuccess() {

                    isInitialized = true
                    if(Credential.get().subscription == null || Credential.get().subscription.isEmpty() ){
                        isKeyless = true
//                        livenessSpinner.visibility = View.INVISIBLE
                        callback.onInitializeSuccess()
                    }
                    else{
                        if(Credential.get().secureAuthorizations.ozoneAuth || Credential.get().secureAuthorizations.chipExtract) {
                            findViewById<Button>(R.id.main_mrz_camera).visibility = View.VISIBLE
                        }
                        getFacialLivenessCredentials(callback)
                    }
                }

                override fun onInitializeFailed(error: List<Error>) {
                    callback.onInitializeFailed(error)
                }
            }
            /*
            * The initFromXml and AcuantTokenService is just for the sample app, you should be
            * generating these tokens on one of your secure servers, passing it to the app,
            * and then calling initializeWithToken passing the config and token.
            */
            @Suppress("ConstantConditionIf")
            if (useTokenInit) {
                Toast.makeText(this@MainActivity, "Using Token Init", Toast.LENGTH_SHORT).show()
                Credential.initFromXml("acuant.config.xml", this)
                AcuantTokenService(Credential.get(), object : AcuantTokenServiceListener {
                    override fun onSuccess(token: String) {

                        if (!isInitialized) {
                            AcuantInitializer.initializeWithToken("acuant.config.xml",
                                    token,
                                    this@MainActivity,
                                    listOf(ImageProcessorInitializer(), EchipInitializer(), MrzCameraInitializer()),
                                    initCallback)
                        } else {
                            if(Credential.setToken(token)) {
                                initCallback.onInitializeSuccess()
                            } else {
                                initCallback.onInitializeFailed(listOf(Error(-2, "Error in setToken\nBad/expired token")))
                            }
                        }

                    }

                    override fun onFail(responseCode: Int) {
                        initCallback.onInitializeFailed(listOf(Error(responseCode, "Error in getToken service.\nCode: $responseCode")))
                    }

                }).execute()
            } else {
                Toast.makeText(this@MainActivity, "Using Credential Init", Toast.LENGTH_SHORT).show()
                AcuantInitializer.initialize("acuant.config.xml",
                        this@MainActivity,
                        listOf(ImageProcessorInitializer(), EchipInitializer(), MrzCameraInitializer()),
                        initCallback)
            }

        } catch(e: AcuantException) {
            Log.e("Acuant Error", e.toString())
            showAcuDialog(e.toString())
        }
    }

    private fun showAcuDialog(message: Int, @Suppress("SameParameterValue") title: String = "Error",
                              yesOnClick: DialogInterface.OnClickListener? = null,
                              noOnClick: DialogInterface.OnClickListener? = null) {
        showAcuDialog(getString(message), title, yesOnClick, noOnClick)
    }

    private fun showAcuDialog(message: String, title: String = "Error",
                              yesOnClick: DialogInterface.OnClickListener? = null,
                              noOnClick: DialogInterface.OnClickListener? = null) {

        setProgress(false)
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle(title)
        alert.setMessage(message)
        if (yesOnClick != null) {
            alert.setPositiveButton("YES", yesOnClick)
            if (noOnClick != null) {
                alert.setNegativeButton("NO", noOnClick)
            }
        } else {
            alert.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }
        alert.show()
    }

    private fun getFacialLivenessCredentials(callback: IAcuantPackageCallback){
        AcuantIPLiveness.getFacialCaptureCredential(object:FacialCaptureCredentialListener{
            override fun onDataReceived(result: Boolean) {
                if(result){
                    runOnUiThread{
//                        livenessArrayAdapter.remove("tmp")
//                        livenessArrayAdapter.insert("Liveness: Enhanced Liveness",2)
                        loadLastLiveness(true)
                    }
                } else {
//                    livenessArrayAdapter.remove("tmp")
                    loadLastLiveness()
                }
                callback.onInitializeSuccess()
            }

            override fun onError(errorCode: Int, description: String) {
                Log.e("", description)
                loadLastLiveness()
                callback.onInitializeSuccess()
                //callback.onInitializeFailed(listOf())
            }
        })
    }

    private fun loadLastLiveness(hasEnhanced: Boolean = false) {
        val prefs = this.getSharedPreferences("com.acuant.sampleapp", Context.MODE_PRIVATE)
        val lastSel = prefs.getInt("lastLiveness", 0)
        if ((hasEnhanced && lastSel == 2) || lastSel < 2) {
//            livenessSpinner.setSelection(lastSel)
//            livenessSelected = lastSel
//            livenessArrayAdapter.notifyDataSetChanged()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //do nothing
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        livenessSelected = position

        val prefs = this.getSharedPreferences("com.acuant.sampleapp", Context.MODE_PRIVATE)
        prefs.edit().putInt("lastLiveness", position).apply()
    }

    private fun setProgress(visible : Boolean, text : String = "") {
        if(visible) {
            progressDialog?.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            progressDialog?.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        progressText?.text = text
    }

    private fun readFromFile(fileUri: String): ByteArray{
        val file = File(fileUri)
        val bytes = ByteArray(file.length().toInt())
        println("READFROMFILE ${bytes.size.toString()}")
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
        file.delete()
        return bytes
    }

    private fun handleKeyless(acuantImage: AcuantImage?) {
        handleKeyless(acuantImage?.isPassport)
    }

    private fun handleKeyless(isPassport: Boolean?){
        if(isPassport == true || frontCaptured){
            showHGLiveness(Constants.REQUEST_CAMERA_HG_SELFIE_KEYLESS)
        }
        else{
            this@MainActivity.runOnUiThread {
                showAcuDialog(R.string.scan_back_side_id, "Message",
                    { dialog, _ ->
                frontCaptured = true
                dialog?.dismiss()
                captureWaitTime = 2
                showDocumentCaptureCamera()
            }, { dialog, _ -> dialog?.dismiss() })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CAMERA_PHOTO && resultCode == AcuantCameraActivity.RESULT_SUCCESS_CODE) {
            val url = data?.getStringExtra(ACUANT_EXTRA_IMAGE_URL)
            capturedBarcodeString = data?.getStringExtra(ACUANT_EXTRA_PDF417_BARCODE)
            if (url != null) {
                setProgress(true, "Cropping...")
                AcuantImagePreparation.evaluateImage(this, CroppingData(url), object : EvaluateImageListener {

                    override fun onSuccess(image: AcuantImage) {
                        print("IMage Data****${image.image}")

                        //Enable for debug saving
                        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                  /*      try {
                            val output = FileOutputStream(File(Environment.getExternalStorageDirectory().toString(), "test.png"))
                            image.image.compress(Bitmap.CompressFormat.PNG, 100, output)
                            output.flush()
                            output.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }*/
                       // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        setProgress(false)
                        if (isKeyless) {
                            handleKeyless(image)
                        } else {
                            recentImage = image

                            showConfirmation(!frontCaptured, false)
                        }
                    }

                    override fun onError(error: Error) {
                        showAcuDialog(error.errorDescription)
                    }
                })
            } else {
                showAcuDialog("Camera failed to return valid image path")
            }
        } else if (requestCode == Constants.REQUEST_CAMERA_MRZ && resultCode == AcuantCameraActivity.RESULT_SUCCESS_CODE) {
            val result = data?.getSerializableExtra(ACUANT_EXTRA_MRZ_RESULT) as MrzResult

            val confirmNFCDataActivity = Intent(this, NfcConfirmationActivity::class.java)
            confirmNFCDataActivity.putExtra("DOB", result.dob)
            confirmNFCDataActivity.putExtra("DOE", result.passportExpiration)
            confirmNFCDataActivity.putExtra("DOCNUMBER", result.passportNumber)
            confirmNFCDataActivity.putExtra("COUNTRY", result.country)
            confirmNFCDataActivity.putExtra("THREELINE", result.threeLineMrz)

            this.startActivity(confirmNFCDataActivity)
        } else if (requestCode == Constants.REQUEST_CAMERA_BARCODE && resultCode == AcuantCameraActivity.RESULT_SUCCESS_CODE) {
            capturedBarcodeString = data?.getStringExtra(ACUANT_EXTRA_PDF417_BARCODE)

            setProgress(true, "Uploading...")

            val alert = AlertDialog.Builder(this@MainActivity)
            alert.setTitle("Message")
            if (livenessSelected != 0) {
                alert.setMessage("Capture Selfie Image now.")
            } else {
                alert.setMessage("Continue.")
            }
            alert.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                setProgress(true, "Getting Data...")
                uploadBackImageOfDocument()
                showFrontCamera()
            }
            if (livenessSelected != 0) {
                alert.setNegativeButton("CANCEL") { dialog, _ ->
                    setProgress(true, "Getting Data...")
                    facialLivelinessResultString = "Facial Liveliness Failed"
                    capturingSelfieImage = false
                    uploadBackImageOfDocument()
                    dialog.dismiss()
                }
            }
            alert.show()

        } else if(requestCode == Constants.REQUEST_HELP_MRZ && resultCode == Constants.REQUEST_HELP_MRZ) {

            showMrzCaptureCamera()

        } else if (requestCode == Constants.REQUEST_CONFIRMATION && resultCode == Constants.REQUEST_CONFIRMATION) {

            val isFront = data!!.getBooleanExtra("isFrontImage", true)
            val isConfirmed = data.getBooleanExtra("Confirmed", true)
            if (isConfirmed) {
                if (isFront) {
                    capturedFrontImage = recentImage
                    if (isHealthCard) {
                        frontCaptured = true
                        showAcuDialog(R.string.scan_back_side_health_insurance_card, "Message",
                            { dialog, _ ->
                                dialog.dismiss()
                                showDocumentCaptureCamera()
                            }, { dialog, _ ->
                                dialog.dismiss()
                                uploadHealthCard()
                            })

                    } else {
                        processFrontOfDocument()
                    }
                } else {
                    capturedBackImage = recentImage
                    if (!isHealthCard) {
                        val alert = AlertDialog.Builder(this@MainActivity)
                        alert.setTitle("Message")
                        if (capturedBarcodeString != null && capturedBarcodeString!!.trim().isNotEmpty()) {
                            if (livenessSelected != 0) {
                                alert.setMessage("Following barcode is captured.\n\n"
                                        + "Barcode String :\n\n"
                                        + capturedBarcodeString!!.subSequence(0, (capturedBarcodeString!!.length * 0.25).toInt())
                                        + "...\n\n"
                                        + "Capture Selfie Image now.")
                            } else {
                                alert.setMessage("Following barcode is captured.\n\n"
                                        + "Barcode String :\n\n"
                                        + capturedBarcodeString!!.subSequence(0, (capturedBarcodeString!!.length * 0.25).toInt()))
                            }
                            alert.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                setProgress(true, "Getting Data...")
                                uploadBackImageOfDocument()
                                showFrontCamera()
                            }
                            if (livenessSelected != 0) {
                                alert.setNegativeButton("CANCEL") { dialog, _ ->
                                    setProgress(true, "Getting Data...")
                                    facialLivelinessResultString = "Facial Liveliness Failed"
                                    capturingSelfieImage = false
                                    uploadBackImageOfDocument()
                                    dialog.dismiss()
                                }
                            }
                        } else if (barcodeExpected) {
                            alert.setMessage("A barcode was expected but was not captured. Please try capturing the barcode.")

                            alert.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                setProgress(true, "Getting Data...")
                                showBarcodeCaptureCamera()
                            }
                        } else {
                            if (livenessSelected != 0) {
                                alert.setMessage("Capture Selfie Image now.")
                            } else {
                                alert.setMessage("Continue.")
                            }
                            alert.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                setProgress(true, "Getting Data...")
                                uploadBackImageOfDocument()
                                showFrontCamera()
                            }
                            if (livenessSelected != 0) {
                                alert.setNegativeButton("CANCEL") { dialog, _ ->
                                    setProgress(true, "Getting Data...")
                                    facialLivelinessResultString = "Facial Liveliness Failed"
                                    capturingSelfieImage = false
                                    uploadBackImageOfDocument()
                                    dialog.dismiss()
                                }
                            }
                        }
                        alert.show()
                    } else {
                        uploadHealthCard()
                    }

                }
            } else {
                showDocumentCaptureCamera()
            }
        } else if (requestCode == Constants.REQUEST_RETRY && resultCode == Constants.REQUEST_RETRY) {
            isRetrying = true
            showDocumentCaptureCamera()
        }
        //this is the old IP liveness workflow. NOT supported any more, but left in for reference if needed.
        /* else if (requestCode == Constants.REQUEST_CAMERA_IP_SELFIE) {
            when (resultCode) {
                ErrorCodes.ERROR_CAPTURING_FACIAL -> showFaceCaptureError()
                ErrorCodes.USER_CANCELED_FACIAL -> {
                    setProgress(true, "Getting Data...")
                    capturingSelfieImage = false
                    facialLivelinessResultString = "Facial Liveliness Failed"
                }
                else -> {
                    val userId = data?.getStringExtra(FacialCaptureConstant.ACUANT_USERID_KEY)!!
                    val token = data.getStringExtra(FacialCaptureConstant.ACUANT_TOKEN_KEY)!!
                    startFacialLivelinessRequest(token, userId)
                }
            }
        } */
        else if (requestCode == Constants.REQUEST_CAMERA_HG_SELFIE){
            if (resultCode == FacialLivenessActivity.RESPONSE_SUCCESS_CODE) {
                capturedSelfieImage = FaceCapturedImage.bitmapImage
                facialLivelinessResultString = "Facial Liveliness: true"
                processFacialMatch()
            } else {
                showFaceCaptureError()
            }
        } else if (requestCode == Constants.REQUEST_CAMERA_FACE_CAPTURE) {

            when (resultCode) {
                FaceCaptureActivity.RESPONSE_SUCCESS_CODE -> {
                    val url = data?.getStringExtra(FaceCaptureActivity.OUTPUT_URL)

                    if (url == null) {
                        showFaceCaptureError()
                        return
                    }

                    processingFacialLiveness = true

                    val bytes = readFromFile(url)
                    capturedSelfieImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val plData = PassiveLivenessData(capturedSelfieImage as Bitmap)
                    AcuantPassiveLiveness.processFaceLiveness(plData, object : PassiveLivenessListener {
                        override fun passiveLivenessFinished(result: PassiveLivenessResult) {
                            facialLivelinessResultString = when (result.livenessAssessment) {
                                AcuantPassiveLiveness.LivenessAssessment.Live -> {
                                    "Facial Liveliness: live"
                                }
                                AcuantPassiveLiveness.LivenessAssessment.NotLive -> {
                                    "Facial Liveliness: not live"
                                }
                                AcuantPassiveLiveness.LivenessAssessment.PoorQuality -> {
                                    "Facial Liveliness: poor quality image (unable to verify liveness)"
                                }
                                else -> {
                                    "Facial Liveliness: ${result.errorCode} - ${result.errorDesc}"
                                }
                            }
                            processingFacialLiveness = false
                        }
                    })
                    processFacialMatch()
                }
                FaceCaptureActivity.RESPONSE_CANCEL_CODE -> {
                    showFaceCaptureCanceled()
                }
                else -> {
                    showFaceCaptureError()
                }
            }
        }
    }

    private fun showFaceCaptureError() {
        showAcuDialog("Would you like to retry?", "Error Capturing Face", { dialog, _ ->
            dialog.dismiss()
            showFrontCamera()
        }, { dialog, _ ->
            dialog.dismiss()
            capturingSelfieImage = false
            facialLivelinessResultString = "Facial Liveliness Failed"
        })
    }

    private fun showFaceCaptureCanceled() {
        showAcuDialog("Would you like to retry?", "User Canceled Face Capture", { dialog, _ ->
            dialog.dismiss()
            showFrontCamera()
        }, { dialog, _ ->
            dialog.dismiss()
            capturingSelfieImage = false
            facialLivelinessResultString = "Facial Liveliness Failed"
        })
    }

    private fun startFacialLivelinessRequest(token: String, userId:String) {
        setProgress(true, "Getting Data...")
        AcuantIPLiveness.getFacialLiveness(
                token,
                userId,
                object: FacialCaptureLisenter {
                    override fun onDataReceived(result: FacialCaptureResult) {
                        facialLivelinessResultString = "Facial Liveliness: " + result.isPassed
                        val decodedString = Base64.decode(result.frame, Base64.NO_WRAP)
                        capturedSelfieImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        setProgress(false)
                        processFacialMatch()
                    }

                    override fun onError(errorCode:Int, errorDescription: String) {
                        capturingSelfieImage = false
                        facialLivelinessResultString = "Facial Liveliness Failed"
                        showAcuDialog(errorDescription, "Error Retreiving Facial Data")
                    }
                }
        )
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun imageclick(@Suppress("UNUSED_PARAMETER") view: View){

        println("Image clicked")
        val intent =   Intent(this@MainActivity, KYCActivity::class.java)

        startActivityForResult(intent, Constants.BACK)
        finish()

    }


    // ID/Passport Clicked
    fun idPassPortClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        Log.e("Start Scan**","  "+currentTime)
        if (!hasInternetConnection()) {
            showAcuDialog("No internet connection available.")
        } else {
            if (isInitialized && (!useTokenInit || Credential.get()?.token?.isValid == true)) {
                frontCaptured = false
                cleanUpTransaction()
                captureWaitTime = 0
                showDocumentCaptureCamera()
            } else {
                setProgress(true, "Initializing...")
                initializeAcuantSdk(object: IAcuantPackageCallback{
                    override fun onInitializeSuccess() {
                        this@MainActivity.runOnUiThread {
                            setProgress(false)
                            frontCaptured = false
                            cleanUpTransaction()
                            captureWaitTime = 0
                            showDocumentCaptureCamera()
                        }
                    }

                    override fun onInitializeFailed(error: List<Error>) {
                        this@MainActivity.runOnUiThread {
                            showAcuDialog("Could not initialize")
                        }
                    }
                })
            }
        }
    }

    // Health Insurance Clicked
    fun healthInsuranceClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        if (!hasInternetConnection()) {
            showAcuDialog("No internet connection available.")
        } else {
            if (isInitialized && (!useTokenInit || Credential.get()?.token?.isValid == true)) {
                frontCaptured = false
                cleanUpTransaction()
                isHealthCard = true
                captureWaitTime = 0
                showDocumentCaptureCamera()
            } else {
                setProgress(true, "Initializing...")
                initializeAcuantSdk(object: IAcuantPackageCallback{
                    override fun onInitializeSuccess() {
                        this@MainActivity.runOnUiThread {
                            frontCaptured = false
                            cleanUpTransaction()
                            isHealthCard = true
                            captureWaitTime = 0
                            showDocumentCaptureCamera()
                            setProgress(false)
                        }
                    }

                    override fun onInitializeFailed(error: List<Error>) {
                        this@MainActivity.runOnUiThread {
                            showAcuDialog("Could not initialize")
                        }
                    }
                })
            }
        }
    }


    fun readMrzClicked(@Suppress("UNUSED_PARAMETER") view: View) {

        if (!hasInternetConnection()) {
            showAcuDialog("No internet connection available.")
        } else {
            if (isInitialized && (!useTokenInit || Credential.get()?.token?.isValid == true)) {
                frontCaptured = false
                cleanUpTransaction()
                captureWaitTime = 0
                showMrzHelpScreen()
            } else {
                setProgress(true, "Initializing...")
                initializeAcuantSdk(object: IAcuantPackageCallback{
                    override fun onInitializeSuccess() {
                        this@MainActivity.runOnUiThread {
                            setProgress(false)
                            frontCaptured = false
                            cleanUpTransaction()
                            captureWaitTime = 0
                            showMrzHelpScreen()
                        }
                    }

                    override fun onInitializeFailed(error: List<Error>) {
                        this@MainActivity.runOnUiThread {
                            showAcuDialog("Could not initialize")
                        }
                    }
                })
            }
        }
    }

    private fun showMrzHelpScreen() {
        val intent = Intent(this, MrzHelpActivity::class.java)

        startActivityForResult(intent, Constants.REQUEST_HELP_MRZ)
    }

    private fun showMrzCaptureCamera() {
        val cameraIntent = Intent(
                this@MainActivity,
                AcuantCameraActivity::class.java
        )
        cameraIntent.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS,
                AcuantCameraOptions
                        .MrzCameraOptionsBuilder()
                        .build()
        )
        startActivityForResult(cameraIntent, Constants.REQUEST_CAMERA_MRZ)
    }

    //Show front camera to capture face for passive liveness
    private fun showFaceCapture() {
        val cameraIntent = Intent(
                this@MainActivity,
                FaceCaptureActivity::class.java
        )

        //Optional, should only be used if you are changing some of the options,
        // pointless to pass default options
        //
        //cameraIntent.putExtra(ACUANT_EXTRA_FACE_CAPTURE_OPTIONS, FaceCaptureOptions())

        startActivityForResult(cameraIntent, Constants.REQUEST_CAMERA_FACE_CAPTURE)
    }

    //Show Rear Camera to Capture Image of ID,Passport or Health Insurance Card
    fun showDocumentCaptureCamera() {

        capturedBarcodeString = null
        val cameraIntent = Intent(
                this@MainActivity,
                AcuantCameraActivity::class.java
        )
        cameraIntent.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS,
                AcuantCameraOptions
                        .DocumentCameraOptionsBuilder()
                        .setAutoCapture(autoCaptureEnabled)
                        .build()
        )
        startActivityForResult(cameraIntent, Constants.REQUEST_CAMERA_PHOTO)
    }

    //Show Rear Camera to Capture Image of ID,Passport or Health Insurance Card
    private fun showBarcodeCaptureCamera() {

        capturedBarcodeString = null
        val cameraIntent = Intent(
                this@MainActivity,
                AcuantCameraActivity::class.java
        )
        cameraIntent.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS,
                AcuantCameraOptions
                        .BarcodeCameraOptionsBuilder()
                        .build()
        )
        startActivityForResult(cameraIntent, Constants.REQUEST_CAMERA_BARCODE)
    }

    //Show Front Camera to Capture Live Selfie
    fun showFrontCamera() {
        try {

            when (livenessSelected) {
                2 -> {
                    capturingSelfieImage = true
                    showIPLiveness()
                }
                1 -> {
                    capturingSelfieImage = true
                    showFaceCapture()
                    //showHGLiveness(Constants.REQUEST_CAMERA_HG_SELFIE)
                }
                else -> {
                    capturingSelfieImage = false
                    //just go to results
                }
            }

        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showIPLiveness() {
        setProgress(true, "Loading...")
        AcuantIPLiveness.getFacialSetup(object :FacialSetupLisenter {
            override fun onDataReceived(result: FacialSetupResult?) {
                setProgress(false)
                if (result != null) {
                    result.allowScreenshots = true
                    //this is the old IP liveness workflow. NOT supported any more, but left in for reference if needed.
                    /*val facialIntent = AcuantIPLiveness.getFacialCaptureIntent(this@MainActivity, result)
                    startActivityForResult(facialIntent, Constants.REQUEST_CAMERA_IP_SELFIE)*/
                    AcuantIPLiveness.runFacialCapture(this@MainActivity, result, this@MainActivity)
                } else {
                    handleInternalError()
                }
            }

            override fun onError(errorCode: Int, description: String?) {
                setProgress(false)
                handleInternalError()
            }
        })
    }

    @Suppress("SameParameterValue")
    private fun showHGLiveness(requestCode: Int) {
        val cameraIntent = Intent(
                this@MainActivity,
                FacialLivenessActivity::class.java
        )

        //cameraIntent.putExtra(Constants.HG_FRAME_RATE_TARGET, 10f)

        startActivityForResult(cameraIntent, requestCode)
    }

    fun handleInternalError() {
        runOnUiThread {
            showAcuDialog("Would you like to retry?", "Internal Error", DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                showFrontCamera()
            }, DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                capturingSelfieImage = false
                facialLivelinessResultString = "Facial Liveliness Failed"
            })
        }
    }

    //process health card images
    private fun uploadHealthCard() {
        this@MainActivity.runOnUiThread {
            setProgress(true, "Processing...")
        }
        val idOptions = IdOptions()
        idOptions.cardSide = CardSide.Front
        idOptions.isHealthCard = true
        idOptions.isRetrying = false
        val frontData = if (capturedFrontImage?.rawBytes != null) {
            EvaluatedImageData(capturedFrontImage!!.rawBytes)
        } else {
            showAcuDialog("Image bytes were null.")
            return
        }

        AcuantDocumentProcessor.createInstance(idOptions, object : CreateInstanceListener {
            override fun instanceCreated(instanceId: String?, error: Error?) {
                if (error == null) {
                    // Success : Instance Created
                    documentInstanceID = instanceId
                    print("instances  ID*******${instanceId}")
                    // Upload front image
                    AcuantDocumentProcessor.uploadImage(documentInstanceID,frontData,idOptions, object : UploadImageListener {
                        override fun imageUploaded(error: Error?,classification: Classification?) {
                            if (error == null) {
                                if (capturedBackImage != null) {
                                    // Upload back image
                                    val backData = if (capturedBackImage?.rawBytes != null) {
                                        EvaluatedImageData(capturedBackImage!!.rawBytes)
                                    } else {
                                        showAcuDialog("Image bytes were null.")
                                        return
                                    }
                                    idOptions.cardSide = CardSide.Back
                                    AcuantDocumentProcessor.uploadImage(documentInstanceID, backData, idOptions, object: UploadImageListener {
                                        override fun imageUploaded(error: Error?,classification: Classification?) {
                                            if (error == null) {
                                                getHealthCardData()
                                            } else {
                                                showAcuDialog(error.errorDescription)
                                            }
                                        }

                                    })
                                } else {
                                    getHealthCardData()
                                }
                            } else {
                                showAcuDialog(error.errorDescription)
                            }
                        }
                    })
                } else {
                    // Failure
                    this@MainActivity.runOnUiThread {
                        showAcuDialog(error.errorDescription)
                    }
                }
            }
        })
    }

    // health card data
    fun getHealthCardData(){
        // Get Data
        AcuantDocumentProcessor.getData(documentInstanceID, true, object: GetDataListener {
            override fun processingResultReceived(result: ProcessingResult?) {
                this@MainActivity.runOnUiThread {
                    setProgress(false)
                }
                if (result == null || result.error != null) {
                    showAcuDialog(result?.error?.errorDescription ?: ErrorDescriptions.ERROR_DESC_CouldNotGetHealthCardData)
                } else {
                    val healthCardResult = result as HealthInsuranceCardResult
                    val resultStr = CommonUtils.stringFromResult(healthCardResult)
                    showHealthCardResults(null, null, null, healthCardResult.frontImage, healthCardResult.backImage, null, null, resultStr)
                    AcuantDocumentProcessor.deleteInstance(healthCardResult.instanceID, DeleteType.MedicalCard, object : DeleteListener
                    {
                        override fun instanceDeleted(success: Boolean) {
                            if (success) {
                                Log.d("DELETE", "Medical Card Instance Deleted successfully")
                            }
                        }
                    })
                }
            }
        })
    }


    // Process Front image
    private fun processFrontOfDocument() {
        this@MainActivity.runOnUiThread {
            setProgress(true, "Uploading  & Classifying...")
        }

        val idOptions = IdOptions()
        idOptions.cardSide = CardSide.Front
        idOptions.isHealthCard = false
        idOptions.isRetrying = isRetrying

        val frontData = if (capturedFrontImage?.rawBytes != null) {
            EvaluatedImageData(capturedFrontImage!!.rawBytes)
        } else {
            showAcuDialog("Image bytes were null.")
            return
        }

        if (isRetrying) {
            uploadFrontImageOfDocument(documentInstanceID!!, frontData, idOptions)

        } else {
            AcuantDocumentProcessor.createInstance(idOptions, object: CreateInstanceListener {
                override fun instanceCreated(instanceId: String?, error: Error?) {
                    if (error == null) {
                        // Success : Instance Created
                        documentInstanceID = instanceId
                        uploadFrontImageOfDocument(instanceId!!, frontData, idOptions)

                    } else {
                        // Failure
                        this@MainActivity.runOnUiThread {
                            showAcuDialog(error.errorDescription)
                        }
                    }
                }
            })
        }
    }

    // Upload front Image of Driving License
    fun uploadFrontImageOfDocument(instanceId: String, frontData: EvaluatedImageData, idOptions: IdOptions) {
        numberOfClassificationAttempts += 1
        // Upload front Image of DL
        Log.d("InstanceId:", instanceId)
        AcuantDocumentProcessor.uploadImage(instanceId, frontData, idOptions, object : UploadImageListener {
                override fun imageUploaded(error: Error?, classification: Classification?) {
                    if (error == null) {
                        // Successfully uploaded
                        setProgress(false)
                        frontCaptured = true
                        barcodeExpected = classification?.type?.referenceDocumentDataTypes?.contains(0) ?: false
                        if (isBackSideRequired(classification)) {
                            this@MainActivity.runOnUiThread {
                                showAcuDialog(R.string.scan_back_side_id, "Message", DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                    captureWaitTime = 2
                                    showDocumentCaptureCamera()
                                }, DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                })
                            }
                        }
                        else {
                            setProgress(true,"Please Wait")
                            getData()

                          /*  val alert = AlertDialog.Builder(this@MainActivity)

                            alert.setTitle("Message")

                            if (livenessSelected != 0) {
                                alert.setMessage("Capture Selfie Image")
                            } else {
                                alert.setMessage("Continue")
                            }
                            alert.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                setProgress(true, "Getting Data...")
                                //showFrontCamera()
                                getData()
                            }
                            if (livenessSelected != 0) {
                                alert.setNegativeButton("CANCEL") { dialog, _ ->
                                    facialLivelinessResultString = "Facial Liveliness Failed"
                                    setProgress(true, "Getting Data...")
                                    getData()
                                    dialog.dismiss()
                                }
                            }
                            alert.show()*/
                        }

                    } else {
                        // Failure
                        this@MainActivity.runOnUiThread {
                            setProgress(false)
                            if (error.errorCode == ErrorCodes.ERROR_CouldNotClassifyDocument) {
                                showClassificationError()
                            } else {
                                showAcuDialog(error.errorDescription)
                            }
                        }
                    }
                }
            })
    }


    //Upload Back side of Driving License
    private fun uploadBackImageOfDocument() {
        val idOptions = IdOptions()
        idOptions.cardSide = CardSide.Back
        idOptions.isHealthCard = false
        idOptions.isRetrying = false

        val backData = if (capturedBackImage?.rawBytes != null) {
            EvaluatedImageData(capturedBackImage!!.rawBytes, capturedBarcodeString)
        } else {
            showAcuDialog("Image bytes were null.")
            return
        }

        AcuantDocumentProcessor.uploadImage(documentInstanceID, backData, idOptions, object : UploadImageListener {
            override fun imageUploaded(error: Error?, classification: Classification?) {
                if (error == null) {
                    getData()
                }
            }
        })
    }

    // Get data
    fun getData() {
        AcuantDocumentProcessor.getData(documentInstanceID,false, object : GetDataListener {
            override fun processingResultReceived(result: ProcessingResult?) {
                if (result == null || result.error != null) {
                    this@MainActivity.runOnUiThread {
                        showAcuDialog(result?.error?.errorDescription ?: ErrorDescriptions.ERROR_DESC_CouldNotGetConnectData)
                    }
                    return
                } else if ((result as IDResult).fields == null || result.fields.dataFieldReferences == null) {
                    this@MainActivity.runOnUiThread {
                        showAcuDialog("Unknown error happened.\nCould not extract data")
                    }
                    return
                }
                var docNumber = ""
                var cardType = "ID1"
                var frontImageUri: String? = null
                var backImageUri: String? = null
                var signImageUri: String? = null
                var faceImageUri: String? = null
                var resultString: String? = ""
                val fieldReferences = result.fields.dataFieldReferences
                for (reference in fieldReferences) {
                    if (reference.key == "Document Class Name" && reference.type == "string") {
                        if (reference.value == "Driver License") {
                            cardType = "ID1"
                        } else if (reference.value == "Passport") {
                            cardType = "ID3"
                        }
                    } else if (reference.key == "Document Number" && reference.type == "string") {
                        docNumber = reference.value
                    } else if (reference.key == "Photo" && reference.type == "uri") {
                        faceImageUri = reference.value
                    } else if (reference.key == "Signature" && reference.type == "uri") {
                        signImageUri = reference.value
                    }
                    else if (reference.key == "Issue Date" && reference.type == "string") {
                      //  signImageUri = reference.value
                        print("Issue Date *******${reference.value}")
                    }

                  

                }

                for (image in result.images.images) {
                    if (image.side == 0) {
                        frontImageUri = image.uri
                    } else if (image.side == 1) {
                        backImageUri = image.uri
                    }
                }

                for (reference in fieldReferences) {
                    if (reference.type == "string") {
                        resultString = resultString + reference.key + ":" + reference.value + System.lineSeparator()
                    }
                }

                resultString = "Authentication Result : " +
                        AuthenticationResult.getString(Integer.parseInt(result.result)) +
                        System.lineSeparator() +
                        System.lineSeparator() +
                        resultString
                thread {
                    val frontImage = loadAssureIDImage(frontImageUri, Credential.get())
                    val backImage = loadAssureIDImage(backImageUri, Credential.get())
                    val faceImage = loadAssureIDImage(faceImageUri, Credential.get())
                    val signImage = loadAssureIDImage(signImageUri, Credential.get())

                    println("frontimageUri::"+frontImageUri)

                    println("backImageUri::"+backImageUri)
                    println("faceImageUri::"+faceImageUri)
                    capturedFaceImage = faceImage
                    MainActivity@ capturingImageData = false
                    while (capturingSelfieImage) {
                        Thread.sleep(100)
                    }
                    this@MainActivity.runOnUiThread {

                        showResults(result.biographic.birthDate, result.biographic.expirationDate, docNumber, frontImage, backImage, faceImage, signImage, resultString, cardType,frontImageUri,backImageUri,faceImageUri)
                    }
                }
            }
        })
    }

    //process Facial Match
    fun processFacialMatch() {
        //MainActivity@ capturingFacialMatch = true
        thread {
            while (capturingImageData) {
                Thread.sleep(100)
            }
            this@MainActivity.runOnUiThread {
                val facialMatchData = FacialMatchData()
                facialMatchData.faceImageOne = capturedFaceImage
                facialMatchData.faceImageTwo = capturedSelfieImage
                if(facialMatchData.faceImageOne != null && facialMatchData.faceImageTwo != null){
                    setProgress(true, "Processing...")
                    AcuantFaceMatch.processFacialMatch(facialMatchData, FacialMatchListener { result ->
                        this@MainActivity.runOnUiThread {
                            if(result!!.error == null) {
                                //this code bugs with proguard and messes with the order, not sure why it was used but the following three lines seem to replace it just fine
                                //val resultStr = CommonUtils.stringFromFacialMatchResult(result)
                                facialResultString = "isMatch: ${result.isMatch}\n"
                                facialResultString += "score: ${result.score}\n"
                                facialResultString += "transactionId: ${result.transactionId}\n"
                                transactionId=result.transactionId
                                isMatch=result.isMatch
                                print("TransactionID*******${transactionId}")
                            } else {
                                showAcuDialog(result.error.errorDescription)
                            }
                        }
                        capturingSelfieImage = false
                        capturingFacialMatch = false
                    })
                }
                else{
                    capturingSelfieImage = false
                    capturingFacialMatch = false
                }
            }
        }
    }

    //Show Confirmation UI
    fun showConfirmation(isFrontImage: Boolean, isBarcode: Boolean) {
        val confirmationIntent = Intent(
                this@MainActivity,
                ConfirmationActivity::class.java
        )
        confirmationIntent.putExtra("isFrontImage", isFrontImage)
        confirmationIntent.putExtra("isBarcode", isBarcode)
        if (recentImage != null) {
            image = recentImage!!.image
            confirmationIntent.putExtra("sharpness", recentImage!!.sharpness)
            confirmationIntent.putExtra("glare", recentImage!!.glare)
            confirmationIntent.putExtra("dpi", recentImage!!.dpi)
            confirmationIntent.putExtra("barcode", capturedBarcodeString)
        }
        startActivityForResult(confirmationIntent, Constants.REQUEST_CONFIRMATION)
    }

    //Show Classification Error
    fun showClassificationError() {
        val classificationErrorIntent = Intent(
                this@MainActivity,
                ClassificationFailureActivity::class.java
        )
        if (recentImage != null) {
            image = recentImage!!.image
        }
        startActivityForResult(classificationErrorIntent, Constants.REQUEST_RETRY)
    }

    //Show Health card Results
    fun showHealthCardResults(dateOfBirth: String?, dateOfExpiry: String?, documentNumber: String?, frontImage: Bitmap?, backImage: Bitmap?, faceImage: Bitmap?, signImage: Bitmap?, resultString: String?) {
        ProcessedData.cleanup()
        ProcessedData.frontImage = frontImage
        ProcessedData.backImage = backImage
        ProcessedData.faceImage = faceImage
        ProcessedData.signImage = signImage
        ProcessedData.dateOfBirth = dateOfBirth
        ProcessedData.dateOfExpiry = dateOfExpiry
        ProcessedData.documentNumber = documentNumber
        ProcessedData.isHealthCard = true
        ProcessedData.formattedString = resultString
        ProcessedData.instanceId = transactionId

        val resultIntent = Intent(
                this@MainActivity,
                ResultActivity::class.java
        )
        startActivity(resultIntent)
    }

    // Show ID Result
    fun showResults(dateOfBirth: String?, dateOfExpiry: String?,
                    documentNumber: String?, frontImage: Bitmap?,
                    backImage: Bitmap?, faceImage: Bitmap?,
                    signImage: Bitmap?,
                    resultString: String?,
                    cardType: String?,
                    frontImageUri: String?,
                    backImageUri: String?,
                    faceImageUri: String?
    ) {
        ProcessedData.cleanup()
        ProcessedData.frontImage = frontImage
        ProcessedData.backImage = backImage
        ProcessedData.faceImage = faceImage
        ProcessedData.capturedFaceImage = capturedSelfieImage
        ProcessedData.signImage = signImage
        ProcessedData.dateOfBirth = dateOfBirth
        ProcessedData.dateOfExpiry = dateOfExpiry
        ProcessedData.documentNumber = documentNumber
        ProcessedData.cardType = cardType
        ProcessedData.frontImageUri = frontImageUri
        ProcessedData.backImageUri = backImageUri
        ProcessedData.faceImageUri = faceImageUri
        if (!isHealthCard) {
            thread {
                while (capturingFacialMatch || processingFacialLiveness) {
                    Thread.sleep(100)
                }
                this@MainActivity.runOnUiThread {
                    facialResultString = if (facialResultString == null) "Facial Match Failed" else facialResultString
                    ProcessedData.formattedString =
                        (facialLivelinessResultString ?: "No Liveness Test Performed") +
                                System.lineSeparator() + facialResultString+
                                System.lineSeparator() + resultString
                    ProcessedData.instanceId = transactionId

//                    if(!isMatch){
//                        val facialMatchFailedIntent = Intent(
//                            this@MainActivity,
//                            FacialMatchFailedActivity::class.java
//                        )
//                        if (recentImage != null) {
//                            image = recentImage!!.image
//                        }
//                        startActivity(facialMatchFailedIntent)
//                    }else{
                    val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    Log.e("EndTime Scan**","  "+currentTime)
                    setProgress(false)
                        val resultIntent = Intent(
                            this@MainActivity,
                            ResultActivity::class.java
                        )
                        startActivity(resultIntent)
//                    }
                    setProgress(false)
                }
            }
        } else {
            val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            Log.e("EndTime Scan**","  "+currentTime)
            ProcessedData.formattedString = resultString
            ProcessedData.instanceId = transactionId
            val resultIntent = Intent(
                    this@MainActivity,
                    ResultActivity::class.java
            )

            setProgress(false)
            startActivity(resultIntent)
        }
    }

    fun loadAssureIDImage(url: String?, credential: Credential?): Bitmap? {
        if (url != null && credential != null) {
            val c = URL(url).openConnection() as HttpURLConnection
            val auth = CredentialHelper.getAcuantAuthHeader(credential)
            c.setRequestProperty("Authorization", auth)
            c.useCaches = false
            c.connect()
            val img = BitmapFactory.decodeStream(c.inputStream)
            c.disconnect()
            return img
        }
        return null
    }

    fun isBackSideRequired(classification : Classification?):Boolean{
        var isBackSideScanRequired = false
        if (classification?.type != null && classification.type.supportedImages != null) {
            val list = classification.type.supportedImages as ArrayList<HashMap<*, *>>
            for (i in list.indices) {
                val map = list[i]
                if (map["Light"] == 0) {
                    if (map["Side"] == 1) {
                        isBackSideScanRequired = true
                    }
                }
            }
        }
        return isBackSideScanRequired
    }

    override fun onProgress(status: String, progress: Int) {
        setProgress(true, "$progress%\n$status")
    }

    override fun onSuccess(userId: String, token: String) {
        startFacialLivelinessRequest(token, userId)
    }

    override fun onFail(error: Error) {
        showFaceCaptureError()
    }

    override fun onCancel() {
        setProgress(true, "Getting Data...")
        capturingSelfieImage = false
        facialLivelinessResultString = "Facial Liveliness Failed"
    }

    companion object {
        var image: Bitmap? = null
    }

}