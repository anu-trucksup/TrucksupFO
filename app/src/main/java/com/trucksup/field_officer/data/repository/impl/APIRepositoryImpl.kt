package com.trucksup.field_officer.data.repository.impl


import android.text.TextUtils
import com.glovejob.data.model.UserSessionResponse
import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
import com.trucksup.field_officer.data.model.NewUserProfile
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
import com.trucksup.field_officer.data.model.category.CategoryAllResponse
import com.trucksup.field_officer.data.model.deleteResponse.DeleteProfileResponse
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.data.model.otp.NewOtpResponse
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileResponse
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.data.network.safeApiCall
import com.trucksup.field_officer.data.repository.APIRepository
import com.trucksup.field_officer.data.services.ApiService
import com.trucksup.field_officer.presenter.utils.CommonApplication
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerRequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinaceDataSubmitResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.LoanDataSubmitRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.VerifyTruckResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody


class APIRepositoryImpl constructor(private val apiService: ApiService) : APIRepository {

    override suspend fun loginUser(
        token: String,
        request: LoginRequest
    ): ResultWrapper<LoginResponse> {
        val response = safeApiCall(Dispatchers.IO) { apiService.loginUser(token, request) }
        when (response) {
            is ResultWrapper.Success -> {
                if (response.value.loginDetails != null) {
                    val details = response.value.loginDetails
                    val srdp = CommonApplication.getSharedPreferences()
                    srdp?.edit()?.putString("access_token", details.token)
                        ?.putString("user_name", details.profilename)
                        ?.putString("user_image", details.profilephoto)
                        ?.putString("user_password", details.password)
                        ?.putString("user_referral", details.referralcode)
                        ?.putString("user_email", details.email)?.apply()

                }
            }

            else -> {}
        }

        return response
    }

    override fun logoutUser() {
        val srdp = CommonApplication.getSharedPreferences()
        srdp?.edit()?.remove("access_token")?.apply()
        srdp?.edit()?.remove("expires_in")?.apply();
        srdp?.edit()?.remove("refresh_token")?.apply();
        srdp?.edit()?.remove("token_type")?.apply()
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val srdp = CommonApplication.getSharedPreferences()
        val token = srdp?.getString("access_token", null)
        return token != null
    }

    override suspend fun registerUser(
        token: String,
        request: SignRequest
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
        otp: String,
        email: String,
        mobileNumber: String,
        mobileCode: String
    ): ResultWrapper<Response<String>> {
        val map = HashMap<String, String>()
        if (email != null && !TextUtils.isEmpty(email)) {
            map["email"] = email
        } else {
            map["mobileNumber"] = mobileNumber
            map["mobileCode"] = mobileCode
        }
//        map.put("mobile", email)
//        map.put("countryCode", email)
        map.put("otp", otp)
        return safeApiCall(Dispatchers.IO) { apiService.verifyOTP(map) }
    }

    override suspend fun verifyUserOTP(
        otp: String,
        email: String,
        mobileNumber: String,
        mobileCode: String,
        userId: Int
    ): ResultWrapper<Response<String>> {
        val map = HashMap<String, String>()
        if (email != null && !TextUtils.isEmpty(email)) {
            map["email"] = email
        } else {
            map["mobile"] = mobileNumber
            map["countryCode"] = mobileCode
        }
//        map.put("mobile", email)
//        map.put("countryCode", email)
        map.put("otp", otp)
        map.put("id", userId.toString())
        return safeApiCall(Dispatchers.IO) { apiService.verifyUserOTP(map) }
    }


    override suspend fun checkUserProfile(
        email: String,
        mobile: String,
        countryCode: String
    ): ResultWrapper<CheckUserProfileResponse> {
        return safeApiCall(Dispatchers.IO) {
            apiService.checkUserProfile(
                email,
                mobile,
                countryCode
            )
        }
    }


    override suspend fun sendOTP(
        auth: String,
        request: OtpRequest
    ): ResultWrapper<NewOtpResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.sendOTP(auth, request) }
    }

    override suspend fun EditsendOTP(
        id: String, mobile: String, email: String,
        countryCode: String
    ): ResultWrapper<Response<String>> {
        return safeApiCall(Dispatchers.IO) {
            apiService.EditsendOTP(
                id,
                mobile,
                email,
                countryCode
            )
        }
    }

    override suspend fun getUserProfile(): ResultWrapper<Response<NewUserProfile>> {
        return safeApiCall(Dispatchers.IO) { apiService.getUserProfile() }
    }

    override suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.updateUserProfile(updateProfileRequest) }
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

    override suspend fun getAllCategoryList(): ResultWrapper<CategoryAllResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getAllCategoryList() }
    }


    override suspend fun deleteUserProfile(): ResultWrapper<DeleteProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.deleteUserProfile() }
    }

    override suspend fun deleteUserReview(
        reviewid: Int,
        shopId: Int
    ): ResultWrapper<DeleteProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.deleteUserReview(reviewid, shopId) }
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

}
