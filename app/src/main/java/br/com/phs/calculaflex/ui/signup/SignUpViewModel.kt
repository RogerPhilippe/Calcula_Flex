package br.com.phs.calculaflex.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.concrete.canarinho.validator.ValidadorTelefone
import br.com.phs.calculaflex.exceptions.EmailInvalidException
import br.com.phs.calculaflex.exceptions.PasswordInvalidException
import br.com.phs.calculaflex.extensions.isValidEmail
import br.com.phs.calculaflex.models.NewUser
import br.com.phs.calculaflex.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel : ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    val signUpState = MutableLiveData<RequestState<FirebaseUser>>()

    fun signUp(newUser: NewUser) {
        signUpState.value = RequestState.Loading
        if (validateFields(newUser)) {
            mAuth.createUserWithEmailAndPassword(
                newUser.email ?: "",
                newUser.password ?: ""
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveInFireStore(newUser)
                    } else {
                        signUpState.value = RequestState.Error(
                            Throwable(
                                task.exception?.message ?: "Não foi possível realizar a requisição"
                            )
                        )
                    }
                }
        } else {
            signUpState.value = RequestState.Error(Throwable("Não foi possível criar a conta"))
        }
    }

    private fun saveInFireStore(newUser: NewUser) {
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .set(newUser)
            .addOnSuccessListener { sendEmailVerification() }
            .addOnFailureListener { err ->
                signUpState.value = RequestState.Error(
                    Throwable(err.message)
                )
            }
    }

    private fun sendEmailVerification() {
        mAuth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { _ ->
                signUpState.value = RequestState.Success(mAuth.currentUser!!)
            }
    }

    private fun validateFields(newUser: NewUser): Boolean {

        if (newUser.username?.isEmpty() == true) {
            signUpState.value = RequestState.Error(Throwable("Informe o nome do usuário"))
            return false
        }
        if (newUser.email?.isValidEmail() == true) {
            signUpState.value = RequestState.Error(EmailInvalidException())
            return false
        }
        if (newUser.password?.isEmpty() == true) {
            signUpState.value = RequestState.Error(PasswordInvalidException("Informe uma senha"))
            return false
        }
        if (newUser.phone?.isEmpty() == true) {
            signUpState.value = RequestState.Error(Throwable("Informe o número do telefone"))
            return false
        }
        if (!ValidadorTelefone.TELEFONE.ehValido(newUser.phone)) {
            signUpState.value = RequestState.Error(Throwable("Informe um telefone válido"))
            return false
        }
        if (newUser.email?.length ?: 0 < 6) {
            signUpState.value =
                RequestState.Error(PasswordInvalidException("Senha com no mínimo 6 caracteres"))
            return false
        }
        return true
    }
}