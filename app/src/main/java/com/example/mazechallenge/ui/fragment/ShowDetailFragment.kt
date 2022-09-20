package com.example.mazechallenge.ui.fragment

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.RequestManager
import com.example.mazechallenge.R
import com.example.mazechallenge.common.collectLifecycleFlow
import com.example.mazechallenge.common.onInfo
import com.example.mazechallenge.databinding.FragmentShowDetailBinding
import com.example.mazechallenge.model.Show
import com.example.mazechallenge.ui.viewmodel.FavoriteListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ShowDetailFragment : Fragment(R.layout.fragment_show_detail) {
    private lateinit var binding: FragmentShowDetailBinding
    private lateinit var favoriteListViewModel: FavoriteListViewModel

    private val args: ShowDetailFragmentArgs by navArgs()

    @Inject
    lateinit var glide: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowDetailBinding.bind(view)
        favoriteListViewModel =
            ViewModelProvider(requireActivity())[FavoriteListViewModel::class.java]

        loadShowDetailsIntoView(args.show)

        // handle favorite
        updateFavoriteDrawable(args.show.isFavorite)
        binding.fabToggleFavorite.setOnClickListener {
            favoriteListViewModel.toggleShowAsFavorite(args.show)
        }
        collectLifecycleFlow(favoriteListViewModel.favoriteToggle) { isFavorite ->
            updateFavoriteDrawable(isFavorite)
            onInfo(if (isFavorite) "Added to favorites" else "Removed from favorites")
        }

        // view episodes
        binding.fabEpisodesAlt.setOnClickListener {
            findNavController().navigate(
                ShowDetailFragmentDirections.actionShowDetailFragmentToEpisodeListFragment(args.show)
            )
        }
    }

    private fun loadShowDetailsIntoView(show: Show) {
        val loader = CircularProgressDrawable(requireContext())
        loader.strokeWidth = 8f
        loader.centerRadius = 30f
        val loaderColor = Color.parseColor("#4F378B")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            loader.colorFilter = BlendModeColorFilter(loaderColor, BlendMode.SRC_ATOP)
        } else {
            loader.setColorFilter(loaderColor, PorterDuff.Mode.SRC_ATOP)
        }
        loader.start()

        show.image?.original?.let {
            glide
                .load(show.image.original)
                .placeholder(loader)
                .into(binding.ivOriginal)
        }
        binding.tvNameDetail.text = show.name
        if (show.genres.isNotEmpty()) {
            binding.tvGenre1.text = show.genres[0]
            binding.tvGenre1.visibility = View.VISIBLE
        } else {
            binding.tvGenre1.visibility = View.GONE
        }
        if (show.genres.size > 1) {
            binding.tvGenre2.text = show.genres[1]
            binding.tvGenre2.visibility = View.VISIBLE
        } else {
            binding.tvGenre2.visibility = View.GONE
        }

        binding.tvSummary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(show.summary, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(show.summary)
        }

        binding.root.transitionToEnd()

        show.schedule?.let { schedule ->
            schedule.days?.let { days ->
                var daysString = ""
                if (days.isNotEmpty()) {
                    daysString += "${days[0]}s"
                    if (days.size == 2) {
                        daysString += "s and ${days[1]}s"
                    } else if (days.size > 2) {
                        for (i in 1 until days.size - 1)
                            daysString += "s, ${days[i]}"
                        daysString += "s and ${days[days.size - 1]}s"
                    }
                }
                binding.tvSchedule.text = daysString
            }
            schedule.time?.let { binding.tvScheduleHour.text = it }
        }
    }

    private fun updateFavoriteDrawable(isFavorite: Boolean) {
        binding.fabToggleFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_unmarked
            )
        )
    }

}