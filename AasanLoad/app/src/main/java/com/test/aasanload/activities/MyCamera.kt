package com.test.aasanload.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.test.aasanload.R
import com.test.aasanload.constants.MyConstants
import com.test.aasanload.constants.MyConstants.TAG
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.jvm.internal.Intrinsics

class MyCamera : AppCompatActivity() {

    private val isFirstTime: Boolean=false
    private var network: String=""
    private var cameraExecutor: Executor?=null
    private var imageCapture: ImageCapture?=null
    var cameraProviderFuture:ListenableFuture<ProcessCameraProvider>? =null
    var cameraProvider: ProcessCameraProvider? = null
    var camera:PreviewView?=null
    private val RATIO_16_9_VALUE = 1.7777777777777777
    private val RATIO_4_3_VALUE = 1.3333333333333333

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerax)
        this.network = intent.getStringExtra(MyConstants.NETWORK_KEY).toString()

        camera=findViewById(R.id.camera)
        cameraExecutor= Executors.newSingleThreadExecutor()
        startCamera()
    }
    private fun startCamera() {
         cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture?.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture?.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(camera?.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor!!, LuminosityAnalyzer(object : LumaListener{
                        override fun luma(lum: Bitmap) {
                        val image=InputImage.fromBitmap(lum,0)
                        recognizer.process(image).addOnSuccessListener {
                            var texts=ArrayList<String>()

                            for (block in it.textBlocks) {
                                for (line in block.lines) {
                                    for (element in line.elements) {
                                        val elementText = element.text
                                        texts.add(elementText)
                                    }
                                }
                            }
                            receiveDetections(texts)
                        }.addOnFailureListener {

                        }
                        }
                    }))

                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {


                // Bind use cases to camera
                cameraProvider?.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }



    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val imageToBitmap = imageToBitmap(image)
            val height = imageToBitmap.height
            val width = imageToBitmap.width
//
//            val buffer = image.planes[0].buffer
//            val data = buffer.toByteArray()
//            val pixels = data.map { it.toInt() and 0xFF }
//            val luma = pixels.average()

            listener.luma(imageToBitmap)

            //image.close()
        }
        fun imageToBitmap(imageProxy: ImageProxy): Bitmap {

            val obj = imageProxy.planes[0]
            val buffer = obj.buffer
            val obj2 = imageProxy.planes[2]
            val buffer2 = obj2.buffer
            val remaining = buffer.remaining()
            val remaining2 = buffer2.remaining()
            val bArr = ByteArray(remaining + remaining2)
            buffer[bArr, 0, remaining]
            buffer2[bArr, remaining, remaining2]
            val yuvImage = YuvImage(bArr, 17, imageProxy.width, imageProxy.height, null)
            val byteArrayOutputStream = ByteArrayOutputStream()
            yuvImage.compressToJpeg(
                Rect(0, 0, yuvImage.width, yuvImage.height),
                50,
                byteArrayOutputStream
            )
            val toByteArray = byteArrayOutputStream.toByteArray()
            val decodeByteArray = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.size)
            return decodeByteArray
        }
    }

    interface LumaListener{
        fun luma(lum: Bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        val executorService: ExecutorService? = cameraExecutor as ExecutorService?
        executorService?.shutdown()
    }

    fun receiveDetections(sparseArray: Collection<*>) {
        val arrayList: ArrayList<String> = ArrayList<String>()
        val size = sparseArray.size
        for (i in 0 until size) {
            val valueAt: Any? = sparseArray.elementAt(i)
            val textBlock = valueAt as TextBlock
            val stringBuilder = java.lang.StringBuilder()
            stringBuilder.append("receiveDetections: ")
            stringBuilder.append(textBlock.text)
            val stringBuilder2 = stringBuilder.toString()
            val str = TAG
            Log.d(str, stringBuilder2)
            var value = textBlock.text
            value = isCodeNumber(value!!)
            if (value != null) {
                if ((if ((value as CharSequence).length > 0) 1 else null) == 1) {
                    arrayList.add(value)
                    Log.d(str, value)
                }
            }
        }
        val collection: Collection<*> = arrayList
        if (!collection.isEmpty() && !this.isFirstTime) {
            val executorService: ExecutorService? = cameraExecutor as ExecutorService?
            executorService?.shutdown()
            val intent = Intent(this as Context, DialerActivity::class.java)
            intent.putExtra(MyConstants.CODE_ARRAY, arrayList)
            intent.putExtra(MyConstants.NETWORK_KEY, this.network)
            startActivity(intent)
            finish()
        } else if (!collection.isEmpty() && this.isFirstTime) {
            Handler(Looper.getMainLooper()).postDelayed({ receiveDetections(collection) }, 500)
        }
    }

    private fun isCodeNumber(str: String): String {
        val str2 = ""
        if (str.length < 14) {
            return str2
        }
        for (str3 in str.split("\n").toTypedArray()) {
            if (str3.length >= 14) {
                val length = str3.length
                var str4 = str2
                for (i in 0 until length) {
                    val charAt = str3[i]
                    if (Character.isDigit(charAt)) {
                        val stringBuilder = StringBuilder()
                        stringBuilder.append(str4)
                        stringBuilder.append(charAt)
                        str4 = stringBuilder.toString()
                    } else if (charAt != ' ') {
                        str4 = str2
                    }
                }
                return if (str4.length == 14) str4 else str2
            }
        }
        return str2
    }

    private fun aspectRatio(i: Int, i2: Int): Int {
        val max = Math.max(i, i2).toDouble() / Math.min(i, i2).toDouble()
        return if (Math.abs(max - RATIO_4_3_VALUE) <= Math.abs(max - RATIO_16_9_VALUE)) 0 else 1
    }

    fun dpToPixels(i: Int): Float {
        val f = i.toFloat()
        val resources = resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
    }



}