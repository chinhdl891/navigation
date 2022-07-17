package com.example.navigtion_component.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigtion_component.R
import com.example.navigtion_component.adapter.NewsAdapter
import com.example.navigtion_component.database.NewDataBase
import com.example.navigtion_component.databinding.FragmentSendDataBinding
import com.example.navigtion_component.model.News
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SendDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listNews = mutableListOf<News>()
        val mBinding: FragmentSendDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_send_data, container, false)
        mBinding.rcvSendContent.layoutManager = LinearLayoutManager(activity)
        if (activity?.let { NewDataBase.getDatabase(it).newDAO().count() } == 0) {
            val databaseReference =
                Firebase.database.getReferenceFromUrl("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app/News")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        postSnapshot.getValue(News::class.java)
                            ?.let {
                                activity?.let { it1 ->
                                    listNews.add(it)
                                    NewDataBase.getDatabase(it1).newDAO().onInsert(it)
                                }
                            }
                        val newsAdapter = context?.let {
                            NewsAdapter(listNews, it, senData = {
                                val bundle = bundleOf(
                                    "id" to it
                                )
                            if (checkNetWork()){
                                findNavController().navigate(R.id.fragmentGotoDetail, bundle)
                            } else {
                                findNavController().navigate(R.id.detailFragment, bundle)
                            }
                            })
                        }
                        mBinding.rcvSendContent.adapter = newsAdapter
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        } else {
            listNews.also {
                activity?.let { it1 -> NewDataBase.getDatabase(it1).newDAO().getList() }
                    ?.let { it2 -> it.addAll(it2) }
            }
            val newsAdapter = context?.let {
                NewsAdapter(listNews, it, senData = {
                    val bundle = bundleOf(
                        "id" to it
                    )
                    if (checkNetWork()){
                        findNavController().navigate(R.id.fragmentGotoDetail, bundle)
                    } else {
                        findNavController().navigate(R.id.detailFragment, bundle)
                    }
                })
            }
            mBinding.rcvSendContent.adapter = newsAdapter
        }
        val controller = findNavController()
        mBinding.btnSendBundle.setOnClickListener {
            val content = mBinding.edtSendData.text.toString().trim()
            val bundle = bundleOf(
                "content" to content
            )
            controller.navigate(R.id.takeFragmentBundle, bundle)
        }
        mBinding.btnSendArgs.setOnClickListener {
            val content = mBinding.edtSendData.text.toString().trim()
            val control =
                SendDataFragmentDirections.actionSendDataFragmentToTakeFragmentSafeArgs(content)
            controller.navigate(control)
        }
        return mBinding.root
    }

   private fun checkNetWork(): Boolean {
        val connectivityManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val connected =
            (return connectivityManager!!.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED)
    }
}


