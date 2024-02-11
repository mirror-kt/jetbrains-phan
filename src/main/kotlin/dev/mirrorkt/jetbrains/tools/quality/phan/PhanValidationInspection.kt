package dev.mirrorkt.jetbrains.tools.quality.phan

import com.jetbrains.php.tools.quality.QualityToolAnnotator
import com.jetbrains.php.tools.quality.QualityToolValidationInspection
import dev.mirrorkt.jetbrains.phan.PHAN

object PhanValidationInspection : QualityToolValidationInspection() {
    override fun getToolName(): String = PHAN

    override fun getAnnotator(): QualityToolAnnotator<*> = PhanAnnotator
}
