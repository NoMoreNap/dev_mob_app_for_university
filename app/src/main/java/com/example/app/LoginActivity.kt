package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityLoginBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:3001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString()

            if (email.isEmpty()) {
                binding.etEmailLogin.error = "Введите email"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPasswordLogin.error = "Введите пароль"
                return@setOnClickListener
            }

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$baseUrl/auth/login")
            .post(body)
            .build()

        thread {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""

                runOnUiThread {
                    if (responseBody.trim().startsWith("[")) {
                        val users = JSONArray(responseBody)

                        if (users.length() > 0) {
                            Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val error = JSONObject(responseBody)
                        Toast.makeText(this, error.optString("message", "Ошибка входа"), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Ошибка соединения: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}