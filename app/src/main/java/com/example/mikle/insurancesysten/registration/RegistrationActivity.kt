package com.example.mikle.insurancesysten.registration

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.User

class RegistrationActivity : BaseActivity() {


    private var viewModel: RegistrationViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        viewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)
            //Thread{viewModel!!.userDao.insert(User(0, "admin", "admin", "admin", 1))}.start()
        viewModel!!.backgroundLivaData.observe(this, Observer { onBackground() })
        findViewById<Button>(R.id.registrationButton).setOnClickListener{
            viewModel!!.register(
                findViewById<EditText>(R.id.name).text.toString(),
                findViewById<EditText>(R.id.surname).text.toString(),
                findViewById<EditText>(R.id.fatherName).text.toString(),
                findViewById<EditText>(R.id.email).text.toString(),
                findViewById<EditText>(R.id.password).text.toString(),
                findViewById<EditText>(R.id.repeatPassword).text.toString()
            )
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
                Toast.makeText(this, value.success, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun beginLoading() {
    }

    override fun stopLoading() {
    }
}
