package com.example.minisocialnetwork

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.minisocialnetwork.databinding.ActivityProfileBinding
import com.example.minisocialnetwork.utils.Constants
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_profile)
        val email = intent.getStringExtra(Constants.KEY_EMAIL)
        setValueBySaveTextKey(email, sharedPreferences.getString(Constants.KEY_LAST_FULL_NAME, ""))
        isLogout()
    }

    private fun setValueBySaveTextKey(email: String?, savedText: String?) {
        if (email != null) {
            parsingAndSetName(email)
            val editor = sharedPreferences.edit()
            editor.putString(Constants.KEY_LAST_FULL_NAME, email)
            editor.apply()
        } else {
            parsingAndSetName(savedText.toString())
        }
    }

    private fun parsingAndSetName(email: String) {
        val elements = email.split("@")[0].replace(".", " ").split(" ")
        val textView = findViewById<TextView>(R.id.textViewName)
        if (elements.size >= 2) {
            val name = elements[0].replaceFirstChar { it.titlecase(Locale.getDefault()) }
            val lastName = elements[1].replaceFirstChar { it.titlecase(Locale.getDefault()) }
            textView.text = "$name $lastName"
        } else {
            textView.text = elements[0]
        }
    }

    private fun isLogout() {
        val button = findViewById<Button>(R.id.buttonLogout)
        button.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.remove(Constants.KEY_LAST_FULL_NAME)
            editor.remove(Constants.KEY_REMEMBER_ME)
            editor.apply()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, R.anim.anim_slide_down)
            finish()
        }
    }
}