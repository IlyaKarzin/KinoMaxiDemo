package ru.maxi.kinomaxi.demo.authorization.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.R
import ru.maxi.kinomaxi.demo.databinding.FragmentAuthorizationBinding
import ru.maxi.kinomaxi.demo.setSubtitle
import ru.maxi.kinomaxi.demo.setTitle

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private var _viewBinding: FragmentAuthorizationBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _viewBinding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(getString(R.string.login_lable))
        setSubtitle(null)

        viewBinding.loginTextField.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewBinding.loginTextField.error = null
            }
        }

        viewBinding.passwordTextField.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewBinding.passwordTextField.error = null
            }
        }

        viewBinding.loginButton.setOnClickListener {
            val username = viewBinding.loginTextField.editText?.text.toString()
            val password = viewBinding.passwordTextField.editText?.text.toString()

            viewModel.loginUser(username, password)

        }

        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch { viewModel.viewState.collect { state ->
                        when (state) {
                            is AuthorizationViewState.Success -> {
                                findNavController().navigate(R.id.action_authorizationFragment_to_accountDetailsFragment)
                            }

                            is AuthorizationViewState.Error -> {
                                Snackbar.make(requireView(), R.string.sign_in_error, Snackbar.LENGTH_SHORT).show()

                                //Без пробела loginTextField не принимает вид ошибки
                                viewBinding.loginTextField.error = " "
                                viewBinding.passwordTextField.error = getString(R.string.fields_error)
                            }

                            AuthorizationViewState.Loading -> {}
                        }
                    } }
                }
            }
        }
    }
}