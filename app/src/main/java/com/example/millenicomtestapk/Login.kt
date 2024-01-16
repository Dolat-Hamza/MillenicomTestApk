package com.example.millenicomtestapk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    var usernameText : String = "admin"
    var passwordText : String = "admin"
    @RequiresApi(34)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val username: EditText = findViewById(R.id.username)
        val password: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login)


        loginButton.setOnClickListener {
            usernameText = username.text.toString()
            passwordText = password.text.toString()

            if (usernameText == "admin" && passwordText == "admin") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
