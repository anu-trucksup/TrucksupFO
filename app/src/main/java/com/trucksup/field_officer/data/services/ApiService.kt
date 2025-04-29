package com.trucksup.field_officer.data.services

import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllTSDetailsRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.data.model.DutyStatusResponse
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
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
import com.trucksup.field_officer.presenter.cityPicker.CityListbySearchRequest
import com.trucksup.field_officer.presenter.cityPicker.CitySearchRequest
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutRequest
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerRequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.CompleteMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllBADetailsResponse
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
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.GetAllGPDetailsResponse
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
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllTSDetailsResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.VerifyTruckResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    //User Auth Module

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

    @POST("BOAppApiGateway/apiateway/ResetPassword")
    @Headers("Accept: application/json")
    suspend fun resetPassword(
        @Header("Authorization") credentials: String,
        @Body forgetRequest: ForgetRequest
    ): ForgetResponse

    @POST("BOAppApiGateway/apiateway/BOVerifyOTP")
    @Headers("Accept: application/json")
    suspend fun verifyOTP(
        @Header("Authorization") auth: String,
        @Body request: VerifyOtpRequest
    ): VerifyOtpResponse

    @POST("BOAppApiGateway/apiateway/BOSendOTP")
    @Headers("Accept: application/json")
    suspend fun sendOTP(
        @Header("Authorization") auth: String,
        @Body request: OtpRequest
    ): OtpResponse

    @POST("BOAppApiGateway/apiateway/GetBOProfile")
    @Headers("Accept: application/json")
    suspend fun getUserProfile(
        @Header("Authorization") auth: String,
        @Body getProfileRequest: GetProfileRequest
    ): GetProfileResponse

    @POST("BOAppApiGateway/apiateway/BOEditProfile")
    @Headers("Accept: application/json")
    suspend fun updateUserProfile(
        @Header("Authorization") auth: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): UpdateProfileResponse


    @POST("BOAppApiGateway/apiateway/BOUserLogOut")
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

    @POST("BOAppApiGateway/apiateway/GetBOLocationByPinCode")
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

    //Finance & Insurance

    @POST("Apigateway/Gateway/GetInquiryOptions")
    @Headers("Accept: application/json")
    suspend fun getFinanceData(
        @Header("Authorization") auth: String,
        @Body request: FinanceDataLiatRequest
    ): FinanceDataLiatResponse

    @POST("/BOAppApiGateway/apiateway/BOAddFinanceInquiry")//Apigateway/Gateway/SubmitFinanceInquiry
    @Headers("Accept: application/json")
    suspend fun submitFinanceData(
        @Header("Authorization") auth: String,
        @Body request: LoanDataSubmitRequest
    ): FinaceDataSubmitResponse

    @POST("BOAppApiGateway/apiateway/BOFinInsInquiryHistory")
    @Headers("Accept: application/json")
    suspend fun getInquiryHistory(
        @Header("Authorization") auth: String,
        @Body request: InquiryHistoryRequest
    ): InquiryHistoryResponse

    @POST("BOAppApiGateway/apiateway/BOAddInsuranceInquiry")
    @Headers("Accept: application/json")
    suspend fun submitInsuranceInquiry(
        @Header("Authorization") auth: String,
        @Body request: SubmitInsuranceInquiryRequest
    ): SubmitInsuranceInquiryData


    //SmartFuel
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

    //HomeScreen

    @POST("BOAppApiGateway/apiateway/BOUpdateDutyStatus")
    @Headers("Accept: application/json")
    suspend fun dutyStatus(
        @Header("Authorization") auth: String,
        @Body request: DutyStatusRequest
    ): DutyStatusResponse

    @POST("BOAppApiGateway/apiateway/BOHomeMenuItems")
    @Headers("Accept: application/json")
    suspend fun getAllHomeCountStatus(
        @Header("Authorization") credentials: String,
        @Body homeCountRequest: HomeCountRequest
    ): HomeCountResponse


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

    //New OnBoarding TS/BA/GP

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

    //Schedule Meeting TS
    @POST("BOAppApiGateway/apiateway/TSMeetSchedule")
    @Headers("Accept: application/json")
    suspend fun scheduleMeetingTS(
        @Header("Authorization") auth: String,
        @Body request: ScheduleMeetTSRequest
    ): ScheduleMeetingResponse

    @POST("BOAppApiGateway/apiateway/BAMeetComplete")
    @Headers("Accept: application/json")
    suspend fun completeTSMeeting(
        @Header("Authorization") auth: String,
        @Body request: CompleteMeetingBARequest
    ): ScheduleMeetingResponse

    //Schedule Meeting BA
    @POST("BOAppApiGateway/apiateway/BAMeetSchedule")
    @Headers("Accept: application/json")
    suspend fun scheduleMeetingBA(
        @Header("Authorization") auth: String,
        @Body request: ScheduleMeetingBARequest
    ): ScheduleMeetingResponse

    @POST("BOAppApiGateway/apiateway/BAMeetComplete")
    @Headers("Accept: application/json")
    suspend fun completeBAMeeting(
        @Header("Authorization") auth: String,
        @Body request: CompleteMeetingBARequest
    ): ScheduleMeetingResponse

    //Schedule Meeting GP
    @POST("BOAppApiGateway/apiateway/GPMeetSchedule")
    @Headers("Accept: application/json")
    suspend fun scheduleMeetingGP(
        @Header("Authorization") auth: String,
        @Body request: ScheduleMeetingGPRequest
    ): ScheduleMeetingResponse

    @POST("BOAppApiGateway/apiateway/GPMeetComplete")
    @Headers("Accept: application/json")
    suspend fun completeGPMeeting(
        @Header("Authorization") auth: String,
        @Body request: CompleteMeetingGPRequest
    ): ScheduleMeetingResponse


    //add by me
    @POST("BOAppApiGateway/apiateway/GetTSDetails")
    @Headers("Accept: application/json")
    suspend fun getAllBADetails(
        @Header("Authorization") auth: String,
        @Body request: GetAllTSDetailsRequest
    ): GetAllBADetailsResponse

    @POST("BOAppApiGateway/apiateway/GetTSDetails")
    @Headers("Accept: application/json")
    suspend fun getAllTSDetails(
        @Header("Authorization") auth: String,
        @Body request: GetAllTSDetailsRequest
    ): GetAllTSDetailsResponse

    @POST("BOAppApiGateway/apiateway/GetTSDetails")
    @Headers("Accept: application/json")
    suspend fun getAllGPDetails(
        @Header("Authorization") auth: String,
        @Body request: GetAllTSDetailsRequest
    ): GetAllGPDetailsResponse

    //Today Followup
    @POST("BOAppApiGateway/apiateway/GetTodaysFollowup")
    @Headers("Accept: application/json")
    suspend fun getTodayFollowup(
        @Header("Authorization") auth: String,
        @Body request: FollowUpRequest
    ): FollowUpResponse

    @POST("BOAppApiGateway/apiateway/GetAllBAMeets")
    @Headers("Accept: application/json")
    suspend fun getAllMeetupBA(
        @Header("Authorization") auth: String,
        @Body request: GetAllMeetUpBARequest
    ): GetAllMeetupBAResponse

    @POST("BOAppApiGateway/apiateway/GetAllTSMeets")
    @Headers("Accept: application/json")
    suspend fun getAllMeetupTS(
        @Header("Authorization") auth: String,
        @Body request: GetAllMeetupTSRequest
    ): GetAllMeetUpTSResponse

    @POST("BOAppApiGateway/apiateway/GetBOAllGPMeets")
    @Headers("Accept: application/json")
    suspend fun getAllMeetupGP(
        @Header("Authorization") auth: String,
        @Body request: GetAllMeetupTSRequest
    ): GetAllMeetUpTSResponse


    //Add Misc
    @POST("BOAppApiGateway/apiateway/GetBOMiscLeads")
    @Headers("Accept: application/json")
    suspend fun getBOMiscLeads(
        @Header("Authorization") auth: String,
        @Body request: GetMiscLeadRequest
    ): GetAllMiscLeadResponse


    @POST("BOAppApiGateway/apiateway/BOAddMisc")
    @Headers("Accept: application/json")
    suspend fun addMiscLeadsByBO(
        @Header("Authorization") auth: String,
        @Body request: AddMiscLeadRequest
    ): AddMiscLeadsResponse

    @POST("BOAppApiGateway/apiateway/BOAddMisc")
    @Headers("Accept: application/json")
    suspend fun updateMiscLeadsByBO(
        @Header("Authorization") auth: String,
        @Body request: UpdateMiscLeadsRequest
    ): UpdateMiscLeadsResponse

    //HomeScreen

    @POST("BOAppApiGateway/apiateway/GetTodaysFollowup")
    @Headers("Accept: application/json")
    suspend fun getTotalDownload(
        @Header("Authorization") auth: String,
        @Body request: FollowUpRequest
    ): FollowUpResponse

    @POST("BOAppApiGateway/apiateway/GetTodaysFollowup")
    @Headers("Accept: application/json")
    suspend fun getTotalEarning(
        @Header("Authorization") auth: String,
        @Body request: FollowUpRequest
    ): FollowUpResponse

    @POST("BOAppApiGateway/apiateway/GetTodaysFollowup")
    @Headers("Accept: application/json")
    suspend fun getTotalAddLoad(
        @Header("Authorization") auth: String,
        @Body request: FollowUpRequest
    ): FollowUpResponse

    @POST("BOAppApiGateway/apiateway/GetTodaysFollowup")
    @Headers("Accept: application/json")
    suspend fun getTotalAddLoadDetails(
        @Header("Authorization") auth: String,
        @Body request: FollowUpRequest
    ): FollowUpResponse

    @POST("BOAppApiGateway/apiateway/GetTodaysFollowup")
    @Headers("Accept: application/json")
    suspend fun getAllTargetCount(
        @Header("Authorization") auth: String,
        @Body request: FollowUpRequest
    ): FollowUpResponse

}
