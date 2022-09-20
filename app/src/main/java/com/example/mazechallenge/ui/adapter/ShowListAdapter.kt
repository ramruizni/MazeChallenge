package com.example.mazechallenge.ui.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.RequestManager
import com.example.mazechallenge.databinding.ItemShowListBinding
import com.example.mazechallenge.model.Show
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShowListAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<ShowListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemShowListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Show) {
            binding.tvName.text = item.name

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

            item.image?.medium?.let {
                glide
                    .load(item.image.medium)
                    .placeholder(loader)
                    .into(binding.ivPortrait)
                binding.root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }

        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Show>() {
        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
            //return oldItem.hashCode() == newItem.hashCode()
            return oldItem.id == newItem.id
        }
    }

    private var differ = AsyncListDiffer(this, diffCallback)

    var list: List<Show>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    var onItemClick: ((Show) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemShowListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            holder.bindItem(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}