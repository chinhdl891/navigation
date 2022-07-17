package com.example.navigtion_component.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.navigtion_component.R
import com.example.navigtion_component.databinding.FragmentTakeSafeArgsBinding


class TakeFragmentSafeArgs : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mBinding: FragmentTakeSafeArgsBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_take_safe_args,
                container,
                false
            )
        val bundle : TakeFragmentSafeArgsArgs by navArgs()
        val content = bundle.sendContent
        mBinding.tvTakeArgsContent.text = content
        return mBinding.root
    }

}