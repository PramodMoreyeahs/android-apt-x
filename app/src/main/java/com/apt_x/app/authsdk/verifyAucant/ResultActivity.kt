package com.apt_x.app.authsdk.verifyAucant

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.acuant.acuantcommon.model.Credential
import com.apt_x.app.R
import com.apt_x.app.model.AddAddressResponse
import com.apt_x.app.model.KYCResponse
import com.apt_x.app.preferences.MyPref
import com.apt_x.app.preferences.Pref
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls
import com.apt_x.app.utils.Utils
import com.apt_x.app.views.activity.home.HomeActivity
import com.apt_x.app.views.activity.kyc.KYCViewModel
import com.apt_x.app.views.activity.kyc.KYCfailedActivity
import com.apt_x.app.views.activity.signup.ThankYouActivity
import com.apt_x.app.views.activity.verification.AddAddressActivity
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ResultActivity : AppCompatActivity() {

    private lateinit var imgFaceViewer: ImageView
    private lateinit var imgSignatureViewer: ImageView
    private lateinit var frontSideCardImageView: ImageView
    private lateinit var backSideCardImageView: ImageView
    private lateinit var textViewCardInfo: TextView
    private lateinit var nfcScanningBtn: Button
    private lateinit var imgCapturedFaceViewer: ImageView
    var kycViewModel: KYCViewModel? = null
    var file: File? = null
    var BirthPlace = ""
    var DocumentClassName = ""
    var DocumentNumber = ""
    var FullName = ""
    var GivenName = ""
    var bitmapImg: Bitmap? = null
    var IssueDate = 0
    var IssuingAuthorityNative = ""
    var IssuingStateCode = ""
    var IssuingStateName = ""
    var Surname = ""
    var Address = ""
    var Authentication = "Passed"
    var apiCalls: ApiCalls? = null
    var issueDate = ""
    var issueStateName = ""

    var AuthenticationResult = ""
    var issueStateCode = ""
    var issueAuth = ""
    var issuingStatename = ""
    var state = ""
    var addres = ""
    var city = ""
    var postalCode = ""


    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        Log.e("EndTime Scan**", "  " + currentTime)

        kycViewModel = ViewModelProviders.of(this).get(KYCViewModel::class.java)
        apiCalls = ApiCalls.getInstance(this)

        kycViewModel!!.response_validator.observe(this, responseObserver)
        kycViewModel!!.response_validator_kyc.observe(this, responseObserverKyc)
        kycViewModel!!.response_validator_addAddress.observe(this,addressResponseObserver)

        frontSideCardImageView = findViewById(R.id.frontSideCardImage)
        backSideCardImageView = findViewById(R.id.backSideCardImage)
        imgFaceViewer = findViewById(R.id.faceImage)
        imgCapturedFaceViewer = findViewById(R.id.faceImageCaptured)
        imgSignatureViewer = findViewById(R.id.signatureImage)
        textViewCardInfo = findViewById(R.id.textViewLicenseCardInfo)
        nfcScanningBtn = findViewById(R.id.buttonNFC)

        if (ProcessedData.cardType.equals(
                "ID3",
                true
            ) && (Credential.get().secureAuthorizations.ozoneAuth || Credential.get().secureAuthorizations.chipExtract)
        ) {
            nfcScanningBtn.visibility = View.VISIBLE
        } else {
            nfcScanningBtn.visibility = View.GONE
        }

        if (ProcessedData.frontImage != null) {
            frontSideCardImageView.setImageBitmap(ProcessedData.frontImage)
        }
        if (ProcessedData.backImage != null) {
            backSideCardImageView.setImageBitmap(ProcessedData.backImage)
        }
        if (ProcessedData.faceImage != null) {
            imgFaceViewer.setImageBitmap(ProcessedData.faceImage)
        }
        if (ProcessedData.capturedFaceImage != null) {
            imgCapturedFaceViewer.setImageBitmap(ProcessedData.capturedFaceImage)

            val baos = ByteArrayOutputStream()
            ProcessedData.capturedFaceImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            val compressImage: ByteArray = baos.toByteArray()
            val sEncodedImage: String = Base64.encodeToString(compressImage, Base64.DEFAULT)

            MyPref.getInstance(applicationContext)
                .writePrefs(MyPref.USER_SELFI1, sEncodedImage)
        }
        if (ProcessedData.signImage != null) {
            imgSignatureViewer.setImageBitmap(ProcessedData.signImage)
        }
        if (ProcessedData.formattedString != null) {
            textViewCardInfo.text = ProcessedData.formattedString
        }


//        val lines: List<String> = ProcessedData.formattedString.split("\\r?\\n")

        val lstValues: List<String> = ProcessedData.formattedString.split("\n").map { it -> it.trim() }
        lstValues.forEach { it ->
            Log.i("Values", "value=$it")
        }
        var arr = lstValues.toTypedArray()
        arr.forEach {
            Log.i("ArrayItem", " Array item=" + it)

            if (it.contains("Authentication Result")) {
                AuthenticationResult = it.split(":")[1]
                Log.i("BirthPlace=", AuthenticationResult)
            }
            if (it.contains("Birth Place")) {
                BirthPlace = it.split(":")[1]
                Log.i("BirthPlace=", BirthPlace)
            }
            if (it.contains("Document Number")) {
                DocumentNumber = it.split(":")[1]
                Log.i("DocumentNumber=", DocumentNumber)

            }
            if (it.contains("Document Class Name")) {
                DocumentClassName = it.split(":")[1].toUpperCase().replace(" ", "_")
                Log.i("DocumentClassName=", DocumentClassName)

            }
            if (it.contains("Full Name")) {
                FullName = it.split(":")[1]
                Log.i("FullName=", FullName)

            }
            if (it.contains("Given Name")) {
                GivenName = it.split(":")[1]
                Log.i("GivenName=", GivenName)

            }
            if (it.contains("Issuing Authority")) {
                IssuingAuthorityNative = it.split(":")[1]
                Log.i("IssuingAuthorityNative=", IssuingAuthorityNative)

            }
            if (it.contains("Issuing State Code")) {
                IssuingStateCode = it.split(":")[1]
                Log.i("IssuingStateCode=", IssuingStateCode)
            }
            if (it.contains("Surname")) {
                Surname = it.split(":")[1]
                println("Surname=" + Surname)
            }
            if (it.contains("Address State")) {
                Address = it.split(":")[1]
                state = it.split(":")[1]
                println("Address State=" + Address)
            }
            if (it.contains("Issue Date")) {
                issueDate = it.split(":")[1]
                // println("Address State=" + Address)
            }
            if (it.contains("Issuing State Name")) {
                issueStateName = it.split(":")[1]
                // println("Address State=" + Address)
            }
            if (it.contains("Birth Place")) {
                BirthPlace = it.split(":")[1]
                // println("Address State=" + Address)
            }
            if (it.contains("Issuing Authority")) {
                issueAuth = it.split(":")[1]
                //println("Address State=" + Address)
            }
            if (it.contains("Issuing State Code")) {
                issueStateCode = it.split(":")[1]
                //println("Address State=" + Address)
            }

            if (it.contains("Issuing State Name")) {
                issuingStatename = it.split(":")[1]
                //println("Address State=" + Address)
            }
            if (it.contains("Address City")) {
                city = it.split(":")[1]
                //println("Address State=" + Address)
            }
            if (it.contains("Address Postal Code")) {
                postalCode = it.split(":")[1]
                //println("Address State=" + Address)
            }
            if (it.contains("Address")) {
                Address = it.split(":")[1]
                println("Addreess=" + Address)
                //println("Address State=" + Address)
            }
            if (it.contains("Address Line 1")) {
                addres = it.split(":")[1]
                println("Addreess=" + addres)
                //println("Address State=" + Address)
            }


        }
    }

    var responseObserver =
        androidx.lifecycle.Observer { kycResponse: KYCResponse ->
            Utils.hideProgressDialog()
            if (kycResponse.status) {
                Pref.setBoolean(this, true, Pref.IS_KYC_FILLED)
                startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
                finish()
            } else {
                Utils.showToast(
                    this,
                    kycResponse.message
                )
            }
        }

    fun nfcPressed(@Suppress("UNUSED_PARAMETER") v: View) {
        val confirmNFCDataActivity = Intent(this, NfcConfirmationActivity::class.java)
        confirmNFCDataActivity.putExtra("DOB", formatDateForNfc(ProcessedData.dateOfBirth))
        confirmNFCDataActivity.putExtra("DOE", formatDateForNfc(ProcessedData.dateOfExpiry))
        confirmNFCDataActivity.putExtra("DOCNUMBER", ProcessedData.documentNumber)
        confirmNFCDataActivity.putExtra("COUNTRY", ProcessedData.country)
        this.startActivity(confirmNFCDataActivity)
    }

    private fun formatDateForNfc(date: String): String {
        var pattern = Regex("[0-9]{2}-[0-9]{2}-[0-9]{4}")
        var out = ""
        if (pattern.matches(date)) {
            out = date.substring(8, 10) + date.substring(0, 2) + date.substring(3, 5)
        }
        pattern = Regex("[0-9]{2}-[0-9]{2}-[0-9]{2}")
        if (pattern.matches(date)) {
            out = date.substring(6, 8) + date.substring(0, 2) + date.substring(3, 5)
        }
        return out
    }


    fun callApiGetEvents(v: View) {

        val aptCardId = MyPref.getInstance(this).readPrefs(MyPref.APPID)
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType = "application/json".toMediaTypeOrNull()
        val arr: List<String> = ProcessedData.frontImageUri.split("Document/")
        val transaction = arr[1].split("/").toTypedArray()
       // Log.e("TRansacrtion", "" + transaction[0])

        val jsonObject1 = JsonObject()
        jsonObject1.addProperty("payeeId", aptCardId)
        jsonObject1.addProperty("instance_id", transaction[0])
       // jsonObject1.addProperty("Birth Date", Utils.convertDate(ProcessedData.dateOfBirth))
        jsonObject1.addProperty("Birth Date",Utils.convertDate(ProcessedData.dateOfBirth))
        println("BIRTHDATE--> ${ProcessedData.dateOfBirth}")
        Pref.IS_DOB = Utils.convertDate(ProcessedData.dateOfBirth)
        MyPref.getInstance(applicationContext).writePrefs(MyPref.DOBSHARED, Utils.convertDate(ProcessedData.dateOfBirth))
        jsonObject1.addProperty("Birth Place", BirthPlace)
        jsonObject1.addProperty("Birth Place Native", BirthPlace)
        jsonObject1.addProperty("Document Class Name", DocumentClassName)
        jsonObject1.addProperty("Document Number", DocumentNumber)
        jsonObject1.addProperty("Expiration Date",Utils.convertDate(ProcessedData.dateOfExpiry))
        jsonObject1.addProperty("Full Name", FullName)
        jsonObject1.addProperty("Full Name Native", FullName)
        jsonObject1.addProperty("Given Name", GivenName)
        jsonObject1.addProperty("Given Name Native", GivenName)
        jsonObject1.addProperty("Issue Place Native", IssuingAuthorityNative)
        jsonObject1.addProperty("Issuing Authority Native", IssuingAuthorityNative)
        jsonObject1.addProperty("Issuing State Code", issueStateCode)
        jsonObject1.addProperty("Issuing State Name", issuingStatename)
        jsonObject1.addProperty("Surname", Surname)
        jsonObject1.addProperty("Surname Native", Surname)
        jsonObject1.addProperty("Authentication", Authentication)
        jsonObject1.addProperty("Photo", ProcessedData.frontImageUri)
        jsonObject1.addProperty("Issue Date", Utils.convertDate(issueDate))
        jsonObject1.addProperty("Issuing Authority", issueAuth)



        print("Data>>>>>>$jsonObject1")
        print("Data  dob>>>>>>${ProcessedData.dateOfBirth}")
        print("Data  convertesdob>>>>>>${Utils.convertDate(ProcessedData.dateOfBirth)}")

        val body = RequestBody.create(
            mediaType, jsonObject1.toString()
        )
    var apiUrl = "https://sec.sandbox.aptpay.com/aptverify/callback" // Staging
      //var apiUrl = "https://sec.aptpay.com/aptverify/callback" // Production


//        if (BuildConfig.BUILD_TYPE.equals("debug", ignoreCase = true)) {
//            apiUrl ="https://sec.sandbox.aptpay.com"+apiUrl
//        } else if (BuildConfig.BUILD_TYPE.equals("release", ignoreCase = true)) {
//            apiUrl =" https://sec.aptpay.com"+apiUrl
//        }

        val request: Request = Request.Builder()
            .url(apiUrl)
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("AptPayApiKey", "ohl9KWxW2rdtx9f3EEmhzQaoAdtQ8d") //Staging
            .build()
     /*   val request: Request = Request.Builder()
            .url(apiUrl)
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("AptPayApiKey", "En9qyQqGezeRdUf7rR6tOJPiq0w5V5") //Production
            .build()*/
        Utils.showDialog(this, getString(R.string.please_wait))
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("On Failure" + e.message)
                // Utils.showToast(this@ResultActivity, getString(R.string.something_went_wrong))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    println("onSuccess")
                    getKyc()
                    //callNextScreen()
                } else {
                    Utils.hideProgressDialog()
                   //  getKyc()
                      Utils.showToast(this@ResultActivity, getString(R.string.provided_detail_not_correct))

                    println("OnSuccess::code" + response.code)
                    callKYCfailScreen()

                }
            }
        })
    }

    private fun callKYCfailScreen() {
        startActivity(Intent(this, KYCfailedActivity::class.java))
        finish()
    }

    fun getKyc() {
        kycViewModel?.getKyc(apiCalls)
    }

    fun callNextScreen() {
        var dob=Utils.convertDate(ProcessedData.dateOfBirth)
        print("DOB*****$dob")
        if (addres.equals("")&& city.equals("")) {
            MyPref.getInstance(applicationContext).writeBooleanPrefs(Pref.IS_KYC_FILLED, true)
            MyPref.getInstance(applicationContext).writeBooleanPrefs(Pref.IS_LOGIN, true)
            startActivity(Intent(this, AddAddressActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("Type", "P")
                    .putExtra("dob",dob)
                    .putExtra("isFirsttime", "true")
            )
            finishAffinity()
        } else {
              if(postalCode=="")
              {
                  postalCode="0"
              }

            kycViewModel?.addAddress(apiCalls, addres,Address,city,postalCode,"CA",state,dob)

            Log.d("Address AOI calls",""+addres+" "+city+" "+postalCode+" "+state)

        }


    }

    var responseObserverKyc =
        androidx.lifecycle.Observer { kycResponse: KYCResponse ->
            Utils.hideProgressDialog()
            if (kycResponse.status) {
                callNextScreen()

            } else {
                Utils.showToast(
                    this,
                    kycResponse.message
                )
            }
        }

    var addressResponseObserver =
        androidx.lifecycle.Observer { addAddressResponse: AddAddressResponse ->
            if (addAddressResponse.status) {

                MyPref.getInstance(applicationContext).writeBooleanPrefs(Pref.IS_LOGIN, true)
                MyPref.getInstance(applicationContext).writeBooleanPrefs(Pref.IS_KYC_FILLED, true)
                MyPref.getInstance(applicationContext).writeBooleanPrefs(Pref.IS_ADDRESS_FILLED, true)

                startActivity(
                    Intent(
                        this,
                        ThankYouActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("Type", "L")
                )
                finishAffinity()
            } else {
                Utils.showToast(
                    this@ResultActivity,
                    getString(R.string.something_went_wrong)
                )
            }
        }
}
