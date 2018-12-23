package com.example.mikle.insurancesysten.login

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mikle.insurancesysten.R
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mikle.insurancesysten.admin.AdminActivity
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.entity.User
import com.example.mikle.insurancesysten.preferences.IAPreferences
import com.example.mikle.insurancesysten.registration.RegistrationActivity
import com.example.mikle.insurancesysten.user.UserActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    private var viewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val id = IAPreferences.getInt(IAPreferences.IS_LOGGED)
        viewModel!!.backgroundLivaData.observe(this, Observer { onBackground() })
        if (id != IAPreferences.IS_LOGGED.default) {
            viewModel!!.userById(id).observe(this, Observer { user: User? ->
                tryOpenActivity(user)
            })
            return
        }
    }

    private fun tryOpenActivity(user: User?) {
        if (user != null) {
            openActivity(user)
        } else {
            Toast.makeText(this, "Ошибка входа!", Toast.LENGTH_SHORT).show()
        }
    }

    protected fun onBackground() {
        val value = viewModel!!.backgroundLivaData.value!!
        when (value.status) {
            BackgroundResult.Status.LOADING -> {
                beginLoading()
            }
            BackgroundResult.Status.ERROR -> {
                stopLoading()
                Toast.makeText(this, value.error, Toast.LENGTH_SHORT).show()
            }
            BackgroundResult.Status.SUCCESS -> {
                stopLoading()
                openActivity(viewModel!!.user!!)
            }
        }
    }

    private fun openActivity(user: User) {
        intent = Intent(this, UserActivity::class.java)
        intent.putExtra("USER", user)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        when (user.roleId) {
            1 -> {
                intent = Intent(this, AdminActivity::class.java)
            }
        }
        startActivity(intent)
        finish()
    }

    fun login(view: View) {
        viewModel!!.login(email.text.toString(), password.text.toString())
    }

    fun registration(view: View) {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }

    override fun beginLoading() {
    }

    override fun stopLoading() {
    }


}
