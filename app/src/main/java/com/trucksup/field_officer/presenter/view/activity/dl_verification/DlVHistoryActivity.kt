/*
package com.trucksup.field_officer.presenter.view.activity.dl_verification

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.PreferenceManager.PreferenceManager
import com.logistics.trucksup.R
import com.logistics.trucksup.activities.TransactionHistory
import com.trucksup.field_officer.presenter.view.activity.dl_verification.interface_controller.DlVerifyController
import com.logistics.trucksup.activities.vehicleVerify.JWTtoken
import com.logistics.trucksup.adaptor.DLHistoryAdapter
import com.logistics.trucksup.databinding.ActivityDlVhistoryBinding
import com.logistics.trucksup.modle.GenerateJWTtokenResponse
import com.logistics.trucksup.modle.VerifiedDLDetailsResponse
import com.logistics.trucksup.modle.requestModle.GenerateJWTtokenRequest
import com.logistics.trucksup.modle.requestModle.VerifiedDLDetailsRequest
import com.logistics.trucksup.network.vModle.MyResponse
import com.logistics.trucksup.progress_bar.LoadingUtils
import com.trucksup.field_officer.databinding.ActivityDlVhistoryBinding

class DlVHistoryActivity : AppCompatActivity(), JWTtoken, DlVerifyController {

    private lateinit var binding: ActivityDlVhistoryBinding

    //    var progressDailogBox: ProgressDailogBox? = null
    var dLHistoryAdapter: DLHistoryAdapter? = null
    var list = ArrayList<VerifiedDLDetailsResponse.VerifiedDLDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dl_vhistory)

//        progressDailogBox = ProgressDailogBox(this)

        getToken()

        setListener()
    }

    private fun setListener() {
        //back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        //transaction history button
        binding.btnTransactionHis.setOnClickListener {
            val intent = Intent(this, TransactionHistory::class.java)
            startActivity(intent)
        }

        binding.etSearch.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                    filter(binding.etSearch.getText().toString())
                    return true
                }
                return false
            }
        })
    }

    private fun filter(text: String) {
        try {
            // creating a new array list to filter our data.
            val filteredlist: ArrayList<VerifiedDLDetailsResponse.VerifiedDLDetail> = ArrayList()

            // running a for loop to compare elements.
            for (item in list) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.licenseNumber.lowercase()
                        .contains(text.lowercase()) || item.trxnDateTime.lowercase()
                        .contains(text.lowercase()) || item.name.lowercase()
                        .contains(text.lowercase()) || item.doe.lowercase()
                        .contains(text.lowercase())
                ) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item)
                }
            }
            if (filteredlist.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                binding.noData.visibility = View.VISIBLE
                binding.rv.visibility = View.GONE
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                dLHistoryAdapter?.filterList(filteredlist)
                binding.noData.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
            }
        } catch (e: Exception) {

        }
    }

    fun getToken() {
//        progressDailogBox?.show()
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

    fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        var request = VerifiedDLDetailsRequest(
            requestId = PreferenceManager.getRequestNo(),
            requestDatetime = PreferenceManager.getServerDateUtc(""),
            requestedBy = PreferenceManager.getPhoneNo(this),
            mobileNumber = PreferenceManager.getPhoneNo(this),
            limit = 0
        )
        MyResponse().verifiedDLDetails(response.accessToken, request, this, this)
    }

    override fun onTokenFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

    override fun onVerifySuccess(response: VerifiedDLDetailsResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()

        if (response.verifiedDLDetails.isNullOrEmpty()) {
            binding.noData.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE
        } else {
            binding.noData.visibility = View.GONE
            binding.rv.visibility = View.VISIBLE
            list = response.verifiedDLDetails
            dLHistoryAdapter = DLHistoryAdapter(this@DlVHistoryActivity, list)
            binding.rv.apply {
                layoutManager =
                    LinearLayoutManager(this@DlVHistoryActivity, RecyclerView.VERTICAL, false)
                adapter = dLHistoryAdapter
                hasFixedSize()
            }

            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.isNullOrEmpty()) {
                        if (dLHistoryAdapter != null) {
                            dLHistoryAdapter!!.filterList(list)
                            binding.noData.visibility = View.GONE
                            binding.rv.visibility = View.VISIBLE
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

    override fun onVerifyFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

}*/
