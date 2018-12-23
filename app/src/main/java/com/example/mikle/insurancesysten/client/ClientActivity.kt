package com.example.mikle.insurancesysten.client

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.entity.IndividualClient
import com.example.mikle.insurancesysten.entity.LegalClient
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_client.*


class ClientActivity : BaseActivity() {

    companion object {
        var activity: BaseActivity? = null
    }

    private var viewModel: ClientViewModel? = null

    private var individualClient: IndividualClient? = null
    private var legalClient: LegalClient? = null

    @SuppressLint("RestrictedApi")
    private val mOnNavigationItemSelectedListener =  { item: Int ->
        when(item){
            R.id.navigation_client -> {
                inClientAdd.visibility = View.GONE
                recycler.visibility = View.GONE
                if (isIndividual()){
                    individualClientCard.visibility = View.VISIBLE
                    legalClientCard.visibility = View.GONE
                    initIndividualClientCard()
                } else {
                    legalClientCard.visibility = View.VISIBLE
                    individualClientCard.visibility = View.GONE
                    initLegalClientCard()
                }
            }
            else -> {
                recycler.visibility = View.VISIBLE
                inClientAdd.visibility = View.VISIBLE
                legalClientCard.visibility = View.GONE
                individualClientCard.visibility = View.GONE
            }
        }
        viewModel!!.onNavigationClicked(item, this)
        true
    }

    fun isIndividual() = intent.getBooleanExtra("isIndividual", true)

    private fun initIndividualClientCard() {
        individualClient = intent.getSerializableExtra("Client") as IndividualClient
        viewModel!!.individualClient = individualClient
        viewModel!!.legalClient = null
        fullName.setText(individualClient!!.fullName)
        birthDate.setText(individualClient!!.birthDate)
        sex.setText(individualClient!!.sex)
        driving.setText(individualClient!!.drivingExperience.toString())
        individualAddress.setText(individualClient!!.address)
        individualPhone.setText(individualClient!!.phone)
    }

    private fun initLegalClientCard() {
        legalClient = intent.getSerializableExtra("Client") as LegalClient
        viewModel!!.legalClient = legalClient
        viewModel!!.individualClient = null
        companyName.setText(legalClient!!.name)
        uniqueNumber.setText(legalClient!!.uniqueNumber)
        director.setText(legalClient!!.director)
        accountant.setText(legalClient!!.accountant)
        legalAddress.setText(legalClient!!.address)
        legalPhone.setText(legalClient!!.phone)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        activity = this
        recycler.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)
        viewModel!!.backgroundLivaData.observe(this, Observer { onBackground() })
        viewModel!!.adapterLiveData.observe(this, Observer { onAdapterChanged() })
        viewModel!!.dialogLiveData.observe(this, Observer { onDialogChanged() })
        viewModel!!.onNavigationClicked(clientNavigation.selectedItemId, this)
        clientNavigation.setOnNavigationItemSelectedListener{mOnNavigationItemSelectedListener(it.itemId)}
        mOnNavigationItemSelectedListener(clientNavigation.selectedItemId)
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

    fun onFabClient(view: View) {
        viewModel!!.onFab(clientNavigation.selectedItemId, this)
    }

    private fun onAdapterChanged() {
        val adapter = viewModel!!.adapterLiveData.value
        adapter?.setOwner(this)
        recycler.adapter = adapter

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
