package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.jetbrains.php.tools.quality.QualityToolProjectConfigurableForm
import com.jetbrains.php.tools.quality.QualityToolType

@Suppress("UnstableApiUsage")
class PhanConfigurable(project: Project) : QualityToolProjectConfigurableForm(project), Configurable.NoScroll {
    override fun getQualityToolType(): QualityToolType<*> = PhanQualityToolType
}
