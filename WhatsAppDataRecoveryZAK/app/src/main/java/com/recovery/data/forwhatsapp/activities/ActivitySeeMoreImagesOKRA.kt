package com.recovery.data.forwhatsapp.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recovery.data.forwhatsapp.LongClickInterfaceOKRA
import com.recovery.data.forwhatsapp.R
import com.recovery.data.forwhatsapp.fileobserverspkg.RecoverFilesOKRA
import com.recovery.data.forwhatsapp.fragmentstatuspkg.MyFilesListStatusOKRA
import com.recovery.data.forwhatsapp.imagespkg.AdapterImagesOKRA
import com.recovery.data.forwhatsapp.imagespkg.ImagesUrlInterfaceOKRA
import java.io.File
import java.net.URLConnection
import java.util.*

class ActivitySeeMoreImagesOKRA : AppCompatActivity(), ImagesUrlInterfaceOKRA, LongClickInterfaceOKRA {
    private val TAG = "92727"
    var frameLayout: FrameLayout? = null
    var recyclerView: RecyclerView? = null
    var _images_adapter: AdapterImagesOKRA? = null
    var image_files = ArrayList<File>()
    var fragment = false
    var contex: Context? = null
    var delete: ImageView? = null
    var share: ImageView? = null
    var progressBar: ProgressBar? = null
    var constraintLayout: ConstraintLayout? = null
    var searchView: SearchView? = null
    var linearSelected: LinearLayout? = null
    var selectDot: ImageView? = null
    var selectall = ArrayList<String>()
    /* fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         // container.getLayoutTransition().setAnimateParentHierarchy(false);
         return inflater.inflate(R.layout.activity_images_, container, false)
     }

     fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         contex = getContext().getApplicationContext()
         initComponents(view)
         setAdapterData()
     }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_okra)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle("")
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        contex = getApplicationContext()
//        findViewById<TextView>(R.id.tv_image_all).visibility = View.VISIBLE
        initComponents()
        setAdapterData()
        findViewById<ProgressBar>(R.id.imagebar).visibility = View.GONE
        val id = searchView!!.context
                .resources
                .getIdentifier("android:id/search_src_text", null, null)
        val id2 = searchView!!.context
                .resources
                .getIdentifier("android:id/search_button", null, null)
        val id3 = searchView!!.context
                .resources
                .getIdentifier("android:id/search_close_btn", null, null)
        val textView = searchView!!.findViewById<View>(id) as TextView
        textView.setTextColor(Color.BLACK)
        val searchClose = searchView!!.findViewById<ImageView>(id3)
        searchClose.setColorFilter(Color.GRAY)
        val searchIcon = searchView!!.findViewById<ImageView>(id2)
        searchIcon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_search_black))
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes[textView] = 0 //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
        }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                _images_adapter!!.filter.filter(newText)
                return true
            }
        })
        linearSelected!!.setOnClickListener(View.OnClickListener {
            if (selectall.size == 0) {
                selectDot!!.setBackgroundResource(R.drawable.ic_chek_icon)
                for (i in image_files.indices) {
                    selectall.add(image_files[i].absolutePath)
                }
                _images_adapter!!.selectedRows = selectall
                _images_adapter!!.notifyDataSetChanged()
            } else {
                selectDot!!.setBackgroundResource(R.drawable.ic_unchek_icon)
                _images_adapter!!.selectedRows = ArrayList()
                selectall.clear()
                _images_adapter!!.notifyDataSetChanged()
            }
        })
        /* loadBannerAdd()
         admobBanner()*/
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_images_);
        getSupportActionBar().setTitle("Images");
        contex=getApplicationContext();
        initComponents();
    }*/
    private fun initComponents() {
        // manage_directory = Images_Manage_Recovery_Directory()
        progressBar = findViewById(R.id.imagebar)
        delete = findViewById(R.id.delete)
        constraintLayout = findViewById(R.id.nodatacons)
        share = findViewById<ImageButton>(R.id.share)
        frameLayout = findViewById(R.id.imagesframelayout)
        searchView = findViewById<SearchView>(R.id.searchView)
        recyclerView = findViewById(R.id.images_recycleview)
        linearSelected = findViewById<LinearLayout>(R.id.linearSelectAll3)
        selectDot = findViewById<ImageView>(R.id.imageselectdot3)
        //recyclerView.getLayoutTransition().setAnimateParentHierarchy(false);
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView?.setLayoutManager(layoutManager)
        //init adapter
        _images_adapter = AdapterImagesOKRA(image_files, this)
        _images_adapter!!.setInterface(this)
        _images_adapter!!.setLongClickListenersSeeMore(this)
        //setAadapteData
        //cardView_images_adapter.setItemClickListeners(this);
        recyclerView?.setAdapter(_images_adapter)
        delete?.setOnClickListener(DeleteSelectedItems())
        share?.setOnClickListener(ShareSelectedItems())

        delete?.setVisibility(View.GONE)
        share?.setVisibility(View.GONE)
    }

    inner class ShareSelectedItems : View.OnClickListener {
        override fun onClick(view: View) {
            val paths: ArrayList<String> = _images_adapter!!.getSelectedRows()
            if (paths != null && paths.size != 0) {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND_MULTIPLE
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
                intent.type = "image/*" /* This example is sharing jpeg images. */
                val files = ArrayList<Uri>()
                for (path in paths /* List of the files you want to send */) {
                    val file = File(path)
                    val uri = FileProvider.getUriForFile(getApplicationContext(), resources.getString(R.string.authority), file)
                    files.add(uri)
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
                startActivity(intent)
            }
            _images_adapter!!.setSelectedRows(ArrayList<String>())
            setAdapterData()
        }
    }

    inner class DeleteSelectedItems : View.OnClickListener {
        override fun onClick(view: View) {
            if(_images_adapter!!.selectedRows.size>0) {
                deleteWarningDialog()
            }else{
                Toast.makeText(this@ActivitySeeMoreImagesOKRA, "Kindly Select atleast one Video", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.generic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                ArrayList<String> paths = _images_adapter.getSelectedRows();
                if (paths != null && paths.size() != 0) {
                    for (String path : paths) {
                        File f = new File(path);
                        f.delete();
                    }
                    _images_adapter.setSelectedRows(new ArrayList<>());
                    setAdapterData();
                }
                break;

            case R.id.menu_share:
                ArrayList<String> paths2 = _images_adapter.getSelectedRows();
                if (paths2 != null && paths2.size() != 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                    intent.setType("image/ *"); */
    /* This example is sharing jpeg images. */ /*
                    ArrayList<Uri> files = new ArrayList<Uri>();
                    for (String path : paths2 */
    /* List of the files you want to send */ /*) {
                        File file = new File(path);
                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.fha.whatsappdatarecoveryapp.fileprovider", file);
                        files.add(uri);
                    }
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                    startActivity(intent);
                }
                _images_adapter.setSelectedRows(new ArrayList<>());
                setAdapterData();
                break;
        }
        return true;
    }*/

    private fun deleteWarningDialog() {
        val dialog = Dialog(this@ActivitySeeMoreImagesOKRA)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_warning_layout_okra)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(getDrawable(android.R.color.transparent))
        dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        val btn_yes = dialog.findViewById<Button>(R.id.btn_yes)
        val no = dialog.findViewById<Button>(R.id.btn_no)
        no.setOnClickListener { view: View? -> dialog.dismiss() }
        btn_yes.setOnClickListener { view: View? ->
            val paths: ArrayList<String> = _images_adapter!!.getSelectedRows()
            if (paths != null && paths.size != 0) {
                for (path in paths) {
                    val f = File(path)
                    f.delete()
                    MyFilesListStatusOKRA.files.remove(f)
                    if (MyFilesListStatusOKRA.files.size > 0) {

                        findViewById<View>(R.id.images_recycleview).visibility = View.VISIBLE
                    } else {
                        findViewById<View>(R.id.nodatacons).visibility = View.VISIBLE
                        delete!!.visibility = View.GONE
                        share!!.visibility = View.GONE
                    }
                }
                _images_adapter!!.setSelectedRows(ArrayList<String>())
                setAdapterData()
            }

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setAdapterData() {
        linearSelected!!.visibility=View.GONE
        selectDot!!.setBackgroundResource(R.drawable.ic_unchek_icon)
        image_files.clear()
        image_files.addAll(MyFilesListStatusOKRA.files)
        _images_adapter?.notifyDataSetChanged()
        //image_files=manage_directory.get_Recovery_Images();
        Log.d(TAG, "setAdapterData: recoverd images size :: " + image_files.size)

        if (image_files.size > 0) {
            recyclerView?.visibility = View.VISIBLE
        }
    }


    override fun setUrlandLaunchInterface(url: String?,name: String?) {
        /*     fragment = true
             recyclerView!!.visibility = View.GONE
             frameLayout!!.visibility = View.VISIBLE
             // getSupportActionBar().hide();
             val fragmentManager: FragmentManager = supportFragmentManager
             val fragmentTransaction = fragmentManager.beginTransaction()
             val fragment = Images_View_Fragment()
             val bundle = Bundle()
             bundle.putString("url", url)
             fragment.arguments = bundle
             fragmentTransaction.replace(R.id.imagesframelayout, fragment, "92727")
             fragmentTransaction.commit()*/

        val intent = Intent(this@ActivitySeeMoreImagesOKRA, ActivityViewImageOKRA::class.java)
        intent.putExtra("url", url)
        intent.putExtra("imgname",name)
        startActivity(intent)
    }

    override fun onLongClick(url: String?, position: Int) {
        val pickDialog = PickDialog(url, position)
        pickDialog.showDialog(this)
    }

    /*  @Override
    public void onBackPressed() {
        if(fragment){
            final Images_View_Fragment fragment = (Images_View_Fragment) getSupportFragmentManager().findFragmentByTag("92727");
            if(fragment.isBackpressed()){
                fragment.setBackpressed(false);
                fragment.getView().setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
            }else
                super.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }*/
    inner class PickDialog(var url: String?, var position: Int) {
        fun showDialog(activity: Activity?) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.delete_share_dialogue_okra)
            val delete = dialog.findViewById<Button>(R.id.delete)
            val share = dialog.findViewById<Button>(R.id.share)
            val save = dialog.findViewById<Button>(R.id.save)
            save.setOnClickListener {
                val recoverFiles = RecoverFilesOKRA(getApplicationContext())
                if (recoverFiles.storeFileToRecoveryFolder(url)) {
                    image_files.removeAt(position)
                    _images_adapter?.notifyDataSetChanged()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        val contentUri = Uri.fromFile(File(url))
                        scanIntent.data = contentUri
                        sendBroadcast(scanIntent)
                    } else {
                        val intent = Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()))
                        sendBroadcast(intent)
                    }
                }
                dialog.dismiss()
            }
            delete.setOnClickListener {
                val file = File(url)
                if (file.exists()) {
                    file.delete()
                    System.gc()
                    image_files.removeAt(position)
                    _images_adapter?.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
            share.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = URLConnection.guessContentTypeFromName(url)
                //intent.setData(Uri.parse(url));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url))
                val chooserIntent = Intent.createChooser(intent, "Sharing Images")
                startActivity(chooserIntent)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun onLongClicked(flag: Boolean) {
        if (flag) {
            delete!!.visibility = View.VISIBLE
            share!!.visibility = View.VISIBLE
            linearSelected!!.visibility=View.VISIBLE
        } else {
            delete!!.visibility = View.GONE
            share!!.visibility = View.GONE
            linearSelected!!.visibility=View.GONE
            selectDot!!.setBackgroundResource(R.drawable.ic_unchek_icon)
        }
    }
}