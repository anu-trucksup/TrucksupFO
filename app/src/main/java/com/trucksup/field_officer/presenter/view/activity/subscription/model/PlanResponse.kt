package com.logistics.trucksup.modle

data class PlanResponse(
    val broker: List<Broker>,
    val message: String,
    val owner: List<Broker>,
    val addons: List<AddonsData>,
    val vehicleverification: List<AddonsData>,
    val brokerFaq: List<OwnerFaq>,
    val ownerFaq: List<OwnerFaq>,
    val requestedBy: String,
    val responseDatetime: String,
    val status: Int,
    var currentPlanDetails: CurantPlanData? = null
)

data class CurantPlan(
    val amountPaid: String,
    val endDate: String,
    val planId: String,
    val planName: String,
    val planStatus: String,
    val startdate: String,
    val text1: String,
    val text2: String,
    val transactionDatetime: String,
    val validityDays: String,
    val upGradeDate: String,
    val upgradeBtnVisible: String,
    val subsPendingLoads: String,
    val isWelcomePlan: String,
    val freeLoadCount: String

)


data class CurantPlanData(
    var welcomePlan: CurantPlan? = null,
    var previousPlan: CurantPlan? = null,

    )

data class Broker(
    val actualAmount: Int? = null,
    val planList: List<BrokerPlan>? = null,
    val createdAt: Any? = null,
    val createdBy: Any? = null,
    val currentPlan: String? = null,
    val description: String? = null,
    val disabledPlan: String? = null,
    val gstPercent: Int? = null,
    val headerId: Int? = null,
    val id: Int? = null,
    val isVisible: Boolean? = null,
    val mrp: Int? = null,
    val planAmount: Int? = null,
    val recommended: String? = null,
    val sellingPrice: Int? = null,
    val subType: String? = null,
    val subscriptionName: String? = null,
    val tags: String? = null,
    val userType: Int? = null,
    val validityDays: Int? = null,
    val isClick: Boolean = false,
    val freebies: List<AddonsData>,
    val addLoadinPkg: String? = null,
    val perDayAmount: String? = null,
    val discount: List<BrokerDiscount>? = null,
    val planStatus: String? = null
)

data class AddonsData(
    val actualPrice: Int? = null,
    val cgstPercent: Int? = null,
    val createdBy: String? = null,
    val gstInclude: Boolean? = null,
    val gstPercent: Int? = null,
    val imageurl: String? = null,
    val isAddon: Boolean? = null,
    val isIncluded: String? = null,
    val matrixName: String? = null,
    val mid: Int? = null,
    val moduleName: String? = null,
    val mrp: Int? = null,
    val perUnit: Int? = null,
    val pkgUnit: Int? = null,
    val planId: Int? = null,
    val sellingPrice: Int? = null,
    val sgstPercent: Int? = null,
    val subType: String? = null,
    val validityDays: Int? = null,
    val counterDay: Int? = null,
    val dataType: String? = null,
    val notes: String? = null,
    val unitText: String
)


data class BrokerDiscount(
    val planName: String? = null,
    val message: String? = null,
    val discountPercent: String? = null,
    val messagehindi: String? = null,

    )


data class BrokerPlan(
    val activeFlag: Any? = null,
    val actualPrice: Int? = null,
    val cgstPercent: Int? = null,
    val createdAt: Any? = null,
    val createdBy: String? = null,
    val deletedAt: Any? = null,
    val deletedBy: Any? = null,
    val gstInclude: Boolean? = null,
    val gstPercent: Int? = null,
    val isAddon: Boolean? = null,
    val matrixName: String? = null,
    val mid: Int? = null,
    val modifiedAt: Any? = null,
    val modifiedBy: Any? = null,
    val moduleName: String? = null,
    val mrp: Int? = null,
    val perUnit: Int? = null,
    val pkgUnit: Int? = null,
    val planId: Int? = null,
    val sellingPrice: Int? = null,
    val sgstPercent: Int? = null,
    val subType: String? = null,
    val validityDays: Int? = null,
    var isIncluded: String? = null,
    val imageurl: String? = null,
    val matrixDescription: String,
    val isPaid: String

)

data class OwnerFaq(
    val answer: String,
    val id: Int,
    val listAns: List<Ans>,
    val question: String
)

data class Ans(
    val listAns: String,
    val qid: Int
)