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
import com.example.navigtion_component.R
import com.example.navigtion_component.databinding.FragmentGotoDetailBinding


class FragmentGotoDetail : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val mBinding: FragmentGotoDetailBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_goto_detail, container, false)
        val bundle = arguments
        val id = bundle?.get("id")
        mBinding.tvGotoDetailContent.text = getString(R.string.txt_tran_to_detail, id.toString())
        mBinding.btnGotoTran.setOnClickListener {
            pushNotification(id as Long)
        }
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