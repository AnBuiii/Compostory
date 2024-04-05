package repostory

class CompostoryRepository {
    private val screens = hashMapOf<String, () -> Unit>()

    fun addScreen(name: String, callback: () -> Unit) {
        screens[name] = callback
    }

    companion object {
        val instance: CompostoryRepository by lazy { CompostoryRepository() }
    }
}