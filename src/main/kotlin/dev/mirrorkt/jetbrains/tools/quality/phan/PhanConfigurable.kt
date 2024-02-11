package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox
import com.jetbrains.php.tools.quality.QualityToolProjectConfigurableForm
import com.jetbrains.php.tools.quality.QualityToolType
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel

@Suppress("UnstableApiUsage")
class PhanConfigurable(project: Project) : QualityToolProjectConfigurableForm(project), Configurable.NoScroll {
    override fun getQualityToolType(): QualityToolType<*> = PhanQualityToolType

    override fun getQualityToolOptionPanel(
        configurationBox: QualityToolConfigurationComboBox<*>,
        validate: Runnable
    ): QualityToolsOptionsPanel = PhanOptionsPanel(myProject, configurationBox, validate)

    override fun getId(): String = "settings.php.quality.tools.phan"
}
