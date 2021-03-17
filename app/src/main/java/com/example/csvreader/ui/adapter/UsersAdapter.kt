package com.example.csvreader.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.csvreader.domain.model.UserModel

class UsersAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var users = listOf<UserModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserItemViewHolder(UserItemView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserItemViewHolder) {
            holder.bindItem(users[position])
        }
    }

    override fun getItemCount(): Int = users.size

    inner class UserItemViewHolder(private val userItemView: UserItemView) : RecyclerView.ViewHolder(userItemView) {
        fun bindItem(userModel: UserModel) {
            userItemView.userModel = userModel
        }
    }
}
