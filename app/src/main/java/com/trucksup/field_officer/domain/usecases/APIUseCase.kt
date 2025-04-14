package com.trucksup.field_officer.domain.usecases


import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
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
import com.trucksup.field_officer.data.model.category.CategoryAllResponse
import com.trucksup.field_officer.data.model.deleteResponse.DeleteProfileResponse
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.data.model.otp.NewOtpResponse
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileResponse
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.data.repository.APIRepository
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
import javax.inject.Inject

class APIUseCase @Inject constructor(private val apiRepository: APIRepository) {

    suspend fun loginUser(
        token: String, request: LoginRequest
    ): ResultWrapper<LoginResponse> {
        return apiRepository.loginUser(token,request)
    }

    suspend fun registerUser(token: String, request: SignRequest): ResultWrapper<SignResponse> {
        return apiRepository.registerUser(token,request)
    }

    suspend fun verifyOTP(
        otp: String,
        email: String,
        mobileNumber: String,
        mobileCode: String
    ): ResultWrapper<Response<String>> {
        return apiRepository.verifyOTP(otp, email, mobileNumber, mobileCode)
    }

    suspend fun verifyUserOTP(
        otp: String,
        email: String,
        mobileNumber: String,
        mobileCode: String,
        userId: Int
    ): ResultWrapper<Response<String>> {
        return apiRepository.verifyUserOTP(otp, email, mobileNumber, mobileCode, userId)
    }


    suspend fun checkUserProfile(
        email: String,
        mobile: String,
        countryCode: String
    ): ResultWrapper<CheckUserProfileResponse> {
        return apiRepository.checkUserProfile(email, mobile, countryCode)
    }

    suspend fun resetPassword(
        token: String, request: ForgetRequest
    ): ResultWrapper<ForgetResponse> {
        return apiRepository.resetPassword(token,request)
    }



    suspend fun sendOTP(
        auth: String,
        request: OtpRequest
    ): ResultWrapper<NewOtpResponse> {
        return apiRepository.sendOTP(auth, request)
    }

    suspend fun EditsendOTP(
        id: String,
        mobile: String,
        email: String,
        countryCode: String
    ): ResultWrapper<Response<String>> {
        return apiRepository.EditsendOTP(id, mobile, email, countryCode)
    }

    suspend fun getUserProfile(): ResultWrapper<Response<NewUserProfile>> {
        return apiRepository.getUserProfile()
    }

    suspend fun getAllCategoryList(): ResultWrapper<CategoryAllResponse> {
        return apiRepository.getAllCategoryList()
    }


    suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse> {
        return apiRepository.updateUserProfile(updateProfileRequest)
    }


    fun logoutUser() {
        apiRepository.logoutUser()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return apiRepository.isUserLoggedIn()
    }

    suspend fun deleteUserReview(reviewid: Int, shopId: Int): ResultWrapper<DeleteProfileResponse> {
        return apiRepository.deleteUserReview(reviewid, shopId)
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
}