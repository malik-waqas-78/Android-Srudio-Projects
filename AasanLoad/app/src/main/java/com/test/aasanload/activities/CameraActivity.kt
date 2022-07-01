/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.aasanload.activities


import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.Camera
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.test.aasanload.constants.MyConstants
import com.test.aasanload.databinding.ActivityCameraActiviyBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


typealias LumaListener = (luma: Bitmap) -> Unit
/**
 * Main fragment for this app. Implements all camera operations including:
 * - Viewfinder
 * - Photo taking
 * - Image analysis
 */
class CameraActivity : AppCompatActivity() {

    private var _fragmentCameraBinding: ActivityCameraActiviyBinding? = null

     public val binding get() = _fragmentCameraBinding!!

    private var isFirstTime: Boolean=false
    private var network: String=""
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService
    public  var rect:Rect=Rect()

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */


    override fun onDestroy() {
        _fragmentCameraBinding = null
        super.onDestroy()

        // Shut down our background executor
        cameraExecutor.shutdown()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _fragmentCameraBinding = ActivityCameraActiviyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.network = intent.getStringExtra(MyConstants.NETWORK_KEY).toString()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels


        rect.left=0
        rect.top=(height/2)-50
        rect.right=width
        rect.bottom=height

        cameraExecutor = Executors.newSingleThreadExecutor()
        // Wait for the views to be properly laid out
        binding.camera.post {



            // Set up the camera and its use cases
            setUpCamera()
        }
    }



    /**
     * Inflate camera controls and update the UI manually upon config changes to avoid removing
     * and re-adding the view finder from the view hierarchy; this provides a seamless rotation
     * transition on devices that support it.
     *
     * NOTE: The flag is supported starting in Android 8 but there still is a small flash on the
     * screen for devices that run Android 9 or below.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Rebind the camera with the updated display metrics
        bindCameraUseCases()

        // Enable or disable switching between cameras

    }

    /** Initialize CameraX, and prepare to bind the camera use cases  */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }



            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this@CameraActivity))
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution


        val screenAspectRatio = aspectRatio(1080, 720)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = binding.camera.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                // We request aspect ratio but no resolution to match preview config, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()

        // ImageAnalysis
        imageAnalyzer = ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer (rect){ luma ->
                        val image= InputImage.fromBitmap(luma,0)
                        runOnUiThread {
                            binding.imageView.setImageBitmap(luma)
                        }
                        recognizer.process(image).addOnSuccessListener {
                            var texts=ArrayList<String>()
                            if(isFirstTime){
                                return@addOnSuccessListener
                            }
                            for (block in it.textBlocks) {
                                for (line in block.lines) {
                                    for (element in line.elements) {
                                        val elementText = element.text
                                       Log.d(TAG, "bindCameraUseCases: "+elementText)
                                        if(elementText.length>=14)
                                            texts.add(elementText)
                                    }
                                }
                            }
                            if(texts.size>0)
                                receiveDetections(texts)
                        }.addOnFailureListener {
                            //Log.i(TAG, "bindCameraUseCases: ")
                        }
                    })
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(binding.camera.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }


    fun receiveDetections(sparseArray: ArrayList<String>) {
        val arrayList: ArrayList<String> = ArrayList<String>()
        val size = sparseArray.size
        for (i in 0 until size) {
            val valueAt = sparseArray.elementAt(i)
            var value = valueAt
            value = isCodeNumber(value!!)
            if (value.length==14) {
                arrayList.add(value)
            }
        }
        val collection: ArrayList<String> = arrayList
        if (!collection.isEmpty() && !this.isFirstTime) {
            cameraExecutor.shutdown()
            isFirstTime=true
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
                        str4+=charAt
                    } else if (charAt != ' ') {
                        str4 = str2
                    }
                }
                return if (str4.length == 14) str4 else str2
            }
        }
        return str2
    }

    /**
     *  [androidx.camera.core.ImageAnalysis.Builder] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /** Method used to re-draw the camera UI controls, called every time configuration changes. */



    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    /**
     * Our custom image analysis class.
     *
     * <p>All we need to do is override the function `analyze` with our desired operations. Here,
     * we compute the average luminosity of the image by looking at the Y plane of the YUV frame.
     */
    open class LuminosityAnalyzer(var rect:Rect,var listener: LumaListener? = null) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val imageToBitmap = imageToBitmap(image)
//            val height = imageToBitmap.height
//            val width = imageToBitmap.width
//
//            val buffer = image.planes[0].buffer
//            val data = buffer.toByteArray()
//            val pixels = data.map { it.toInt() and 0xFF }
//            val luma = pixels.average()

            listener?.invoke(imageToBitmap)

            image.close()
        }

        fun imageToBitmap(imageProxy: ImageProxy): Bitmap {
            val yBuffer = imageProxy.planes[0].buffer // Y
            val vuBuffer = imageProxy.planes[2].buffer // VU

            val ySize = yBuffer.remaining()
            val vuSize = vuBuffer.remaining()

            val nv21 = ByteArray(ySize + vuSize)

            yBuffer.get(nv21, 0, ySize)
            vuBuffer.get(nv21, ySize, vuSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
            val imageBytes = out.toByteArray()

            val myBitmap :Bitmap= BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            // Clone a portion of the Bitmap object.
            // Clone a portion of the Bitmap object.
           // val cloneBitmap: Bitmap = Bitmap.createBitmap(myBitmap,0,(myBitmap.width/2)-50,myBitmap.width,100)

// Draw the cloned portion of the Bitmap object.

// Draw the cloned portion of the Bitmap object.

            imageProxy.close()
            return myBitmap
        }

    }

    companion object {

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
                File(baseFolder, SimpleDateFormat(format, Locale.US)
                        .format(System.currentTimeMillis()) + extension)
    }


}
