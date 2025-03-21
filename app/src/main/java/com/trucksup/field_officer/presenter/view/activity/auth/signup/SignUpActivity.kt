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
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.NewResisterRequest
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.databinding.ActivitySignUpBinding
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

    private var mSignUpBinding: ActivitySignUpBinding? = null
    private var passwordDialog: Dialog? = null
    private var signupViewModel: SignupViewModel? = null


    //private FirebaseAnalytics mFirebaseAnalytics;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the FirebaseAnalytics instance.
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mSignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        //click Listener
        Utils.setStatusBarColorAndIcons(this)
        //click Listener
        mSignUpBinding!!.topView.ivBack.setOnClickListener(this)

        mSignUpBinding!!.loginTxt.setOnClickListener(this)
        mSignUpBinding!!.signUpBtn.setOnClickListener(this)
        // mSignUpBinding!!.tvVerify.setOnClickListener(this)


        // mSignUpBinding!!.signUpButtonView.setEnabled(false);
        signupViewModel = ViewModelProvider(this).get(
            SignupViewModel::class.java
        )
        mSignUpBinding!!.setViewModel(signupViewModel)

        mSignUpBinding!!.confirmPasswordTxt.addTextChangedListener(object : TextWatcher {
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
        })

        /*  mSignUpBinding!!.phoneNoTxt.onFocusChangeListener =
              OnFocusChangeListener { view, hasFocus ->
                  if (hasFocus) {
                      if (mSignUpBinding!!.emailTxt.text.isNotEmpty() && isValidEmail(mSignUpBinding!!.emailTxt.text)) {
                          if (!isOTPDialogVerified) {
                              sendOTP()
                          }

                          if (mSignUpBinding!!.firstNameTxt.text.toString().isEmpty()) {
                              mSignUpBinding!!.firstNameError.visibility = View.VISIBLE
                              mSignUpBinding!!.firstNameTxt.background =
                                  getDrawable(R.drawable.error_red_background_view)
                          } else {
                              mSignUpBinding!!.firstNameError.visibility = View.GONE
                              mSignUpBinding!!.firstNameTxt.background =
                                  getDrawable(R.drawable.edit_text_background_view)
                          }
                      } else {
                          if (mSignUpBinding!!.firstNameTxt.text.toString().isEmpty()) {
                              mSignUpBinding!!.firstNameError.visibility = View.VISIBLE
                              mSignUpBinding!!.firstNameTxt.background =
                                  getDrawable(R.drawable.error_red_background_view)
                          } else {
                              mSignUpBinding!!.firstNameError.visibility = View.GONE
                              mSignUpBinding!!.firstNameTxt.background =
                                  getDrawable(R.drawable.edit_text_background_view)
                          }
                      }
                  }
              }*/

        mSignUpBinding!!.otpTxt.addTextChangedListener(object : TextWatcher {
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
        })

        setupObserver()

    }


    private fun setupObserver() {
        signupViewModel?.resultSendOTPLD?.observe(this@SignUpActivity
        ) { responseModel: ResponseModel<Response<String>> ->                   // send otp observer
            if (responseModel.networkError != null) {
                dismissProgressDialog()

            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()

            } else if (responseModel.genericError != null) {
                dismissProgressDialog()

            } else {
                dismissProgressDialog()
            }
        }

        signupViewModel?.verifyOTPResultLD?.observe(this@SignUpActivity) { responseModel ->                // verify otp observer
            if (responseModel.networkError != null) {
                dismissProgressDialog()

            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()


                if (responseModel.serverResponseError == "in.co.ksquaretech.backend.otp.not.found") {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, responseModel.serverResponseError, Toast.LENGTH_SHORT)
                        .show()
                }

                //  mSignUpBinding!!.otpErrorText.visibility = View.VISIBLE
                //  mSignUpBinding!!.otpErrorText.text = AppConstant.AppLabelName.invalid_otp
                //  mSignUpBinding!!.otpTxt.background = getDrawable(R.drawable.error_red_background_view)

            } else if (responseModel.genericError != null) {
                dismissProgressDialog()

            } else {
                mSignUpBinding!!.otpErrorText.visibility = View.GONE
                mSignUpBinding!!.otpTxt.background =
                    getDrawable(R.drawable.edit_text_background_view)
                dismissProgressDialog()
                Toast.makeText(this, "OTP verified Successfully", Toast.LENGTH_SHORT).show()

            }
        }


        signupViewModel!!.registerUserLD.observe(this@SignUpActivity) { responseModel ->
            // register user observer
            if (passwordDialog != null && passwordDialog!!.isShowing) passwordDialog!!.dismiss()
            if (responseModel.networkError != null) {
                dismissProgressDialog()

            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()
                Utils.showToastDialog(
                    responseModel.serverResponseError,
                    this,
                    "Ok"
                )
            } else if (responseModel.genericError != null) {
                dismissProgressDialog()

            } else {
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


        /*  signupViewModel!!.checkUserProfileLD.observe(this@SignUpActivity) { responseModel: ResponseModel<CheckUserProfileResponse> ->                 // check userprofile observer
              if (responseModel.networkError != null) {
                  dismissProgressDialog()
                  Utils.showToastDialog(
                      AppConstant.AppLabelName.networkError,
                      this,
                      "Ok"
                  )
              } else if (responseModel.serverResponseError != null) {
                  dismissProgressDialog()
                  Utils.showToastDialog(
                      responseModel.serverResponseError,
                      this,
                      "Ok"
                  )
              } else if (responseModel.genericError != null) {
                  dismissProgressDialog()
                  Utils.showToastDialog(
                      AppConstant.AppLabelName.genericError,
                      this,
                      "Ok"
                  )
              } else {
                  dismissProgressDialog()
                  if (responseModel.success!!.payload != null) {
                      if (mSignUpBinding!!.emailTxt.text.toString() != null && mSignUpBinding!!.emailTxt.text.toString().length > 0) {
                          dismissProgressDialog()
                          Utils.showToastDialog(
                              "com.skiptq.backend.user.exist.with.same.email",
                              this,
                              "Ok"
                          )
                      } else {
                          Utils.showToastDialog(
                              "com.skiptq.backend.user.exist.with.same.mobile",
                              this,
                              "Ok"
                          )
                      }
                  } else {
                      sendOTP() // send otp fun call
                  }
              }
          }*/

        mSignUpBinding!!.retryTextView.visibility = View.INVISIBLE

        textWatcher();
    }

    private fun textWatcher() {

        mSignUpBinding!!.phoneNoTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (isValidMobile(mSignUpBinding!!.phoneNoTxt.text.toString())) {
                    // mSignUpBinding!!.mobileErrorText.visibility = View.GONE
                } else {
                    val em = "Not a valid mobile no"
                    LoggerMessage.onSNACK(mSignUpBinding!!.phoneNoTxt, em, applicationContext)

                }
            }
        })

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
                    mSignUpBinding?.profileName?.setError("Enter Name")
                    return
                }
                if (isValidName(mSignUpBinding?.profileName?.text.toString().trim())) {
                    mSignUpBinding?.profileName?.setError("Enter Right Name")
                    return
                }
                if (getSpecialCharacterCount(mSignUpBinding?.profileName?.text.toString()) == 0) {
                    mSignUpBinding?.profileName?.setError("Enter Right Name")
                    return
                }

                if (mSignUpBinding?.profileImage?.tag.toString().equals("none")) {
                    val em: String = resources.getString(R.string.enterProfilePhoto)
                    LoggerMessage.onSNACK(mSignUpBinding?.profileImage!!, em, this)
                    return
                }

                if (mSignUpBinding?.profileName?.text.toString().isNotEmpty()
                    && mSignUpBinding?.passwordTxt?.text.toString().isNotEmpty()
                ) {
                    if (isValidMobile(mSignUpBinding?.phoneNoTxt?.text.toString())) {

                            showProgressDialog()

                            val request = NewResisterRequest(
                                "",
                                "",
                                mSignUpBinding!!.phoneNoTxt.text.toString(),
                                mSignUpBinding!!.passwordTxt.text.toString()
                            )

                            signupViewModel!!.signUp(request)

                    } else {
                        Utils.showToastDialog(
                            "Enter valid Email",
                            this,
                            "Ok"
                        )
                    }


                } else {

                    /* mSignUpBinding!!.firstNameTxt.setBackgroundResource(
                         if (mSignUpBinding!!.firstNameTxt.text.toString()
                                 .isEmpty()
                         ) R.drawable.error_red_background_view else R.drawable.edit_text_background_view
                     )*/
                    //  mSignUpBinding!!.lastNameTxt.setBackgroundResource(mSignUpBinding!!.lastNameTxt.getText().toString().isEmpty() ? R.drawable.error_red_background_view : R.drawable.edit_text_background_view);
                    /* if (mSignUpBinding!!.firstNameTxt.text.toString().isEmpty()) {
                         mSignUpBinding!!.firstNameError.visibility = View.VISIBLE
                     } else {
                         mSignUpBinding!!.firstNameError.visibility = View.GONE
                     }*/

                    if (mSignUpBinding!!.phoneNoTxt.text.toString().isEmpty()) {

                        LoggerMessage.onSNACK(
                            mSignUpBinding!!.phoneNoTxt,
                            "Enter Mobile number.",
                            applicationContext
                        )

                    } else {

                        LoggerMessage.onSNACK(
                            mSignUpBinding!!.phoneNoTxt,
                            "Not a valid mobile number.",
                            applicationContext
                        )

                    }
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
            var intent=Intent(this, CameraXActivity::class.java)
            intent.putExtra("FRONT","y")
            intent.putExtra("BACK","n")
            startForResult.launch(intent)
        }
    }

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == 100) {

            if (Uri.parse(result.data?.getStringExtra("image"))!=null) {

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, Uri.parse(result.data?.getStringExtra("image"))))
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(result.data?.getStringExtra("image")))
                }
                mSignUpBinding?.profileImage?.setImageURI(result.data?.data)
               /* var newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
                var newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!

                uploadImage(newFile, "")*/
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 11 && data != null) {
            var mainBitmap: Bitmap = data.extras?.get("data") as Bitmap
            mSignUpBinding?.profileImage?.setImageBitmap(mainBitmap)

            val newBitmap: Bitmap =
                FileHelper().resizeImage(data.extras?.get("data") as Bitmap, 500, 500)!!
            //  var newFile: File = FileHelp().bitmapTofile(newBitmap,this)!!

            val name = "trucksUp_image" + System.currentTimeMillis() + ".jpg"
            val imageFile = File(filesDir, name)

            val os: OutputStream
            try {
                os = FileOutputStream(imageFile)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 99, os)
                os.flush()
                os.close()

                mSignUpBinding?.profileImage?.tag = "uploaded"

                /* LoadingUtils?.showDialog(this, false)
                 LoadingUtils.showDialog(this, false)
                 MyResponse()?.uploadImage(
                     "jpg",
                     "DOC" + PreferenceManager.getRequestNo(),
                     "" + PreferenceManager.getPhoneNo(this),
                     PreferenceManager.prepareFilePart(imageFile!!),
                     this,
                     this
                 )*/
            } catch (e: java.lang.Exception) {
                Log.e(javaClass.simpleName, "Error writing bitmap", e)
                LoggerMessage.onSNACK(
                    mSignUpBinding?.profileImage!!,
                    "Request cancelled or something went wrong.",
                    this
                )
            }


        } else {
            LoggerMessage.toastPrint("Request cancelled or something went wrong.", baseContext)
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


    companion object {
        fun isValidEmail(target: CharSequence?): Boolean {
            return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches())
        }
    }

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