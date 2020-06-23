package br.com.phs.calculaflex.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import br.com.phs.calculaflex.R
import br.com.phs.calculaflex.models.RequestState
import br.com.phs.calculaflex.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment() {

    override val layout = R.layout.fragment_sign_up
    private val signUpViewModel: SignUpViewModel by viewModels()

    private var checkBoxDone = false

    private lateinit var tvTerms: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTerms = view.findViewById(R.id.tvTerms)
        tvTerms.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_signUpFragment_to_termsFragment)
        }

        btCreateAccount.setOnClickListener {

            signUpViewModel.signUp(etEmailSignUp.text.toString(), etPasswordSignUp.text.toString())

        }

        setUpCheckboxListener()
        registerObserver()

    }

    private fun setUpCheckboxListener() {
        cbTermsSignUp.setOnClickListener {
            if (checkBoxDone) {
                cbTermsSignUp.speed = -1f
                cbTermsSignUp.playAnimation()
                checkBoxDone = false
            } else {
                cbTermsSignUp.speed = 1f
                cbTermsSignUp.playAnimation()
                checkBoxDone = true
            }
        }
    }

    private fun registerObserver() {
        this.signUpViewModel.signUpState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    hideLoading()
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signUpFragment_to_main_nav_graph)
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is RequestState.Loading -> showLoading("Realizando a autenticação")
            }
        })
    }
}
