package io.github.anbuiii.compostory.compiler

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import io.github.anbuiii.compostory.annotation.Compostory
import io.github.anbuiii.compostory.compiler.repostory.CompostoryRepository
import io.github.anbuiii.compostory.compiler.utils.writeText

internal class CompostoryProcessor(
    val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    val repository: CompostoryRepository = CompostoryRepository.instance

    private var invoked = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Compostory::class.qualifiedName!!)
        symbols.filter { it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(CompostoryVisitor(), Unit) }
        if (!invoked) {
            val homeScreenKt =
                codeGenerator.createNewFile(Dependencies(true), "", "HomeScreenKt")
            val screenList = repository.screens.map { it.key }.joinToString {
                """
                    "$it" to ${it}Detail()
                """.trimIndent()
            }
            val homeScreenText =
                """
                    import androidx.compose.foundation.lazy.LazyColumn
                    import androidx.compose.foundation.lazy.items
                    import androidx.compose.material.Button
                    import androidx.compose.material.Text
                    import androidx.compose.runtime.Composable
                    import cafe.adriel.voyager.navigator.LocalNavigator
                    import cafe.adriel.voyager.navigator.currentOrThrow
                    import cafe.adriel.voyager.core.screen.Screen
                    
                    class HomeScreen() : Screen {
                        private val screens = listOf(
                            $screenList
                        )
                        
                        @Composable
                        override fun Content() {
                            val navigator = LocalNavigator.currentOrThrow
                            LazyColumn() {
                                items(screens) {
                                    Button(
                                        onClick = {
                                            navigator.push(it.second)
                                        }
                                    ) {
                                        Text(it.first)
                                    }
                                }
                            }
                        }
                    }
                """.trimIndent()
            homeScreenKt.writeText(homeScreenText)


            val compostoryAppKt =
                codeGenerator.createNewFile(Dependencies(true), "", "CompostoryApp")
            val compostoryAppText =
                """
                import androidx.compose.foundation.layout.fillMaxSize
                import androidx.compose.material.Scaffold
                import androidx.compose.runtime.Composable
                import androidx.compose.ui.Modifier
                import cafe.adriel.voyager.navigator.Navigator
                import cafe.adriel.voyager.transitions.SlideTransition

                @Composable
                fun CompostoryApp() {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Navigator(
                            screen = HomeScreen(),
                            content = { navigator ->
                                ProvideAppNavigator(
                                    navigator = navigator,
                                    content = {
                                        SlideTransition(navigator)
                                    }
                                )
                            }
                        )
                    }
                }
                """.trimIndent()
            invoked = true
            compostoryAppKt.writeText(compostoryAppText)
        }
        return emptyList()
    }

    inner class CompostoryVisitor : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val functionName = function.simpleName.asString()
            val packageName = function.containingFile?.packageName?.asString() ?: ""
            val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, packageName, functionName)
            repository.addScreen(functionName, {})
            val text =
                """
                    import androidx.compose.runtime.Composable
                    import cafe.adriel.voyager.core.screen.Screen
                    
                    class ${functionName}Detail : Screen {
                        @Composable
                        override fun Content() {
                            ${functionName}()
                        }
                    }
                    """.trimIndent()
            file.writeText(text)
        }
    }
}

class CompostoryProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return CompostoryProcessor(environment.codeGenerator)
    }
}
