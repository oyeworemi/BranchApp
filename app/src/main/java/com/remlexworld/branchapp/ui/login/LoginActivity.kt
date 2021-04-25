package com.remlexworld.branchapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.remlexworld.branchapp.R
import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.util.SessionManager
import com.remlexworld.branchapp.model.LoginRequest
import com.remlexworld.branchapp.ui.messages.MessagesActivity
import com.remlexworld.branchapp.util.dismissKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subscribeUi()


        login.setOnClickListener {

            if (isFieldsValidated()) {
                login(LoginRequest(email = username.text.toString().trim(),
                    password = password.text.toString().trim()))


                this?.dismissKeyboard(it)

            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showError(msg: String) {
        Snackbar.make(container, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    private fun subscribeUi() {
        viewModel.loginResponse.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        sessionManager.saveAuthToken(it.authToken)
                        val intent = Intent(applicationContext, MessagesActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
            }
        })
    }


    private fun login(loginRequest: LoginRequest) {

        lifecycleScope.launchWhenCreated {

            viewModel.login(loginRequest)

        }

    }

    private fun isFieldsValidated(): Boolean {
        var msg: String? = null
        if (TextUtils.isEmpty(username.text.toString())
                || TextUtils.isEmpty(password.text.toString())) {
            msg = resources.getString(R.string.text_enter_all_fields)

        } else if (TextUtils.isEmpty(username.text.toString())) {
            msg = resources.getString(R.string.text_enter_email)
        } else if (TextUtils.isEmpty(password.text.toString())) {
            msg = resources.getString(R.string.text_enter_password)
        }
        if (msg == null) return true

        showError(msg)

        return false
    }


}