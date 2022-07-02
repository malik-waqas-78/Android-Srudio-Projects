package com.recovery.data.forwhatsapp.activities

//import com.facebook.ads.*
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.LoadAdError
//import com.codesk.datarecovery.forwhatsapp.AATAdmobFullAdManager.Companion.getAdSize
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.recovery.data.forwhatsapp.R
import com.recovery.data.forwhatsapp.StatusDataSearchInterfaceOKRA


class ActivityStatusMainOKRA : AppCompatActivity() {

    var statusDataSearchInterfaceOKRA: StatusDataSearchInterfaceOKRA? = null
   
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_main_okra)
        val searchView = findViewById<SearchView>(R.id.searchView9)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle("")
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        if(AATAdsManager.isAppInstalledFromPlay(this)) {
////            loadFbBannerAdd()
////            loadAdmobBanner()
//        }
        val id = searchView.context
                .resources
                .getIdentifier("android:id/search_src_text", null, null)
        val id2 = searchView.context
                .resources
                .getIdentifier("android:id/search_button", null, null)
        val id3 = searchView.context
                .resources
                .getIdentifier("android:id/search_close_btn", null, null)
        val textView = searchView.findViewById<View>(id) as TextView
        textView.setTextColor(Color.BLACK)
        val searchClose = searchView.findViewById<ImageView>(id3)
        searchClose.setColorFilter(Color.BLACK)

        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes[textView] = 0 //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
        }

        //searchView.setBackgroundResource(R.drawable.searchview_background);
        val searchIcon = searchView.findViewById<ImageView>(id2)
        searchIcon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_search_black))
        searchView.setBackgroundResource(R.drawable.searchview_background)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                statusDataSearchInterfaceOKRA?.sendTextToFragment(newText)
                return true
            }
        })
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }

        })


    }
    fun setIntrface(inter: StatusDataSearchInterfaceOKRA){
        statusDataSearchInterfaceOKRA=inter
    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.example_menu, menu)
//        val searchItem = menu.findItem(R.id.action_search)
//        val searchView = searchItem.actionView as SearchView
//        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
//        val id = searchView.context
//                .resources
//                .getIdentifier("android:id/search_src_text", null, null)
//        val id2 = searchView.context
//                .resources
//                .getIdentifier("android:id/search_button", null, null)
//        val id3 = searchView.context
//                .resources
//                .getIdentifier("android:id/search_close_btn", null, null)
//        val textView = searchView.findViewById<View>(id) as TextView
//        textView.setTextColor(Color.BLACK)
//        val searchClose = searchView.findViewById<ImageView>(id3)
//        searchClose.setColorFilter(Color.BLACK)
//
//        try {
//            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
//            mCursorDrawableRes.isAccessible = true
//            mCursorDrawableRes[textView] = 0 //This sets the cursor resource ID to 0 or @null which will make it visible on white background
//        } catch (e: Exception) {
//        }
//
//        //searchView.setBackgroundResource(R.drawable.searchview_background);
//        val searchIcon = searchView.findViewById<ImageView>(id2)
//        searchIcon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_search_black))
//        searchView.setBackgroundResource(R.drawable.searchview_background)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                statusDataSearchInterface?.sendTextToFragment(newText)
//                return true
//            }
//        })
//        return true
//    }

//    fun loadFbBannerAdd() {
//        val adView = AdView(this, resources.getString(R.string.fbbannerad), AdSize.BANNER_HEIGHT_50)
//        val adListener: AdListener = object : AdListener {
//            override fun onError(ad: Ad, adError: AdError) {
//                Log.d("TAG", "onError: " + adError.errorMessage)
//            }
//
//            override fun onAdLoaded(ad: Ad) {}
//            override fun onAdClicked(ad: Ad) {}
//            override fun onLoggingImpression(ad: Ad) {}
//        }
//        val relativeLayout = findViewById<RelativeLayout>(R.id.top_banner)
//        relativeLayout.addView(adView)
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
//    }
//    fun loadAdmobBanner() {
//        val mAdView = com.google.android.gms.ads.AdView(this@ActivityStatusMainAAT)
//        val adSize = getAdSize(this@ActivityStatusMainAAT)
//        mAdView.adSize = adSize
//        mAdView.adUnitId = resources.getString(R.string.admob_banner)
//        val adRequest = AdRequest.Builder().build()
//        val adViewLayout = findViewById<View>(R.id.bottom_banner) as RelativeLayout
//        adViewLayout.addView(mAdView)
//        mAdView.loadAd(adRequest)
//        mAdView.adListener = object : com.google.android.gms.ads.AdListener() {
//            override fun onAdClosed() {
//                super.onAdClosed()
//            }
//
//            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                super.onAdFailedToLoad(loadAdError)
//                adViewLayout.visibility = View.GONE
//            }
//
//            override fun onAdClicked() {
//                super.onAdClicked()
//            }
//
//            override fun onAdImpression() {
//                super.onAdImpression()
//            }
//
//            override fun onAdOpened() {
//                super.onAdOpened()
//            }
//
//            override fun onAdLoaded() {
//                super.onAdLoaded()
//                adViewLayout.visibility = View.VISIBLE
//            }
//        }
//    }


    /*override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.getItemId() === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }*/
}