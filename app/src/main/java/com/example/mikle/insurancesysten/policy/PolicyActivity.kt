package com.example.mikle.insurancesysten.policy

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.InsurancePolicy
import com.example.mikle.insurancesysten.entity.Payment
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyActivity : BaseActivity() {


    private var viewModel: PolicyViewModel? = null
    private var policy: InsurancePolicy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)
        policy = intent.getSerializableExtra("Policy") as InsurancePolicy
        paymentRecycler.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProviders.of(this).get(PolicyViewModel::class.java)
        viewModel!!.backgroundLivaData.observe(this, Observer { onBackground() })
        viewModel!!.dialogLiveData.observe(this, Observer { onDialogChanged() })
        val adapter = viewModel!!.paymentAdapter(policy!!.id)
        adapter.setOwner(this)
        paymentRecycler.adapter = adapter
        initView()

    }

    private fun initView() {
        category.setText(policy!!.categoryName)
        date.setText(policy!!.date)
        viewModel!!.insertClientName(clientName, intent.getBooleanExtra("IsIndividual", true), policy!!)
        viewModel!!.insertUserName(userName, policy!!)
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
            }
        }
    }

    fun onPaymentAdd(view: View){
        viewModel!!.createAddPaymentDialog(this, policy!!)
    }

    private fun onDialogChanged() {
        viewModel!!.dialogLiveData.value!!.show()
    }


    override fun beginLoading() {
        //progress.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        //progress.visibility = View.GONE
    }
}
