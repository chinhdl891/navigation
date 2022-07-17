package com.example.navigtion_component.adapter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.navigtion_component.model.News
import com.example.navigtion_component.R
import com.example.navigtion_component.Utils
import java.text.SimpleDateFormat


class NewsAdapter(var newsList: List<News>, context: Context, var senData : (Long) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.ViewHolderNews>() {
    var context: Context
    var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("E, dd MMM yyyy")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNews {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolderNews(view)
    }

    override fun onBindViewHolder(holder: ViewHolderNews, position: Int) {
        val news = newsList[position]
        holder.tvTitle.text = news.title
        holder.tvAuthor.text = "Đăng bởi: " + news.upLoadBy
        val date = news.date?.toLong()
        holder.tvDate.setText(simpleDateFormat.format(date).toString() + "")
        val image = news.image
        Thread {
            val bitmap: Bitmap? = Utils.decode(image)
            holder.imgNews.post(Runnable { holder.imgNews.setImageBitmap(bitmap) })
        }.start()
        holder.itemView.setOnClickListener {
           senData(news.id.toLong())
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class ViewHolderNews(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDate: TextView
        var tvTitle: TextView
        var tvAuthor: TextView
        var imgNews: ImageView

        init {
            tvDate = itemView.findViewById(R.id.txtDate)
            imgNews = itemView.findViewById(R.id.imgNews)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvAuthor = itemView.findViewById(R.id.txtTacGia)
        }
    }

    init {
        this.context = context
    }
    val CHANNEL_ID = "hello"
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

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