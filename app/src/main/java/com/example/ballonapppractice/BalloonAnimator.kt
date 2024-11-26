package com.example.ballonapppractice

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.random.Random

class BalloonsManager(private val context: Context, private val container: ViewGroup) {
    private val balloons = mutableListOf<BouncingBalloon>()

    fun createBalloons(count: Int, balloonWidth: Int, balloonHeight: Int, speed: Float = 2f) {
        // Clear existing balloons
        balloons.forEach { it.destroy() }
        balloons.clear()
        container.removeAllViews()

        // Create new balloons
        repeat(count) {
            val balloonView = createBalloonImageView(balloonWidth, balloonHeight)
            container.addView(balloonView)

            val balloon = BouncingBalloon(balloonView, speed)
            balloons.add(balloon)

            balloonView.post {
                balloon.setScreenDimensions(container.width, container.height)
                balloon.startBouncing()
            }
        }
    }

    private fun createBalloonImageView(width: Int, height: Int): ImageView {
        return ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(width, height)
            setImageResource(R.drawable.ballon)  // Your balloon drawable
        }
    }
}
class BouncingBalloon(
    private val balloon: ImageView,
    private var baseSpeed: Float
) {
    private var currentAnimator: ValueAnimator? = null
    private var isPopped = false
    private var velocityX = 0f
    private var velocityY = 0f
    private var positionX = 0f
    private var positionY = 0f
    private var screenWidth = 0
    private var screenHeight = 0

    init {
        balloon.setOnClickListener {
            if (!isPopped) {
                balloon.setImageResource(R.drawable.bubble_popped)

                popBalloon()
            }
        }
    }

    fun setScreenDimensions(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
        // Random starting position within screen bounds
        positionX = Random.nextFloat() * (width - balloon.width)
        positionY = Random.nextFloat() * (height - balloon.height)
        setRandomVelocity()
    }

    private fun setRandomVelocity() {
        val angle = Math.toRadians(Random.nextDouble(360.0))
        velocityX = (baseSpeed * Math.cos(angle)).toFloat()
        velocityY = (baseSpeed * Math.sin(angle)).toFloat()
    }

    fun startBouncing() {
        isPopped = false
        balloon.visibility = View.VISIBLE
        balloon.alpha = 1f
        balloon.x = positionX
        balloon.y = positionY

        currentAnimator?.cancel()

        currentAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 16
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addUpdateListener {
                if (!isPopped) {
                    updatePosition()
                    checkBounce()
                    balloon.x = positionX
                    balloon.y = positionY
                }
            }

            start()
        }
    }

    fun destroy() {
        currentAnimator?.cancel()
        currentAnimator = null
    }

    private fun updatePosition() {
        positionX += velocityX
        positionY += velocityY
    }

    private fun checkBounce() {
        if (positionX + balloon.width > screenWidth) {
            positionX = screenWidth - balloon.width.toFloat()
            velocityX *= -1
        } else if (positionX < 0) {
            positionX = 0f
            velocityX *= -1
        }

        if (positionY + balloon.height > screenHeight) {
            positionY = screenHeight - balloon.height.toFloat()
            velocityY *= -1
        } else if (positionY < 0) {
            positionY = 0f
            velocityY *= -1
        }
    }

    private fun popBalloon() {
        isPopped = true
        currentAnimator?.cancel()
        balloon.postDelayed ({
            balloon.setImageResource(R.drawable.bubble_popped_complete)
        },100)

        balloon.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                balloon.visibility = View.INVISIBLE
                balloon.scaleX = 1f
                balloon.scaleY = 1f
//                balloon.postDelayed({
//                    resetBalloon()
//                }, 1000)
            }
            .start()
    }

    private fun resetBalloon() {
        setScreenDimensions(screenWidth, screenHeight)
        startBouncing()
    }
}