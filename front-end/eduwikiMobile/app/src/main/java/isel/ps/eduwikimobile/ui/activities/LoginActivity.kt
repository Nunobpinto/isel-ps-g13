package isel.ps.eduwikimobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.comms.Session
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.paramsContainer.LoginParametersContainer
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var app: EduWikiApplication
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = this.applicationContext as EduWikiApplication
        session = Session()
        btn_login.setOnClickListener { login() }
    }

    private fun login() {
        btn_login.isEnabled = false
        val username = input_username.text.toString()
        val password = input_password.text.toString()
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all the authentication fields", Toast.LENGTH_LONG).show()
        } else {
            app.controller.actionHandler(
                    AppController.AUTH_USER,
                    LoginParametersContainer(
                            username = username,
                            password = password,
                            app = app,
                            successCb = { _ ->
                                session.setLogin(this, username, password)
                                val intent = Intent(baseContext, MainActivity::class.java)
                                startActivity(intent)
                            },
                            errorCb = { error ->
                                if (error.exception is TimeoutError) {
                                    Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                                } else {
                                    btn_login.isEnabled = true
                                    Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                                }
                            }
                    )
            )
        }
    }
}