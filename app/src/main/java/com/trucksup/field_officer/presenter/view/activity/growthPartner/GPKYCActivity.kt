package com.trucksup.field_officer.presenter.view.activity.growth_partner

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpkycactivityBinding
import com.trucksup.field_officer.databinding.ItemRadioImageBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.growthPartner.GPOnBoardStoreProofActivity
import com.trucksup.field_officer.presenter.view.activity.growthPartner.vml.GPScheduleMeetingVM
import java.io.File

class GPKYCActivity : BaseActivity(), TrucksFOImageController, View.OnClickListener {
    private lateinit var binding: ActivityGpkycactivityBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var mViewModel: GPScheduleMeetingVM? = null
    private var imageType: Int = 0
    private var selectedIndex = -1
    private var ScreenViewPostion = 0
    private var kycSelfieImgKey: String? = ""
    private var kycSelfieImgUrl: String? = ""
   /* private var kycDocImgKey: String? = ""
    private var kycDocImgUrl: String? = ""*/


    private val GpOnBoardingKycDocItems = listOf(
        GpOnBoardingKycDocItem(R.drawable.ic_pan, R.drawable.img_aadhar, "Aadhaar KYC"),
        GpOnBoardingKycDocItem(R.drawable.ic_pan, R.drawable.img_aadhar, "PAN Card"),
        GpOnBoardingKycDocItem(R.drawable.ic_voter, R.drawable.img_aadhar, "Voter Card"),
        GpOnBoardingKycDocItem(R.drawable.ic_driving, R.drawable.img_aadhar, "Driving License")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpkycactivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)

        // Create and add cards dynamically
        GpOnBoardingKycDocItems.forEachIndexed { index, item ->
            val RadioImageBinding: ItemRadioImageBinding
            RadioImageBinding = ItemRadioImageBinding.inflate(layoutInflater)

            RadioImageBinding.imageView.setImageResource(item.imageMain)
            RadioImageBinding.icon.setImageResource(item.imageIcon)
            RadioImageBinding.title.text = item.label
            //Recomanded tab show specific layout
            if (index == 0) {
                RadioImageBinding.mcvRecomanded.visibility = View.VISIBLE
            }

            // Card click selects the radio button
            RadioImageBinding.materialCardView4.setOnClickListener {
                updateSelection(index)
            }

            RadioImageBinding.radioButton.setOnClickListener {
                updateSelection(index)
            }

            binding.cardContainer.addView(RadioImageBinding.root)
        }
        initialize()
        camera()


        //pan number test
        binding.voterIdEditText.filters = arrayOf(
            InputFilter { source, _, _, _, _, _ ->
                source.toString().uppercase().replace(Regex("[^A-Z0-9]"), "")
            },
            InputFilter.LengthFilter(10)
        )

        binding.voterIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().uppercase()
                //binding.voterIdEditText.setText(input)
                binding.voterIdEditText.setSelection(input.length)

                if (isValidVoterId(input)) {
                    binding.voterIdEditText.setBackgroundResource(R.drawable.ic_tick) // ✅ border
                } else {
                    binding.voterIdEditText.setBackgroundResource(R.drawable.cancel_icon) // ❌ border
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        //pan number test
    }

    fun isValidVoterId(id: String): Boolean {
        return id.matches(Regex("^[A-Z]{3}[0-9]{7}$"))
    }


    @SuppressLint("SetTextI18n")
    private fun updateSelection(selected: Int) {
        selectedIndex = selected
        for (i in 0 until binding.cardContainer.childCount) {
            val itemView = binding.cardContainer.getChildAt(i)
            val radioButton = itemView.findViewById<RadioButton>(R.id.radioButton)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            val title = itemView.findViewById<TextView>(R.id.title)
            radioButton.isChecked = i == selected
            if (selectedIndex == i) {
                ScreenViewPostion = 1
                imageView.visibility = View.VISIBLE
                binding.kycSelectionLinear.visibility = View.GONE
                binding.kycSubmittedLinear.visibility = View.VISIBLE
                binding.kycSubmittedTitle.setText("" + title.text.toString())
            } else {
                imageView.visibility = View.GONE
            }

        }
    }

    fun kyc_verified_continue() {
        startActivity(Intent(this, GPOnBoardStoreProofActivity::class.java))
    }

    fun kyc_submitted_procceed() {
        ScreenViewPostion = 2
        binding.kycSelectionLinear.visibility = View.GONE
        binding.kycSubmittedLinear.visibility = View.GONE
        binding.kycVerifiedLinear.visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        if (ScreenViewPostion == 1) {
            ScreenViewPostion = 0
            binding.kycSelectionLinear.visibility = View.VISIBLE
            binding.kycSubmittedLinear.visibility = View.GONE
            binding.kycVerifiedLinear.visibility = View.GONE
            binding.kycSubmittedTitle.setText("")
        } else if (ScreenViewPostion == 2) {
            ScreenViewPostion = 1
            binding.kycSelectionLinear.visibility = View.GONE
            binding.kycSubmittedLinear.visibility = View.VISIBLE
            binding.kycVerifiedLinear.visibility = View.GONE
        } else {
            super.onBackPressed()
        }

    }

    private fun initialize() {
        binding.kycVerifiedContinueBtn.setOnClickListener(this)
        binding.kycSubmittedProcceedBtn.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.kycSubmittedImage.setOnClickListener(this)

    }

    //add by me
    private fun camera() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        Uri.parse(imageUris.toString())
                    )
                    // Set the image in imageview for display
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
                    val bitmapConvertedImage: File = FileHelp().bitmapTofile(newBitmap, this)!!
                    uploadImage(bitmapConvertedImage, "")

                    //handleImageCapture(bitmap)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun launchCamera(flipCamera: Boolean, cameraOpen: Int, focusView: Boolean) {
        imageType = 1
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", flipCamera)
        intent.putExtra("cameraOpen", cameraOpen)
        intent.putExtra("focusView", focusView)
        launcher!!.launch(intent)
    }

    fun uploadImage(file: File, token: String) {
        LoadingUtils.showDialog(this, false)
        if (imageType == 3) {
            mViewModel?.uploadImages(
                PreferenceManager.getAuthToken(),
                "pdf",
                "GrowthPartner",
                PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
                PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
                this
            )
        } else {
            mViewModel?.uploadImages(
                PreferenceManager.getAuthToken(),
                "image",
                "GrowthPartner",
                PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
                PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
                this
            )
        }
    }
    //add by me
    override fun onClick(view: View) {
        when (view.id) {
            R.id.kyc_verified_continue_btn -> kyc_verified_continue()
            R.id.iv_back -> onBackPressed()
            R.id.kyc_submitted_procceed_btn -> {
                ScreenViewPostion = 1
                kyc_submitted_procceed()
            }

            R.id.kyc_submitted_image -> {
                launchCamera(false, 0, true)

            }

        }
    }

    override fun getImage(value: String, url: String) {
        LoadingUtils.hideDialog()
        if (imageType == 1) {
            kycSelfieImgKey = value
            kycSelfieImgUrl = url
            //binding.cutFrontBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.kycSubmittedImage)
            } catch (e: Exception) {
            }
        } /*else if (imageType == 2) {
            kycDocImgKey = value
            kycDocImgUrl = url
            //binding.cutBackBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgBackCamera)
            } catch (e: Exception) {
            }
        }else if (imageT == 3) {
            prevPolicyDocsImgKey = value
            prevPolicyDocsImgUrl = url
            binding.cutDeclareBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(R.drawable.pdf_icon)
                    .into(binding.imgchequeDoc)
            } catch (e: Exception) {
            }
        }*/
    }

    override fun dataSubmitted(message: String) {}

    override fun imageError(error: String) {
        LoadingUtils.hideDialog()
        LoggerMessage.onSNACK(binding.main, error, this)
    }

}

data class GpOnBoardingKycDocItem(
    val imageIcon: Int,
    val imageMain: Int,
    val label: String,
)
