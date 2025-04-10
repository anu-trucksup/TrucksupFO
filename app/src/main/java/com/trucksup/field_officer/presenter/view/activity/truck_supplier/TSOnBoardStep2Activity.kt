package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.FromToModel
import com.trucksup.field_officer.databinding.ActivityTsonboardStep2Binding
import com.trucksup.field_officer.databinding.PreferredLaneDialogBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.other.TokenViewModel
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.add_truck.AddTruckInterface
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.VehicleDetail
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.vml.TSOnboard2ViewModel
import com.trucksup.field_officer.presenter.view.adapter.TSTrucksDetailsAdapter
import com.trucksup.field_officer.presenter.view.adapter.TrucksDetailsAdap
import com.trucksup.fieldofficer.adapter.PreferredLaneAdap
import com.trucksup.fieldofficer.adapter.TSTruckDeatilsPreferredLaneAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TSOnBoardStep2Activity : BaseActivity(), PreferredLaneAdap.ControllerListener,
    TrucksDetailsAdap.ControllerListener, TSTrucksDetailsAdapter.ControllerListener,
    TSTruckDeatilsPreferredLaneAdapter.ControllerListener ,AddTruckInterface{

    private lateinit var binding: ActivityTsonboardStep2Binding
    private var preferredLaneList = ArrayList<FromToModel>()
    private var trucksDetailsList = ArrayList<VehicleDetail>()

    private var mViewModel: TSOnboard2ViewModel? = null
    private var mTokenViewModel: TokenViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsonboardStep2Binding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)
        mTokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]
        mViewModel = ViewModelProvider(this)[TSOnboard2ViewModel::class.java]

        setListener()
        setupObserver()
    }

    private fun setupObserver() {
        mViewModel?.resultVerifyTruckLD?.observe(this@TSOnBoardStep2Activity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@TSOnBoardStep2Activity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.statusCode == 200) {
                        val request = RcRequest(
                            "I",
                            PreferenceManager.getPhoneNo(this),
                            binding.vehicalNo.text.toString()
                        )
                        mViewModel?.getRcDetails(PreferenceManager.getAuthToken(), request)
                    } else {
                        val abx = AlertBoxDialog(
                            this@TSOnBoardStep2Activity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@TSOnBoardStep2Activity,
                        "No data found",
                        "m"
                    )
                    abx.show()
                }
            }
        }

        mViewModel?.resultRcResponseLD?.observe(this@TSOnBoardStep2Activity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@TSOnBoardStep2Activity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.vehicleno.isNullOrEmpty()) {
                        val abx = AlertBoxDialog(
                            this@TSOnBoardStep2Activity,
                            resources.getString(R.string.valid_truckno),
                            "m"
                        )
                        abx.show()
                    } else {

                        val vehicleDialog = AddTruckDialog(
                            this@TSOnBoardStep2Activity,this,
                            binding.vehicalNo.text.toString(),
                            responseModel.success,
                            "add"
                        )

                        vehicleDialog.show()
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@TSOnBoardStep2Activity,
                        "No data found",
                        "m"
                    )
                    abx.show()
                }
            }
        }

    }

    private fun setListener() {
        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        //add preferred lane
        binding.btnPreferredLane.setOnClickListener {
            //addPreferredLane(this)
            preferredLaneList.add(
                FromToModel(
                    binding.etFrom.getText().toString(),
                    binding.etTo.getText().toString()
                )
            )
            setRvPreferredLane()
        }

        //add truck details
        binding.btnAddTrucksDetails.setOnClickListener {

            binding.vehicalNo.clearFocus()
            if (TextUtils.isEmpty(binding.vehicalNo.text)) {
                binding.vehicalNo.error = getString(R.string.enter_truckno)
                return@setOnClickListener
            }
            if (getSpecialCharacterCount(binding.vehicalNo.text.toString()) == 0) {
                binding.vehicalNo.error = getString(R.string.enter_truckno)
            }

            showProgressDialog(this, false)
            verifyTruck()
            //addNewTruckDialog()
        }


        //submit button
        binding.btnSubmit.setOnClickListener {
            val happinessCodeBox = HappinessCodeBox(
                this, getString(R.string.hapinessCodeMsg),
                getString(R.string.EnterHappinessCode),
                getString(R.string.resand_sms)
            )
            happinessCodeBox.show()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun verifyTruck() {
        mViewModel?.verifyTruckDetails(
            PreferenceManager.getAuthToken(),
            binding.vehicalNo.text.toString(),
            PreferenceManager.getPhoneNo(this)
        )

        /*  val apiInterface = ApiClient().getClient
          apiInterface.verifyTruck(
              PreferenceManager.getAuthToken(),
              binding.vehicalNo.text.toString(),
              PreferenceManager.getPhoneNo(this)
          )
              ?.enqueue(object : Callback<VerifyTruckResponse> {
                  override fun onResponse(
                      call: Call<VerifyTruckResponse>,
                      response: Response<VerifyTruckResponse>
                  ) {

                      if (response.isSuccessful) {
                          dismissProgressDialog()

                          if (response.body()?.statusCode == 200) {
                              getRc()
                          } else {
                              val abx = AlertBoxDialog(
                                  this@TSOnBoardStep2Activity,
                                  response.body()?.message.toString(), "m"
                              )
                              abx.show()
                          }

                      } else {
                          dismissProgressDialog()
                      }
                  }

                  override fun onFailure(call: Call<VerifyTruckResponse>, t: Throwable) {
                      dismissProgressDialog()
                  }
              })*/

    }

    /*fun getRc() {
        val apiInterface = ApiClient().getClient
        val ph: String = PreferenceManager.getPhoneNo(this)

        val request = RcRequest("I", ph, binding.vehicalNo.text.toString())
        //"9801163497"
        showProgressDialog(this, false)
        apiInterface.getRc(PreferenceManager.getAuthToken(), request)
            ?.enqueue(object : Callback<RcResponse> {
                override fun onResponse(
                    call: Call<RcResponse>,
                    response: Response<RcResponse>
                ) {

                    if (response.isSuccessful) {
                        LoadingUtils.hideDialog()


                        if (response.body()?.vehicleno == null) {
                            val abx: MyAlartBox = MyAlartBox(
                                this@PreferreScreen,
                                resources.getString(R.string.valid_truckno),
                                "m"
                            )
                            abx.show()
                        } else {

                            // val vehicledailog: VehicalDetailsBox =
                            vehicledailog = VehicalDetailsBox(
                                this@PreferreScreen,
                                binding.vehicalNo.text.toString(),
                                response.body()!!,
                                "add",
                                VehicleDetail("", "", "", "", "", "", "", "", "", "", "")
                            )

                            vehicledailog.show()
                        }

                    } else {
                        LoadingUtils.hideDialog()
                    }
                    // println(mUser.albumid)

                }

                override fun onFailure(call: Call<RcResponse>, t: Throwable) {
                    //    LoggerMessage.LogErrorMsg("Error", "" + t.message)
                    LoadingUtils.hideDialog()
                }
            })

    }*/

    private fun getSpecialCharacterCount(s: String?): Int {

        val blockCharacterSet =
            "~#^&|$%*!@/()[]-'\":;,?{}+=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
        blockCharacterSet.toCharArray()

        for (b in blockCharacterSet) {
            if (!TextUtils.isEmpty(s) && s!!.contains(b)) {
                return 0
                break
            }
        }
        return 1
    }

    private fun addPreferredLane(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = PreferredLaneDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //submit button
        binding.btnSubmit.setOnClickListener {
            preferredLaneList.add(
                FromToModel(
                    binding.etFrom.getText().toString(),
                    binding.etTo.getText().toString()
                )
            )
            setRvPreferredLane()
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setRvPreferredLane() {
        binding.rvPreferredLane.apply {
            layoutManager =
                LinearLayoutManager(this@TSOnBoardStep2Activity, RecyclerView.VERTICAL, false)
            adapter = TSTruckDeatilsPreferredLaneAdapter(
                this@TSOnBoardStep2Activity,
                preferredLaneList,
                this@TSOnBoardStep2Activity
            )
            hasFixedSize()
        }
    }

    private fun setRvTruckDetails() {
        binding.rvTrucksDetails.apply {
            layoutManager =
                LinearLayoutManager(this@TSOnBoardStep2Activity, RecyclerView.VERTICAL, false)
            adapter = TSTrucksDetailsAdapter(
                this@TSOnBoardStep2Activity,
                trucksDetailsList,
                this@TSOnBoardStep2Activity
            )
            hasFixedSize()
        }
    }

    override fun onDelete(position: Int) {
        preferredLaneList.removeAt(position)
        setRvPreferredLane()
    }

    override fun onDeleteTruck(position: Int) {
        trucksDetailsList.removeAt(position)
        setRvTruckDetails()
    }


    override fun settruckdetails(vehicleDetail: VehicleDetail) {

        binding.vehicalNo.setText("")
        trucksDetailsList.add(vehicleDetail)

        setRvTruckDetails()

    }

}
