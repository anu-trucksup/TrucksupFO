package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityFinanceBinding
import com.trucksup.field_officer.presenter.cityPicker.CityPicker
import com.trucksup.field_officer.presenter.cityPicker.CityStateDialog
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.dialog.FinaceSubmitBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.LoanDataSubmitRequest
import com.trucksup.field_officer.presenter.utils.truckMenu.TruckMenu
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class FinanceActivity : BaseActivity(), ChipController, CityPicker {
    private lateinit var binding: ActivityFinanceBinding
    private lateinit var chipAdapter: LoanChipAdapter
    private var mViewModel: FinanceViewModel? = null
    private var loanFor: String = "other"
    private var loanAmount: String = ""
    private var sourceValue: String? = "Trucksup"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finance)

        mViewModel = ViewModelProvider(this)[FinanceViewModel::class.java]
        //binding.name.setText(PreferenceManager.getUserData(this)?.profileName)

        PreferenceManager.setPhoneNo("8527257606", this)

        binding.mobileNumber.setText(PreferenceManager.getPhoneNo(this))

        setupObserver()
        //binding.etReferralCode.setText(PreferenceManager.getUserData(this)?.salesCode)

        binding.etReferralCode.setText("7BGHJ9")
        getData()
    }


    private fun setupObserver() {
        mViewModel?.resultFinanceLD?.observe(this@FinanceActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@FinanceActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.inquiryDetails != null && responseModel.success.inquiryDetails.size > 0) {

                    val inquiryList = responseModel.success.inquiryDetails as ArrayList<chipData>
                    dataList(inquiryList)

                } else { }
            }
        }


        mViewModel?.resultsubmitFinanceLD?.observe(this@FinanceActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = MyAlartBox(this@FinanceActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.message != null) {

                    updateData(
                        responseModel.success.message.toString(),
                        responseModel.success.message1
                    )

                } else {

                }
            }
        }
    }


    private fun loadChips() {
        // Clear any previous chips
        binding.chipGroup.removeAllViews()
        // Add chips dynamically to the ChipGroup
        for (i in 0 until chipAdapter.count) {
            val chipView = chipAdapter.getView(i, null, binding.chipGroup)
            binding.chipGroup.addView(chipView)
        }
    }

    override fun updateData(chipTextList: ArrayList<chipData>, value: String) {
        loanAmount = value

        LoggerMessage.LogErrorMsg("select value", "Value===" + value)

        chipAdapter = LoanChipAdapter(this, chipTextList, this)
        chipAdapter.notifyDataSetChanged()
        loadChips()
    }

    private fun getData() {

        showProgressDialog(this, true)
        val request = FinanceDataLiatRequest(
            PreferenceManager.getPhoneNo(this),
            "Required Loan Amount",
            PreferenceManager.getServerDateUtc(""),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this)
        )
        mViewModel?.getFinanceData(request)
    }

    private fun updateData(message: String, message1: String) {
        val abx = FinaceSubmitBox(this, message, message1, "cl")
        abx.show()
    }

    private fun dataList(data: ArrayList<chipData>) {
        dismissProgressDialog()
        chipAdapter = LoanChipAdapter(this, data, this)
        loadChips()
    }

    override fun requestError(error: String) {
        dismissProgressDialog()
        LoggerMessage.onSNACK(binding.city, error, this)
    }


    /*    override fun GoogleApiKey(key: String, view: TextView, type: String, apiType: String) {
            dismissProgressDialog()

            var cityDailog: CityState =
                CityState(this, this, "cs", binding.city!!, key, apiType, "M", true)
            cityDailog?.show()
        }

        override fun GoogleCodeApiKey(
            key: String,
            type: String,
            value: String,
            id: String,
            apiType: String
        ) {
            var myResponse: MyResponse = MyResponse()
            showProgressDialog()
            if (apiType.toString().toUpperCase().equals("M")) {
                var vollyRequests: VollyRequests = VollyRequests()
                var data: PathsModle =
                    PreferenceManager.stringToPath(PreferenceManager.getApiPath(this))
                vollyRequests.translateText(
                    this,
                    value,
                    id,
                    "en",
                    this,
                    type,
                    key,
                    data.GetPlaceSearches
                )
            } else {
                myResponse.translateText(value, id, "en", this, "from", key, this)
            }
        }

        override fun googleApiError(error: String) {
            dismissProgressDialog()
        }
    */


    override fun fromCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    ) {
    }

    override fun toCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    ) {
    }

    override fun cityState(
        cityEng: String,
        cityHi: String,
        stateEn: String,
        stateHi: String,
        id: String,
        type: String
    ) {
        binding.city.text = cityEng
        binding.state.text = stateEn
        binding.city.error = null
        binding.state.error = null
    }

    fun getCityState(v: View) {
        val cityDialog = CityStateDialog(this, this, "cs", binding.city, "M", true)
        cityDialog.show()
    }


    fun submit(v: View) {
        if (validation()) {
            dataSubmit()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun dataSubmit() {

        val request = LoanDataSubmitRequest(
            binding.city.text.toString(),
            PreferenceManager.getPhoneNo(this),
            loanAmount,
            binding.mobileNumber.text.toString(),
            binding.name.text.toString(),
            PreferenceManager.getProfileType(this).toString(),
            PreferenceManager.getServerDateUtc(""),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            binding.state.text.toString(),
            binding.typeLoan.selectedItem.toString(),
            loanFor,
            PreferenceManager.getPhoneNo(this),
            binding.etReferralCode.getText().toString(),
            sourceValue!!
        )
        showProgressDialog(this, false)
        mViewModel?.submitFinanceData(request)
    }

    private fun validation(): Boolean {
        if (TextUtils.isEmpty(binding.name.text)) {
            LoggerMessage.onSNACK(
                binding.state,
                resources.getString(R.string.enterYourName),
                this
            )
            return false
        }
        if (loanFor == "other") {
            if (getSpecialCharacterCount(binding.name.text.toString()) == 0) {
                LoggerMessage.onSNACK(
                    binding.name,
                    resources.getString(R.string.enterYourrightName),
                    this
                )
                return false
            }
        }
        if (TextUtils.isEmpty(binding.mobileNumber.text)) {
            LoggerMessage.onSNACK(
                binding.state,
                resources.getString(R.string.enter_mobile_no),
                this
            )
            return false
        }

        if (binding.mobileNumber.text.length < 10) {
            LoggerMessage.onSNACK(
                binding.state,
                resources.getString(R.string.enter_right_number_v),
                this
            )
            return false
        }
        if (!isValidPhoneNumber(binding.mobileNumber.text.toString())) {
            LoggerMessage.onSNACK(
                binding.state,
                resources.getString(R.string.enter_right_number_v),
                this
            )
            return false
        }
        if (TextUtils.isEmpty(binding.city.text)) {
            LoggerMessage.onSNACK(binding.state, resources.getString(R.string.slectCity), this)
            return false
        }
        if (TextUtils.isEmpty(binding.state.text)) {
            LoggerMessage.onSNACK(binding.state, resources.getString(R.string.enterState), this)
            return false
        }
        if (TextUtils.isEmpty(loanAmount)) {
            LoggerMessage.onSNACK(
                binding.state,
                resources.getString(R.string.enterLoanPrice),
                this
            )
            return false
        }
//        if (TextUtils.isEmpty(binding.etReferralCode.text))
//        {
//            LoggerMessage.onSNACK( binding.etReferralCode!!,resources.getString(R.string.enter_referral_code2),this)
//
//            return false
//        }
        return true
    }

    fun backScreen(v: View) {
        onBackPressed()
    }


    fun viewPreviousEnquiry(v: View) {
        //finance
        val intent = Intent(this, FinanceHistoryActivity::class.java)
        intent.putExtra("HISTORY_TYPE", "Finance")
        startActivity(intent)
    }


    fun mobileInfo(v: View) {
        val menu = TruckMenu
        menu.aboutPlan(this, binding.info, "", resources.getString(R.string.alt_mobile_info))
    }


    private fun isValidPhoneNumber(phone: String): Boolean {
        val p = Pattern.compile("([6,7,8,9][0-9]{0,9})")
        val m = p.matcher(phone)
        return (m.find() && m.group() == phone)
    }

    private fun getSpecialCharacterCount(s: String?): Int {
        val blockCharacterSet =
            "1234567890~#^&|$%*!@/()[]-'\":;,?{}+=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
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