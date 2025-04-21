package com.trucksup.field_officer.data.repository.impl


import com.glovejob.data.model.UserSessionResponse
import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.data.model.DutyStatusResponse
import com.trucksup.field_officer.data.model.GetUserProfileResponse
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.PrivacyAllResponse
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
import com.trucksup.field_officer.data.network.safeApiCall
import com.trucksup.field_officer.data.repository.APIRepository
import com.trucksup.field_officer.data.services.ApiService
import com.trucksup.field_officer.presenter.utils.CommonApplication
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
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
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
import kotlinx.coroutines.Dispatchers


class APIRepositoryImpl constructor(private val apiService: ApiService) : APIRepository {

    override suspend fun loginUser(
        token: String,
        request: LoginRequest
    ): ResultWrapper<LoginResponse> {
        val response = safeApiCall(Dispatchers.IO) { apiService.loginUser(token, request) }
        return response
    }

    override fun logoutUser() {
        val srdp = CommonApplication.getSharedPreferences()
        srdp?.edit()?.remove("access_token")?.apply()
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val srdp = CommonApplication.getSharedPreferences()
        val token = srdp?.getString("access_token", null)
        return token != null
    }

    override suspend fun registerUser(
        token: String, request: SignRequest
    ): ResultWrapper<SignResponse> {
        val response = safeApiCall(Dispatchers.IO) { apiService.registerUser(token, request) }
        return response
    }

    override suspend fun resetPassword(
        token: String, request: ForgetRequest
    ): ResultWrapper<ForgetResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.resetPassword(token, request)
        }
    }

    override suspend fun verifyOTP(
        auth: String, request: VerifyOtpRequest
    ): ResultWrapper<VerifyOtpResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.verifyOTP(auth, request) }
    }

    override suspend fun sendOTP(
        auth: String,
        request: OtpRequest
    ): ResultWrapper<OtpResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.sendOTP(auth, request) }
    }

    override suspend fun getUserProfile(token: String,request: GetProfileRequest): ResultWrapper<GetProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getUserProfile(token,request) }
    }

    override suspend fun updateUserProfile(token: String,updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.updateUserProfile(token,updateProfileRequest) }
    }


    override suspend fun privacyDetails(name: String): ResultWrapper<PrivacyAllResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun userSessionData(
        socialOrigin: String?,
        email: String?
    ): ResultWrapper<UserSessionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllHomeCountStatus(
        authToken: String,
        homeCountRequest: HomeCountRequest
    ): ResultWrapper<HomeCountResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.getAllHomeCountStatus(
                authToken,
                homeCountRequest
            )
        }
    }


    override suspend fun deleteUserProfile(): ResultWrapper<DeleteProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.deleteUserProfile() }
    }

    override suspend fun logoutAccount(
        auth: String,
        request: LogoutRequest
    ): ResultWrapper<LogoutResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.logoutAccount(auth, request) }
    }

    override suspend fun autoImageSlide(): ResultWrapper<AutoImageSlideResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.autoImageSlide() }
    }

    override suspend fun getCityStateByPin(
        authToken: String,
        request: PinCodeRequest
    ): ResultWrapper<PinCodeResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getPinData(authToken, request) }
    }

    override suspend fun getFinanceData(
        authToken: String,
        request: FinanceDataLiatRequest
    ): ResultWrapper<FinanceDataLiatResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getFinanceData(authToken, request) }
    }

    override suspend fun submitFinanceData(
        authToken: String,
        request: LoanDataSubmitRequest
    ): ResultWrapper<FinaceDataSubmitResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.submitFinanceData(authToken, request) }
    }

    override suspend fun getInquiryHistory(
        authToken: String,
        request: InquiryHistoryRequest
    ): ResultWrapper<InquiryHistoryResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getInquiryHistory(authToken, request) }
    }

    override suspend fun submitInsuranceInquiry(
        authToken: String,
        request: SubmitInsuranceInquiryRequest
    ): ResultWrapper<SubmitInsuranceInquiryData> {
        return safeApiCall(Dispatchers.IO) {
            apiService.submitInsuranceInquiry(
                authToken,
                request
            )
        }
    }

    override suspend fun getSubscriptionPlanData(
        authToken: String,
        planRequest: PlanRequest
    ): ResultWrapper<PlanResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.getSubscriptionPlanList(
                authToken,
                planRequest
            )
        }
    }

    override suspend fun verifyTruck(
        authToken: String,
        vehicleRegNo: String?,
        mobileNo: String?
    ): ResultWrapper<VerifyTruckResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.verifyTruck(
                authToken,
                vehicleRegNo,
                mobileNo
            )
        }
    }

    override suspend fun getRcDetails(
        authToken: String,
        rcRequest: RcRequest
    ): ResultWrapper<RcResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getRcDetails(authToken, rcRequest) }
    }

    override suspend fun getAddLoadFilter(request: AddLoadFilterRequest): ResultWrapper<AddLoadFilterResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getAddLoadFilter(request) }
    }

    override suspend fun onBoardTruckSupplier(
        authToken: String,
        request: PrefferLanRequest
    ): ResultWrapper<PrefferdResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.onBoardTruckSupplier(
                authToken,
                request
            )
        }
    }

    override suspend fun onBoardBusinessAssociate(
        authToken: String,
        request: AddBrokerRequest
    ): ResultWrapper<AddBrokerResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.onBoardBusinessAssociate(
                authToken,
                request
            )
        }
    }

    override suspend fun onBoardGrowthPartner(
        authToken: String,
        request: PrefferLanRequest
    ): ResultWrapper<PrefferdResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.onBoardGrowthPartner(
                authToken,
                request
            )
        }
    }

    override suspend fun addSmartFuelLead(
        authToken: String,
        request: AddSmartFuelLeadRequest
    ): ResultWrapper<AddSmartFuelLeadResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.addSmartFuelLead(authToken, request) }
    }

    override suspend fun getSmartFuelHistory(
        authToken: String,
        request: SmartFuelHistoryRequest
    ): ResultWrapper<SmartFuelHistoryResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getSmartFuelHistory(authToken, request) }
    }

    override suspend fun dutyStatus(
        authToken: String,
        request: DutyStatusRequest
    ): ResultWrapper<DutyStatusResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.dutyStatus(authToken, request) }
    }

    override suspend fun scheduleMeetingTS(
        authToken: String,
        request: ScheduleMeetTSRequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.scheduleMeetingTS(authToken, request) }
    }

    override suspend fun completeTSMeeting(
        authToken: String,
        request: CompleteMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.completeTSMeeting(authToken, request) }
    }

    override suspend fun scheduleMeetingBA(
        authToken: String,
        request: ScheduleMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.scheduleMeetingBA(authToken, request) }
    }

    override suspend fun completeBAMeeting(
        authToken: String,
        request: CompleteMeetingBARequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.completeBAMeeting(authToken, request) }
    }

    override suspend fun scheduleMeetingGP(
        authToken: String,
        request: ScheduleMeetingGPRequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.scheduleMeetingGP(authToken, request) }
    }

    override suspend fun completeGPMeeting(
        authToken: String,
        request: CompleteMeetingGPRequest
    ): ResultWrapper<ScheduleMeetingResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.completeGPMeeting(authToken, request) }
    }

}
