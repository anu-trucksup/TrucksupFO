package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityFinanceBinding
import com.trucksup.field_officer.presenter.common.dialog.FinaceSubmitBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import java.util.regex.Matcher
import java.util.regex.Pattern

class FinanceActivity : BaseActivity(), ChipCantroler {
    private lateinit var binding: ActivityFinanceBinding
    private lateinit var chipAdapter: LoanChipAdapter

    private var loanFor: String = "self"
    private var loanAmount: String = ""
    private var sourceValue: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finance)

        sourceValue = intent.getStringExtra("SOURCE_VALUE")

        //binding.name.setText(PreferenceManager.getUserData(this)?.profileName)

        binding.mobileNumber.setText(PreferenceManager.getPhoneNo(this))

        //binding.etReferralCode.setText(PreferenceManager.getUserData(this)?.salesCode)
        getData()
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

        /*  showProgressDialog()
          var request: FinanceDataLiatRequest = FinanceDataLiatRequest(
              PreferenceManager.getPhoneNo(this),
              "Required Loan Amount",
              PreferenceManager.getServerDateUtc(""),
              PreferenceManager.getRequestNo(),
              PreferenceManager.getPhoneNo(this)
          )
          MyResponse().getFinanceData(request, this, this)*/
    }

    override fun updateData(message: String, message1: String) {
        dismissProgressDialog()
        dismissProgressDialog()
        val abx = FinaceSubmitBox(this, message, message1, "cl")
        abx.show()
    }

    override fun DataList(data: ArrayList<chipData>) {
        dismissProgressDialog()
        chipAdapter = LoanChipAdapter(this, data, this)
        loadChips()
    }

    override fun requestError(error: String) {
        dismissProgressDialog()
        LoggerMessage.onSNACK(binding.city, error, this)
    }

    fun clickSelf(v: View) {
        loanFor = "self"
        binding.self.setBackgroundResource(R.drawable.self_finace_bt_blue)
        binding.self.setTextColor(resources.getColor(R.color.white))

        binding.other.setBackgroundResource(R.drawable.other_finace_bt_gray)
        binding.other.setTextColor(resources.getColor(R.color.secondry_text))

        //  binding.name.setText(PreferenceManager.getUserData(this).profileName)

        binding.mobileNumber.setText(PreferenceManager.getPhoneNo(this))

        //referral code
        //  binding.etReferralCode.setText(PreferenceManager.getUserData(this).salesCode)

        binding.city.setText("")
        binding.state.setText("")

        binding.name.isFocusable = false
        binding.name.isClickable = false  // optional, to disable click events
        binding.name.isFocusableInTouchMode = false
        binding.name.isCursorVisible = false  // optional, hide the cursor
        binding.name.keyListener = null

        //referral code or sales code
        binding.etReferralCode.isFocusable = false
        binding.etReferralCode.isClickable = false  // optional, to disable click events
        binding.etReferralCode.isFocusableInTouchMode = false
        binding.etReferralCode.isCursorVisible = false  // optional, hide the cursor
//        binding.etReferralCode.keyListener = null

        getData()
    }

    fun clickOther(v: View) {
        loanFor = "other"
        binding.self.setBackgroundResource(R.drawable.other_finace_bt_gray)
        binding.self.setTextColor(resources.getColor(R.color.secondry_text))
        binding.other.setBackgroundResource(R.drawable.self_finace_bt_blue)
        binding.other.setTextColor(resources.getColor(R.color.white))

        binding.name.setText("")

        binding.mobileNumber.setText("")

        //referral code
        binding.etReferralCode.setText("")

        binding.city.setText("")
        binding.state.setText("")

        binding.name.isFocusable = true
        binding.name.isClickable = true  // optional, to re-enable click events
        binding.name.isFocusableInTouchMode = true
        binding.name.isCursorVisible = true  // optional, show the cursor
        binding.name.keyListener = EditText(this).keyListener  // restore typing capability

        //referral code
        binding.etReferralCode.isFocusable = true
        binding.etReferralCode.isClickable = true  // optional, to re-enable click events
        binding.etReferralCode.isFocusableInTouchMode = true
        binding.etReferralCode.isCursorVisible = true  // optional, show the cursor
//        binding.etReferralCode.keyListener = EditText(this).keyListener  // restore typing capability

        getData()
    }

    /* override fun GoogleApiKey(key: String, view: TextView, type: String, apiType: String) {
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
 
     override fun translate(text: String, state: String, TranslateText: String, type: String) {
         dismissProgressDialog()
 
         binding.city.setText(TranslateText)
         binding.state.setText(state)
 
     }
 
     override fun TranslateError(error: String) {
     }
 
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
         binding.city?.setText("" + cityEng)
         binding.state?.setText("" + stateEn)
         binding.city?.error = null
         binding.state?.error = null
     }*/

    fun getCityState(v: View) {
        /* showProgressDialog()
         var masterAPI: MasterAPI = MasterAPI()
         masterAPI.getGoodleAPI(this, binding.city!!, "cs", "cs", "", "")*/
//        var cityDailog:CityState = CityState(this,this,"cs",city!!,googleCodeApi)
//        cityDailog?.show()
    }


    fun submit(v: View) {
        if (validation()) {

            dataSubmit()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun dataSubmit() {

        /* val request: LoanDataSubmitRequest = LoanDataSubmitRequest(
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
         showProgressDialog()
         MyResponse().submitFinanceData(request, this, this)*/
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
        finish()
    }


    fun viewPreviousEnquiry(v: View) {
        //finance
        val intent = Intent(this, FinanceHistoryActivity::class.java)
        intent.putExtra("HISTORY_TYPE", "Insurance")
        startActivity(intent)
    }


    /*  fun mobileInfo(v: View) {
          val menu: TruckMenu = TruckMenu
          menu.aboutPlan(this, binding.info, "", resources.getString(R.string.alt_mobile_info))
      }*/

    fun Number_Validate(number: String): Boolean? {
        val expression = "^[0-9-1+]{10,15}$"
        val inputStr: CharSequence = number
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(inputStr)
        return if (matcher.matches()) true else false
        // return !TextUtils.isEmpty(number) && number.length == 10 && Patterns.PHONE.matcher(number).matches()
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

    fun viewPreviousInquery(v: View) {
        //finance
        val intent = Intent(this, FinanceHistoryActivity::class.java)
        intent.putExtra("HISTORY_TYPE", "Finance")
        startActivity(intent)
    }
}