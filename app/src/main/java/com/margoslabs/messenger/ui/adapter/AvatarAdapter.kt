package com.margoslabs.messenger.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.margoslabs.messenger.data.preferences.AvatarType
import com.margoslabs.messenger.databinding.ItemAvatarBinding

class AvatarAdapter(
    private val onAvatarClick: (AvatarType) -> Unit
) : ListAdapter<AvatarType, AvatarAdapter.AvatarViewHolder>(AvatarDiffCallback()) {
    
    private var selectedAvatar: AvatarType? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AvatarViewHolder(binding, onAvatarClick) { selectedType ->
            val previousSelected = selectedAvatar
            selectedAvatar = selectedType
            if (previousSelected != null) {
                notifyItemChanged(currentList.indexOf(previousSelected))
            }
            notifyItemChanged(currentList.indexOf(selectedType))
        }
    }
    
    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bind(getItem(position), getItem(position) == selectedAvatar)
    }
    
    fun setSelectedAvatar(avatarType: AvatarType) {
        val previousSelected = selectedAvatar
        selectedAvatar = avatarType
        if (previousSelected != null) {
            val previousIndex = currentList.indexOf(previousSelected)
            if (previousIndex >= 0) {
                notifyItemChanged(previousIndex)
            }
        }
        val newIndex = currentList.indexOf(avatarType)
        if (newIndex >= 0) {
            notifyItemChanged(newIndex)
        }
    }
    
    class AvatarViewHolder(
        private val binding: ItemAvatarBinding,
        private val onAvatarClick: (AvatarType) -> Unit,
        private val onSelectionChanged: (AvatarType) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(avatarType: AvatarType, isSelected: Boolean) {
            val drawable = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(Color.parseColor(avatarType.colorHex))
            }
            binding.ivAvatarPreview.background = drawable
            
            // Обновляем обводку в зависимости от выбора
            binding.root.strokeWidth = if (isSelected) 4 else 2
            binding.root.strokeColor = if (isSelected) {
                android.graphics.Color.parseColor("#FF6200EE") // Primary color
            } else {
                android.graphics.Color.parseColor("#E0E0E0") // Gray
            }
            
            binding.root.setOnClickListener {
                onSelectionChanged(avatarType)
                onAvatarClick(avatarType)
            }
        }
    }
    
    private class AvatarDiffCallback : DiffUtil.ItemCallback<AvatarType>() {
        override fun areItemsTheSame(oldItem: AvatarType, newItem: AvatarType): Boolean {
            return oldItem == newItem
        }
        
        override fun areContentsTheSame(oldItem: AvatarType, newItem: AvatarType): Boolean {
            return oldItem == newItem
        }
    }
}

