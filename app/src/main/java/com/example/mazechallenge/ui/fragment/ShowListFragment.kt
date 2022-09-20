package com.example.mazechallenge.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazechallenge.R
import com.example.mazechallenge.common.*
import com.example.mazechallenge.databinding.FragmentShowListBinding
import com.example.mazechallenge.ui.adapter.ShowListAdapter
import com.example.mazechallenge.ui.viewmodel.ShowListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowListFragment : Fragment(R.layout.fragment_show_list) {
    private lateinit var binding: FragmentShowListBinding
    private lateinit var showListViewModel: ShowListViewModel

    @Inject
    lateinit var showListAdapter: ShowListAdapter

    private var canReloadList = true
    private var searchText = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowListBinding.bind(view)
        showListViewModel = ViewModelProvider(requireActivity())[ShowListViewModel::class.java]

        val adapterLayoutManager = GridLayoutManager(requireContext(), 3)

        binding.rvList.apply {
            adapter = showListAdapter
            layoutManager = adapterLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (searchText.isNotEmpty()) return
                    if (dy > 0) {
                        val pastItems = adapterLayoutManager.findFirstVisibleItemPosition()
                        if (canReloadList && (adapterLayoutManager.childCount + pastItems) >= adapterLayoutManager.itemCount - 200) {
                            canReloadList = false
                            showListViewModel.fetchNextPage()
                        }
                    }
                }

            })
        }

        binding.svName.addTextChangedListener(TextWatcherDelayed(
            onTextChanged = {
                searchText = it.toString()
            },
            afterTextChanged = {
                if (searchText.isEmpty()) {
                    showListViewModel.fetchFirstPageIfEmpty()
                } else {
                    showListViewModel.fetchListByName(searchText)
                }
            }
        ))

        binding.ivSearch.setOnClickListener {
            binding.svName.requestFocus()
            showKeyboard(binding.svName)
        }

        binding.ivClear.setOnClickListener {
            binding.svName.setText("")
            binding.svName.clearFocus()
            hideKeyboard()
        }


        collectLifecycleFlow(showListViewModel.showList) {
            binding.tvListEmpty.visibility = View.GONE
            if (searchText.isNotEmpty() && it.status == Status.LOADING) onLoading(true)
            when (it.status) {
                Status.ERROR -> {
                    binding.tvListEmpty.visibility = View.GONE
                    onError(it.message)
                    onLoading(false)
                }
                Status.SUCCESS -> {
                    it.data?.let { list ->
                        showListAdapter.list = list
                        canReloadList = true

                        binding.tvListEmpty.visibility =
                            if (list.isEmpty()) View.VISIBLE else View.GONE
                        onLoading(false)
                    }
                }
                else -> {}
            }
        }

        showListAdapter.onItemClick = { show ->
            findNavController().navigate(
                ShowListFragmentDirections.actionShowListFragmentToShowDetailFragment(show)
            )
        }

        if (searchText.isEmpty()) {
            showListViewModel.fetchFirstPageIfEmpty()
        }

        binding.fabFavorites.setOnClickListener {
            findNavController().navigate(
                ShowListFragmentDirections.actionShowListFragmentToFavoriteListFragment()
            )
        }
    }
}