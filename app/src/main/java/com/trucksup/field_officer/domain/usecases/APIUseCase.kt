package com.trucksup.field_officer.domain.usecases


import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.data.model.DutyStatusResponse
import com.trucksup.field_officer.data.model.GetUserProfileResponse
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.Response
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
import com.trucksup.field_officer.data.repository.APIRepository
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutRequest
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerRequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.CompleteMeetingBARequest
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
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.ScheduleMeetTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.VerifyTruckResponse
import javax.inject.Inject

class APIUseCase @Inject constructor(private val apiRepository: APIRepository) {

    suspend fun loginUser(
        token: String, request: LoginRequest
    ): ResultWrapper<LoginResponse> {
        return apiRepository.loginUser(token, request)
    }

    suspend fun registerUser(token: String, request: SignRequest): ResultWrapper<SignResponse> {
        return apiRepository.registerUser(token, request)
    }

    suspend fun verifyOTP(
        token: String, request: VerifyOtpRequest
    ): ResultWrapper<VerifyOtpResponse> {
        return apiRepository.verifyOTP(token,request)
    }

    suspend fun resetPassword(
        token: String, request: ForgetRequest
    ): ResultWrapper<ForgetResponse> {
        return apiRepository.resetPassword(token, request)
    }

    suspend fun sendOTP(
        auth: String,
        request: OtpRequest
    ): ResultWrapper<OtpResponse> {
        return apiRepository.sendOTP(auth, request)
    }


    suspend fun getUserProfile(token: String, request: GetProfileRequest): ResultWrapper<GetProfileResponse> {
        return apiRepository.getUserProfile(token, request)
    }

    suspend fun getAllHomeCountStatus(
        authToken: String,
        homeCountRequest: HomeCountRequest
    ): ResultWrapper<HomeCountResponse> {
        return apiRepository.getAllHomeCountStatus(authToken, homeCountRequest)
    }

    suspend fun updateUserProfile(token: String, updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse> {
        return apiRepository.updateUserProfile(token,updateProfileRequest)
    }

    fun logoutUser() {
        apiRepository.logoutUser()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return apiRepository.isUserLoggedIn()
    }

    suspend fun logoutAccount(
        auth: String,
        request: LogoutRequest
    ): ResultWrapper<LogoutResponse> {
        return apiRepository.logoutAccount(auth, request)
    }

    suspend fun deleteUserProfile(): ResultWrapper<DeleteProfileResponse> {
        return apiRepository.deleteUserProfile()
    }

    suspend fun autoImageSlide(): ResultWrapper<AutoImageSlideResponse> {
        return apiRepository.autoImageSlide()
    }

    suspend fun getFinanceData(
        authToken: String,
        request: FinanceDataLiatRequest
    ): ResultWrapper<FinanceDataLiatResponse> {
        return apiRepository.getFinanceData(authToken, request)
    }

    suspend fun submitFinanceData(
        authToken: String,
        request: LoanDataSubmitRequest
    ): ResultWrapper<FinaceDataSubmitResponse> {
        return apiRepository.submitFinanceData(authToken, request)
    }

    suspend fun getInquiryHistory(
        authToken: String,
        request: InquiryHistoryRequest
    ): ResultWrapper<InquiryHistoryResponse> {
        return apiRepository.getInquiryHistory(authToken, request)
    }

    suspend fun submitInsuranceInquiry(
        authToken: String,
        request: SubmitInsuranceInquiryRequest
    ): ResultWrapper<SubmitInsuranceInquiryData> {
        return apiRepository.submitInsuranceInquiry(authToken, request)
    }

    suspend fun getSubscriptionPlanData(
        authToken: String,
        planRequest: PlanRequest
    ): ResultWrapper<PlanResponse> {
        return apiRepository.getSubscriptionPlanData(authToken, planRequest)
    }

    suspend fun getCityStateByPin(
        authToken: String,
        request: PinCodeRequest
    ): ResultWrapper<PinCodeResponse> {
        return apiRepository.getCityStateByPin(authToken, request)
    }

    suspend fun verifyTruck(
        authToken: String,
        vehicleRegNo: String?,
        mobileNo: String?
    ): ResultWrapper<VerifyTruckResponse> {
        return apiRepository.verifyTruck(authToken, vehicleRegNo, mobileNo)
    }

    suspend fun getRcDetails(authToken: String, rcRequest: RcRequest): ResultWrapper<RcResponse> {
        return apiRepository.getRcDetails(authToken, rcRequest)
    }

    suspend fun getAddLoadFilter(request: AddLoadFilterRequest): ResultWrapper<AddLoadFilterResponse> {
        return apiRepository.getAddLoadFilter(request)
    }

    suspend fun onBoardTruckSupplier(
        authToken: String,
        request: PrefferLanRequest
    ): ResultWrapper<PrefferdResponse> {
        return apiRepository.onBoardTruckSupplier(authToken, request)
    }

    suspend fun onBoardBusinessAssociate(
        authToken: String,
        request: AddBrokerRequest
    ): ResultWrapper<AddBrokerResponse> {
        return apiRepository.onBoardBusinessAssociate(authToken, request)
    }

    suspend fun onBoardGrowthPartner(
        authToken: String,
        request: PrefferLanRequest
    ): ResultWrapper<PrefferdResponse> {
        return apiRepository.onBoardGrowthPartner(authToken, request)
    }

    suspend fun addSmartFuelLead(
        authToken: String,
        request: AddSmartFuelLeadRequest
    ): ResultWrapper<AddSmartFuelLeadResponse> {
        return apiRepository.addSmartFuelLead(authToken, request)
    }

    suspend fun getSmartFuelHistory(
        authToken: String,
        request: SmartFuelHistoryRequest
    ): ResultWrapper<SmartFuelHistoryResponse> {
        return apiRepository.getSmartFuelHistory(authToken, request)
    }

    suspend fun dutyStatus(
        authToken: String,
        request: DutyStatusRequest
    ): ResultWrapper<DutyStatusResponse> {
        return apiRepository.dutyStatus(authToken, request)
    }

    suspend fun scheduleMeetingTS(
        authToken: String,
        request: ScheduleMeetTSRequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return apiRepository.scheduleMeetingTS(authToken, request)
    }

    suspend fun completeTSMeeting(
        authToken: String,
        request: CompleteMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return apiRepository.completeTSMeeting(authToken, request)
    }

    suspend fun scheduleMeetingBA(
        authToken: String,
        request: ScheduleMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return apiRepository.scheduleMeetingBA(authToken, request)
    }

    suspend fun completeBAMeeting(
        authToken: String,
        request: CompleteMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return apiRepository.completeBAMeeting(authToken, request)
    }

    suspend fun scheduleMeetingGP(
        authToken: String,
        request: ScheduleMeetingGPRequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return apiRepository.scheduleMeetingGP(authToken, request)
    }

    suspend fun completeBAMeeting(
        authToken: String,
        request: CompleteMeetingGPRequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return apiRepository.completeGPMeeting(authToken, request)
    }
}