package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpOnboardingBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.GPOnboardingData

class GPOnboardingActivity : BaseActivity() {
    private lateinit var binding: ActivityGpOnboardingBinding
    val categories = listOf("Select", "Business", "Individual")
    val itemsMap = mapOf(
        "Business" to listOf("Select", "Store", "Dealership", "Distributor ", "Financial Service Firm"),
        "Individual" to listOf("Select", "Sales Agent", "Installation Freelancer")
    )
    var selectedCategory:String=""
    var selectedSubCategory:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGpOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        //test
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.partnerTypeSpinner.adapter = categoryAdapter

        binding.partnerTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
                val items = itemsMap[selectedCategory] ?: emptyList()
                val itemAdapter = ArrayAdapter(this@GPOnboardingActivity, android.R.layout.simple_spinner_item, items)
                itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.subPartnerTypeSpinner.adapter = itemAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }

        binding.subPartnerTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSubCategory = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        //test

        binding.btnAdd.setOnClickListener(){
            checkValidation()
        }

        binding.ivBack.setOnClickListener(){
            onBackPressed()
        }
    }

    private fun checkValidation() {
        if (binding.ETAccountHolderName.text.isEmpty()) {
            binding.ETAccountHolderName.requestFocus()
            binding.ETAccountHolderName.setError(getString(R.string.inputGPName))
        } else if (binding.ETAccountHolderNumber.text.isEmpty()) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.setError(getString(R.string.inputGPNumber))
        }else if (binding.ETAccountHolderNumber.text.length < 10) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.error = getString(R.string.enter_right_number_v)
        }else if(selectedCategory.equals("Select")){
            LoggerMessage.onSNACK(
                binding.partnerTypeSpinner,
                getString(R.string.inputGPPartnerType),
                this
            )
        }else if(selectedSubCategory.equals("Select") || selectedSubCategory.isEmpty()){
            LoggerMessage.onSNACK(
                binding.subPartnerTypeSpinner,
                getString(R.string.inputGPName),
                this
            )
        }else{
            growth_continue()
        }
        /*else if (binding.ETPanNumberNOB.length() == 10) {
            val s = binding.ETPanNumberNOB.toString() // get your editext value here
            val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            val matcher: Matcher = pattern.matcher(s)
            // Check if pattern matches
            if (matcher.matches()) {
                panNumber = binding.ETPanNumberNOB.toString()
            } else {
                //LoggerMessage.tostPrint("Enter Right PAN No", this)
                binding.ETPanNumberNOB?.setError("Enter Right PAN Noss")
                // pan_no?.setText("")
            }
        } else {
            binding.ETPanNumberNOB.requestFocus()
            *//*val customErrorDrawable = resources.getDrawable(com.trucksup.fieldofficer.R.drawable.ic_phone)
            customErrorDrawable.setBounds(
                0,
                0,
                customErrorDrawable.intrinsicWidth,
                customErrorDrawable.intrinsicHeight
            )
            binding.ETPanNumberNOB?.setError("Enter Right PAN No" , customErrorDrawable)*//*
            binding.ETPanNumberNOB?.setError("Enter Right PAN No")
            binding.ETPanNumberNOB?.setText("")
        }*/

    }

    fun growth_continue() {
        val userData = GPOnboardingData(binding.ETAccountHolderName.text.toString(),
            binding.ETAccountHolderNumber.text.toString(), selectedCategory,
            selectedCategory,"","",""
        ,"","","","","","",""
        ,"","","", "",""
        ,"","","","","") // only name filled

        val gson = Gson()
        val jsonData = gson.toJson(userData)
        val intent = Intent(this, GPPersonalDetailUpdateActivity::class.java)
        intent.putExtra("userDataJson", jsonData)
        startActivity(intent)

        //startActivity(Intent(this, GPPersonalDetailUpdateActivity::class.java))
    }

}