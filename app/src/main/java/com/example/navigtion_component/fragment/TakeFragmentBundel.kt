package com.example.navigtion_component.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.navigtion_component.R
import com.example.navigtion_component.databinding.FragmentTakeBundleBinding

class BlankFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mBinding: FragmentTakeBundleBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_take_bundle, container, false)
        val bundle = arguments
        val content = bundle?.getString("content")
        mBinding.tvTakeBundleContent.text = content
        return mBinding.root
    }

}