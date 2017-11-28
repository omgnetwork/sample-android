package co.omisego.omgshop.pages.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.pages.products.ProductListActivity
import co.omisego.omgshop.pages.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.View, LoginContract.Presenter>(), LoginContract.View {
    override val mPresenter: LoginContract.Presenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initInstance()
    }

    private fun initInstance() {
        btnLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ProductListActivity::class.java))
            finish()
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

    override fun showLoading(title: String, msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoginSuccess(response: Login.Response) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoginFailed(response: Error) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRegister() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
