package com.trucksup.field_officer.data.repository

import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.glovejob.data.model.UserSessionResponse
import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.logistics.trucksup.activities.preferre.modle.GetMeetScheduleDetailsRequest
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.data.model.DutyStatusResponse
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.PrivacyAllResponse
import com.trucksup.field_officer.data.model.authModel.ForgetRequest
import com.trucksup.field_officer.data.model.authModel.ForgetResponse
import com.trucksup.field_officer.data.model.authModel.LoginRequest
import com.trucksup.field_officer.data.model.authModel.LoginResponse
import com.trucksup.field_officer.data.model.authModel.SignRequest
import com.trucksup.field_officer.data.model.authModel.SignResponse
import com.trucksup.field_officer.data.model.deleteResponse.DeleteProfileResponse
import com.trucksup.field_officer.data.model.home.HomeCountRequest
import com.trucksup.field_officer.data.model.home.HomeCountResponse
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.data.model.otp.OtpResponse
import com.trucksup.field_officer.data.model.otp.VerifyOtpRequest
import com.trucksup.field_officer.data.model.otp.VerifyOtpResponse
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadRequest
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadResponse
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryRequest
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryResponse
import com.trucksup.field_officer.data.model.user.GetProfileRequest
import com.trucksup.field_officer.data.model.user.GetProfileResponse
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileResponse
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutRequest
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerRequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.CompleteMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetUpBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetupBAResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.ScheduleMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.ScheduleMeetingResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinaceDataSubmitResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.LoanDataSubmitRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.CompleteMeetingGPRequest
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.ScheduleMeetingGPRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.AddMiscLeadRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.AddMiscLeadsResponse
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetAllMiscLeadResponse
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetMiscLeadRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.UpdateMiscLeadsRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.UpdateMiscLeadsResponse
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetUpTSResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetupTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.ScheduleMeetTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetMeetScheduleDetailsResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.VerifyTruckResponse

interface APIRepository {

    suspend fun loginUser(token: String, request: LoginRequest): ResultWrapper<LoginResponse>

    suspend fun registerUser(token: String, request: SignRequest): ResultWrapper<SignResponse>

    suspend fun resetPassword(token: String, request: ForgetRequest): ResultWrapper<ForgetResponse>

    suspend fun sendOTP(auth: String, request: OtpRequest): ResultWrapper<OtpResponse>

    suspend fun verifyOTP(auth: String, request: VerifyOtpRequest): ResultWrapper<VerifyOtpResponse>

    suspend fun getUserProfile(token: String, request: GetProfileRequest): ResultWrapper<GetProfileResponse>

    fun logoutUser()

    suspend fun isUserLoggedIn(): Boolean

    suspend fun updateUserProfile(token: String, updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse>

    suspend fun getAllHomeCountStatus(authToken: String, homeCountRequest: HomeCountRequest): ResultWrapper<HomeCountResponse>

    suspend fun privacyDetails(name: String): ResultWrapper<PrivacyAllResponse>

    suspend fun userSessionData(socialOrigin: String?, email: String?): ResultWrapper<UserSessionResponse>

    suspend fun logoutAccount(
        auth: String,
        request: LogoutRequest
    ): ResultWrapper<LogoutResponse>

    suspend fun deleteUserProfile(): ResultWrapper<DeleteProfileResponse>

    suspend fun autoImageSlide(): ResultWrapper<AutoImageSlideResponse>

    suspend fun getFinanceData(
        authToken: String,
        request: FinanceDataLiatRequest
    ): ResultWrapper<FinanceDataLiatResponse>

    suspend fun submitFinanceData(
        authToken: String,
        request: LoanDataSubmitRequest
    ): ResultWrapper<FinaceDataSubmitResponse>

    suspend fun getInquiryHistory(
        authToken: String,
        request: InquiryHistoryRequest
    ): ResultWrapper<InquiryHistoryResponse>

    suspend fun submitInsuranceInquiry(
        authToken: String,
        request: SubmitInsuranceInquiryRequest
    ): ResultWrapper<SubmitInsuranceInquiryData>

    suspend fun getSubscriptionPlanData(
        authToken: String,
        planRequest: PlanRequest
    ): ResultWrapper<PlanResponse>

    suspend fun getCityStateByPin(
        authToken: String,
        request: PinCodeRequest
    ): ResultWrapper<PinCodeResponse>

    suspend fun verifyTruck(
        authToken: String,
        vehicleRegNo: String?,
        mobileNo: String?
    ): ResultWrapper<VerifyTruckResponse>

    suspend fun getRcDetails(authToken: String, rcRequest: RcRequest): ResultWrapper<RcResponse>

    suspend fun getAddLoadFilter(request: AddLoadFilterRequest): ResultWrapper<AddLoadFilterResponse>

    suspend fun onBoardTruckSupplier(
        authToken: String,
        request: PrefferLanRequest
    ): ResultWrapper<PrefferdResponse>

    suspend fun onBoardBusinessAssociate(
        authToken: String,
        request: AddBrokerRequest
    ): ResultWrapper<AddBrokerResponse>

    suspend fun onBoardGrowthPartner(
        authToken: String,
        request: PrefferLanRequest
    ): ResultWrapper<PrefferdResponse>

    suspend fun addSmartFuelLead(
        authToken: String,
        request: AddSmartFuelLeadRequest
    ): ResultWrapper<AddSmartFuelLeadResponse>

    suspend fun getSmartFuelHistory(
        authToken: String,
        request: SmartFuelHistoryRequest
    ): ResultWrapper<SmartFuelHistoryResponse>

    suspend fun dutyStatus(
        authToken: String,
        request: DutyStatusRequest
    ): ResultWrapper<DutyStatusResponse>

    suspend fun scheduleMeetingTS(
        authToken: String,
        request: ScheduleMeetTSRequest
    ): ResultWrapper<ScheduleMeetingResponse>

    suspend fun completeTSMeeting(
        authToken: String,
        request: CompleteMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse>

    suspend fun scheduleMeetingBA(
        authToken: String,
        request: ScheduleMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse>

    suspend fun completeBAMeeting(
        authToken: String,
        request: CompleteMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse>

    suspend fun scheduleMeetingGP(
        authToken: String,
        request: ScheduleMeetingGPRequest
    ): ResultWrapper<ScheduleMeetingResponse>

    suspend fun completeGPMeeting(
        authToken: String,
        request: CompleteMeetingGPRequest
    ): ResultWrapper<ScheduleMeetingResponse>

    //add by me
    suspend fun getTSMeetScheduleData(
        authToken: String,
        request: GetMeetScheduleDetailsRequest
    ): ResultWrapper<GetMeetScheduleDetailsResponse>

    suspend fun getBAMeetScheduleData(
        authToken: String,
        request: GetMeetScheduleDetailsRequest
    ): ResultWrapper<GetMeetScheduleDetailsResponse>

    suspend fun getGPMeetScheduleData(
        authToken: String,
        request: GetMeetScheduleDetailsRequest
    ): ResultWrapper<GetMeetScheduleDetailsResponse>

    suspend fun updateMiscLeadsByBO(
        authToken: String,
        request: UpdateMiscLeadsRequest,
    ): ResultWrapper<UpdateMiscLeadsResponse>

    suspend fun addMiscLeadsByBO(
        authToken: String,
        request: AddMiscLeadRequest,
    ): ResultWrapper<AddMiscLeadsResponse>

    suspend fun getBOMiscLeads(
        authToken: String,
        request: GetMiscLeadRequest,
    ): ResultWrapper<GetAllMiscLeadResponse>

    suspend fun getAllMeetupGP(
        authToken: String,
        request: GetAllMeetupTSRequest,
    ): ResultWrapper<GetAllMeetUpTSResponse>

    suspend fun getAllMeetupBA(
        authToken: String,
        request: GetAllMeetUpBARequest,
    ): ResultWrapper<GetAllMeetupBAResponse>

    suspend fun getAllMeetupTS(
        authToken: String,
        request: GetAllMeetupTSRequest,
    ): ResultWrapper<GetAllMeetUpTSResponse>

    suspend fun getTodayFollowup(
        authToken: String,
        request: FollowUpRequest,
    ): ResultWrapper<FollowUpResponse>

    suspend fun getTotalEarning(
        authToken: String,
        request: FollowUpRequest,
    ): ResultWrapper<FollowUpResponse>

    suspend fun getTotalDownload(
        authToken: String,
        request: FollowUpRequest,
    ): ResultWrapper<FollowUpResponse>

    suspend fun getTotalAddLoad(
        authToken: String,
        request: FollowUpRequest,
    ): ResultWrapper<FollowUpResponse>

    suspend fun getTotalAddLoadDetails(
        authToken: String,
        request: FollowUpRequest,
    ): ResultWrapper<FollowUpResponse>

    suspend fun getAllTargetCount(
        authToken: String,
        request: FollowUpRequest,
    ): ResultWrapper<FollowUpResponse>
}