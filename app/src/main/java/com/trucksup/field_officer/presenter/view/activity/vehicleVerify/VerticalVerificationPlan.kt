package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.subscription.GetPlansResponse
import com.trucksup.field_officer.presenter.utils.LoggerMessage

class VerticalVerificationPlan(var planList: ArrayList<GetPlansResponse.Data>) :
    BottomSheetDialogFragment(),
    TrackingPlanCountCantroler {
    var coutnList = ArrayList<VasPlan>()
    var mContext: Context? = null
    var mView: View? = null
    var amount: String = ""
    var title: TextView? = null
    var dataList: RecyclerView? = null
    var next: TextView? = null
    var nextLay: MaterialCardView? = null
    var info: ImageButton? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
        // Inflate the layout for this fragment
        mContext = context
        mView = inflater.inflate(R.layout.vehicle_verification_plan, container, false)

        inst()
        return mView
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme
    fun inst() {
        dataList = mView?.findViewById(R.id.planList)
        next = mView?.findViewById(R.id.next)
        title = mView?.findViewById(R.id.tvTitle)
        info = mView?.findViewById(R.id.info)
        nextLay = mView?.findViewById(R.id.continueLay)

        title?.text = planList[0].serviceName

//        if (PreferenceManager.getLanguage(mContext!!)=="en")
//        {
//            title?.text="Vehicle Verification"
//        }
//        else{
//
//            title?.text="वाहन वेरिफिकेशन"
//        }

        info?.setOnClickListener {
            try {
              /*  var menu: TruckMenu = TruckMenu
                menu.aboutPlan(
                    mContext as Activity,
                    info,
                    planList[0].serviceName!!,
                    planList[0].notes!!
                )*/
            } catch (e: Exception) {

            }
        }

        next?.isEnabled = true
//        next?.setBackgroundDrawable(resources.getDrawable(R.drawable.view_history_background2))
//        next?.setTextColor(resources.getColor(R.color.white))
        LoggerMessage?.LogErrorMsg("Data Size", ">>>>>>>>> " + coutnList?.size)

        next?.setOnClickListener {
            nextData()
        }
        setData(planList)
    }

//    fun setData(planListData: ArrayList<GetPlansResponse.Data>) {
//        var adaptor =
//            VerificationExtraPlanAdaptor(mContext!!, planListData, this)
//
//        dataList?.layoutManager = LinearLayoutManager(mContext!!)
//
//        dataList?.adapter = adaptor
//        adaptor.notifyDataSetChanged()
//    }

    fun setData(planListData: ArrayList<GetPlansResponse.Data>) {

        var aList = ArrayList<GetPlansResponse.Data>()

        for (value in planListData) {
            var d: GetPlansResponse.Data = GetPlansResponse.Data(
                value.actualPrice,
                value.contractEndDate,
                value.contractStartDate,
                value.gstPercent,
                value.hsnNo,
                value.imageUrl,
                value.mrp,
                value.notes,
                value.recommended,
                value.serviceDescriptoin,
                value.serviceName,
                value.serviceProvider,
                value.serviceQty,
                value.serviceType,
                value.serviceValidityType,
                value.unitText,
                value.vasId,
                value.remainingCounts,
                1
            )

            aList.add(d)

            //added by yash
            var r: Int = 1 * value.mrp!!
            amount = r.toString()

            var dataAdd: VasPlan = VasPlan(true, 1, value.vasId.toString())
            coutnList.add(dataAdd)
        }


        var adaptor: VerificationExtraPlanAdaptor =
            VerificationExtraPlanAdaptor(mContext!!, aList, this)

        dataList?.layoutManager = LinearLayoutManager(mContext!!)

        dataList?.adapter = adaptor
        adaptor.notifyDataSetChanged()
    }

    override fun ClickAdd(rs: Int, day: String, data: VasPlan) {
        var isHave: Boolean = false
        amount = rs.toString()
        LoggerMessage.LogErrorMsg("amount ", "amount >>>>==== " + rs)
        if (coutnList.size == 0) {

            coutnList.add(data)
            calculateData()
            // Log.e("data size","Data size=="+0+"\ndata add "+data+"\n main data "+checkAddon)
        } else {
            var s: Int = coutnList.size - 1
            for (i in 0..s) {


                if (coutnList[i].vasId.toString().toInt() == data.vasId.toString().toInt()) {
                    coutnList.set(i, data)
                    isHave = true

                    break
                }

            }
        }

//        next?.visibility = View.VISIBLE
        next?.isEnabled = true
//        next?.setBackgroundDrawable(resources.getDrawable(R.drawable.view_history_background2))
//        next?.setTextColor(resources.getColor(R.color.white))
        LoggerMessage?.LogErrorMsg("Data Size", ">>>>>>>>> " + coutnList?.size)
    }

    override fun ClickMines(rs: Int, day: String, data: VasPlan) {
        var isHave: Boolean = false
        amount = rs.toString()
        LoggerMessage.LogErrorMsg("amount ", "amount >>>>==== " + rs)
        if (coutnList.size == 0) {

            coutnList.add(data)
            calculateData()
        } else {


            var s: Int = coutnList.size - 1
            for (i in 0..s) {


                if (coutnList[i].vasId == data.vasId) {
                    coutnList.set(i, data)
                    isHave = true

                    break
                }

            }
        }
        if (isHave == false) {
            coutnList.add(data)
        }
        if (coutnList[0].qty == 0) {
//            next?.visibility = View.GONE
            next?.isEnabled = false
//            next?.setBackgroundDrawable(resources.getDrawable(R.drawable.view_history_background))
//            next?.setTextColor(resources.getColor(R.color.unsellect_text_color))
        }
    }

    override fun calculateData() {

    }

    fun nextData() {
        val jsonStringAddons = Gson().toJson(coutnList)
        LoggerMessage.LogErrorMsg("Plan data", "Data====" + jsonStringAddons)
        var intent: Intent = Intent(context, PaymentCheckoutNewActivity::class.java)
        intent.putExtra("pn", planList[0].serviceName)
        intent.putExtra("cd", coutnList[0].qty.toString())
        intent.putExtra("st",planList[0].serviceType)
        intent.putExtra("rc",planList[0].remainingCounts)
        intent.putExtra("am", amount)
        intent.putExtra("data", jsonStringAddons)
        context?.startActivity(intent)
        this.dismiss()

    }
}