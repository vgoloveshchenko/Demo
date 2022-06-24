package com.example.demo.presentation.ui.users.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.ItemErrorBinding
import com.example.demo.databinding.ItemLoadingBinding
import com.example.demo.databinding.ItemUserBinding
import com.example.demo.domain.model.users.User
import com.example.demo.presentation.model.PagingDisplayItem

class UserAdapter(
    context: Context,
    private val onUserClicked: (User) -> Unit,
    private val onRetryClicked: () -> Unit
) : ListAdapter<PagingDisplayItem<User>, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingDisplayItem.Content -> TYPE_USER
            is PagingDisplayItem.Error -> TYPE_ERROR
            PagingDisplayItem.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_USER -> {
                UserViewHolder(
                    binding = ItemUserBinding.inflate(layoutInflater, parent, false),
                    onUserClicked = onUserClicked
                )
            }
            TYPE_ERROR -> {
                ErrorViewHolder(
                    binding = ItemErrorBinding.inflate(layoutInflater, parent, false),
                    onRetryClicked = onRetryClicked
                )
            }
            TYPE_LOADING -> {
                LoadingViewHolder(
                    binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> error("Unsupported viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = (getItem(position) as? PagingDisplayItem.Content)?.data ?: return
        (holder as? UserViewHolder)?.bind(user)
    }

    companion object {

        private const val TYPE_USER = 1
        private const val TYPE_ERROR = 2
        private const val TYPE_LOADING = 3

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PagingDisplayItem<User>>() {
            override fun areItemsTheSame(
                oldItem: PagingDisplayItem<User>,
                newItem: PagingDisplayItem<User>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PagingDisplayItem<User>,
                newItem: PagingDisplayItem<User>
            ): Boolean {
                val oldUser = oldItem as? PagingDisplayItem.Content
                val newUser = newItem as? PagingDisplayItem.Content
                return oldUser?.data == newUser?.data
            }
        }
    }
}