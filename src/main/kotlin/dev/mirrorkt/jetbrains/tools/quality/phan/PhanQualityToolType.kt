package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.jetbrains.php.tools.quality.QualityToolBlackList
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager
import com.jetbrains.php.tools.quality.QualityToolConfigurationProvider
import com.jetbrains.php.tools.quality.QualityToolProjectConfiguration
import com.jetbrains.php.tools.quality.QualityToolType
import com.jetbrains.php.tools.quality.QualityToolValidationInspection
import dev.mirrorkt.jetbrains.phan.PHAN

object PhanQualityToolType : QualityToolType<PhanConfiguration>() {
    override fun getDisplayName(): String = PHAN

    override fun getQualityToolBlackList(project: Project): QualityToolBlackList = object : QualityToolBlackList() {}

    override fun getConfigurationManager(project: Project): QualityToolConfigurationManager<PhanConfiguration> =
        project.service<PhanConfigurationManager>()

    override fun getInspection(): QualityToolValidationInspection = PhanValidationInspection()

    override fun getConfigurationProvider(): QualityToolConfigurationProvider<PhanConfiguration>? =
        PhanConfigurationProvider.getInstances()

    override fun createConfigurableForm(
        project: Project,
        configuration: PhanConfiguration
    ): QualityToolConfigurableForm<PhanConfiguration> = PhanConfigurableForm(project, configuration)

    override fun getToolConfigurable(project: Project): Configurable = PhanConfigurable(project)

    override fun getProjectConfiguration(project: Project): QualityToolProjectConfiguration<*> =
        project.service<PhanProjectConfiguration>()

    override fun createConfiguration(): PhanConfiguration = PhanConfiguration()

    override fun getInspectionId(): String = "Phan"

    override fun getInspectionShortName(project: Project): String = inspection.shortName
}
