package com.trucksup.field_officer.data.services

import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
import com.trucksup.field_officer.data.model.CountryResponse
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.NewResisterRequest
import com.trucksup.field_officer.data.model.NewUserProfile
import com.trucksup.field_officer.data.model.PasswordRequest
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
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
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinaceDataSubmitResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.LoanDataSubmitRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @POST()
    fun generateJWTtoken(
        @Header("x-api-key") auth: String,
        @Body request: GenerateJWTtokenRequest,
        @Url url: String
    ): Call<GenerateJWTtokenResponse>?


    @POST("global/user/login?platform=mobile")
    @Headers("Accept: application/json")
    suspend fun loginUser(
        @Query("userName") username: String,
        @Query("password") Password: String,
        @Query("countryCode") countryCode: String
    ): TokenZ

    @POST("global/user/register")
    @Headers("Accept: application/json")
    suspend fun registerUser(@Body registerUserRequest: NewResisterRequest): Response<User>

    @POST("global/user/verify/otp")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    suspend fun verifyOTP(@FieldMap params: Map<String, String>): Response<String>

    @POST("global/user/verify/user/otp")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    suspend fun verifyUserOTP(@FieldMap params: Map<String, String>): Response<String>


    @POST("global/user/forgot/password")
    @Headers("Accept: application/json")
    suspend fun forgotPassword(
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("countryCode") countryCode: String
    ): Response<String>

    @POST("global/user/reset/password")
    @Headers("Accept: application/json")
    suspend fun resetPassword(
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("countryCode") countryCode: String,
        @Body passwordRequest: PasswordRequest
    ): Response<String>

    @POST("global/user/validate/secret/answer")
    @Headers("Accept: application/json")
    suspend fun validateQuestionAnswer(
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("countryCode") countryCode: String,
        @Query("secretAnswer") secretAnswer: String
    ): Response<Boolean>

    @GET("global/user/check/referenceCode")
    @Headers("Accept: application/json")
    suspend fun checkReferenceCode(
        @Query("referenceCode") referenceCode: String
    ): Response<String>

    @POST("global/user/send/otp")
    @Headers("Accept: application/json")
    suspend fun sendOTP(
        @Query("mobile") mobile: String,
        @Query("email") email: String,
        @Query("countryCode") countryCode: String
    ): Response<String>

    @POST("global/user/send/user/otp")
    @Headers("Accept: application/json")
    suspend fun EditsendOTP(
        @Query("id") id: String,
        @Query("mobile") mobile: String,
        @Query("email") email: String,
        @Query("countryCode") countryCode: String
    ): Response<String>


    @GET("global/user/profile")
    @Headers("Accept: application/json")
    suspend fun getUserProfile(): Response<NewUserProfile>

    @POST("global/user/update")
    @Headers("Accept: application/json")
    suspend fun updateUserProfile(@Body updateProfileRequest: UpdateProfileRequest): UpdateProfileResponse

    //@GET("global/admin/fetch/user/profile")
    @Headers("Accept: application/json")
    suspend fun checkUserProfile(
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("countryCode") countryCode: String
    ): CheckUserProfileResponse

    /*  @PUT("global/user/profile")
      @Headers("Accept: application/json")
      suspend fun updateUserProfile(
          @Query("profileId") profileId: Int,
          @Body userProfile: UserUpdateProfile
      ): Response<UserProfile>*/


    @GET("category/all")
    @Headers("Accept: application/json")
    suspend fun getAllCategoryList(): CategoryAllResponse


    @GET("global/city/by-country")
    @Headers("Accept: application/json")
    suspend fun getcountryDetails(
        @Query("countryId") countryId: String
    ): CountryResponse


    @GET("shop/delete/review")
    @Headers("Accept: application/json")
    suspend fun deleteUserReview(
        @Query("id") id: Int,
        @Query("shopId") shopId: Int
    ): DeleteProfileResponse

    @GET("global/user/delete/user/account")
    @Headers("Accept: application/json")
    suspend fun deleteUserProfile(): DeleteProfileResponse

    @GET("advertisement/fetch-recent")
    @Headers("Accept: application/json")
    suspend fun autoImageSlide(): AutoImageSlideResponse

    @POST("THAPIGateway/apiateway/GetLocationByPinCode")
    @Headers("Accept: application/json")
    suspend fun getPinData(
        @Header("Authorization") auth: String,
        @Body request: PinCodeRequest
    ): PinCodeResponse

    @POST("TrucksUpAPIGateway/gateway/plans")
    @Headers("Accept: application/json")
    suspend fun getSubscriptionPlanList(
        @Header("Authorization") auth: String,
        @Body planRequest: PlanRequest
    ): PlanResponse

    @POST("Apigateway/Gateway/GetInquiryOptions")
    @Headers("Accept: application/json")
    suspend fun getFinanceData(
        @Header("Authorization") auth: String,
        @Body request: FinanceDataLiatRequest
    ): FinanceDataLiatResponse


    @POST("Apigateway/Gateway/SubmitFinanceInquiry")
    @Headers("Accept: application/json")
    suspend fun submitFinanceData(
        @Header("Authorization") auth: String,
        @Body request: LoanDataSubmitRequest
    ): FinaceDataSubmitResponse

    @POST("Apigateway/Gateway/InquiryHistory")
    @Headers("Accept: application/json")
    suspend fun getInquiryHistory(
        @Header("Authorization") auth: String,
        @Body request: InquiryHistoryRequest
    ): InquiryHistoryResponse

    @POST("Apigateway/Gateway/SubmitInsuranceInquiry")
    @Headers("Accept: application/json")
    suspend fun submitInsuranceInquiry(
        @Header("Authorization") auth: String,
        @Body request: SubmitInsuranceInquiryRequest
    ): SubmitInsuranceInquiryData


    @Multipart
    @POST("upload-imagefile")
    suspend fun uploadImage(
        @Query("bucketName") bucketName: String?,
        @Query("ID") id: Int?,
        @Query("Position") position: Int?,
        @Query("RequestId") requestId: Int?,
        @Part file: MultipartBody.Part?,
    ): ImageResponse
}
