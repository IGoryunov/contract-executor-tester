package service

class ByteArrayClassLoader : ClassLoader() {
    private val className = "Contract";
    private lateinit var contractClass: Class<*>

    fun buildClass(byteCode: ByteArray): Class<*> =
        defineClass(className, byteCode, 0, byteCode.size).also { contractClass = it }

    override fun findClass(name: String): Class<*>? {
        if (name == className && ::contractClass.isInitialized) {
            return contractClass
        }
        return super.findClass(name)
    }
}