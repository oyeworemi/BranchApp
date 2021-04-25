package com.remlexworld.branchapp.ui.messages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.remlexworld.branchapp.R
import com.remlexworld.branchapp.model.Message
import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_messages.*
import javax.inject.Inject


/**
 * Shows list of messages
 */
@AndroidEntryPoint
class MessagesActivity : AppCompatActivity() {

    private val list = ArrayList<Message>()
    private val viewModel by viewModels<MessagesViewModel>()
    private lateinit var messagesAdapter: MessagesAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        init()
        subscribeUi()

    }

    private fun init() {
        title = "Messages"
        val layoutManager = LinearLayoutManager(this)
        rvMessages.layoutManager = layoutManager

        messagesAdapter = MessagesAdapter(this, list)
        rvMessages.adapter = messagesAdapter

        lifecycleScope.launchWhenCreated {

            sessionManager.fetchAuthToken()?.let { viewModel.fetchMessages(it) }

        }

    }

    private fun subscribeUi() {
        viewModel.messageList.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let { list ->
                        messagesAdapter.updateData(list)
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

    private fun showError(msg: String) {
        Snackbar.make(vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }







}