package com.trucksup.field_officer.data.repository.impl


import android.text.TextUtils
import com.glovejob.data.model.UserSessionResponse
import com.trucksup.field_officer.data.model.AutoImageSlideResponse
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
import com.trucksup.field_officer.data.model.NewResisterRequest
import com.trucksup.field_officer.data.model.NewUserProfile
import com.trucksup.field_officer.data.model.PasswordRequest
import com.trucksup.field_officer.data.model.PrivacyAllResponse
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.TokenZ
import com.trucksup.field_officer.data.model.User
import com.trucksup.field_officer.data.model.category.CategoryAllResponse
import com.trucksup.field_officer.data.model.deleteResponse.DeleteProfileResponse
import com.trucksup.field_officer.data.model.image.UploadImageResponse
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileResponse
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.data.network.safeApiCall
import com.trucksup.field_officer.data.repository.APIRepository
import com.trucksup.field_officer.data.services.ApiService
import com.trucksup.field_officer.presenter.utils.CommonApplication
import kotlinx.coroutines.Dispatchers


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


    override suspend fun uploadImage(purpose: String,contentType: String): ResultWrapper<UploadImageResponse> {
        return safeApiCall(Dispatchers.IO) { apiService.uploadImage(purpose,contentType) }
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
}
