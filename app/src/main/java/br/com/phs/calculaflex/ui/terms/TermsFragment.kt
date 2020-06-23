package br.com.phs.calculaflex.ui.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import br.com.phs.calculaflex.R
import br.com.phs.calculaflex.ui.base.BaseFragment

class TermsFragment : BaseFragment() {
    override val layout = R.layout.fragment_terms
    private lateinit var wvTerms: WebView
    private lateinit var ivBack: ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wvTerms = view.findViewById(R.id.wvTerms)
        ivBack = view.findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        wvTerms.loadUrl("https://calcula-flex-19mob-8542c.web.app")
    }
}