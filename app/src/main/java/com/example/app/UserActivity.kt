package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email") ?: ""
        val course = intent.getIntExtra("course", 1)

        binding.tvEmail.text = email
        binding.tvCourse.text = "Курс: $course"

        val image = when (course) {
            1 -> R.drawable.course1
            2 -> R.drawable.course2
            3 -> R.drawable.course3
            4 -> R.drawable.course4
            else -> R.mipmap.ic_launcher
        }

        binding.ivCourseImage.setImageResource(image)
        binding.ivBackground.setImageResource(image)
    }
}