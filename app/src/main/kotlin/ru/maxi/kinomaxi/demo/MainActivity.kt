package ru.maxi.kinomaxi.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: MainActivityViewModel by viewModels()

    private val navController: NavController
        get() {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            return navHostFragment.navController
        }

    private val menuProvider: KinoMaksiMenu by lazy {

        KinoMaksiMenu(this.applicationContext, navController)
    }
    private var isMenuProviderAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.checkSession()

        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)

        setupActionBarWithNavController(navController)

        (navController as? NavHostController)?.setLifecycleOwner(this)
        (navController as? NavHostController)?.setOnBackPressedDispatcher(onBackPressedDispatcher)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.appBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }

        addMenuProvider(menuProvider, this)
        isMenuProviderAdded = true

        navController.addOnDestinationChangedListener { _, destination, _ ->
            handleMenuVisibility(destination.id)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    menuProvider.updateAccountIcon(state)
                }
            }
        }
    }

    private fun handleMenuVisibility(destinationId: Int) {
        if (destinationId == R.id.authorizationFragment || destinationId == R.id.accountDetailsFragment) {
            if (isMenuProviderAdded) {
                removeMenuProvider(menuProvider)
                isMenuProviderAdded = false
            }
        } else {
            if (!isMenuProviderAdded) {
                addMenuProvider(menuProvider, this)
                isMenuProviderAdded = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }
}
