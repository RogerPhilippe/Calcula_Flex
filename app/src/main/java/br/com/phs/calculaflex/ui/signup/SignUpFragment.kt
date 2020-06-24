package br.com.phs.calculaflex.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import br.com.concrete.canarinho.watcher.TelefoneTextWatcher
import br.com.concrete.canarinho.watcher.evento.EventoDeValidacao
import br.com.phs.calculaflex.R
import br.com.phs.calculaflex.extensions.hideKeyboard
import br.com.phs.calculaflex.models.NewUser
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
            findNavController().navigate(R.id.action_signUpFragment_to_termsFragment)
        }

        registerObserver()
        setUpListener()
    }

    private fun setUpListener() {

        etPhoneSignUp.addTextChangedListener(TelefoneTextWatcher(object : EventoDeValidacao {
            override fun totalmenteValido(valorAtual: String?) { }
            override fun invalido(valorAtual: String?, mensagem: String?) { }
            override fun parcialmenteValido(valorAtual: String?) { }
        }))

        tvTerms.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_termsFragment)
        }

        btCreateAccount.setOnClickListener {
            hideKeyboard()
            if (!checkBoxDone) {
                Toast.makeText(context, "Leia e aceite os termos de uso", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpViewModel.signUp(
                NewUser(
                    etUserNameSignUp.text.toString(),
                    etEmailSignUp.text.toString(),
                    etPhoneSignUp.text.toString(),
                    etPasswordSignUp.text.toString())
            )
        }

        setUpCheckboxListener()

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
