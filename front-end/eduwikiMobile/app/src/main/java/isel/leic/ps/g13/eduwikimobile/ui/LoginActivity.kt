package isel.leic.ps.g13.eduwikimobile.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import isel.leic.ps.g13.eduwikimobile.R
import isel.leic.ps.g13.eduwikimobile.comms.HttpRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "Login Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener { login() }
    }

    private fun login () {
        val auth = ""
        /*val request = HttpRequest(
                urlPath = "http://10.0.2.2/user"
        )*/
    }
}
