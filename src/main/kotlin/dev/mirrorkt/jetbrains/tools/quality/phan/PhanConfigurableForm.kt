package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.jetbrains.php.PhpBundle
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm
import com.jetbrains.php.tools.quality.QualityToolType
import dev.mirrorkt.jetbrains.phan.PHAN
import org.jetbrains.annotations.NonNls

class PhanConfigurableForm(project: Project, configuration: PhanConfiguration) :
    QualityToolConfigurableForm<PhanConfiguration>(
        project,
        configuration,
        PHAN,
        "phan"
    ) {
    @Suppress("RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun getQualityToolType(): QualityToolType<*> = PhanQualityToolType

    override fun validateMessage(@NonNls message: String): Pair<Boolean, String> {
        val version = extractVersion(message.trim().replaceFirst("PHPStan.* ([\\d.]*).*", "$1").trim());
        return if (version == null || !message.contains(PHAN)) {
            Pair.create(false, PhpBundle.message("quality.tool.can.not.determine.version", message))
        } else {
            Pair.create(true, "OK, $message")
        }
    }
}
