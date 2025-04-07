package com.trucksup.field_officer.presenter.view.activity.other

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import com.mindev.mindev_pdfviewer.MindevPDFViewer
import com.mindev.mindev_pdfviewer.PdfScope
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage

class ViewPdfScreen : BaseActivity() {
    var pdf: MindevPDFViewer? = null
    var progressBar: ProgressBar? = null
    var url: String? = null
    var downloadVisibal: String = "y"
    var download_B: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf_screen)

        url = intent.getStringExtra("pdf")

        if (intent.getStringExtra("button") != null) {
            downloadVisibal = intent.getStringExtra("button").toString()
        }

        pdf = findViewById(R.id.pdf_viewer)
        progressBar = findViewById(R.id.progressBar)
        download_B = findViewById(R.id.download)
        pdf?.initializePDFDownloader(url!!, statusListener)
        lifecycle.addObserver(PdfScope())
        if (downloadVisibal == "n") {
            download_B?.visibility = View.GONE
        } else {
            download_B?.visibility = View.VISIBLE
        }
    }

    private val statusListener = object : MindevPDFViewer.MindevViewerStatusListener {
        override fun onStartDownload() {
        }

        override fun onPageChanged(position: Int, total: Int) {
            progressBar?.setProgress(total)
        }

        override fun onProgressDownload(currentStatus: Int) {

        }

        override fun onSuccessDownLoad(path: String) {
            pdf?.fileInit(path)
            progressBar?.visibility = View.GONE
            if (downloadVisibal == "n") {
                download_B?.visibility = View.GONE
            } else {
                download_B?.visibility = View.VISIBLE
            }
        }

        override fun onFail(error: Throwable) {
            progressBar?.visibility = View.GONE
        }

        override fun unsupportedDevice() {
            progressBar?.visibility = View.GONE
        }

    }

    fun download(v: View) {
        var fileName: String = url!!.substring(url!!.lastIndexOf('/') + 1)
        val source: Uri = Uri.parse(url!!)

        val request = DownloadManager.Request(source)
        request.setDescription("TrucksUp")
        request.setTitle(fileName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOCUMENTS,
            "/TrucksUp/$fileName"
        )

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        manager!!.enqueue(request)
        LoggerMessage.toastPrint("Download Start", this)
    }

    fun backScreen(v: View) {
        onBackPressed()
    }

}