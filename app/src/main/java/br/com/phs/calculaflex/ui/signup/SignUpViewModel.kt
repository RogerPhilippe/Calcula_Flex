package br.com.phs.calculaflex.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.phs.calculaflex.exceptions.EmailInvalidException
import br.com.phs.calculaflex.exceptions.PasswordInvalidException
import br.com.phs.calculaflex.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpViewModel : ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val signUpState = MutableLiveData<RequestState<FirebaseUser>>()

    fun signUp(email: String, password: String) {
        signUpState.value = RequestState.Loading
        if (validateFields(email, password)) {
            mAuth.createUserWithEmailAndPassword(
                email,
                password
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification()
                    } else {
                        signUpState.value = RequestState.Error(
                            Throwable(
                                task.exception?.message ?: "Não foi possível realizar a requisição"
                            )
                        )
                    }
                }
        }
    }

    private fun sendEmailVerification() {
        mAuth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                signUpState.value = RequestState.Success(mAuth.currentUser!!)
            }
    }

    private fun validateFields(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            signUpState.value = RequestState.Error(EmailInvalidException())
            return false
        }
        if (password.isEmpty()) {
            signUpState.value = RequestState.Error(PasswordInvalidException("Informe uma senha"))
            return false
        }
        if (email.length < 6) {
            signUpState.value =
                RequestState.Error(PasswordInvalidException("Senha com no mínimo 6 caracteres"))
            return false
        }
        return true
    }
}