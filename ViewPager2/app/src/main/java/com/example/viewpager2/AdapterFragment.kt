package com.example.viewpager2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterFragment (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment :Fragment=Fragment()
        when(position){
            0->{
                fragment=Fone()

            }
            1->{
                fragment=Ftwo()
            }
            2->{
                fragment=fThree()
            }
        }
        return  fragment;
    }
}