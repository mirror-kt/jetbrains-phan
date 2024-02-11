package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.jetbrains.php.PhpBundle
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm
import com.jetbrains.php.tools.quality.QualityToolType
import dev.mirrorkt.jetbrains.phan.PHAN
import org.jetbrains.annotations.NonNls

class PhanConfigurableForm<C : PhanConfiguration>(project: Project, configuration: C) :
    QualityToolConfigurableForm<C>(
        project,
        configuration,
        PHAN,
        "phan"
    ) {
    companion object {
        private val VERSION_REGEX = """.*Phan (?<version>[\d.]*).*""".toRegex()
    }

    @Suppress("RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun getQualityToolType(): QualityToolType<*> = PhanQualityToolType

    override fun validateMessage(@NonNls message: String): Pair<Boolean, String> {
        val version = VERSION_REGEX.find(message)
            ?.groups
            ?.get("version")
            ?.value
            ?.let { extractVersion(it) }
        return if (version == null || !message.contains(PHAN)) {
            Pair.create(false, PhpBundle.message("quality.tool.can.not.determine.version", message))
        } else {
            Pair.create(true, "OK, $message")
        }
    }
}
