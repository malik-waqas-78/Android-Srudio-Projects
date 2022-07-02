package com.photo.recovery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.photo.recovery.activites.ShowImageAAT
import com.photo.recovery.adapters.GeneralMediaAdapterAAT
import com.photo.recovery.databinding.ImageFargmentAatBinding


class ImageFragmentAAT(var position:Int, var visibility:Int): Fragment(){

    private var _binding: ImageFargmentAatBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=ImageFargmentAatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.applicationContext?.let {
            binding.viewIamge.visibility=View.VISIBLE
            Glide.with(it).load(GeneralMediaAdapterAAT.images[position].filePath).into(binding.viewIamge)
        }
    }



    override fun onResume() {
        super.onResume()
        (activity as ShowImageAAT)?.changeName(GeneralMediaAdapterAAT.images[position].fileName)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}