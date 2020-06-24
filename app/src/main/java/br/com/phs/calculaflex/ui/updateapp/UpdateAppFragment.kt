package br.com.phs.calculaflex.ui.updateapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import br.com.phs.calculaflex.R
import br.com.phs.calculaflex.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_update_app.*

class UpdateAppFragment : BaseFragment(){

    override val layout = R.layout.fragment_update_app

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners() {

        btnUpdate.setOnClickListener { openAppInStore() }
        btnCancel.setOnClickListener { activity?.finish() }

    }

    private fun openAppInStore() {
        var intent: Intent
        val packageName = activity?.packageName
        try {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
            startActivity(intent)   }
    }

}