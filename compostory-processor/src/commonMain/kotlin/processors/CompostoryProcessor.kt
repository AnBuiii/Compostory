package processors

import com.compostory.Compostory
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import repostory.CompostoryRepository
import utils.writeText

class CompostoryProcessor(
    val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    val repository: CompostoryRepository = CompostoryRepository.instance

    private val invoked = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Compostory::class.qualifiedName!!)
        symbols.filter { it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(CompostoryVisitor(), Unit) }
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
