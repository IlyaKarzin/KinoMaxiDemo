package ru.maxi.kinomaxi.demo.accountDetails.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.databinding.FragmentAccountDetailsBinding
import ru.maxi.kinomaxi.demo.setSubtitle

@AndroidEntryPoint
class AccountDetailsFragment : Fragment() {

    private var _viewBinding: FragmentAccountDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: AccountDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _viewBinding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSubtitle(null)

        viewModel.checkSession()

        viewBinding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.viewState.collect{ state ->
                            when (state){
                                is AccountDetailsViewState.Authorized -> {
                                    val username = state.userPreferences.username
                                    viewBinding.username.text = username

                                    val imageView = viewBinding.profileImage
                                    val avatar = state.userPreferences.avatarUrl
                                    if (!avatar.isNullOrEmpty()) {
                                        Glide.with(imageView).load(avatar).circleCrop()
                                            .into(imageView)
                                    } else {
                                        val firstLetterUsername =
                                            state.userPreferences.username?.firstOrNull()
                                                ?.uppercase()
                                        drawGlideLetter(imageView, firstLetterUsername!!)
                                    }
                                }

                                is AccountDetailsViewState.Error -> {
                                    Snackbar.make(
                                        requireView(),
                                        state.message,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    viewModel.checkSession()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        findNavController().navigateUp()
                                    }, 500)
                                }

                                is AccountDetailsViewState.Loading -> {

                                }
                            }
                        }
                    }
                }
            }
        }
    }

        private fun drawGlideLetter(imageView: ShapeableImageView, letter: String) {
            val size = 256
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 200f
                isAntiAlias = true
            }

            val backgroundPaint = Paint().apply {
                color = Color.GRAY
            }
            canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), backgroundPaint)

            paint.color = Color.BLACK

            val textWidth = paint.measureText(letter)
            val xPosition = (size - textWidth) / 2
            val textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent
            val baseline = (size - textHeight) / 2 - paint.fontMetrics.ascent
            val yOffset = size / 70.toFloat()
            canvas.drawText(letter, xPosition, baseline - yOffset, paint)


            Glide.with(imageView)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView)
        }

}
