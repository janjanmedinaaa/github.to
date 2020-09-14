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
import com.medina.juanantonio.githubto.common.extensions.toNegative
import com.medina.juanantonio.githubto.data.model.User
import com.medina.juanantonio.githubto.databinding.ItemUserBinding
import com.medina.juanantonio.githubto.databinding.ItemUserLoadingBinding

class UserListAdapter(
    private val userItemListener: UserItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var userList: ArrayList<UserListItem> = arrayListOf()

    companion object {
        private const val USER_ITEM = 0
        private const val USER_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == USER_ITEM) {
            val binding: ItemUserBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user,
                parent,
                false
            )
            return UserViewHolder(binding)
        } else {
            val binding: ItemUserLoadingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user_loading,
                parent,
                false
            )
            return LoadingViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList[position] is UserLoadingItem) USER_LOADING else USER_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = userList[position]

        if (holder is UserViewHolder) {
            item as User
            holder.bind(item, position)
        }
    }

    fun refreshUser(user: User, position: Int) {
        userList[position] = user
        notifyItemChanged(position)
    }

    fun addUsers(newUserList: ArrayList<User>) {
        removeLoading()

        val currentListSize = userList.size
        userList.addAll(newUserList)
        notifyItemInserted(currentListSize)
    }

    fun addLoading() {
        userList.add(UserLoadingItem())
        notifyItemChanged(userList.lastIndex)
    }

    fun removeLoading() {
        if (userList.isNotEmpty() &&
            userList.last() is UserLoadingItem
        ) {
            userList.remove(userList.last())
            notifyItemRemoved(userList.size)
        }
    }

    inner class LoadingViewHolder(
        binding: ItemUserLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class UserViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User, position: Int) {
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
        }
    }

    interface UserItemListener {
        fun onItemClicked(item: User, position: Int)
    }
}

interface UserListItem

class UserLoadingItem : UserListItem