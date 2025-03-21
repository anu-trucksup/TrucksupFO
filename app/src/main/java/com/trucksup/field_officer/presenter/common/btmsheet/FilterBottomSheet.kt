/*
package com.glovejob.app.common.btmsheet

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections

*/
/**
 * A simple [Fragment] subclass.
 * Use the [FilterBottomSheet.newInstance] factory method to
 * create an instance of this fragment.
 *//*

@AndroidEntryPoint
class FilterBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private var minAdapter: ArrayAdapter<String>? = null
    private var maxAdapter: ArrayAdapter<String>? = null
    private var mLogoutBinding: FilterBottomSheetBinding? = null
    private var listener: OnFilterApplyClick? = null
    private var msg: String? = null
    private lateinit var selectedList: ArrayList<FilterCondition>

    var minBudgetshownList: Array<String> =
        arrayOf("Min","5 Lacs", "10 Lacs", "15 Lacs", "20 Lacs", "25 Lacs", "50 Lacs", "80 Lacs", "1 Cr", "10+ Cr")

    var maxBudgetshownList: Array<String> =
        arrayOf("Max","5 Lacs", "10 Lacs", "15 Lacs", "20 Lacs", "25 Lacs", "50 Lacs", "80 Lacs", "1 Cr", "10+ Cr")

    var minBudgetList: Array<String> =
        arrayOf("","500000", "1000000", "1500000", "2000000", "2500000", "5000000","8000000", "10000000", "100000000")

    var bedroomsArray: Array<String> =
        arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "10+")

    var visibleBudget: Boolean = true
    var visiblePropertyType: Boolean = true
    var visibleBedrooms: Boolean = true

    var propartyArray: Array<String> = arrayOf(
        "Residential",
        "Commercial",
        "Flat/Apartment",
        "Independent House/Villa",
        "Agricultural Land",
        "Farm House",
        "Service Apartment"
    )
    var filterList = arrayListOf<FilterCondition>()

    var propertyTypedatas = mutableListOf<Bean>()
    val selectedpropertyTypeList = mutableListOf<Bean>()

    var noBedroomList = mutableListOf<Bean>()
    val selectednoBedroomList = mutableListOf<Bean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mLogoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.filter_bottom_sheet, container, false)
        val view: View = mLogoutBinding!!.getRoot()
        initialize()
        return view
    }

    private fun initialize() {

        mLogoutBinding?.btnApplyFilter?.setOnClickListener(this)
        mLogoutBinding?.btnClearAll?.setOnClickListener(this)

        if (selectedList != null && selectedList.size > 0) {
            initData()
            initBedroomData()
            setspinner()
            selectedList.forEach {
                if (it.filterName.equals("type")) {
                    val type = it.filterValue
                    val temp = type.split(",")

                    for (j in propartyArray.indices) {
                        for (type in temp) {
                            if (type.equals(propartyArray[j])) {
                                propertyTypedatas.set(j,Bean(propartyArray.get(j), true))
                            } else { }

                        }
                    }
                }

                propertyTypedatas.forEachIndexed(){index: Int, bean: Bean ->
                    run {
                        if (bean.isSelect) {
                            selectedpropertyTypeList.add(bean)
                        }
                    }

                }
                if (it.filterName.equals("noOfBadroom")) {
                    var noOfBedroom = it.filterValue
                    val temp = noOfBedroom.split(",");

                    for (j in bedroomsArray.indices) {
                        for (type in temp) {
                            if (type.equals(bedroomsArray[j])) {
                                noBedroomList.set(j,Bean(bedroomsArray.get(j), true))
                            } else { }
                        }


                    }
                }

                noBedroomList.forEachIndexed(){index: Int, bean: Bean ->
                    run {
                        if (bean.isSelect) {
                            selectednoBedroomList.add(bean)
                        }
                    }

                }

                if (it.filterName.equals("fromAsking")) {
                    val fromValue = pricecheck(it.filterValue.toInt())
                    val spinnerPosition = minAdapter?.getPosition(fromValue)
                    mLogoutBinding?.minBudgetDropdown?.setSelection(spinnerPosition!!)
                }
                if (it.filterName.equals("toAsking")) {
                    val toAsking = pricecheck(it.filterValue.toInt())
                    var spinnerPosition = maxAdapter?.getPosition(toAsking);
                    mLogoutBinding?.maxBudgetDropdown?.setSelection(spinnerPosition!!)
                }

            }

            mLogoutBinding?.budgetDropdownLayout?.setVisibility(View.VISIBLE)
            mLogoutBinding?.arrowUpBudget?.setVisibility(View.VISIBLE)
            mLogoutBinding?.arrowDownBudget?.setVisibility(View.GONE)
            visibleBudget = false

            mLogoutBinding?.propertyTypeDropdown?.setVisibility(View.VISIBLE)
            mLogoutBinding?.arrowUpProperty?.setVisibility(View.VISIBLE)
            mLogoutBinding?.arrowDownProperty?.setVisibility(View.GONE)
            visiblePropertyType = false

            mLogoutBinding?.bedroomsDropdown?.setVisibility(View.VISIBLE)
            mLogoutBinding?.arrowUpBedrooms?.setVisibility(View.VISIBLE)
            mLogoutBinding?.arrowDownBedrooms?.setVisibility(View.GONE)
            visibleBedrooms = false

            //initBedroomData()

            setProprtyType()
            setNoBedroom()
        } else {
            initData()
            initBedroomData()
            setspinner()
            setProprtyType()
            setNoBedroom()
        }


        mLogoutBinding?.budgetLayout?.setOnClickListener(View.OnClickListener { view ->

            if (visibleBudget) {
                mLogoutBinding?.budgetDropdownLayout?.setVisibility(View.VISIBLE)
                mLogoutBinding?.arrowUpBudget?.setVisibility(View.VISIBLE)
                mLogoutBinding?.arrowDownBudget?.setVisibility(View.GONE)
                visibleBudget = false
            } else {
                mLogoutBinding?.budgetDropdownLayout?.setVisibility(View.GONE)
                mLogoutBinding?.arrowUpBudget?.setVisibility(View.GONE)
                mLogoutBinding?.arrowDownBudget?.setVisibility(View.VISIBLE)
                visibleBudget = true

            }

        })

        mLogoutBinding?.propertyTypeLayout?.setOnClickListener(View.OnClickListener { view ->

            if (visiblePropertyType) {
                mLogoutBinding?.propertyTypeDropdown?.setVisibility(View.VISIBLE)
                mLogoutBinding?.arrowUpProperty?.setVisibility(View.VISIBLE)
                mLogoutBinding?.arrowDownProperty?.setVisibility(View.GONE)
                visiblePropertyType = false
            } else {
                mLogoutBinding?.propertyTypeDropdown?.setVisibility(View.GONE)
                mLogoutBinding?.arrowUpProperty?.setVisibility(View.GONE)
                mLogoutBinding?.arrowDownProperty?.setVisibility(View.VISIBLE)
                visiblePropertyType = true
            }
        })

        mLogoutBinding?.bedroomsLayout?.setOnClickListener(View.OnClickListener { view ->

            if (visibleBedrooms) {
                mLogoutBinding?.bedroomsDropdown?.setVisibility(View.VISIBLE)
                mLogoutBinding?.arrowUpBedrooms?.setVisibility(View.VISIBLE)
                mLogoutBinding?.arrowDownBedrooms?.setVisibility(View.GONE)
                visibleBedrooms = false
            } else {
                mLogoutBinding?.bedroomsDropdown?.setVisibility(View.GONE)
                mLogoutBinding?.arrowUpBedrooms?.setVisibility(View.GONE)
                mLogoutBinding?.arrowDownBedrooms?.setVisibility(View.VISIBLE)
                visibleBedrooms = true
            }

        })

    }

    private fun setProprtyType() {
        //initializing LayoutManager & Divider
        mLogoutBinding?.propertyTypeDropdown?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        //Creating a Custom adapter
        val adapter = PropertyTypeAdapter(propertyTypedatas)
        mLogoutBinding?.propertyTypeDropdown?.adapter = adapter

        */
/**
 * Another way of handling the listener,
 * without interface.
 *//*


        adapter!!.setOnItemClickLitener(object : PropertyTypeAdapter.OnItemClickListener {
            override fun onItemClick(bean: Bean) {
                if (bean.isSelect) {
                    selectedpropertyTypeList.add(bean)
                } else {
                    selectedpropertyTypeList.remove(bean)
                }
                // tv_count.text = String.format("已选中 %s 项", selectDatas.size)
            }


        })
    }

    private fun setNoBedroom() {
        //initializing LayoutManager & Divider
        mLogoutBinding?.bedroomsDropdown?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        //binding?.spinType?.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))

        //Creating a Custom adapter
        val adapter = PropertyTypeAdapter(noBedroomList)
        mLogoutBinding?.bedroomsDropdown?.adapter = adapter

        */
/**
 * Another way of handling the listener,
 * without interface.
 *//*


        adapter!!.setOnItemClickLitener(object : PropertyTypeAdapter.OnItemClickListener {
            override fun onItemClick(bean: Bean) {
                if (bean.isSelect) {
                    selectednoBedroomList.add(bean)
                } else {
                    selectednoBedroomList.remove(bean)
                }
                // tv_count.text = String.format("已选中 %s 项", selectDatas.size)
            }


        })
    }

    private fun initData() {
        propertyTypedatas.clear()
        for (i in propartyArray.indices) {
            propertyTypedatas.add(Bean(propartyArray.get(i), false))
        }
    }

    private fun initBedroomData() {
        noBedroomList.clear()
        for (i in bedroomsArray.indices) {
            noBedroomList.add(Bean(bedroomsArray.get(i), false))
        }
    }

    private fun setspinner() {
        minAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1, minBudgetshownList
        )
        mLogoutBinding?.minBudgetDropdown?.adapter = minAdapter

        maxAdapter = ArrayAdapter<String>( requireContext(),
            android.R.layout.simple_list_item_1,
            maxBudgetshownList
        )
        mLogoutBinding?.maxBudgetDropdown?.adapter = maxAdapter
    }



    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_clear_all -> {
                dismiss()
                listener!!.onfilterListener(arrayListOf())
            }

            R.id.btn_apply_filter -> {
                dismiss()


                var selectBedroom = ""
                selectednoBedroomList.forEach { it ->
                    selectBedroom += it.title + ","
                }
                if (selectBedroom.isNotEmpty()) {
                    val filtervalType = FilterCondition("noOfBadroom", selectBedroom)
                    filterList.add(filtervalType)
                }

                var selectType = ""
                selectedpropertyTypeList.forEach { it ->
                    selectType += it.title + ","
                }
                if (selectType.isNotEmpty()) {
                    val filtervalType = FilterCondition("type", selectType)
                    filterList.add(filtervalType)
                }

                if (!mLogoutBinding?.minBudgetDropdown?.selectedItem.toString().equals("Min")) {
                    val pos = mLogoutBinding?.minBudgetDropdown?.selectedItemPosition
                    val fromAsk = FilterCondition(
                        "fromAsking",
                       minBudgetList.get(pos?:0)
                    )
                    filterList.add(fromAsk)
                }
                if (!mLogoutBinding?.maxBudgetDropdown?.selectedItem.toString().equals("Max")
                ) {
                    val pos = mLogoutBinding?.maxBudgetDropdown?.selectedItemPosition
                    val toAsk = FilterCondition(
                        "toAsking",
                        minBudgetList.get(pos?:0)
                    )
                    filterList.add(toAsk)

                }

                listener!!.onfilterListener(filterList)
            }
        }
    }



    interface OnFilterApplyClick {
        fun onfilterListener(filterList: ArrayList<FilterCondition>)
    }

    companion object {
        private var isEditCheck: Boolean? = null

        */
/**
 * Use this factory method to create a new instance of
 * this fragment using the provided parameters.
 *
 * @return A new instance of fragment LogoutBottomSheet.
 *//*

        // TODO: Rename and change types and number of parameters
        fun newInstance(
            clickListener: OnFilterApplyClick?,
            msg: String?,
            isEdit: Boolean?,
            selectedList: ArrayList<FilterCondition>
        ): FilterBottomSheet {
            val dialog = FilterBottomSheet()
            dialog.listener = clickListener
            dialog.msg = msg
            isEditCheck = isEdit
            dialog.selectedList = selectedList
            return dialog
        }
    }

    fun pricecheck(number: Int): String {

        var numberString = if (Math.abs(number / 10000000) > 10) {
            (number / 10000000).toString() + "+ Cr"
        }else if (Math.abs(number / 10000000) > 1) {
            (number / 10000000).toString() + " Cr"
        }else if (Math.abs(number / 100000) > 1) {
            (number / 100000).toString() + " Lacs"
        }  else {
            number.toString()
        }

        return numberString.toString()
    }





}*/
