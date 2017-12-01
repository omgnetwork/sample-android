package co.omisego.omgshop.pages.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.pages.products.ProductListActivity
import co.omisego.omgshop.pages.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.View, LoginContract.Presenter>(), LoginContract.View {
    override val mPresenter: LoginContract.Presenter by lazy { LoginPresenter(SharePrefsManager(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initInstance()
    }

    private fun initInstance() {
        mPresenter.checkHasLogin()
        btnLogin.setOnClickListener {
            val request = Login.Request(etEmail.text.toString(), etPassword.text.toString())
            if (mPresenter.validateEmail(request.email) && mPresenter.validatePassword(request.password)) {
                mPresenter.handleLogin(request)
            }
        }
        val clickSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                mPresenter.handleClickRegisterButton()
            }
        }

        val ss = SpannableString(getString(R.string.activity_login_register))

        // To make the word "RegisterActivity" clickable with hi.
        ss.setSpan(clickSpan, 27, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvRegister.text = ss
        tvRegister.movementMethod = LinkMovementMethod.getInstance()


        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable) {
                tilEmail.isErrorEnabled = !mPresenter.validateEmail(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable) {
                tilPassword.isErrorEnabled = !mPresenter.validatePassword(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun showLoading(title: String, msg: String) {
        log("show loading")
    }

    override fun showLoginSuccess(response: Login.Response) {
        startActivity(Intent(this@LoginActivity, ProductListActivity::class.java))
        finish()
    }

    override fun showLoginFailed(response: Error) {
        // TODO:
    }

    override fun showEmailErrorHint(msg: String) {
        tilEmail.error = msg
        tilEmail.isErrorEnabled = true
    }

    override fun showPasswordErrorHint(msg: String) {
        tilPassword.error = msg
        tilPassword.isErrorEnabled = true
    }

    override fun showRegister() {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }
}
