package android.util

class Log {
    companion object {
        @JvmStatic fun d(tag: String?, msg: String?): Int = 0
        @JvmStatic fun e(tag: String?, msg: String?): Int = 0
        @JvmStatic fun i(tag: String?, msg: String?): Int = 0
        @JvmStatic fun w(tag: String?, msg: String?): Int = 0
        @JvmStatic fun v(tag: String?, msg: String?): Int = 0
    }
}