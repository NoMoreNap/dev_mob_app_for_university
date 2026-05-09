package com.example.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemUserBinding

class UserAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvEmail.text = user.email
            binding.tvCourse.text = "Курс: ${user.course}"

            val image = when (user.course) {
                1 -> R.drawable.course1
                2 -> R.drawable.course2
                3 -> R.drawable.course3
                4 -> R.drawable.course4
                else -> R.mipmap.ic_launcher
            }

            binding.ivCourse.setImageResource(image)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, UserActivity::class.java)

                intent.putExtra("email", user.email)
                intent.putExtra("course", user.course)

                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size
}