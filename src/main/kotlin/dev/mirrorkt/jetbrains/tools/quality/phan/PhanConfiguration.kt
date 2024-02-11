package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.xmlb.annotations.Attribute
import com.intellij.util.xmlb.annotations.Transient
import com.jetbrains.php.PhpBundle
import com.jetbrains.php.tools.quality.QualityToolConfiguration
import dev.mirrorkt.jetbrains.phan.PhanBundle

open class PhanConfiguration : QualityToolConfiguration {
    companion object {
        private const val DEFAULT_MAX_MESSAGES_PER_FILE = 50
        private const val DEFAULT_TIMEOUT_MS = 30000
    }

    private var myPhanPath = ""
    private var myMaxMessagesPerFile = DEFAULT_MAX_MESSAGES_PER_FILE
    private var myTimeoutMs = DEFAULT_TIMEOUT_MS

    @Transient
    override fun getToolPath(): String = myPhanPath

    override fun setToolPath(toolPath: String) {
        myPhanPath = toolPath
    }

    @get:Attribute("tool_path")
    var serializedToolPath: String?
        get() = serialize(toolPath)
        set(value) {
            myPhanPath = deserialize(value)
        }

    @Attribute("max_messages_per_file")
    override fun getMaxMessagesPerFile(): Int = myMaxMessagesPerFile

    @Attribute("timeout")
    override fun getTimeout(): Int = myTimeoutMs

    override fun setTimeout(timeout: Int) {
        myTimeoutMs = timeout
    }

    override fun getId(): String = PhpBundle.message("local")

    override fun getInterpreterId(): String? = null

    override fun clone(): QualityToolConfiguration = clone(PhanConfiguration())

    fun clone(settings: PhanConfiguration): PhanConfiguration {
        settings.myPhanPath = myPhanPath
        settings.myTimeoutMs = myTimeoutMs
        settings.myMaxMessagesPerFile = myMaxMessagesPerFile

        return settings
    }

    override fun compareTo(other: QualityToolConfiguration?): Int {
        if (other !is PhanConfiguration) {
            return 1
        }
        return if (StringUtil.equals(getPresentableName(null), PhanBundle.message("label.system.php"))) {
            return -1
        } else if (StringUtil.equals(other.getPresentableName(null), PhanBundle.message("label.system.php"))) {
            return 1
        } else {
            StringUtil.compare(getPresentableName(null), other.getPresentableName(null), false)
        }
    }
}
