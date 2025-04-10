package com.trucksup.field_officer.data.repository.impl


import android.text.TextUtils
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.glovejob.data.model.UserSessionResponse
import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
import com.trucksup.field_officer.data.model.NewResisterRequest
import com.trucksup.field_officer.data.model.NewUserProfile
import com.trucksup.field_officer.data.model.PasswordRequest
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.PrivacyAllResponse
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.TokenZ
import com.trucksup.field_officer.data.model.User
import com.trucksup.field_officer.data.model.category.CategoryAllResponse
import com.trucksup.field_officer.data.model.deleteResponse.DeleteProfileResponse
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.model.image.UploadImageResponse
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileResponse
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.data.network.safeApiCall
import com.trucksup.field_officer.data.repository.APIRepository
import com.trucksup.field_officer.data.services.ApiService
import com.trucksup.field_officer.presenter.utils.CommonApplication
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinaceDataSubmitResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.LoanDataSubmitRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.AddLoadFilterRequest
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.AddLoadFilterResponse
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.RcResponse
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.VerifyTruckResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Query


class APIRepositoryImpl constructor(private val apiService: ApiService) : APIRepository {

    override suspend fun loginUser(username: String, password: String,
        type: String,
        countryCode: String
    ): ResultWrapper<TokenZ> {
        val response =
            safeApiCall(Dispatchers.IO) { apiService.loginUser(username, password, countryCode = countryCode) }
        when (response) {
            is ResultWrapper.Success -> {
                if(response.value != null && response.value.payload != null){
                    val token = response.value.payload
                    val srdp = CommonApplication.getSharedPreferences()
                    srdp!!.edit().putString("access_token", token.access_token)
                        .putLong("expires_in", token.expires_in ?: 0L)
                        .putString("refresh_token", token.refresh_token)
                        .putString("token_type", token.token_type)
                        .apply()
                }

            }

            else -> {}
        }
        return response
    }

    override fun logoutUser() {
        val srdp = CommonApplication.getSharedPreferences()
        srdp!!.edit().remove("access_token").apply()
        srdp!!.edit().remove("expires_in").apply();
        srdp!!.edit().remove("refresh_token").apply();
        srdp!!.edit().remove("token_type").apply()
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val srdp = CommonApplication.getSharedPreferences()
        val token = srdp?.getString("access_token", null)
        return token != null
    }

    override suspend fun registerUser(registerUserRequest: NewResisterRequest): ResultWrapper<Response<User>> {
        val response = safeApiCall(Dispatchers.IO) { apiService.registerUser(registerUserRequest) }
        when (response) {
            is ResultWrapper.Success -> {
                if (response.value.payload != null)
                    if (response.value.payload!!.loginResponse != null) {
                        val token = response.value.payload!!.loginResponse!!
                        val srdp = CommonApplication.getSharedPreferences()
                        srdp!!.edit().putString("access_token", token.access_token)
                            .putLong("expires_in", token.expires_in ?: 0L)
                            .putString("refresh_token", token.refresh_token)
                            .putString("token_type", token.token_type)
                            .apply()
                    }
            }

            else -> {}
        }
        return response
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
        userId:Int
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


//    override suspend fun refreshToken(grant_type: String, refresh_token: String) {
//        return safeApiCall(Dispatchers.IO) { apiService.refre(languageCode) }
//    }

    override suspend fun forgotPassword(
        email: String,
        mobile: String,
        countryCode: String
    ): ResultWrapper<Response<String>> {
        return safeApiCall(Dispatchers.IO) { apiService.forgotPassword(email, mobile, countryCode) }
    }


      override suspend fun checkUserProfile(
          email: String,
          mobile: String,
          countryCode: String
      ): ResultWrapper<CheckUserProfileResponse> {
          return safeApiCall(Dispatchers.IO) { apiService.checkUserProfile(email, mobile, countryCode) }
      }

    override suspend fun resetPassword(
        email: String, mobile: String, countryCode: String,
        passwordRequest: PasswordRequest
    ): ResultWrapper<Response<String>> {
        return safeApiCall(Dispatchers.IO) {
            apiService.resetPassword(
                email,
                mobile,
                countryCode,
                passwordRequest
            )
        }
    }

    override suspend fun validateQuestionAnswer(
        email: String, mobile: String, countryCode: String,
        secretAnswer: String
    ): ResultWrapper<Response<Boolean>> {
        return safeApiCall(Dispatchers.IO) {
            apiService.validateQuestionAnswer(
                email,
                mobile,
                countryCode,
                secretAnswer
            )
        }
    }

    override suspend fun checkReferenceCode(referenceCode: String): ResultWrapper<Response<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendOTP(
        mobile: String,
        email: String,
        countryCode: String
    ): ResultWrapper<Response<String>> {
        return safeApiCall(Dispatchers.IO) { apiService.sendOTP(mobile, email, countryCode) }
    }

    override suspend fun EditsendOTP(id: String, mobile: String, email: String,
                                     countryCode: String): ResultWrapper<Response<String>> {
        return safeApiCall(Dispatchers.IO) { apiService.EditsendOTP(id,mobile, email, countryCode) }
    }

    override suspend fun getUserProfile(): ResultWrapper<Response<NewUserProfile>> {
        return safeApiCall(Dispatchers.IO) { apiService.getUserProfile() }
    }

    override suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.updateUserProfile(updateProfileRequest) }
    }


    override suspend fun uploadImage(bucketName: String?, id: Int?, position: Int?, requestId: Int?, file: MultipartBody.Part?): ResultWrapper<ImageResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.uploadImage(bucketName , id , position , requestId , file) }
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
    override suspend fun deleteUserReview(reviewid : Int,shopId: Int): ResultWrapper<DeleteProfileResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.deleteUserReview(reviewid,shopId) }
    }
    override suspend fun autoImageSlide(): ResultWrapper<AutoImageSlideResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.autoImageSlide() }
    }
    override suspend fun getCityStateByPin(authToken: String, request: PinCodeRequest): ResultWrapper<PinCodeResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getPinData(authToken,request) }
    }
    override suspend fun getFinanceData(authToken: String, request: FinanceDataLiatRequest): ResultWrapper<FinanceDataLiatResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getFinanceData(authToken,request) }
    }

    override suspend fun submitFinanceData(authToken: String, request: LoanDataSubmitRequest): ResultWrapper<FinaceDataSubmitResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.submitFinanceData(authToken,request) }
    }

    override suspend fun getInquiryHistory(authToken: String, request: InquiryHistoryRequest): ResultWrapper<InquiryHistoryResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getInquiryHistory(authToken,request) }
    }

    override suspend fun submitInsuranceInquiry(authToken: String, request: SubmitInsuranceInquiryRequest): ResultWrapper<SubmitInsuranceInquiryData> {
        return safeApiCall(Dispatchers.IO) { apiService.submitInsuranceInquiry(authToken,request) }
    }

    override suspend fun getSubscriptionPlanData(authToken: String, planRequest: PlanRequest): ResultWrapper<PlanResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getSubscriptionPlanList(authToken,planRequest) }
    }

    override suspend fun verifyTruck(authToken: String,vehicleRegNo: String?, mobileNo: String?): ResultWrapper<VerifyTruckResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.verifyTruck(authToken,vehicleRegNo,mobileNo) }
    }

    override suspend fun getRcDetails(authToken: String, rcRequest: RcRequest): ResultWrapper<RcResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getRcDetails(authToken,rcRequest) }
    }

    override suspend fun getAddLoadFilter(request: AddLoadFilterRequest): ResultWrapper<AddLoadFilterResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.getAddLoadFilter(request) }
    }

    override suspend fun onBoardTruckSupplier(authToken:String, request : PrefferLanRequest): ResultWrapper<PrefferdResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.onBoardTruckSupplier(authToken,request) }
    }

    override suspend fun onBoardBusinessAssociate(authToken:String, request : PrefferLanRequest): ResultWrapper<PrefferdResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.onBoardBusinessAssociate(authToken,request) }
    }

    override suspend fun onBoardGrowthPartner(authToken:String, request : PrefferLanRequest): ResultWrapper<PrefferdResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.onBoardGrowthPartner(authToken,request) }
    }

}
