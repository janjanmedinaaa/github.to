package com.medina.juanantonio.githubto.features

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.medina.juanantonio.githubto.R
import com.medina.juanantonio.githubto.common.extensions.convertDpToPixel
import com.medina.juanantonio.githubto.common.extensions.setMargin
import com.medina.juanantonio.githubto.common.extensions.toNegative
import com.medina.juanantonio.githubto.data.model.User
import com.medina.juanantonio.githubto.databinding.ItemUserBinding

class UserListAdapter(
    private val userItemListener: UserItemListener
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var userList: ArrayList<User> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUserBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_user,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position], position)
    }

    fun refreshUser(user: User, position: Int) {
        userList[position] = user
        notifyItemChanged(position)
    }

    fun addUsers(newUserList: ArrayList<User>) {
        val currentListSize = this.userList.size
        this.userList.addAll(newUserList)
        notifyItemInserted(currentListSize)
    }

    inner class ViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User, position: Int) {
            val context = binding.root.context
            binding.textviewUserUsername.text = item.login
            binding.textviewUserDetails.text = item.html_url
            binding.imageviewNote.visibility =
                if (item.note.isNotEmpty()) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                userItemListener.onItemClicked(item, position)
            }

            Glide
                .with(binding.root.context)
                .load(item.avatar_url)
                .circleCrop()
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val currentCount = position + 1
                        if (currentCount != 1 && currentCount % 4 == 0)
                            resource?.toNegative()

                        return false
                    }
                })
                .into(binding.imageviewUserProfile)

            if (position == userList.size - 1)
                binding.root.setMargin(
                    marginBottom = context.convertDpToPixel(16f).toInt()
                )
        }
    }

    interface UserItemListener {
        fun onItemClicked(item: User, position: Int)
    }
}