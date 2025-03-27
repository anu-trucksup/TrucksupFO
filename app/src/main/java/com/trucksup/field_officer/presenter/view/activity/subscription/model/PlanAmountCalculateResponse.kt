package com.logistics.trucksup.modle

data class PlanAmountCalculateResponse(
    val data: CalculateData,
    val message: String,
    val requestId: String,
    val responseDatetime: String,
    val status: String,
    val statuscode: Int
)

data class CalculateData(
    val addedAddons: List<AddedAddon>,
    val previousPlanAmount: String,
    val validityDays: String,
    val perdayamount: String,
    val remainingdays: String,
    val deductAmount: String,
    val actualAmount: String,
    val addOnAmount: String,
    val amountAfterDeduction: String,
    val subscriptionAmount: String,
    val receavableAmount: String,
    val upgrade: String,

)

data class AddedAddon(
    val actualPrice: String,
    val addonAmount: String,
    val extraAddedQTY: String,
    val freeQty: String,
    val id: String,
    val matrixName: String,
    val sellingPrice: String,
    val totalQty: String
)