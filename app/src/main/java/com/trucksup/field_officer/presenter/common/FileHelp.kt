package com.trucksup.field_officer.presenter.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FileHelp {


    fun bitmapTofile(imageBitmap: Bitmap, context: Context): File? {
        val name = "trucksUp_image" + System.currentTimeMillis() + ".jpg"
        val pt = Environment.DIRECTORY_PICTURES //+  "/trucksUp";
        val MEDIA_PATH = Environment.getExternalStorageDirectory().absolutePath + "/" + pt + "/"

        val filesDir: File = context.getFilesDir()
        val imageFile = File(MEDIA_PATH, name)

        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 99, os)
            os.flush()
            os.close()
        } catch (e: java.lang.Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }

//            val bytes = ByteArrayOutputStream()
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//            val path = MediaStore.Images.Media.insertImage(
//                context.contentResolver,
//                imageBitmap,
//                timeStamp + ".jpeg",
//                null
//            )
//            // LoggerMessage.LogErrorMsg("uri of logo",path)
//            val file = getFile(context, Uri.parse(path))
//            imageBitmap.recycle()
            return imageFile

    }

    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri?): File? {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri!!))
        try {
            context.contentResolver.openInputStream(uri!!).use { ins ->
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


    fun storePhotoOnDisk(capturedBitmap: Bitmap) :File{
        var photoFile:File ?=null
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
    fun createFileFromStream(ins: InputStream, destination: File?) {
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
    fun resizeImage(originalBitmap: Bitmap?, maxWidth: Int, maxHeight: Int): Bitmap? {
        var resizedBitmap: Bitmap? = null

        try {
            // Decode the image file into a Bitmap object
            // val originalBitmap = BitmapFactory.decodeFile(imagePath)
            // Get the original width and height of the image
            val originalWidth = originalBitmap?.width
            val originalHeight = originalBitmap?.height
            Log.e("Org h","hhhhhhhh >>>>>> "+originalHeight)
            if ( 500<originalHeight!! ) {


                // Calculate the scale factor to resize the image while maintaining the aspect ratio
                val scaleFactor = Math.min(
                    maxWidth.toFloat() / originalWidth!!,
                    maxHeight.toFloat() / originalHeight!!
                )

                // Calculate the new width and height based on the scale factor
                val newWidth = Math.round(originalWidth * scaleFactor)
                val newHeight = Math.round(originalHeight * scaleFactor)

                // Resize the original bitmap to the new dimensions
                resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
                originalBitmap.recycle()
            }
            else{
                resizedBitmap =originalBitmap
            }

            // Recycle the original bitmap to free up memory

        } catch (e: Exception) {
            Log.e("ImageUtils", "Error resizing image: " + e.message)
        }
        return resizedBitmap
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
    fun FileToBitmap(file: File) :Bitmap
    {var myBitmap:Bitmap?=null
        if (file.exists()) {
            myBitmap = resizeImage(BitmapFactory.decodeFile(file.absolutePath),500,500)

                return myBitmap!!
        }
        else {
           return myBitmap!!
        }

    }
}