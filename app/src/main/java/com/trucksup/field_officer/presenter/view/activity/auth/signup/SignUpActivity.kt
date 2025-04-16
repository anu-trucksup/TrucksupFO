package com.trucksup.field_officer.presenter.view.activity.auth.signup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.authModel.SignRequest
import com.trucksup.field_officer.databinding.ActivitySignUpBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.AppVersionUtils
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpActivity : BaseActivity(), View.OnClickListener, TrucksFOImageController {
    private var mSignUpBinding: ActivitySignUpBinding? = null
    private var signupViewModel: SignupViewModel? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var frontImgKey: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(mSignUpBinding?.root)

        enableEdgeToEdge()
        //click Listener
        mSignUpBinding!!.topView.ivBack.setOnClickListener(this)

        mSignUpBinding?.loginTxt?.setOnClickListener(this)
        mSignUpBinding?.signUpBtn?.setOnClickListener(this)
        mSignUpBinding?.cvCamera?.setOnClickListener(this)
        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]


        /* mSignUpBinding!!.confirmPasswordTxt.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
             }

             override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
             }

             override fun afterTextChanged(editable: Editable) {
                 val password: String = mSignUpBinding!!.passwordTxt.getText().toString()
                 if (editable.length > 0 && password.length > 0) {
                     if (!editable.toString().equals(password)) {
                         val customErrorDrawable = resources.getDrawable(R.drawable.error_warn)
                         customErrorDrawable.setBounds(
                             0,
                             0,
                             customErrorDrawable.intrinsicWidth,
                             customErrorDrawable.intrinsicHeight
                         )

                         mSignUpBinding!!.confirmPasswordTxt.setError(
                             "Password and Confirm Password should be same.",
                             customErrorDrawable
                         )

                         // give an error that password and confirm password not match
                     } else {
                         val customErrorDrawable = resources.getDrawable(R.drawable.error_confirm)
                         customErrorDrawable.setBounds(
                             0,
                             0,
                             customErrorDrawable.intrinsicWidth,
                             customErrorDrawable.intrinsicHeight
                         )
                         mSignUpBinding!!.confirmPasswordTxt.setError("", customErrorDrawable)
                     }
                 }
             }
         })*/

        setupObserver()
        cameraLauncher()

    }

    private fun cameraLauncher() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        Uri.parse(imageUris.toString())
                    )
                    // Set the image in imageview for display
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
                    val newFile: File = FileHelp().bitmapTofile(newBitmap, this)
                    uploadImage(newFile)

                    //handleImageCapture(bitmap)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun launchCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher?.launch(intent)
    }

    fun uploadImage(file: File) {
        showProgressDialog(this, false)

        signupViewModel?.uploadImages(
            PreferenceManager.getAuthToken(),
            "image",
            PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
            PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
            this
        )
    }

    private fun setupObserver() {
        signupViewModel?.resultSendOTPLD?.observe(
            this@SignUpActivity
        ) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(this@SignUpActivity, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                dismissProgressDialog()
            }
        }

        signupViewModel?.verifyOTPResultLD?.observe(this@SignUpActivity) { responseModel ->                // verify otp observer
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@SignUpActivity,
                    responseModel.serverError.toString(), "m"
                )
                abx.show()
            } else {
                mSignUpBinding!!.otpErrorText.visibility = View.GONE
                mSignUpBinding!!.otpTxt.background =
                    getDrawable(R.drawable.edit_text_background_view)
                dismissProgressDialog()
                Toast.makeText(this, "OTP verified Successfully", Toast.LENGTH_SHORT).show()

            }
        }


        signupViewModel?.registerUserLD?.observe(this@SignUpActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@SignUpActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {

                    Toast.makeText(this, "Signup successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    intent.putExtra("mobile", mSignUpBinding?.phoneNoTxt?.text.toString())
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()


                } else {
                    val abx = AlertBoxDialog(
                        this@SignUpActivity, responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }

            }
        }

    }

    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.login_txt) {
            val signInIntent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(signInIntent)
            setResult(RESULT_OK, intent)
            finish()
        } else if (view.id == R.id.sign_up_btn) {

            if (isOnline(this)) {
                if (TextUtils.isEmpty(mSignUpBinding?.profileName?.text.toString().trim())) {
                    mSignUpBinding?.profileName?.error =
                        resources.getString(R.string.enterProfileName)
                    mSignUpBinding?.profileName?.requestFocus()
                    return
                }
                if (isValidName(mSignUpBinding?.profileName?.text.toString().trim())) {
                    mSignUpBinding?.profileName?.error =
                        resources.getString(R.string.valid_name_format)
                    return
                }
                if (getSpecialCharacterCount(mSignUpBinding?.profileName?.text.toString()) == 0) {
                    mSignUpBinding?.profileName?.error =
                        resources.getString(R.string.valid_name_format)
                    return
                }

                if (mSignUpBinding?.profileImage?.tag.toString().equals("none")) {
                    val em: String = resources.getString(R.string.enterProfilePhoto)
                    LoggerMessage.onSNACK(mSignUpBinding?.profileImage!!, em, this)
                    return
                }

                if (TextUtils.isEmpty(mSignUpBinding?.phoneNoTxt?.text.toString().trim())) {
                    mSignUpBinding?.phoneNoTxt?.error =
                        resources.getString(R.string.enter_mobile_no)
                    mSignUpBinding?.phoneNoTxt?.requestFocus()
                    return
                }

                val password = mSignUpBinding!!.passwordTxt.text.toString()
                if (password.isEmpty()) {
                    LoggerMessage.onSNACK(
                        mSignUpBinding!!.passwordTxt,
                        resources.getString(R.string.enter_password), applicationContext
                    )
                    return
                }

                if (isValidPassword(password)) {

                    if (mSignUpBinding!!.confirmPasswordTxt.text.toString().isEmpty()) {
                        LoggerMessage.onSNACK(
                            mSignUpBinding!!.confirmPasswordTxt,
                            getString(R.string.enter_confirm_password),
                            applicationContext
                        )
                        return
                    }

                    val confirmPassword = mSignUpBinding!!.confirmPasswordTxt.text.toString()

                    if (confirmPassword.length > 0 && password.length > 0) {
                        if (!confirmPassword.equals(password)) {
                            val customErrorDrawable = resources.getDrawable(R.drawable.error_warn)
                            customErrorDrawable.setBounds(
                                0, 0,
                                customErrorDrawable.intrinsicWidth, customErrorDrawable.intrinsicHeight
                            )

                            mSignUpBinding?.confirmPasswordTxt?.setError(
                                "Password and Confirm Password should be same.", customErrorDrawable
                            )
                            //  mSignUpBinding!!.confirmPasswordTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_confirm, 0);
                            return
                        } else {

                            val customErrorDrawable = resources.getDrawable(R.drawable.error_confirm)
                            customErrorDrawable.setBounds(
                                0,
                                0,
                                customErrorDrawable.intrinsicWidth,
                                customErrorDrawable.intrinsicHeight
                            )

                            mSignUpBinding!!.confirmPasswordTxt.setError(
                                "Both Password are same.",
                                customErrorDrawable
                            )

                        }

                    }
                } else {
                    mSignUpBinding?.passwordTxt?.error = getString(R.string.password_validation)
                    return
                }



                if (mSignUpBinding?.phoneNoTxt?.text.toString().isNotEmpty()
                    && mSignUpBinding?.passwordTxt?.text.toString().isNotEmpty()
                ) {
                    if (isValidMobile(mSignUpBinding?.phoneNoTxt?.text.toString())) {

                        showProgressDialog(this, false)

                        val request = SignRequest(
                            requestedBy = mSignUpBinding?.phoneNoTxt?.text.toString(),
                            requestId = PreferenceManager.getRequestNo().toInt(),
                            requestDatetime = PreferenceManager.getServerDateUtc(),
                            deviceid = PreferenceManager.getAndroiDeviceId(this),
                            appVersion = AppVersionUtils.getAppVersionName(this),
                            androidVersion = Build.VERSION.SDK_INT.toString(),
                            profilename = mSignUpBinding?.profileName?.text.toString(),
                            profilephoto = frontImgKey ?: "",
                            mobilenumber = mSignUpBinding?.phoneNoTxt?.text.toString(),
                            password = mSignUpBinding?.passwordTxt?.text.toString()
                        )

                        signupViewModel?.signUp(PreferenceManager.getAuthToken(), request)

                    } else {

                        LoggerMessage.onSNACK(
                            mSignUpBinding!!.phoneNoTxt,
                            resources.getString(R.string.enter_valid_mobile),
                            applicationContext
                        )
                    }


                } else {
                    LoggerMessage.onSNACK(
                        mSignUpBinding!!.phoneNoTxt,
                        resources.getString(R.string.enter_valid_mobile),
                        applicationContext
                    )

                }

            } else {

                LoggerMessage.onSNACK(
                    mSignUpBinding!!.signUpBtn,
                    resources.getString(R.string.no_internet),
                    applicationContext
                )

            }
        } else if (view.id == R.id.cvCamera) {
            launchCamera()
        }
    }

    fun getProfileImage(v: View) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Click", "Click opn location ask  ")
            checkLocationPermission()
        } else {
            /*val intent = Intent(this, CameraActivity::class.java)
            startForResult?.launch(intent)*/
        }
    }

    private fun checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CAMERA
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                }

            }
        }

    }

    /*override fun getImage(value: String) {
        LoadingUtils?.hideDialog()
        Glide.with(this)
            .load(MyResponse.imagePathUrl + value + "&Position=1")
            .into(profileImage!!)
        fileKey = value
        profileImage?.setTag("y")
    }

    override fun imageError(error: String) {
        LoadingUtils?.hideDialog()
    }*/

    private fun isValidName(phone: String): Boolean {

        val p = Pattern.compile("[_0-9](.*)")
        val m = p.matcher(phone)
        return (m.find() && m.group() == phone)
    }

    private fun getSpecialCharacterCount(s: String?): Int {

        val blockCharacterSet =
            "~#^&|$%*!@/()[]-'\":;,?{}+=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
        blockCharacterSet.toCharArray()

        for (b in blockCharacterSet) {
            if (s!!.contains(b)) {

                return 0
                break
            }
        }
        return 1
    }

    override fun getImage(valuekey: String, url: String) {
        dismissProgressDialog()
        Glide.with(this)
            .load(url)
            .into(mSignUpBinding?.profileImage!!)
        mSignUpBinding?.profileImage?.tag = "y"

        frontImgKey = valuekey
    }

    override fun dataSubmitted(message: String) {
    }

    override fun imageError(error: String) {
        dismissProgressDialog()
        LoggerMessage.onSNACK(mSignUpBinding?.profileImage!!, error, this)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
        )
        return passwordRegex.matches(password)
    }

}