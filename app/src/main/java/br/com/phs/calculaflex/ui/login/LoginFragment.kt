package br.com.phs.calculaflex.ui.login

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import br.com.phs.calculaflex.R
import br.com.phs.calculaflex.models.RequestState
import br.com.phs.calculaflex.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    override val layout = R.layout.fragment_login
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater
            .from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        startAnimation()
        setupObserver()
        setupListeners()
    }

    private fun setupEvents() {

        tvResetPassword.setOnClickListener {
            loginViewModel.resetPassword(etUserNameSignUp.text.toString())
        }

        tvNewAccount.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }

    private fun startAnimation() {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_form_login)
        tvSubTitleSignUp.startAnimation(anim)
        containerLogin.startAnimation(anim)
        tvResetPassword.startAnimation(anim)
        tvNewAccount.startAnimation(anim)
    }

    private fun setupObserver() {
        loginViewModel.loginState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is RequestState.Success -> showSuccess()
                is RequestState.Error -> showError(it.throwable)
                is RequestState.Loading -> showLoading("Realizando a autenticação")
            }
        })

        this.loginViewModel.resetPasswordState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    hideLoading()
                    showMessage(it.data)
                }
                is RequestState.Error -> showError(it.throwable)
                is RequestState.Loading -> showLoading("Reenviando o e-mail para alteração")
            }
        })


    }

    private fun setupListeners() {
        btLogin.setOnClickListener { btLoginClickListener() }
    }

    private fun btLoginClickListener() {
        loginViewModel.signIn(etUserNameSignUp.text.toString(), etPhoneSignUp.text.toString())
    }

    private fun showSuccess() {
        hideLoading()
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_loginFragment_to_main_nav_graph)
        showMessage("Logado.")
    }

    private fun showError(throwable: Throwable) {
        hideLoading()
        showMessage(throwable.message)
    }

    private fun showMessage(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}