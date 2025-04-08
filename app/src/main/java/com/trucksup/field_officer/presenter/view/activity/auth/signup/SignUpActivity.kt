package com.trucksup.field_officer.presenter.view.activity.auth.signup

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.NewResisterRequest
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.databinding.ActivitySignUpBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.Utils
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.common.yCamera.CameraXActivity
import com.trucksup.field_officer.presenter.utils.FileHelper
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpActivity : BaseActivity(), View.OnClickListener {
    private  var mSignUpBinding: ActivitySignUpBinding? = null
    private var signupViewModel: SignupViewModel? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var imageUri:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        adjustFontScale(resources.configuration, 1.0f)
        enableEdgeToEdge()
        //click Listener
        mSignUpBinding!!.topView.ivBack.setOnClickListener(this)

        mSignUpBinding!!.loginTxt.setOnClickListener(this)
        mSignUpBinding!!.signUpBtn.setOnClickListener(this)
        mSignUpBinding!!.cvCamera.setOnClickListener(this)
        // mSignUpBinding!!.signUpButtonView.setEnabled(false);
        signupViewModel = ViewModelProvider(this).get(
            SignupViewModel::class.java
        )
        mSignUpBinding!!.setViewModel(signupViewModel)

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
        /*  mSignUpBinding!!.otpTxt.addTextChangedListener(object : TextWatcher {
              override fun afterTextChanged(s: Editable) {
                  mSignUpBinding!!.otpErrorText.visibility = View.GONE
                  mSignUpBinding!!.otpTxt.background =
                      getDrawable(R.drawable.edit_text_background_view)
              }

              override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
              }

              override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
              }
          })

          mSignUpBinding!!.phoneNoTxt.addTextChangedListener(object : TextWatcher {
              override fun afterTextChanged(s: Editable) {

              }

              override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
              }

              override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                  if (mSignUpBinding!!.retryTextView.visibility == View.VISIBLE) {
                      mSignUpBinding!!.otpTxt.setText("")
                      // mSignUpBinding!!.otpTxt.setFocusable(false);
                      mSignUpBinding!!.retryTextView.visibility = View.GONE
                  }
              }
          })*/

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
                    mSignUpBinding?.profileImage?.let {
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
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher!!.launch(intent)
    }

    private fun setupObserver() {
        signupViewModel?.resultSendOTPLD?.observe(
            this@SignUpActivity
        ) { responseModel: ResponseModel<Response<String>> ->                   // send otp observer
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@SignUpActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()
            }
        }

        signupViewModel?.verifyOTPResultLD?.observe(this@SignUpActivity) { responseModel ->                // verify otp observer
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@SignUpActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            }  else {
                mSignUpBinding!!.otpErrorText.visibility = View.GONE
                mSignUpBinding!!.otpTxt.background =
                    getDrawable(R.drawable.edit_text_background_view)
                dismissProgressDialog()
                Toast.makeText(this, "OTP verified Successfully", Toast.LENGTH_SHORT).show()

            }
        }


        signupViewModel!!.registerUserLD.observe(this@SignUpActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@SignUpActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            }  else {
                dismissProgressDialog()
                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                // move to home screen
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                //intent.putExtra("airportId", 0)
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                setResult(RESULT_OK, getIntent())
                finish()
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
                    mSignUpBinding?.profileName?.error = resources.getString(R.string.enterProfileName)
                    mSignUpBinding?.profileName?.requestFocus()
                    return
                }
                if (isValidName(mSignUpBinding?.profileName?.text.toString().trim())) {
                    mSignUpBinding?.profileName?.error = resources.getString(R.string.validnameformate)
                    return
                }
                if (getSpecialCharacterCount(mSignUpBinding?.profileName?.text.toString()) == 0) {
                    mSignUpBinding?.profileName?.error = resources.getString(R.string.validnameformate)
                    return
                }

                if (mSignUpBinding?.profileImage?.tag.toString().equals("none")) {
                    val em: String = resources.getString(R.string.enterProfilePhoto)
                    LoggerMessage.onSNACK(mSignUpBinding?.profileImage!!, em, this)
                    return
                }

                if (TextUtils.isEmpty(mSignUpBinding?.phoneNoTxt?.text.toString().trim())) {
                    mSignUpBinding?.phoneNoTxt?.error = resources.getString(R.string.enter_mobile_no)
                    mSignUpBinding?.phoneNoTxt?.requestFocus()
                    return
                }
                if (mSignUpBinding!!.passwordTxt.text.toString().isEmpty()) {

                    LoggerMessage.onSNACK(
                        mSignUpBinding!!.passwordTxt,
                        resources.getString(R.string.enter_password),
                        applicationContext
                    )

                    return

                }

                if (mSignUpBinding!!.confirmPasswordTxt.text.toString().isEmpty()) {

                    LoggerMessage.onSNACK(
                        mSignUpBinding!!.confirmPasswordTxt,
                        "Please Enter Confirm Password.",
                        applicationContext
                    )

                    return

                }

                val password: String = mSignUpBinding!!.passwordTxt.getText().toString()
                val confirmpassword: String = mSignUpBinding!!.confirmPasswordTxt.getText().toString()

                if (confirmpassword.length > 0 && password.length > 0) {
                    if (!confirmpassword.equals(password)) {
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

                        return
                    }
                }

                if (mSignUpBinding?.phoneNoTxt?.text.toString().isNotEmpty()
                    && mSignUpBinding?.passwordTxt?.text.toString().isNotEmpty()
                ) {
                    if (isValidMobile(mSignUpBinding?.phoneNoTxt?.text.toString())) {

                        showProgressDialog(this,false)

                        val request = NewResisterRequest(
                            "",
                            "",
                            mSignUpBinding!!.phoneNoTxt.text.toString(),
                            mSignUpBinding!!.passwordTxt.text.toString()
                        )

                        //signupViewModel!!.signUp(request)

                    } else {

                        LoggerMessage.onSNACK(
                            mSignUpBinding!!.phoneNoTxt,
                            resources.getString(R.string.enter_valid_mobile),
                            applicationContext
                        )
                    }


                } else {
                    LoggerMessage.onSNACK(
                        mSignUpBinding!!.phoneNoTxt,resources.getString(R.string.enter_valid_mobile),
                        applicationContext
                    )

                    /* Utils.showToastDialog(
                         "Fill Your Details",
                         this,
                         "Ok"
                     )*/
                }

            } else {
                Utils.showToastDialog(
                    "no internet connection.",
                    this,
                    "Ok"
                )
            }
        }else if(view.id == R.id.cvCamera){
            launchCamera()
        }
    }

    private fun isSignupValidatation(): Boolean {
        TODO("Not yet implemented")
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
}