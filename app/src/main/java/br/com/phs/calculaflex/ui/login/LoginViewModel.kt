package br.com.phs.calculaflex.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.phs.calculaflex.exceptions.EmailInvalidException
import br.com.phs.calculaflex.exceptions.PasswordInvalidException
import br.com.phs.calculaflex.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private var mAuth = FirebaseAuth.getInstance()

    val loginState = MutableLiveData<RequestState<FirebaseUser>>()
    val resetPasswordState = MutableLiveData<RequestState<String>>()

    fun signIn(email: String, password: String) {

        loginState.value = RequestState.Loading

        if(validateFields(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        loginState.value = RequestState.Success(mAuth.currentUser!!)
                    } else {
                        loginState.value = RequestState.Error(Throwable(
                            it.exception?.message ?: "Não foi possível realizar a requisição"
                        ))
                    }
                }
        }
    }

    fun resetPassword(email: String) {
        resetPasswordState.value = RequestState.Loading
        if(email.isNotEmpty()) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        resetPasswordState.value = RequestState.Success("Verifique sua caixa de e-mail")
                    } else {
                        resetPasswordState.value = RequestState.Error(
                            Throwable(
                                task.exception?.message ?: "Não foi possível realizar a requisição"
                            )
                        )
                    }
                }
        } else {
            resetPasswordState.value = RequestState.Error(EmailInvalidException())
        }
    }

    private fun validateFields(email: String, password: String): Boolean {

        if(email.isEmpty()) {
            loginState.value = RequestState.Error(EmailInvalidException())
            return false
        }

        if(password.isEmpty()) {
            loginState.value =
                RequestState.Error(PasswordInvalidException("Senha não pode ser vazia"))
            return false
        }

        if(password.length < 6) {
            loginState.value =
                RequestState.Error(PasswordInvalidException("Senha tem que ter pelo menos 6 caracteres"))
            return false
        }

        return true
    }
}