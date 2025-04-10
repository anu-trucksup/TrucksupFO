package com.trucksup.field_officer.data.repository

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

interface APIRepository {

    suspend fun loginUser(
        username: String,
        Password: String,
        type: String,
        countryCode: String
    ): ResultWrapper<TokenZ>

    suspend fun registerUser(registerUserRequest: NewResisterRequest): ResultWrapper<Response<User>>

    suspend fun verifyOTP(
        otp: String,
        email: String,
        mobileNumber: String,
        mobileCode: String
    ): ResultWrapper<Response<String>>

    suspend fun verifyUserOTP(
        otp: String, email: String,
        mobileNumber: String, mobileCode: String, userId: Int
    ): ResultWrapper<Response<String>>

    //    suspend fun refreshToken(grant_type: String, refresh_token: String)
    suspend fun forgotPassword(
        email: String, mobile: String,
        countryCode: String
    ): ResultWrapper<Response<String>>

    suspend fun checkUserProfile(
        email: String, mobile: String,
        countryCode: String
    ): ResultWrapper<CheckUserProfileResponse>

    suspend fun resetPassword(
        email: String, mobile: String, countryCode: String,
        passwordRequest: PasswordRequest
    ): ResultWrapper<Response<String>>

    suspend fun validateQuestionAnswer(
        email: String,
        mobile: String,
        countryCode: String,
        secretAnswer: String
    ): ResultWrapper<Response<Boolean>>

    suspend fun checkReferenceCode(
        referenceCode: String
    ): ResultWrapper<Response<String>>

    suspend fun sendOTP(
        mobile: String,
        email: String,
        countryCode: String
    ): ResultWrapper<Response<String>>

    suspend fun EditsendOTP(
        id: String,
        mobile: String,
        email: String,
        countryCode: String
    ): ResultWrapper<Response<String>>

    suspend fun getUserProfile(): ResultWrapper<Response<NewUserProfile>>

    fun logoutUser()

    suspend fun isUserLoggedIn(): Boolean

    suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse>

    suspend fun getAllCategoryList(): ResultWrapper<CategoryAllResponse>


    suspend fun uploadImage(bucketName: String?, id: Int?, position: Int?, requestId: Int?, file: MultipartBody.Part?): ResultWrapper<ImageResponse>

    suspend fun privacyDetails(name: String): ResultWrapper<PrivacyAllResponse>

    suspend fun userSessionData(
        socialOrigin: String?,
        email: String?
    ): ResultWrapper<UserSessionResponse>

    suspend fun deleteUserReview(reviewid: Int, shopId: Int): ResultWrapper<DeleteProfileResponse>

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

    suspend fun getInquiryHistory(authToken: String, request: InquiryHistoryRequest): ResultWrapper<InquiryHistoryResponse>

    suspend fun submitInsuranceInquiry(authToken: String, request: SubmitInsuranceInquiryRequest): ResultWrapper<SubmitInsuranceInquiryData>

    suspend fun getSubscriptionPlanData(authToken: String, planRequest: PlanRequest): ResultWrapper<PlanResponse>

    suspend fun getCityStateByPin(authToken: String, request: PinCodeRequest): ResultWrapper<PinCodeResponse>

    suspend fun verifyTruck(authToken: String,vehicleRegNo: String?, mobileNo: String?): ResultWrapper<VerifyTruckResponse>

    suspend fun getRcDetails(authToken: String, rcRequest: RcRequest): ResultWrapper<RcResponse>

    suspend fun getAddLoadFilter(request: AddLoadFilterRequest): ResultWrapper<AddLoadFilterResponse>

    suspend fun onBoardTruckSupplier(authToken:String, request : PrefferLanRequest): ResultWrapper<PrefferdResponse>

    suspend fun onBoardBusinessAssociate(authToken:String, request : PrefferLanRequest): ResultWrapper<PrefferdResponse>

    suspend fun onBoardGrowthPartner(authToken:String, request : PrefferLanRequest): ResultWrapper<PrefferdResponse>

}