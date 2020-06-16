package br.com.phs.calculaflex.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import br.com.phs.calculaflex.R
import kotlinx.android.synthetic.main.fragment_login.*

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            val extras = FragmentNavigatorExtras(
                ivLogoApp to "logoApp",
                tvAppName to "textApp"
            )
            NavHostFragment.findNavController(this).navigate(
                R.id.action_splashFragment_to_login_nav_graph, null, null, extras)
        }, 2000)
    }

}