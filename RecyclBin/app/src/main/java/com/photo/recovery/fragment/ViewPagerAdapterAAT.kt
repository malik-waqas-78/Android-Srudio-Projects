package com.photo.recovery.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.photo.recovery.adapters.GeneralMediaAdapterAAT

class ViewPagerAdapterAAT(fragment:Fragment) : FragmentStateAdapter(fragment){

    var visibility= View.VISIBLE

    override fun getItemCount(): Int {
        return GeneralMediaAdapterAAT.images.size
    }

    override fun createFragment(position: Int): Fragment {
       return ImageFragmentAAT(position,visibility)
        }


}