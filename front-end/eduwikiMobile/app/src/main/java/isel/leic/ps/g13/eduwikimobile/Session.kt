package isel.leic.ps.g13.eduwikimobile

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class Session {
    companion object {
        fun getPreferences(ctx: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
        val LOGGED_IN = "LOGGED_IN"
        val USERNAME = "USERNAME"
        val PASSWORD = "PASSWORD"
    }

    fun encodeToString(string: String): String {
        var data = ByteArray(0)

        try {
            data = string.toByteArray(Charset.defaultCharset())

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } finally {

            return Base64.encodeToString(data, Base64.DEFAULT)

        }
    }

    fun setLogin(ctx: Context, username: String, password: String) {
        val editor: SharedPreferences.Editor = getPreferences(ctx).edit()
        editor.putString(USERNAME, username)
        editor.putString(PASSWORD, password)
        editor.putBoolean(LOGGED_IN, true)
        editor.apply() //Asynchronously save in SharedPreferences
    }

    private fun isLoggedIn(ctx: Context, sharedPreferences: SharedPreferences)
            = sharedPreferences.getBoolean(LOGGED_IN, false)

    fun getUserLogged(ctx: Context): User? {
        val sharePref = getPreferences(ctx)
        if(isLoggedIn(ctx, sharePref)) {
            return User(
                    username = sharePref.getString(USERNAME, null),
                    password = sharePref.getString(PASSWORD, null)
            )
        }
        return null
    }
}