package co.omisego.omgshop

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initInstance()
    }

    private fun initInstance() {
        btnLogin.setOnClickListener {
            // TODO : add validation, handle error, navigate to ProductList page
            Toast.makeText(this@LoginActivity, "Login", Toast.LENGTH_SHORT).show()
        }
        val clickSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

        val ss = SpannableString(getString(R.string.activity_login_register))

        // To make the word "RegisterActivity" clickable with hi.
        ss.setSpan(clickSpan, 27, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvRegister.text = ss
        tvRegister.movementMethod = LinkMovementMethod.getInstance()

    }
}
