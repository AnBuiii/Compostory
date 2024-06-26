package io.github.anbuiii.compostory.compiler.repostory

internal class CompostoryRepository {
    internal val screens = hashMapOf<String, () -> Unit>()

    fun addScreen(name: String, callback: () -> Unit) {
        screens[name] = callback
    }

    companion object {
        val instance: CompostoryRepository by lazy { CompostoryRepository() }
    }
}