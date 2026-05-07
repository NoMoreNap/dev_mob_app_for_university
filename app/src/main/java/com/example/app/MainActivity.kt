package com.example.app

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private var isAboutVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bgImage = findViewById<ImageView>(R.id.bgImage)
        val button = findViewById<Button>(R.id.button)
        val overlay = findViewById<View>(R.id.blurOverlay)
        val modal = findViewById<LinearLayout>(R.id.aboutModal)

        Glide.with(this)
            .load("https://s3.slaves.pro/misc/lab_background.png")
            .into(bgImage)

        button.setOnClickListener {
            isAboutVisible = !isAboutVisible

            if (isAboutVisible) {
                overlay.visibility = View.VISIBLE
                modal.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    bgImage.setRenderEffect(
                        RenderEffect.createBlurEffect(18f, 18f, Shader.TileMode.CLAMP)
                    )
                }
            } else {
                overlay.visibility = View.GONE
                modal.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    bgImage.setRenderEffect(null)
                }
            }
        }
    }
}