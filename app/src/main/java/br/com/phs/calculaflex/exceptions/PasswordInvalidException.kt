package br.com.phs.calculaflex.exceptions

class PasswordInvalidException(private val msg: String = "Senha Inválida!") : Throwable(msg)