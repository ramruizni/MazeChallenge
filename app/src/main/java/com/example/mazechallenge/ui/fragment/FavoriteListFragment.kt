package com.example.mazechallenge.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mazechallenge.R
import com.example.mazechallenge.common.Status
import com.example.mazechallenge.common.collectLifecycleFlow
import com.example.mazechallenge.common.onError
import com.example.mazechallenge.common.onLoading
import com.example.mazechallenge.databinding.FragmentFavoriteListBinding
import com.example.mazechallenge.ui.adapter.ShowListAdapter
import com.example.mazechallenge.ui.viewmodel.FavoriteListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteListFragment : Fragment(R.layout.fragment_favorite_list) {
    private lateinit var binding: FragmentFavoriteListBinding
    private lateinit var favoriteListViewModel: FavoriteListViewModel

    @Inject
    lateinit var favoriteListAdapter: ShowListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteListBinding.bind(view)
        favoriteListViewModel =
            ViewModelProvider(requireActivity())[FavoriteListViewModel::class.java]

        val adapterLayoutManager = GridLayoutManager(requireContext(), 3)

        binding.rvFavorites.apply {
            adapter = favoriteListAdapter
            layoutManager = adapterLayoutManager
        }

        collectLifecycleFlow(favoriteListViewModel.favoriteList) {
            binding.tvFavoritesEmpty.visibility = View.VISIBLE
            onLoading(it.status == Status.LOADING)
            when (it.status) {
                Status.ERROR -> {
                    binding.tvFavoritesEmpty.visibility = View.GONE
                    onError(it.message)
                }
                Status.SUCCESS -> {
                    it.data?.let { list ->
                        binding.tvFavoritesEmpty.visibility =
                            if (list.isEmpty()) View.VISIBLE else View.GONE
                        favoriteListAdapter.list = list
                    }
                }
                else -> {}
            }
        }

        favoriteListAdapter.onItemClick = { show ->
            findNavController().navigate(
                FavoriteListFragmentDirections.actionFavoriteListFragmentToShowDetailFragment(show)
            )
        }

        favoriteListViewModel.fetchFavoriteList()
    }

}