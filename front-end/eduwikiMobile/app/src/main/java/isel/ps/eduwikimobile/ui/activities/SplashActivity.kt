package isel.ps.eduwikimobile.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.comms.Session
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    lateinit var intentToStartActivity: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar!!.hide()
        setContentView(R.layout.activity_splash)
        splash_progressBar.indeterminateDrawable.setColorFilter(
                Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
        Handler().postDelayed({
            if (Session().isLoggedIn(this))
                intentToStartActivity = Intent(this, MainActivity::class.java)
            else
                intentToStartActivity = Intent(this, LoginActivity::class.java)
            startActivity(intentToStartActivity)
        }, 2000)
    }
}