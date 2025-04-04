package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.databinding.ActivityTcNewOnboardingBinding
import com.trucksup.field_officer.databinding.VerifyOtpDialogBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.other.TokenViewModel
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.vml.TSOnboardViewModel
import com.trucksup.field_officer.presenter.common.JWTtoken
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TSOnboardingActivity : BaseActivity(), View.OnClickListener, JWTtoken {
    private lateinit var binding: ActivityTcNewOnboardingBinding
    private var mViewModel: TSOnboardViewModel? = null
    private var mTokenViewModel: TokenViewModel? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var imageUri:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTcNewOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)
        mTokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]
        mViewModel = ViewModelProvider(this)[TSOnboardViewModel::class.java]
        setOnClicks()

        setupObserver()
        camera()
    }

    fun camera() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    imageUri = data!!.getStringExtra("result").toString()
                    binding.profileImage?.let {
                        Glide.with(getApplicationContext())
                            .load(data!!.getStringExtra("result")?.toUri())
                            .into(it)
                    }
                    //profileImage?.setRotation(270F)
                    var orFile: File =
                        FileHelp().getFile(this, data!!.getStringExtra("result")?.toUri())!!
                    var newBitmap: Bitmap = FileHelp().FileToBitmap(orFile)


                    val name = "trucksUp_image" + System.currentTimeMillis() + ".jpg"
                    val pt = Environment.DIRECTORY_PICTURES //+  "/trucksUp";
                    val MEDIA_PATH = Environment.getExternalStorageDirectory().absolutePath + "/" + pt + "/"

                    val filesDir: File = getFilesDir()
                    val imageFile = File(filesDir, name)

                    val os: OutputStream
                    os = FileOutputStream(imageFile)
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 99, os)
                    os.flush()
                    os.close()

                    //LoadingUtils?.showDialog(this, false)
                    //LoadingUtils.showDialog(this, false)
                    /*MyResponse()?.uploadImage(
                        "jpg",
                        "DOC" + PreferenceManager.getRequestNo(),
                        "" + PreferenceManager.getPhoneNo(this),
                        PreferenceManager.prepareFilePart(imageFile!!),
                        this,
                        this
                    )*/
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun launchCamera(){
            val intent: Intent = Intent(this, CameraActivity::class.java)
            launcher!!.launch(intent)
    }

    private fun setOnClicks() {
        binding.btnAdd.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.cvCamera.setOnClickListener(this)


        binding.eTPincode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                /*if (binding.eTPincode.text.length == 6) {
                    showProgressDialog(this@TSOnboardingActivity, false)
                    val request = GenerateJWTtokenRequest(
                        username = PreferenceManager.getAccesUserName(this@TSOnboardingActivity),
                        password = PreferenceManager.getAccesPassword(this@TSOnboardingActivity),
                        apiSecreteKey = PreferenceManager.getAccesKey(this@TSOnboardingActivity),
                        userAgent = PreferenceManager.getAccesUserAgaint(this@TSOnboardingActivity),
                        issuer = PreferenceManager.getAccesUserInssur(this@TSOnboardingActivity)
                    )
                    mTokenViewModel?.generateJWTtoken(
                        request,
                        this@TSOnboardingActivity,
                        this@TSOnboardingActivity
                    )

                }*/
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun checkValidation() {
        if (binding.ETAccountHolderName.text.isEmpty()) {
            binding.ETAccountHolderName.requestFocus()
            binding.ETAccountHolderName.setError(getString(R.string.PleaseEnterContactName))
        }else if (imageUri.isEmpty() || imageUri == null) {
            LoggerMessage.onSNACK(
                binding.cvCamera,
                resources.getString(R.string.PleaseSelectStorePhoto),
                this
            )
        } else if (binding.ETAccountHolderNumber.text.isEmpty()) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.setError(getString(R.string.PleaseEnterContactNumber))
        } else if (binding.ETAccountHolderNumber.text.length < 10) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.setError(getString(R.string.enter_right_number_v))
        } else if (binding.ETBusinessName.text.isEmpty()) {
            binding.ETBusinessName.requestFocus()
            binding.ETBusinessName.setError(getString(R.string.PleaseEnterBusinessName))
        } /*else if (binding.ETBusinessAddress.text.isEmpty()) {
            binding.ETBusinessAddress.requestFocus()
            binding.ETBusinessAddress.setError(getString(R.string.PleaseEnterBusinessAddress))
        }*/ else if (binding.eTPincode.text.isEmpty()) {
            binding.eTPincode.requestFocus()
            binding.eTPincode.setError(getString(R.string.PleaseEnterBusinessPincode))
        } else if (binding.eTPincode.text.length < 6) {
            binding.eTPincode.requestFocus()
            binding.eTPincode.setError(getString(R.string.PleaseEnterRightPincode))
        } else {
            val HappinessCodeBox = HappinessCodeBox(
                this, getString(R.string.hapinessCodeMsg),
                getString(R.string.EnterHappinessCode),
                getString(R.string.resand_sms)
            )
            HappinessCodeBox.show()
        }
    }

    private fun getPinData(token: String) {
        val request = PinCodeRequest(
            binding.eTPincode.text.toString(),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this)
        )
        mViewModel?.getCityStateByPin(token, request)
    }

    private fun pinData(city: String, cityHindi: String, state: String, stateHindi: String) {
        dismissProgressDialog()
        if (PreferenceManager.getLanguage(this) == "en") {
            binding.eTcity.setText(city)
            binding.eTState.setText(state)
            // stateProfile = state
        } else {
            binding.eTcity.setText(cityHindi)
            binding.eTState.setText(stateHindi)
            // stateProfile = state
        }
    }

    private fun setupObserver() {
        mViewModel?.resultSCbyPincodeLD?.observe(this@TSOnboardingActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@TSOnboardingActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.data.isNotEmpty()) {
                        pinData(
                            responseModel.success.data[0].district,
                            responseModel.success.data[0].hindiCity,
                            responseModel.success.data[0].stateName,
                            responseModel.success.data[0].hindiState
                        )
                    } else {
                        val abx = MyAlartBox(
                            this@TSOnboardingActivity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()
                    }
                } else {
                    val abx = MyAlartBox(
                        this@TSOnboardingActivity,
                        "no data found",
                        "m"
                    )
                    abx.show()
                }
            }
        }

    }

    private fun setSubmitDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = VerifyOtpDialogBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.tvVerify.setOnClickListener {
            if (binding.pvHapinessCodeverify.text?.isEmpty() == true) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError(getString(R.string.EnterHappinessCode))
                //Toast.makeText(this, "",Toast.LENGTH_SHORT).show()
            } else if (binding.pvHapinessCodeverify.text?.length!! > 6) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError("Wrong")
            } else {
                dialog.dismiss()
            }
        }

        //  timer_progress?.setProgressWithAnimation(65f, 1000)
        object : CountDownTimer(60000, 1000) {
            override fun onTick(msUntilFinished: Long) {
                // displayText.setText("remaining sec: " + msUntilFinished / 1000)
                var tm: Long = msUntilFinished % 1000
                binding.timecounter?.text = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(msUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(msUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    msUntilFinished
                                )
                            )
                )

            }

            override fun onFinish() {
                //goback = true
                binding.timecounter?.visibility = View.GONE
                binding.txtHinttimecounter?.visibility = View.GONE
                binding.resendVerificationCodeTxt?.visibility = View.VISIBLE
                //cancle?.visibility = View.VISIBLE
            }
        }.start()

        dialog.show()
    }

    override fun onClick(v: View?) {
        val item_id = v?.id
        when (item_id) {
            R.id.btnAdd -> checkValidation()
            R.id.iv_back -> onBackPressed()
            R.id.btnCancel -> onBackPressed()
            R.id.cvCamera -> launchCamera()
        }
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        if (!response.accessToken.isNullOrEmpty()) {
            getPinData("Bearer " + response.accessToken)
        } else {
            val abx = MyAlartBox(this@TSOnboardingActivity,
                "Something went wrong", "m")
            abx.show()
        }

    }

    override fun onTokenFailure(msg: String) {
        dismissProgressDialog()
        val abx = MyAlartBox(this@TSOnboardingActivity, msg, "m")
        abx.show()
    }
}