package com.example.minisocialnetwork

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.minisocialnetwork.databinding.ActivitySignUpBinding
import com.example.minisocialnetwork.utils.Constants

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        isRememberMe()
        setContentView(binding.root)
        setListeners()
        dataValidation()
    }

    private fun isRememberMe() {
        if (sharedPreferences.getString(Constants.KEY_REMEMBER_ME, "") != "") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setListeners() {
        binding.buttonRegister.setOnClickListener {
            with(binding) {
                if (isValidEmail(textInputEditTextEmail.text.toString()) &&
                    isValidPassword(textInputEditTextPassword.text.toString())
                ) {
                    if (checkboxRemember.isChecked) saveData()
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    intent.putExtra(
                        Constants.KEY_EMAIL,
                        textInputEditTextEmail.text.toString()
                    )
                    startActivity(intent)
                    overridePendingTransition(R.anim.anim_slide_up, 0)
                    finish()
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 7 && password.contains(Regex("[A-Z]")) &&
                password.contains(Regex("[a-z]")) &&
                password.contains(Regex("\\d")) && !password.contains(' ')
    }

    private fun saveData() {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.KEY_REMEMBER_ME, binding.textInputEditTextEmail.text.toString())
        editor.apply()
    }

    private fun dataValidation() {
        binding.textInputEditTextEmail.doOnTextChanged { text, _, _, _ ->
            binding.textViewInvalidEmail.visibility =
                if (isValidEmail(text.toString())) View.INVISIBLE else View.VISIBLE
        }
        binding.textInputEditTextPassword.doOnTextChanged { text, _, _, _ ->
            binding.textViewInvalidPassword.visibility =
                if (isValidPassword(text.toString())) View.INVISIBLE else View.VISIBLE
        }
    }
}
