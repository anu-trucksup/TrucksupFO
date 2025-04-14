package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.add_truck.AllPickerClick
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.add_truck.LoadItemPikerClick
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.add_truck.LoadUploadAdapter
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadTyre
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.LoadItemManager

class LoadItemsBoxDialog(
    var textTitle: String,
    var title_: String,
    var listData: List<AddLoadTyre>,
    var activityContext: Activity,
    var context: AddTruckDialog
) : Dialog(activityContext), AllPickerClick, LoadItemPikerClick {

    var title: TextView? = null
    var list: RecyclerView? = null
    var search: EditText? = null
    var mn: RelativeLayout? = null
    var filterData: List<AddLoadTyre> = listData
//    var mainList:List<String> = listData

    private var allPickere: LoadItemManager = context as LoadItemManager

    init {
        setCancelable(true)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  this.getWindow()?.setFlags(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        setContentView(R.layout.all_picker)
        this.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.getWindow()
            ?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        this.getWindow()
            ?.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);


        this.setCancelable(true)
        inst()
    }

    @SuppressLint("NewApi")
    fun inst() {

        this.getWindow()?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        );


        title = findViewById(R.id.title)
        list = findViewById(R.id.list)
        search = findViewById(R.id.search)
        mn = findViewById(R.id.mn)
        title?.setText(title_ + "")


        if (textTitle.equals("Product type")) {
            search?.visibility = View.VISIBLE
        } else {
            search?.visibility = View.GONE
        }
        LoggerMessage.LogErrorMsg("Dta size", ">>>>>>>" + filterData.size)

        search?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {


            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if (search?.text.toString().length == 0) {
                    //filterData.clear()
                    selList(filterData)

                } else {
                    LoggerMessage.LogErrorMsg(
                        "enter text",
                        ">>>>>>>" + search?.text.toString().toLowerCase()
                    )
                    setFilter(search?.text.toString().toLowerCase())
                }
            }
        })
        title?.setOnClickListener {

            this.dismiss()

        }
        mn?.setOnClickListener {
            dismiss()
        }
        selList(listData)

    }


    fun selList(listData: List<AddLoadTyre>) {
        val cityAdaptor = LoadUploadAdapter(
            activityContext,
            listData,
            textTitle,
            this
        )

        list?.layoutManager = LinearLayoutManager(activityContext!!)
        list?.adapter = cityAdaptor
        cityAdaptor?.notifyDataSetChanged()
    }

    fun setFilter(valueText: String) {

        var listFilter = ArrayList<AddLoadTyre>()
        for (itoms in 0..listData.size - 1) {
            if (PreferenceManager.getLanguage(activityContext!!) == "en") {

                if (filterData.get(itoms).name.toString().toLowerCase()
                        .startsWith(valueText.toLowerCase()) == true
                ) {
                    LoggerMessage.LogErrorMsg("Filter data", ">>>>>>>>>>>" + filterData.get(itoms))
                    listFilter.add(

                        filterData.get(itoms)

                    )
                }
            } else {
                if (filterData.get(itoms).hindiUnit?.startsWith(valueText) == true) {
                    LoggerMessage.LogErrorMsg("Filter data", ">>>>>>>>>>>" + filterData.get(itoms))
                    listFilter.add(
                        filterData.get(itoms)
                    )
                }
            }

        }

        LoggerMessage.LogErrorMsg("filter text", ">>>>>>>" + valueText)

        LoggerMessage.LogErrorMsg("filter size", ">>>>>>>" + listFilter.size)

        if (listFilter.size == 0) {
            selList(filterData)

        } else {
            selList(listFilter)
        }
    }

    override fun allClick(type: String, value: String) {

        this.dismiss()
    }

    override fun loadItomsClick(
        type: String,
        valueEnglish: String,
        valueHindi: String,
        unit: String,
        id: Int
    ) {
        allPickere.loadItoms(type, valueEnglish, valueHindi, unit, id)
        dismiss()
    }


}