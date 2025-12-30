package com.margoslabs.messenger.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.margoslabs.messenger.R
import com.margoslabs.messenger.data.entity.MessageEntity
import com.margoslabs.messenger.data.preferences.AvatarPreferences
import com.margoslabs.messenger.databinding.ItemMessageBinding

class MessageAdapter(
    private val context: Context,
    private val onLikeClick: (Int) -> Unit
) : ListAdapter<MessageEntity, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {
    
    private val avatarPreferences = AvatarPreferences(context)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding, onLikeClick, avatarPreferences)
    }
    
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class MessageViewHolder(
        private val binding: ItemMessageBinding,
        private val onLikeClick: (Int) -> Unit,
        private val avatarPreferences: AvatarPreferences
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(message: MessageEntity) {
            binding.apply {
                tvMessageTitle.text = message.title
                tvMessageBody.text = message.body
                tvMessageUser.text = message.getDisplayName()
                
                // Получаем аватар из настроек или используем дефолтный
                val avatarType = avatarPreferences.getAvatar(message.userId)
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(Color.parseColor(avatarType.colorHex))
                }
                ivAvatar.background = drawable
                
                // Устанавливаем иконку лайка
                updateLikeIcon(message.isLiked)
                
                // Обработчик клика на лайк
                btnLike.setOnClickListener {
                    onLikeClick(message.id)
                }
            }
        }
        
        private fun updateLikeIcon(isLiked: Boolean) {
            val context = binding.root.context
            if (isLiked) {
                binding.btnLike.setIconResource(R.drawable.ic_heart_filled)
                binding.btnLike.iconTint = ContextCompat.getColorStateList(context, R.color.liked_color)
            } else {
                binding.btnLike.setIconResource(R.drawable.ic_heart_outline)
                binding.btnLike.iconTint = ContextCompat.getColorStateList(context, android.R.color.darker_gray)
            }
        }
    }
    
    private class MessageDiffCallback : DiffUtil.ItemCallback<MessageEntity>() {
        override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem == newItem
        }
    }
}

