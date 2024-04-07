package io.github.anbuiii.compostory.compiler

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.anbuiii.compostory.compiler.utils.writeText

/**
 * Builder for Local Navigator
 *
 * Remove
 */
class BuilderProcessor(
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    private var invoked = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!invoked) {
            val mainScreenKt =
                codeGenerator.createNewFile(Dependencies(true), "", "AppNavigator", "kt")
            val text =
                """
                import androidx.compose.runtime.Composable
                import androidx.compose.runtime.CompositionLocalProvider
                import androidx.compose.runtime.ProvidableCompositionLocal
                import androidx.compose.runtime.staticCompositionLocalOf
                import cafe.adriel.voyager.navigator.Navigator

                val LocalAppNavigator: ProvidableCompositionLocal<Navigator?> = staticCompositionLocalOf { null }

                @Composable
                fun ProvideAppNavigator(navigator: Navigator, content: @Composable () -> Unit) {
                    CompositionLocalProvider(LocalAppNavigator provides navigator) {
                        content()
                    }
                }

                """.trimIndent()
            invoked = true
            mainScreenKt.writeText(text)
        }

        return emptyList()
    }
}

class BuilderProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return BuilderProcessor(environment.codeGenerator)
    }
}