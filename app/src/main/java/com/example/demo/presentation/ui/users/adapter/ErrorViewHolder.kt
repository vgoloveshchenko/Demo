package com.example.demo.presentation.ui.users.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.ItemErrorBinding

class ErrorViewHolder(
    binding: ItemErrorBinding,
    onRetryClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.button.setOnClickListener { onRetryClicked() }
    }
}