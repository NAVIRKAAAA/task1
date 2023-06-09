package com.example.minisocialnetwork

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.minisocialnetwork.databinding.ActivityProfileBinding
import com.example.minisocialnetwork.utils.Constants

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
        binding.textViewName.text = if (elements.size >= 2) {
            val name = elements[0].replaceFirstChar { it.uppercase() }
            val lastName = elements[1].replaceFirstChar { it.titlecase() }
            val fullName = "$name $lastName"
            fullName
        } else {
            elements[0]
        }
    }

    private fun isLogout() {
        binding.buttonLogout.setOnClickListener {
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