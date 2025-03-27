package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.logistics.trucksup.activities.trackingAndVerification.TrackingCheckOut
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.VasPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.TrackingExtraPlanAdaptor
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.controller.TrackingPlanCountCantroler
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.DataTrackingPlan


class VehicalTrackingPlan(
    var planList: ArrayList<DataTrackingPlan>,
    var planDay: Int,
    var type: String
) : BottomSheetDialogFragment(),
    TrackingPlanCountCantroler {
    var coutnList = ArrayList<VasPlan>()
    var mContext: Context? = null
    var mView: View? = null
    var amount: String = ""
    var dataList: RecyclerView? = null
    var next: TextView? = null
    var info: ImageButton? = null
    var cardCount: MaterialCardView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
        // Inflate the layout for this fragment

        mContext = context
        mView = inflater.inflate(R.layout.vehicale_tracking_plan, container, false)
        info = mView?.findViewById<ImageButton>(R.id.info)

        inst()
        return mView
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme
    fun inst() {
        dataList = mView?.findViewById(R.id.planList)
        next = mView?.findViewById(R.id.next)
        cardCount = mView?.findViewById(R.id.cardCount)
        next?.setOnClickListener {
            if (next?.tag == "y") {
                nextData()
            }
        }

        info?.setOnClickListener {
           /* var menu = TruckMenu
            menu.aboutPlan(
                context as Activity,
                info!!,
                planList.get(0).serviceName!!,
                planList[0].notes.toString()
            )*/
        }
        setData(planList)
    }

    fun setData(planListData: ArrayList<DataTrackingPlan>) {

        var aList = ArrayList<DataTrackingPlan>()

        for (value in planListData) {

            if (type == "t") {
                var d: DataTrackingPlan = DataTrackingPlan(
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
                    planDay,
                    value.serviceType,
                    value.serviceValidityType,
                    value.unitText,
                    value.vasId,
                    planDay
                )

                aList.add(d)
                var r: Int = planDay!! * value.mrp!!
                amount = r.toString()
            } else {
                var d: DataTrackingPlan = DataTrackingPlan(
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
                    planDay
                )

                aList.add(d)
                var r: Int = planDay!! * value.mrp!!
                amount = r.toString()
            }
            var dataAdd: VasPlan = VasPlan(true, planDay, value.vasId.toString())
            coutnList.add(dataAdd)
        }


        var adaptor =
            TrackingExtraPlanAdaptor(mContext!!, aList, this, planDay)

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

        next?.visibility = View.VISIBLE

        mContext?.getColor(R.color.blue)?.let { cardCount?.setCardBackgroundColor(it) };
        mContext?.let { next?.setTextColor(it.getColor(R.color.white)) }
        cardCount?.strokeColor = mContext?.getColor(R.color.blue)!!
        next?.tag = "y"
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
            mContext?.getColor(R.color.card_dis)?.let { cardCount?.setCardBackgroundColor(it) };
            mContext?.let { next?.setTextColor(it.getColor(R.color.optinal_text_color)) }
            cardCount?.strokeColor = mContext?.getColor(R.color.card_dis)!!
            next?.tag = "n"
        }
    }

    override fun calculateData() {

    }

    fun nextData() {
        val jsonStringAddons = Gson().toJson(coutnList)
        LoggerMessage.LogErrorMsg("Plan data", "Data====" + jsonStringAddons)

        val parameters = Bundle().apply {
            this.putString("mobile", PreferenceManager.getPhoneNo(mContext!!))
            this.putString("plan", "tracking")
        }
        // FirebaseAnalytics().CreateCustomEventFullData("tracking_chackout",parameters)
        var intent = Intent(context, TrackingCheckOut::class.java)
        intent.putExtra("pn", planList[0].serviceName)
        intent.putExtra("cd", coutnList[0].qty.toString())
        intent.putExtra("am", amount)
        intent.putExtra("data", jsonStringAddons)
        context?.startActivity(intent)
        this.dismiss()

    }
}