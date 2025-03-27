package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.activities.vehicleVerify.VehicleDetailsActivity
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.VerifiedHistorySearch
import com.logistics.trucksup.modle.requestModle.GetVerifiedVehiclesRequest
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.vehicle.GetVerifiedVehiclesResponse
import com.trucksup.field_officer.databinding.ActivityVehicleVerifyHistoryBinding
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import java.io.File

class VehicleVerifyHistoryActivity : BaseActivity(), JWTtoken, VerificationController {

    private lateinit var binding: ActivityVehicleVerifyHistoryBinding

    var list = ArrayList<GetVerifiedVehiclesResponse.VehicleDetail>()
    var adapterSearch: VerifiedHistorySearch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_verify_history)

        getToken()

        setListener()
    }

    private fun setListener() {
        //transaction history button
        binding.btnTransactionHis.setOnClickListener {
          /*  val intent = Intent(this, TransactionHistory::class.java)
            startActivity(intent)*/
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


    fun backScreen(view: View) {
        finish()
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

    override fun onSuccess(response: GetVerifiedVehiclesResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        if (response.vehicleDetail.isNullOrEmpty()) {
            binding.rvVerifiedVehicle.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        } else {
            binding.rvVerifiedVehicle.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
            list = response.vehicleDetail
            adapterSearch = VerifiedHistorySearch(this@VehicleVerifyHistoryActivity, list)
            binding.rvVerifiedVehicle.apply {
                layoutManager =
                    LinearLayoutManager(
                        this@VehicleVerifyHistoryActivity,
                        RecyclerView.VERTICAL,
                        false
                    )
                adapter = adapterSearch?.apply {
                    setControlListener(object : VerifiedHistorySearch.ControlListener {
                        override fun onShareClick(v: View, vehicleNo: String) {

                            val intent = Intent(context, VehicleDetailsActivity::class.java)
                            intent.putExtra("VEHICLE_NO", vehicleNo)
                            intent.putExtra("SHARE", "share")
                            context.startActivity(intent)
//                        findView(v,this@VehicleVerifyHistoryActivity)
                        }
                    })
                }
                hasFixedSize()
            }

            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.isNullOrEmpty()) {
                        if (adapterSearch != null) {
                            adapterSearch!!.filterList(list)
                            binding.noData.visibility = View.GONE
                            binding.rvVerifiedVehicle.visibility = View.VISIBLE
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

    override fun onFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        var request = GetVerifiedVehiclesRequest(
            requestId = PreferenceManager.getRequestNo(),
            requestDatetime = PreferenceManager.getServerDateUtc(""),
            requestedBy = PreferenceManager.getPhoneNo(this),
            mobileNumber = PreferenceManager.getPhoneNo(this),
            limit = 0
        )
       // MyResponse().getVerifiedVehicles(response.accessToken, request, this, this)
    }

    override fun onTokenFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

    private fun filter(text: String) {
        try {
            // creating a new array list to filter our data.
            val filteredlist: ArrayList<GetVerifiedVehiclesResponse.VehicleDetail> = ArrayList()

            // running a for loop to compare elements.
            for (item in list) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.vehicleNumber.lowercase()
                        .contains(text.lowercase()) || item.dataOwnerName.lowercase()
                        .contains(text.lowercase()) || item.dataChasiNo.lowercase()
                        .contains(text.lowercase()) || item.dateTime.lowercase()
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
                binding.rvVerifiedVehicle.visibility = View.GONE
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                adapterSearch?.filterList(filteredlist)
                binding.noData.visibility = View.GONE
                binding.rvVerifiedVehicle.visibility = View.VISIBLE
            }
        } catch (e: Exception) {

        }
    }

    fun findView(view: View, context: Context) {

//        view.isDrawingCacheEnabled = true
//        val bitmap = Bitmap.createBitmap(view.drawingCache)
//        view.isDrawingCacheEnabled = false
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        // Create a canvas using the bitmap
        val canvas = Canvas(bitmap)

        view.draw(canvas)
        if (bitmap != null) {
            var file: File = PreferenceManager.bitmapTofile(bitmap, context)!!

            shareApp(file)
        } else {
            Log.e("File com", "File null ")
        }

    }


    fun shareApp(file: File) {

        var path: Uri? = null
        var nm = PreferenceManager.getUserName(this!!)
        var ch: Char = ' '
        var adhar: String = nm?.replace(ch.toString(), "").toString();
        var n: String = adhar.toUpperCase()
        var code: String = nm
        if (nm.length > 3) {
            code =
                n.subSequence(0, 4).toString() + PreferenceManager.getPhoneNo(this!!).toUpperCase()
        } else {
            code = nm.toString().trim() + PreferenceManager.getPhoneNo(this!!).toUpperCase()
        }
//        var shareText:String="To download the app, click on the secure link given below. Download Link: \n https://play.google.com/store/apps/details?id=com.logistics.trucksup&referrer="+code.trim()
//
//        if (PreferenceManager.getLanguage(this)=="hi")
//        {
//            shareText="ऐप डाउनलोड करने के लिए नीचे दिए गए सुरक्षित लिंक पर क्लिक करे: \n https://play.google.com/store/apps/details?id=com.logistics.trucksup&referrer="+code.trim()
//        }


        val imgBitmap: Bitmap =
            FileHelp().FileToBitmap(file)    // BitmapFactory.decodeResource(context.getResources(), R.drawable.whatsapp_trucks_logo)
        path = PreferenceManager.getImageUri(this!!, imgBitmap)


        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, path);
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, "TrucksUp")
        startActivity(shareIntent)
    }

}