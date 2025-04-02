package com.trucksup.field_officer.presenter.view.activity.other

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.services.ApiService
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.common.JWTtoken
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(private val apiService: ApiService) : ViewModel()  {

    fun generateJWTtoken(
        request: GenerateJWTtokenRequest,
        controller: JWTtoken,
        context: Context
    ) {

        apiService.generateJWTtoken(
            PreferenceManager.getAccesHeader(context).toString(),
            request,
            "JwtAuth/api/Auth/GenerateJWTtoken"
        )
            ?.enqueue(object : Callback<GenerateJWTtokenResponse> {
                override fun onResponse(
                    call: Call<GenerateJWTtokenResponse>,
                    response: Response<GenerateJWTtokenResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode == 200) {
                            controller.onTokenSuccess(response.body()!!)
                        } else {

                            controller.onTokenFailure(response.body()?.message.toString())
                            val abx =
                                MyAlartBox(
                                    context as Activity,
                                    response.body()?.message.toString(),
                                    "m"
                                )
                            abx.show()

                        }
                    } else {
                        val abx =
                            MyAlartBox(
                                context as Activity,
                                context.resources.getString(R.string.no_data_found),
                                "m"
                            )
                        abx.show()
                    }


                }

                override fun onFailure(call: Call<GenerateJWTtokenResponse>, t: Throwable) {
                    LoggerMessage.LogErrorMsg("Error", "" + t.message)
                    controller.onTokenFailure(t.message.toString())
                }
            })
    }


}