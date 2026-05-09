package com.example.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:3001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        loadUsers()
    }

    private fun loadUsers() {
        val request = Request.Builder()
            .url("$baseUrl/users")
            .get()
            .build()

        thread {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: "[]"
                val jsonArray = JSONArray(responseBody)
                val users = mutableListOf<User>()

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    users.add(
                        User(
                            email = item.getString("email"),
                            course = item.getInt("course")
                        )
                    )
                }

                runOnUiThread {
                    binding.rvUsers.adapter = UserAdapter(users)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Ошибка загрузки пользователей: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}