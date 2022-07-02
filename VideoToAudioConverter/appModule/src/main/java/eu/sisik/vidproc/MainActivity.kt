package eu.sisik.vidproc

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.*
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import android.support.v4.content.FileProvider


class MainActivity : AppCompatActivity() {

    private var selectedImgUris = ArrayList<Uri>()
    private var selectedAudioUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            selectedImgUris = savedInstanceState.getParcelableArrayList("selectedImgUris")
            selectedAudioUri = savedInstanceState.getParcelable("selectedAudioUri")
        }

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == CODE_IMAGE_SEARCH && resultCode == Activity.RESULT_OK) {
            addImages(data!!)
        } else if (requestCode == CODE_AUDIO_SEARCH && resultCode == Activity.RESULT_OK) {
            addAudio(data!!)
        }
        else if (requestCode == CODE_ENCODING_FINISHED) {
            progressEncoding.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        configureUi()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
            Toast.makeText(this, getString(R.string.warn_no_storage_permission), Toast.LENGTH_LONG)
                .show()
        } else {
            if (requestCode == CODE_IMAGE_SEARCH) {
                performImagesSearch(
                    this@MainActivity, CODE_IMAGE_SEARCH)
            } else if (requestCode == CODE_AUDIO_SEARCH) {
                performAudioSearch(
                    this@MainActivity, CODE_AUDIO_SEARCH)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("selectedImgUris", selectedImgUris)
        outState?.putParcelable("selectedAudioUri", selectedAudioUri)
    }

    private fun initView() {

        ivPreview.setOnClickListener {
            playPreview()
        }

        butAddImages.setOnClickListener {
            if (needsStoragePermission(this@MainActivity)) {
                requestStoragePermission(this@MainActivity, CODE_IMAGE_SEARCH)
            }
            else {
                performImagesSearch(this@MainActivity, CODE_IMAGE_SEARCH)
            }
        }

        butAddAudio.setOnClickListener {
            if (needsStoragePermission(this@MainActivity)) {
                requestStoragePermission(this@MainActivity, CODE_AUDIO_SEARCH)
            }
            else {
                performAudioSearch(this@MainActivity, CODE_AUDIO_SEARCH)
            }
        }

        butClearImages.setOnClickListener {
            selectedImgUris.clear()
            butCreateTimeLapse.visibility = View.INVISIBLE
            butClearImages.visibility = View.INVISIBLE
            tvSelectedCount.text = ""
        }

        butClearImages.visibility = if (selectedImgUris.isNotEmpty()) View.VISIBLE else View.INVISIBLE

        butClearAudio.setOnClickListener {
            butClearAudio.visibility = View.INVISIBLE
            tvAudioFile.text = ""
        }

        butClearAudio.visibility = if (selectedAudioUri != null) View.VISIBLE else View.INVISIBLE


        butCreateTimeLapse.setOnClickListener {
            requestEncodeImages()
        }
    }

    private fun playPreview() {
        val outFile = File(getOutputPath())
        if (outFile.exists()) {
            val uri =
                if (VERSION.SDK_INT >= VERSION_CODES.N)
                    FileProvider.getUriForFile(this, "$packageName.provider", outFile)
                else
                    Uri.parse(outFile.absolutePath)

            val intent = Intent(Intent.ACTION_VIEW, uri)
                .setDataAndType(uri,"image/*")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setDataAndType(uri, "video/mp4")

            startActivityForResult(intent, CODE_THUMB)
            Toast.makeText(this, "starting", Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(this, getString(R.string.app_name), Toast.LENGTH_LONG).show()
        }
    }

    private fun addImages(data: Intent) {
        if (data != null) {
            if (data.data != null) {
                selectedImgUris.add(data.data)
            } else if (data.clipData != null) {
                val cd = data.clipData
                val unsorted = ArrayList<Uri>()
                for (i in 0 until cd.itemCount) {
                    val item = cd.getItemAt(i)
                    unsorted.add(item.uri)
                }

                // System file picker doesn't guarantee any kind of order when
                // Multiple files are selected, so I at least sort by path/filename
                selectedImgUris.addAll(unsorted.sortedBy { it.encodedPath })
            }
        }

        tvSelectedCount.text = selectedImgUris.size.toString() + " images selected"

        if (selectedImgUris.size > 0) {
            butCreateTimeLapse.visibility = View.VISIBLE
            butClearImages.visibility = View.VISIBLE
        }
    }

    private fun addAudio(data: Intent) {
        if (data != null) {
            if (data.data != null) {
                selectedAudioUri = data.data
                tvAudioFile.text = selectedAudioUri!!.lastPathSegment
                butClearAudio.visibility = View.VISIBLE
            }
        }
    }

    private fun configureUi() {
        if (isServiceRunning(this, EncodingService::class.java))
            progressEncoding.visibility = View.VISIBLE
        else
            progressEncoding.visibility = View.INVISIBLE

        val visibility = if (selectedImgUris.size > 0) View.VISIBLE else View.INVISIBLE
        butClearImages.visibility = visibility
        butCreateTimeLapse.visibility = visibility

        if (selectedAudioUri != null){
            butClearAudio.visibility = View.VISIBLE
            tvAudioFile.text = selectedAudioUri!!.lastPathSegment
        }

        tvSelectedCount.text = selectedImgUris.size.toString() + " images selected"

        val outFile = File(getOutputPath())
        if (outFile.exists()) {
            val thumb = ThumbnailUtils.createVideoThumbnail(outFile.absolutePath,
                MediaStore.Images.Thumbnails.FULL_SCREEN_KIND)
            ivPreview.setImageBitmap(thumb)
        }
    }

    private fun requestEncodeImages() {
        if (selectedImgUris.size > 0) {
            val intent = Intent(this, EncodingService::class.java).apply {
                action = EncodingService.ACTION_ENCODE_IMAGES

                putExtra(EncodingService.KEY_OUT_PATH, getOutputPath())
                putExtra(EncodingService.KEY_IMAGES, selectedImgUris)

                if (selectedAudioUri != null)
                    putExtra(EncodingService.KEY_AUDIO, selectedAudioUri)

                // We want this Activity to get notified once the encoding has finished
                val pi = createPendingResult(CODE_ENCODING_FINISHED, intent, 0)
                putExtra(EncodingService.KEY_RESULT_INTENT, pi)
            }

            startService(intent)

            progressEncoding.visibility = View.VISIBLE
        } else {
            Toast.makeText(this@MainActivity, getString(R.string.err_one_file), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getOutputPath(): String {
        return cacheDir.absolutePath + "/" + OUT_FILE_NAME
    }


    companion object {
        const val TAG = "MainActivity"

        const val CODE_IMAGE_SEARCH = 1110
        const val CODE_AUDIO_SEARCH = 1111
        const val CODE_ENCODING_FINISHED = 1112
        const val CODE_THUMB = 1113

        const val OUT_FILE_NAME = "out.mp4"
    }
}
