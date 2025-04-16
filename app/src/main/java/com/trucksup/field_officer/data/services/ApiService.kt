package com.trucksup.field_officer.data.services

import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.trucksup.field_officer.data.model.otp.OTPResponse
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.data.model.otp.SmsRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
import com.trucksup.field_officer.data.model.CountryResponse
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.data.model.DutyStatusResponse
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.NewUserProfile
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
import com.trucksup.field_officer.data.model.image.TrucksupImageUploadResponse
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.data.model.otp.NewOtpResponse
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadRequest
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadResponse
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryRequest
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryResponse
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileResponse
import com.trucksup.field_officer.presenter.cityPicker.CityListbySearchRequest
import com.trucksup.field_officer.presenter.cityPicker.CitySearchRequest
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutRequest
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerRequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinaceDataSubmitResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceDataLiatResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.LoanDataSubmitRequest
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.VerifyTruckResponse
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

interface ApiService {

    @POST("JwtAuth/api/Auth/GenerateJWTtoken")
    fun generateJWToken(
        @Header("x-api-key") auth: String,
        @Body request: GenerateJWTtokenRequest
    ): Call<GenerateJWTtokenResponse>?


    @POST("BOAppApiGateway/apiateway/Login")
    @Headers("Accept: application/json")
    suspend fun loginUser(
        @Header("Authorization") credentials: String,
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("BOAppApiGateway/apiateway/SignUp")
    @Headers("Accept: application/json")
    suspend fun registerUser(
        @Header("Authorization") credentials: String,
        @Body signRequest: SignRequest
    ): SignResponse

    @POST("global/user/verify/otp")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    suspend fun verifyOTP(@FieldMap params: Map<String, String>): Response<String>

    @POST("global/user/verify/user/otp")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    suspend fun verifyUserOTP(@FieldMap params: Map<String, String>): Response<String>

    @POST("BOAppApiGateway/apiateway/ResetPassword")
    @Headers("Accept: application/json")
    suspend fun resetPassword(
        @Header("Authorization") credentials: String,
        @Body forgetRequest: ForgetRequest
    ): ForgetResponse

    @POST("Apigateway/Gateway/Auth/Code")
    fun sendSms(
        @Header("Authorization") auth: String,
        @Body request: SmsRequest
    ): OTPResponse

    @POST("MessagaeService/api/Message/SendOTP")
    @Headers("Accept: application/json")
    suspend fun sendOTP(
        @Header("Authorization") auth: String,
        @Body request: OtpRequest
    ): NewOtpResponse

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


    @GET("BOAppApiGateway/apiateway/BOHomeMenuItems")
    @Headers("Accept: application/json")
    suspend fun getAllHomeCountStatus( @Header("Authorization") credentials: String,
                                       @Body homeCountRequest: HomeCountRequest): HomeCountResponse


    @GET("global/city/by-country")
    @Headers("Accept: application/json")
    suspend fun getcountryDetails(
        @Query("countryId") countryId: String
    ): CountryResponse


    @GET("BOAppApiGateway/apiateway/BOUserLogOut")
    @Headers("Accept: application/json")
    suspend fun logoutAccount(
        @Header("Authorization") auth: String,
        @Body request: LogoutRequest
    ): LogoutResponse

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


    @POST("TrucksUpAPIGateway/gateway/CitySearch")
    @Headers("Accept: application/json")
    fun searchCity(@Body request: CitySearchRequest): Call<CityListbySearchRequest>?

    @Multipart
    @POST("BOAppApiGateway/apiateway/BOUploadImages")
    @Headers("Accept: application/json")
    fun uploadImages(
        @Header("Authorization") auth: String,
        @Query("filetype") filetype: String?,
        @Query("foldername") foldername: String?,
        @Part imageFile: MultipartBody.Part?,
        @Part watermarkFile: MultipartBody.Part?
    ): Call<TrucksupImageUploadResponse>?

    @GET("Apigateway/Gateway/CheckDuplicateVehicle?")
    @Headers("Accept: application/json")
    suspend fun verifyTruck(
        @Header("Authorization") auth: String,
        @Query("VehicleNo") VehicleRegNo: String?,
        @Query("MobileNo") MobileNo: String?
    ): VerifyTruckResponse

    @POST("Apigateway/Gateway/V3/GetRCDetail")
    @Headers("Accept: application/json")
    suspend fun getRcDetails(
        @Header("Authorization") auth: String,
        @Body request: RcRequest
    ): RcResponse

    @POST("TrucksUpAPIGateway/gateway/GetAddLoadSelectvalues")
    @Headers("Accept: application/json")
    suspend fun getAddLoadFilter(@Body request: AddLoadFilterRequest): AddLoadFilterResponse


    @POST("Apigateway/Gateway/AddOwnerData")
    @Headers("Accept: application/json")
    suspend fun onBoardTruckSupplier(
        @Header("Authorization") auth: String,
        @Body request: PrefferLanRequest
    ): PrefferdResponse

    @POST("Apigateway/Gateway/AddBroker")
    @Headers("Accept: application/json")
    suspend fun onBoardBusinessAssociate(
        @Header("Authorization") auth: String,
        @Body request: AddBrokerRequest
    ): AddBrokerResponse

    @POST("Apigateway/Gateway/AddOwnerData")
    @Headers("Accept: application/json")
    suspend fun onBoardGrowthPartner(
        @Header("Authorization") auth: String,
        @Body request: PrefferLanRequest
    ): PrefferdResponse

    @POST("BOAppApiGateway/apiateway/BOAddSmartFuelLeads")
    @Headers("Accept: application/json")
    suspend fun addSmartFuelLead(
        @Header("Authorization") auth: String,
        @Body request: AddSmartFuelLeadRequest
    ): AddSmartFuelLeadResponse

    @POST("BOAppApiGateway/apiateway/BOGetLeadsHistories")
    @Headers("Accept: application/json")
    suspend fun getSmartFuelHistory(
        @Header("Authorization") auth: String,
        @Body request: SmartFuelHistoryRequest
    ): SmartFuelHistoryResponse

    @POST("BOAppApiGateway/apiateway/BOUpdateDutyStatus")
    @Headers("Accept: application/json")
    suspend fun dutyStatus(
        @Header("Authorization") auth: String,
        @Body request: DutyStatusRequest
    ): DutyStatusResponse
}
