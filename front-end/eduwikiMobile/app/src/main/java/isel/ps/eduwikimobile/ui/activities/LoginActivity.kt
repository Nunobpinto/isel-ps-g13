package isel.ps.eduwikimobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import isel.ps.eduwikimobile.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener { login() }
    }

    private fun login() {
        val intent: Intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
    }
}