package isel.ps.eduwikimobile.comms

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Base64
import isel.ps.eduwikimobile.R

class Session {
    companion object {
        val AUTH_TOKEN = "AUTH_TOKEN"
        val USERNAME = "USERNAME"
        val LOGGED_IN = "LOGGED_IN"
    }

    private fun getPreferences(ctx: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)

    fun encodeToString(username: String, password: String): String {
        val string = "$username:$password"
        val data = string.toByteArray()
        return Base64.encodeToString(data, Base64.DEFAULT).split("\n")[0]
    }

    fun setLogin(ctx: Context, username: String, password: String) {
        val editor: SharedPreferences.Editor = getPreferences(ctx).edit()
        val token = encodeToString(username, password)
        editor.putString(AUTH_TOKEN, token)
        editor.putString(USERNAME, username)
        editor.putBoolean(LOGGED_IN, true)
        editor.apply() //Asynchronously save in SharedPreferences
    }

    fun getAuthToken(ctx: Context): String {
        val sharePref = getPreferences(ctx)
        return sharePref.getString(AUTH_TOKEN, null)
    }

    fun getAuthUsername(ctx: Context): String {
        val sharePref = getPreferences(ctx)
        return sharePref.getString(USERNAME, null)
    }

    fun isLoggedIn(ctx: Context): Boolean {
        val sharePref = getPreferences(ctx)
        return sharePref.getBoolean(LOGGED_IN, false)
    }

    fun setLogout(ctx: Context) {
        getPreferences(ctx).edit().clear().apply()
    }

}