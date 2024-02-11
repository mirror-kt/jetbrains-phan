package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.jetbrains.php.tools.quality.*
import dev.mirrorkt.jetbrains.phan.PHAN

object PhanQualityToolType : QualityToolType<PhanConfiguration>() {
    override fun getDisplayName(): String = PHAN

    override fun getQualityToolBlackList(project: Project): QualityToolBlackList = object : QualityToolBlackList() {}

    override fun getConfigurationManager(project: Project): QualityToolConfigurationManager<PhanConfiguration> =
        project.service<PhanConfigurationManager>()

    override fun getInspection(): QualityToolValidationInspection = PhanValidationInspection

    override fun getConfigurationProvider(): QualityToolConfigurationProvider<PhanConfiguration>? = null

    override fun createConfigurableForm(
        project: Project,
        configuration: PhanConfiguration
    ): QualityToolConfigurableForm<PhanConfiguration> =
        PhanConfigurableForm(project, configuration)

    override fun getToolConfigurable(project: Project): Configurable = PhanConfigurable(project)

    override fun getProjectConfiguration(p0: Project): QualityToolProjectConfiguration<*> = PhanProjectConfiguration()

    override fun createConfiguration(): PhanConfiguration = PhanConfiguration()
}
