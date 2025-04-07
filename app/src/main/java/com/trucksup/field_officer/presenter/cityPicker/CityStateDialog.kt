package com.trucksup.field_officer.presenter.cityPicker

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.CommonApplication
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.adapter.CityLocationAdapter
import retrofit2.Call
import retrofit2.Callback
import java.util.*

private var mSpeechRecognizer: SpeechRecognizer? = null
private var mSpeechRecognizerIntent: Intent? = null
private var search_place: EditText? = null
private var reco: RelativeLayout? = null

class CityStateDialog(
    private var mcontext: Context,
    var cityPicker: CityPicker,
    var type: String,
    private var from_load: TextView,
    private var apiType: String,
    private var voiceSpeach: Boolean
) : Dialog(mcontext),
    CityClick,
    TextToSpeech.OnInitListener {

    var clickCity: CityClick? = null
    var back: ImageButton? = null
    private var micro: ImageButton? = null
    private var anywhere: LinearLayout? = null
    var cityAdapter: CityLocationAdapter? = null
    private var city_list: RecyclerView? = null
    var city: ArrayList<CityNameData>? = ArrayList()
    var id: String = ""
    var lang: String? = null
    private var t1: TextToSpeech? = null
    private var recordAudioRequestCode: Int? = 1011

    init {
        setCancelable(false)

    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.city_dailog)

        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.window
            ?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        this.window
            ?.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        val wlp = window?.attributes
        wlp?.gravity = Gravity.TOP or Gravity.RIGHT
        window?.setAttributes(wlp);
        clickCity = this
        this.setCancelable(true)
        t1 = TextToSpeech(context, this)

        speech(PreferenceManager.getLanguage(context))
        lang = PreferenceManager.getLanguage(context)
        init()

    }

    @SuppressLint("NewApi")
    fun init() {
        search_place = findViewById(R.id.search_place)
        city_list = findViewById(R.id.city_list)
        back = findViewById(R.id.back)
        micro = findViewById(R.id.micro)
        reco = findViewById(R.id.reco)
        Log.e("Type", type)
        anywhere?.visibility = View.GONE
        micro?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermission(mcontext as Activity)
            } else {
                reco?.visibility = View.VISIBLE
                mSpeechRecognizer?.startListening(mSpeechRecognizerIntent);
            }
        }
        back?.setOnClickListener {
            mSpeechRecognizer?.destroy()
            this.dismiss()
        }


        search_place?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if (TextUtils.isEmpty(search_place?.text.toString().toLowerCase().trim())) {

                } else {

                    if (search_place?.text.toString().length >= 3) {
                        setEnglishFilter(search_place?.text.toString().toLowerCase())

                    }
                }

            }
        })

    }


    override fun onClickcity(value: String, state: String, id: String) {
    }

    override fun onClickcityLanguage(
        cityEng: String,
        cityHi: String,
        stateEn: String,
        stateHi: String,
        id: String
    ) {


        LoggerMessage.LogErrorMsg("Type", ">>>>>" + type)
        LoggerMessage.LogErrorMsg("Place id", ">>>>>" + id)
        if (voiceSpeach == true) {
            textRead(cityEng)
        }
        mSpeechRecognizer?.destroy()
        if (type.equals("f")) {
            if (PreferenceManager.getLanguage(context) == "en") {
                from_load.setText(cityEng)
            } else {
                from_load.setText(cityHi)
            }
            cityPicker?.fromCity(cityEng, stateEn, id, type, cityHi, stateHi)



            this.dismiss()
        } else if (type.equals("alf")) {
            if (PreferenceManager.getLanguage(context) == "en") {
                from_load.setText(cityEng)
            } else {
                from_load.setText(cityHi)
            }
            cityPicker?.cityState(cityEng, cityHi, stateEn, stateHi, id, type)
            this.dismiss()
        } else if (type.equals("alt")) {
            if (PreferenceManager.getLanguage(context) == "en") {
                from_load.setText(cityEng)
            } else {
                from_load.setText(cityHi)
            }
            cityPicker?.cityState(cityEng, cityHi, stateEn, stateHi, id, type)

            this.dismiss()
        } else if (type.equals("ac")) {
            if (PreferenceManager.getLanguage(context) == "en") {
                from_load.setText(cityEng)
            } else {
                from_load.setText(cityHi)
            }
            this.dismiss()
        } else if (type.equals("pc")) {
            if (PreferenceManager.getLanguage(context) == "en") {
                from_load.setText(cityEng)
            } else {
                from_load.setText(cityHi)
            }
            this.dismiss()
        } else if (type.equals("cs")) {
            if (PreferenceManager.getLanguage(context) == "en") {
                from_load.setText(cityEng)
            } else {
                from_load.setText(cityHi)
            }
            cityPicker?.cityState(cityEng, cityHi, stateEn, stateHi, id, type)

            this.dismiss()
        } else {
            if (PreferenceManager.getLanguage(context) == "en") {
                from_load.setText(cityEng)
            } else {
                from_load.setText(cityHi)
            }
            cityPicker?.toCity(cityEng, stateEn, id, apiType, cityHi, stateHi)
            this.dismiss()
        }
    }

    fun setEnglishFilter(s: String) {
        var lang: Int = 1
        if (PreferenceManager.getLanguage(context) == "en") {
            lang = 1
        } else {
            lang = 2
        }

        LoggerMessage.LogErrorMsg("text input", ">>>>" + s)
        val newCity = ArrayList<CityNameData>()

        val request = CitySearchRequest(
            s,
            PreferenceManager.getPhoneNo(context),
            PreferenceManager.getRequestNo(),
            lang
        )
        val apiInterface = ApiClient().getClient

        apiInterface.searchCity(request)
            ?.enqueue(object : Callback<CityListbySearchRequest> {
                override fun onResponse(
                    call: Call<CityListbySearchRequest>,
                    response: retrofit2.Response<CityListbySearchRequest>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode?.toInt() == 200) {


                            if (response.body()?.data != null) {

                                cityAdapter = CityLocationAdapter(
                                    context,
                                    clickCity!!, type, response.body()?.data!!
                                )
                                city_list?.layoutManager = LinearLayoutManager(context)
                                city_list?.adapter = cityAdapter

                                cityAdapter?.notifyDataSetChanged()
                            }
                        }

                    } else {
//                        val data: ErrorModel = ErrorModel("No Data Found",""+PreferenceManager.getPhoneNo(context),""+PreferenceManager.getUserData(context)?.profileName,""+ DeviceInfoUtils.getDeviceModel(context),"API","Apigateway/Gateway/InquiryHistory")
//                        ErrorStore().StoreError(data)
//                        controller.inquiryHistoryFailure(context.resources.getString(R.string.no_data_found))
//                        var abx: MyAlartBox =
//                            MyAlartBox(context as Activity, context.resources.getString(R.string.no_data_found), "m")
//                        abx?.show()
                    }
                }

                override fun onFailure(call: Call<CityListbySearchRequest>, t: Throwable) {
                    LoggerMessage.LogErrorMsg("Error", "" + t.message)

                }
            })

        LoggerMessage.LogErrorMsg("Size", ">>>>" + newCity.size)

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            t1!!.setSpeechRate(0.6f)
            var result = 0
            if (PreferenceManager.getLanguage(context) == "en") {
                t1!!.language = Locale("en", "IN")
                result = t1?.setLanguage(Locale("en", "IN"))!!

            } else {
                t1!!.language = Locale("hi", "IN")
                result = t1?.setLanguage(Locale("hi", "IN"))!!
            }

            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e("TTS", "Language is not supported");
            }
            Log.e("TTS", "text read Initilization");

        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    private fun checkPermission(activityContext: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activityContext,
                arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                recordAudioRequestCode!!
            )
        }
    }

    private fun textRead(textToSpeech: String) {
        LoggerMessage.LogErrorMsg("Text to Speech", ">>>>>>>" + textToSpeech)

        t1!!.speak(
            textToSpeech,
            TextToSpeech.QUEUE_FLUSH,
            null,
            TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED
        )

    }

    class SpeechRecognitionListener : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {

        }

        override fun onBeginningOfSpeech() {

        }

        override fun onRmsChanged(rmsdB: Float) {

        }

        override fun onBufferReceived(buffer: ByteArray?) {

        }

        override fun onEndOfSpeech() {

        }

        override fun onError(error: Int) {
            //  mSpeechRecognizer?.startListening(mSpeechRecognizerIntent);
        }

        override fun onResults(results: Bundle?) {
            reco?.visibility = View.GONE
            val matches = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val Query: String = matches!!.get(0)

//          animation_view.playAnimation()
            // Toast.makeText(getContext(), Query, Toast.LENGTH_SHORT).show();

            // Toast.makeText(getContext(), Query, Toast.LENGTH_SHORT).show();
            Handler().postDelayed(Runnable {


                search_place?.setText(Query)
                mSpeechRecognizer?.cancel()
                mSpeechRecognizer?.stopListening()
            }, 1000)
        }

        override fun onPartialResults(partialResults: Bundle?) {

        }

        override fun onEvent(eventType: Int, params: Bundle?) {

        }

    }

    private fun speech(local: String?) {
        val locale = Locale(local)
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        mSpeechRecognizerIntent?.putExtra(RecognizerIntent.LANGUAGE_MODEL_FREE_FORM, local)
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.);
        mSpeechRecognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            context.packageName
        )
        val listener = SpeechRecognitionListener()
        mSpeechRecognizer?.setRecognitionListener(listener as RecognitionListener)

    }

}
