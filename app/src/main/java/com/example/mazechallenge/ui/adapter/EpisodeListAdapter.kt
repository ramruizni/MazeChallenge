package com.example.mazechallenge.ui.adapter

import android.content.Context
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.RequestManager
import com.example.mazechallenge.R
import com.example.mazechallenge.databinding.ItemEpisodeHeaderListBinding
import com.example.mazechallenge.databinding.ItemEpisodeListBinding
import com.example.mazechallenge.model.Episode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class EpisodeListAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val glide: RequestManager,
) : BaseExpandableListAdapter() {

    var map: Map<Int, List<Episode>> = hashMapOf()

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return map[ArrayList(map.keys)[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        val binding = ItemEpisodeListBinding.inflate(inflater)

        val episode = getChild(listPosition, expandedListPosition) as Episode

        binding.tvEpisodeName.text = episode.name

        binding.tvEpisodeSummary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(episode.summary, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(episode.summary)
        }

        binding.tvEpisodeSummary.movementMethod = ScrollingMovementMethod()

        val loader = CircularProgressDrawable(context)
        loader.strokeWidth = 6f
        loader.centerRadius = 24f
        val loaderColor = Color.parseColor("#4F378B")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            loader.colorFilter = BlendModeColorFilter(loaderColor, BlendMode.SRC_ATOP)
        } else {
            loader.setColorFilter(loaderColor, PorterDuff.Mode.SRC_ATOP)
        }
        loader.start()

        episode.image?.original?.let {
            glide
                .load(episode.image.original)
                .placeholder(loader)
                .into(binding.ivEpisodeMedium)
        }

        val textColor = if (isDark()) R.color.text_dark else R.color.text_light
        binding.tvEpisodeName.setTextColor(ContextCompat.getColor(context, textColor))
        binding.tvEpisodeSummary.setTextColor(ContextCompat.getColor(context, textColor))

        return binding.root
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return map[ArrayList(map.keys)[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return ArrayList(map.keys)[listPosition]
    }

    override fun getGroupCount(): Int {
        return ArrayList(map.keys).size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        val binding = ItemEpisodeHeaderListBinding.inflate(inflater)
        val listTitle = "Season " + (getGroup(listPosition) as Int)
        binding.tvSeason.text = listTitle

        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                context,
                if (isDark()) R.color.app_bar_dark else R.color.app_bar_light
            )
        )
        binding.tvSeason.setTextColor(
            ContextCompat.getColor(
                context,
                if (isDark()) R.color.text_dark else R.color.text_light
            )
        )

        return binding.root
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    private fun isDark(): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
    }
}