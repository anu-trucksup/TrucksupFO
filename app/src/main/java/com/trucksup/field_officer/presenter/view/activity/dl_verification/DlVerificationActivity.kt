package com.trucksup.field_officer.presenter.view.activity.dl_verification

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityDlVerificationBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import java.util.Locale

class DlVerificationActivity : BaseActivity()/* JWTtoken, PlansCalculation, DlVerifyController,
    DLVerifyContrllerRes*/ {

    private lateinit var binding: ActivityDlVerificationBinding
    //var progressDailogBox: ProgressDailogBox? = null
    private val calendar = Calendar.getInstance()
    var direction: Int = 0
    var formattedDate1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dl_verification)

//        progressDailogBox = ProgressDailogBox(this)

        setListener()
    }

    private fun setListener() {
        //back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        //view all
        binding.btnViewAll.setOnClickListener {
           /* var intent = Intent(this, DlVHistoryActivity::class.java)
            startActivity(intent)*/
        }

        //verify button
        binding.btnVerify.setOnClickListener {
            if (binding.et1.getText().toString().trim().isNullOrEmpty()) {
                LoggerMessage.onSNACK(binding.et1,
                    resources.getString(R.string.enterDLNo),
                    this
                )
            } else if (formattedDate1?.trim().isNullOrEmpty()) {
                LoggerMessage.onSNACK(
                    binding.et2,
                    resources.getString(R.string.enterDOB),
                    this
                )
            } else {
                direction = 1
               // getToken()
//                var intent= Intent(this,DlDetailsActivity::class.java)
//                startActivity(intent)
            }
        }

        //transaction history button
        binding.btnTransactionHis.setOnClickListener {
           // val intent = Intent(this, TransactionHistory::class.java)
           // startActivity(intent)
        }

        //date of birth
        binding.btnDateOfBirth.setOnClickListener {
            showDatePicker()
        }

        //buy dl verification
        binding.btnBuy.setOnClickListener {
            direction = 2
            //getToken()
        }
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)

                val dateFormat1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                formattedDate1 = dateFormat1.format(selectedDate.time)

                // Update the TextView to display the selected date with the "Selected Date: " prefix
                binding.et2.text = "$formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog

        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -1)
        val maxDate = Calendar.getInstance()
        datePickerDialog.datePicker.setMaxDate(maxDate.getTimeInMillis())
        datePickerDialog.show()
    }


    /*fun getToken() {
       //  progressDailogBox?.show()
        LoadingUtils.showDialog(this, false)

        var request = GenerateJWTtokenRequest(
            username = PreferenceManager.getAccesUserName(this),
            password = PreferenceManager.getAccesPassword(this),
            apiSecreteKey = PreferenceManager.getAccesKey(this),
            userAgent = PreferenceManager.getAccesUserAgaint(this),
            issuer = PreferenceManager.getAccesUserInssur(this)
        )
        MyResponse().generateJWTtoken(request, this, this)
    }

    override fun onResume() {
        super.onResume()
        direction = 0
        getToken()
        binding.et1.getText().clear()
        binding.et2.text = ""
        formattedDate1 = null
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        if (direction == 0) {
            var request = VerifiedDLDetailsRequest(
                requestId = PreferenceManager.getRequestNo(),
                requestDatetime = PreferenceManager.getServerDateUtc(""),
                requestedBy = PreferenceManager.getPhoneNo(this),
                mobileNumber = PreferenceManager.getPhoneNo(this),
                limit = 2
            )
            MyResponse().verifiedDLDetails(response.accessToken, request, this, this)
        } else if (direction == 1) {
            var request = DLVerifyRequest(
                requestId = PreferenceManager.getRequestNo(),
                requestDatetime = PreferenceManager.getServerDateUtc(""),
                requestedBy = PreferenceManager.getPhoneNo(this),
                mobileNumber = PreferenceManager.getPhoneNo(this),
                dob = formattedDate1.toString(),
                idNumber = binding.et1.getText().toString(),
                isPreview = false
            )
            MyResponse().dlVerify(response.accessToken, request, this, this)
        } else if (direction == 2) {
            var request = GetPlansRequest(
                mobileNumber = PreferenceManager.getPhoneNo(this),
                requestId = PreferenceManager.getRequestNo(),
                requestedBy = PreferenceManager.getPhoneNo(this),
                vasServiceType = "dlverification"
            )
            MyResponse().getPlans(response.accessToken, request, this, this)
        }

        direction = 0
    }

    override fun onTokenFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

    override fun onSuccessGetPlans(response: GetPlansResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        val bottomSheetFragment = VerticalVerificationPlan(response.data)
        bottomSheetFragment.show(supportFragmentManager, "verificationPlanFragment")
    }

    override fun onFailureGetPlans(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

    override fun onVerifySuccess(response: VerifiedDLDetailsResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        if (response.planDetails != null) {
            binding.tvBalance.text = response.planDetails.count.toString()
            if (response.verifiedDLDetails.isNullOrEmpty()) {
                binding.btnViewAll.visibility = View.GONE
                binding.rv.visibility = View.GONE
                binding.noDataFoundLayout.visibility = View.VISIBLE
            } else {
                binding.btnViewAll.visibility = View.VISIBLE
                binding.noDataFoundLayout.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
                if (response.verifiedDLDetails != null) {
                    binding.rv.apply {
                        layoutManager = LinearLayoutManager(this@DlVerificationActivity,
                            RecyclerView.VERTICAL, false
                        )
                        adapter = DLAdapter(this@DlVerificationActivity, response.verifiedDLDetails)
                        hasFixedSize()
                    }
                }
            }
        } else {
            binding.tvBalance.text = "0"
        }
    }

    override fun onVerifyFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

    override fun onVerifyDetailsSuccess(response: DLVerifyResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        val jsonString = Gson().toJson(response)
        val intent = Intent(this, DlDetailsActivity::class.java)
        intent.putExtra("DATA", jsonString)
        startActivity(intent)
    }

    override fun onVerifyDetailsFailure(msg: String) {
       //progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        binding.et1.getText().clear()
        binding.et2.text = ""
        formattedDate1 = null
    }*/

}