package com.example.navigtion_component.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigtion_component.model.News
import com.example.navigtion_component.adapter.NewsAdapter
import com.example.navigtion_component.R
import com.example.navigtion_component.database.NewDataBase
import com.example.navigtion_component.databinding.FragmentSendDataBinding
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
                                pushNotification(it)
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
                    pushNotification(it)
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

        mBinding.rcvSendContent.layoutManager = LinearLayoutManager(activity)
        return mBinding.root
    }

    private fun pushNotification(id: Long) {
        val pendingIntent = findNavController().createDeepLink().apply {
            setGraph(R.navigation.nav_graph)
            setDestination(R.id.detailFragment)
            setArguments(bundleOf().apply {
                putLong("id", id)
            })
        }.createPendingIntent()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val builder = activity?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line...")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        }
        with(NotificationManagerCompat.from(requireContext())) {
            notify(123, builder!!.build())
        }

    }

    val CHANNEL_ID = "hello"
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}


