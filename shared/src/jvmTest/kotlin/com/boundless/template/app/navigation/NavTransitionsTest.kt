package com.boundless.template.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterExitTransitionConfig
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavBackStackEntry
import java.lang.reflect.Proxy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NavTransitionsTest {
    @Test
    fun predictiveBackUsesTheSameTransitionsAsInAppBackForEitherSwipeEdge() {
        @Suppress("UNCHECKED_CAST")
        val scope = Proxy.newProxyInstance(
            AnimatedContentTransitionScope::class.java.classLoader,
            arrayOf(AnimatedContentTransitionScope::class.java),
        ) { _, _, _ -> error("The desktop pop transitions do not require scope state") }
            as AnimatedContentTransitionScope<NavBackStackEntry>

        for (edge in listOf(0, 1)) {
            assertMatchingTransitions(
                appPopEnterTransition(scope).config,
                appPredictivePopEnterTransition(scope, edge).config,
            )
            assertMatchingTransitions(
                appPopExitTransition(scope).config,
                appPredictivePopExitTransition(scope, edge).config,
            )
        }
    }

    private fun assertMatchingTransitions(expected: EnterExitTransitionConfig, actual: EnterExitTransitionConfig) {
        assertEquals(expected.fade, actual.fade)
        assertEquals(expected.scale, actual.scale)
        assertEquals(expected.changeSize, actual.changeSize)
        assertEquals(expected.veil, actual.veil)
        val expectedSlide = assertNotNull(expected.slide)
        val actualSlide = assertNotNull(actual.slide)
        assertEquals(expectedSlide.animationSpec, actualSlide.animationSpec)
        for (size in listOf(IntSize(1000, 800), IntSize(501, 300))) {
            assertEquals(expectedSlide.slideOffset(size), actualSlide.slideOffset(size))
        }
    }
}
