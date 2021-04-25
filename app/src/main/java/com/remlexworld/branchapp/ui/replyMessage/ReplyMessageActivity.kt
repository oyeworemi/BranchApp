package com.remlexworld.branchapp.ui.replyMessage

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.remlexworld.branchapp.R
import com.remlexworld.branchapp.model.ReplyMessageRequest
import com.remlexworld.branchapp.model.Message
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_reply_message.*
import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.util.SessionManager
import kotlinx.android.synthetic.main.activity_reply_message.vParent
import javax.inject.Inject


/**
 * Reply selected message
 */

@AndroidEntryPoint
class ReplyMessageActivity : AppCompatActivity() {

    private val list = ArrayList<Message>()
    private lateinit var replyMessageAdapter: ReplyMessageAdapter

    private val viewModel by viewModels<ReplyMessageViewModel>()
    private lateinit var selectedMessage : Message

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply_message)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()

        btnReply.setOnClickListener {

            if (isFieldValidated()) {

                sessionManager.fetchAuthToken()?.let { token ->
                    viewModel.replyMessage(
                        token,
                        ReplyMessageRequest(selectedMessage.thread_id, et_comment.text.toString().trim()))
                }

            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showMessage(msg: String) {
        Snackbar.make(vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    private fun init() {

        val layoutManager = LinearLayoutManager(this)
        rvMessages.layoutManager = layoutManager

        replyMessageAdapter = ReplyMessageAdapter(this, list)
        rvMessages.adapter = replyMessageAdapter

        intent?.getSerializableExtra(EXTRAS_MESSAGE_DETAILS)?.let { message ->
            selectedMessage = message as Message
            viewModel.fetchSelectedMessages(selectedMessage.thread_id)
            subscribeUi()

        } ?: showMessage("Error: Unknown Message")

    }


    private fun subscribeUi() {

        viewModel.messageList.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let { list ->
                        replyMessageAdapter.updateData(list)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showMessage(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
            }

        })

        viewModel.sentMessageResponse.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        showMessage(resources.getString(R.string.text_reply_sent_successfully))
                        clearField()
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showMessage(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
            }
        })
    }


    companion object {
        const val EXTRAS_MESSAGE_DETAILS = "message_details"
    }

    private fun isFieldValidated(): Boolean {
        var msg: String? = null
        if (TextUtils.isEmpty(et_comment.text.toString())) {
            msg = resources.getString(R.string.text_enter_reply)

        }
        if (msg == null) return true

        showMessage(msg)

        return false
    }

    private fun clearField() {
        et_comment.text.clear()
    }
}