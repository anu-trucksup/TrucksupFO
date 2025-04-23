package com.trucksup.field_officer.presenter.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FileHelp {


    fun bitmapTofile(bitmap: Bitmap, context: Context): File {
        val fileName = "bo_image" + System.currentTimeMillis() + ".png"
        // Create a file in the cache directory
        val file = File(context.cacheDir, fileName)
        file.createNewFile()

        // Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos) // or PNG/WebP
        val bitmapData = bos.toByteArray()

        // Write the bytes to file
        FileOutputStream(file).use { fos ->
            fos.write(bitmapData)
            fos.flush()
        }

        return file
    }


    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri?): File {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri!!))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                createFileFromStream(
                    ins!!,
                    destinationFilename
                )
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }


    fun storePhotoOnDisk(capturedBitmap: Bitmap): File {
        var photoFile: File? = null
        Thread {
            val pt = Environment.DIRECTORY_PICTURES //+  "/trucksUp";
            val MEDIA_PATH = Environment.getExternalStorageDirectory().absolutePath + "/" + pt + "/"

            val folder = File(MEDIA_PATH)
            folder.mkdirs()

            val sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HH_mm_SS", Locale.US)

            val format: String = sdf.format(Date())


            photoFile = File(folder, "$format.jpg")

            if (photoFile!!.exists()) {
                photoFile!!.delete()
            }
            try {
                val fos = FileOutputStream(photoFile?.path)

                capturedBitmap.compress(Bitmap.CompressFormat.PNG, 99, fos)

                fos.flush()
                fos.close()
            } catch (e: IOException) {
                Log.e("PictureDemo", "Exception in photoCallback", e)
            }
        }.start()

        return photoFile!!
    }

    private fun queryName(context: Context, uri: Uri): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    private fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    fun resizeImage(originalBitmap: Bitmap?, targetWidth: Int): Bitmap {
        val aspectRatio = originalBitmap?.height?.toDouble()!! / originalBitmap.width.toDouble()
        val targetHeight = (targetWidth * aspectRatio).toInt()
        return Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true)

    }

    private fun compressBitmapToFile(bitmap: Bitmap?, file: File) {
        if (bitmap != null && !bitmap.isRecycled) {
            var outputStream: FileOutputStream? = null
            try {
                outputStream = FileOutputStream(file)
                // Compress the bitmap to JPEG format with 85% quality
                val success = bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                if (success) {
                    Log.d("Comp Img", "Bitmap compressed and saved successfully.")
                } else {
                    Log.e("Comp Img", "Failed to compress the bitmap.")
                }
            } catch (e: IOException) {
                Log.e("Comp Img", "Error compressing bitmap", e)
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close()
                    } catch (e: IOException) {
                        Log.e("Comp Img", "Error closing output stream", e)
                    }
                }
            }
        } else {
            Log.e("Comp Img", "Bitmap is null or already recycled! Compression skipped.")
        }
    }

    fun fileToBitmap(file: File): Bitmap {
        var myBitmap: Bitmap? = null
        if (file.exists()) {
            myBitmap = resizeImage(BitmapFactory.decodeFile(file.absolutePath), 500)
            return myBitmap
        } else {
            return myBitmap!!
        }

    }
}