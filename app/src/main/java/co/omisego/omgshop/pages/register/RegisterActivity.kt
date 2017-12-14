package co.omisego.omgshop.pages.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.custom.MinimalTextChangeListener
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Register
import co.omisego.omgshop.pages.products.ProductListActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity<RegisterContract.View, RegisterContract.Presenter>(), RegisterContract.View {
    override val mPresenter: RegisterContract.Presenter by lazy {
        RegisterPresenter(SharePrefsManager(this))
    }
    private lateinit var mProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_register_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle(R.string.activity_register_loading_dialog_title)
        mProgressDialog.setMessage(getString(R.string.activity_register_loading_dialog_message))

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            if (mPresenter.validateEmail(email) &&
                    mPresenter.validatePassword(password) &&
                    mPresenter.validateFirstName(firstName) &&
                    mPresenter.validateLastName(lastName)) {

                val request = Register.Request(
                        firstName,
                        lastName,
                        email,
                        password
                )
                mPresenter.handleRegister(request)
            }

        }

        etEmail.addTextChangedListener(MinimalTextChangeListener {
            tilEmail.isErrorEnabled = !mPresenter.validateEmail(it.toString())
        })

        etPassword.addTextChangedListener(MinimalTextChangeListener {
            tilPassword.isErrorEnabled = !mPresenter.validatePassword(it.toString())
        })

        etFirstName.addTextChangedListener(MinimalTextChangeListener {
            tilFirstName.isErrorEnabled = !mPresenter.validateFirstName(it.toString())
        })

        etLastName.addTextChangedListener(MinimalTextChangeListener {
            tilLastName.isErrorEnabled = !mPresenter.validateLastName(it.toString())
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showRegisterSuccess(response: Register.Response) {
        startActivity(Intent(this, ProductListActivity::class.java))
        finish()
    }

    override fun showRegisterFailed(response: Error) {
        showMessage(response.description)
    }

    override fun showFirstNameErrorHint(msg: String) {
        tilFirstName.error = msg
        tilFirstName.isErrorEnabled = true
    }

    override fun showLastNameErrorHint(msg: String) {
        tilLastName.error = msg
        tilLastName.isErrorEnabled = true
    }

    override fun showPasswordErrorHint(msg: String) {
        tilPassword.error = msg
        tilPassword.isErrorEnabled = true
    }

    override fun showEmailErrorHint(msg: String) {
        tilEmail.error = msg
        tilEmail.isErrorEnabled = true
    }

    override fun showLoading() {
        mProgressDialog.show()
    }

    override fun hideLoading() {
        mProgressDialog.dismiss()
    }
}
