package com.example.navigtion_component.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.navigtion_component.model.News
import com.example.navigtion_component.R
import com.example.navigtion_component.Utils
import com.example.navigtion_component.database.NewDataBase
import com.example.navigtion_component.databinding.FragmentDetailBinding
import java.text.SimpleDateFormat


class DetailFragment : Fragment() {
    var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("E, dd MMM yyyy")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mBinding: FragmentDetailBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_detail, container, false)
        val bundle = arguments
        val id = bundle?.get("id")
        val news: News =
            activity?.let { NewDataBase.getDatabase(it).newDAO().getListId(id.toString()) }?.get(0)
                ?: News()

        val bitmap = Utils.decode(news.image)
        mBinding.imageNewsDetail.setImageBitmap(bitmap)
        val day = news.date!!.toLong()
        mBinding.tvDate.append(
            Html.fromHtml(
                "" + simpleDateFormat.format(day) + "",
                Html.FROM_HTML_MODE_COMPACT
            )
        )
        mBinding.tvContent.text = Html.fromHtml(
            "<p>&ensp;" + news.content + "</p>",
            Html.FROM_HTML_MODE_COMPACT
        )
        mBinding.tvAuthor.append(news.upLoadBy)
        mBinding.tvTitle.text = Html.fromHtml(
            "<h4>&ensp;" + news.title + "</h4>",
            Html.FROM_HTML_MODE_COMPACT
        )
        return mBinding.root
    }


}