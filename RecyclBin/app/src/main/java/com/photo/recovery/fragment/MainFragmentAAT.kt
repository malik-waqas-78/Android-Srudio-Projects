package com.photo.recovery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.photo.recovery.activites.ShowImageAAT
import com.photo.recovery.databinding.MainFragmentAatBinding


class MainFragmentAAT : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentsAdapterAAT: ViewPagerAdapterAAT

    private var _binding: MainFragmentAatBinding? =null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= MainFragmentAatBinding.inflate(inflater,container,false)
        fragmentsAdapterAAT= ViewPagerAdapterAAT(this)
        viewPager=binding.pager
        viewPager.adapter=fragmentsAdapterAAT
        viewPager.currentItem = ShowImageAAT.position
        /*viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if(position!=0){
                    fragmentsAdapter.visibility=View.VISIBLE
                    viewPager.unregisterOnPageChangeCallback(this)
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })*/
        /*viewPager.visibility=View.GONE*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*android.os.Handler().postDelayed({
            viewPager.visibility=View.VISIBLE
        },500)*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        //Log.d(MyConstants.TAG, "onDestroy: main")
    }
}