package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityRegisterBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.concurrent.thread

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:3001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLoginLink.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmailRegister.text.toString().trim()
            val password = binding.etPasswordRegister.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val course = binding.etCourse.text.toString()

            if (email.isEmpty()) {
                binding.etEmailRegister.error = "Введите email"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.etPasswordRegister.error = "Минимум 6 символов"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Пароли не совпадают"
                return@setOnClickListener
            }

            if (course.isEmpty()) {
                binding.etCourse.error = "Введите курс"
                return@setOnClickListener
            }

            register(email, password, course.toInt())
        }
    }

    private fun register(email: String, password: String, course: Int) {
        val json = JSONObject()

        json.put("email", email)
        json.put("password", password)
        json.put("course", course)

        val body = json.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$baseUrl/auth/register")
            .post(body)
            .build()

        thread {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""

                runOnUiThread {
                    if (responseBody.trim().startsWith("[")) {
                        Toast.makeText(
                            this,
                            "Регистрация успешна",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    } else {
                        val error = JSONObject(responseBody)

                        Toast.makeText(
                            this,
                            error.optString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Ошибка: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}