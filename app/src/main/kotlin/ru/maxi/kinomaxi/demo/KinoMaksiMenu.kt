package ru.maxi.kinomaxi.demo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class KinoMaksiMenu(
    private val context: Context,
    private val navController: NavController,
) : MenuProvider {

    private var menu: Menu? = null
    private var currentState: MainActivityViewState? = null

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        this.menu = menu
        menuInflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
        currentState?.let { updateAccountIcon(it) }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_account -> {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(android.R.anim.fade_in)
                    .setExitAnim(android.R.anim.fade_out)
                    .setPopEnterAnim(android.R.anim.fade_in)
                    .setPopExitAnim(android.R.anim.fade_out)
                    .build()
                when (currentState) {
                    is MainActivityViewState.Authorized -> {
                        navController.navigate(R.id.accountDetailsFragment, null, navOptions)
                    }

                    is MainActivityViewState.Unauthorized -> {
                        navController.navigate(R.id.authorizationFragment, null, navOptions)
                    }

                    else -> {
                        navController.navigate(R.id.authorizationFragment, null, navOptions)
                    }
                }
                true
            }

            else -> false
        }
    }

    /**
     * Обновить иконку кнопки Информация об аккаунте
     * */
    fun updateAccountIcon(state: MainActivityViewState) {
        currentState = state
        val menuItem = menu?.findItem(R.id.action_account) ?: return

        when (state) {
            is MainActivityViewState.Authorized -> {
                val avatarUrl = state.userPreferences?.avatarUrl
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(avatarUrl)
                        .circleCrop()
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                menuItem.icon = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                } else {
                    val firstLetterUsername =
                        state.userPreferences?.username?.firstOrNull()?.uppercase()
                    drawGlideLetter(context, menuItem, firstLetterUsername!!)

                }
            }

            is MainActivityViewState.Unauthorized-> {
                menuItem.setIcon(R.drawable.ic_action_account_24)
            }
        }
    }

    private fun drawGlideLetter(context: Context, menuItem: MenuItem, letter: String) {
        val size = 128
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 100f
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


        Glide.with(context)
            .load(bitmap)
            .apply(RequestOptions.circleCropTransform())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    menuItem.icon = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}