package dev.mirrorkt.jetbrains.tools.quality.phan.remote

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.NullableFunction
import com.intellij.util.xmlb.XmlSerializer
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData
import com.jetbrains.php.remote.tools.quality.QualityToolByInterpreterConfigurableForm
import com.jetbrains.php.remote.tools.quality.QualityToolByInterpreterDialog
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm
import dev.mirrorkt.jetbrains.phan.PHAN
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfigurableForm
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfiguration
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfigurationManager
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfigurationProvider
import org.jdom.Element

class PhanRemoteConfigurationProvider : PhanConfigurationProvider() {
    companion object {
        private const val PHAN_BY_INTERPRETER = "phan_by_interpreter"
    }

    override fun canLoad(tagName: String): Boolean = StringUtil.equals(tagName, PHAN_BY_INTERPRETER)

    override fun load(element: Element): PhanConfiguration =
        XmlSerializer.deserialize(element, PhanRemoteConfiguration::class.java)

    override fun createConfigurationForm(
        project: Project,
        settings: PhanConfiguration
    ): QualityToolConfigurableForm<PhanRemoteConfiguration>? {
        if (settings !is PhanRemoteConfiguration) {
            return null
        }
        val delegate = PhanConfigurableForm(project, settings)
        return QualityToolByInterpreterConfigurableForm(project, settings, delegate)
    }

    override fun createNewInstance(
        project: Project?,
        existingSettings: MutableList<PhanConfiguration>
    ): PhanConfiguration? {
        val dialog =
            QualityToolByInterpreterDialog(project, existingSettings, PHAN, PhanRemoteConfiguration::class.java)
        if (!dialog.showAndGet()) {
            return null
        }
        val id = PhpInterpretersManagerImpl.getInstance(project).findInterpreterId(dialog.selectedInterpreterName)
            .takeUnless { it.isNullOrEmpty() }
            ?: return null
        val settings = PhanRemoteConfiguration().apply {
            setInterpreterId(id)
        }
        val data = PhpInterpretersManagerImpl.getInstance(project).findInterpreterDataById(id)
        fillDefaultSettings(
            project,
            settings,
            project?.service<PhanConfigurationManager>()?.localSettings,
            data,
            data is PhpRemoteSdkAdditionalData
        )

        return settings
    }

    override fun createConfigurationByInterpreter(interpreter: PhpInterpreter): PhanConfiguration =
        PhanRemoteConfiguration().apply {
            setInterpreterId(interpreter.id)
        }

    override fun fillSettingsByDefaultValue(
        settings: PhanConfiguration,
        localConfiguration: PhanConfiguration,
        preparePath: NullableFunction<String, String>
    ) {
        super.fillSettingsByDefaultValue(settings, localConfiguration, preparePath)
        settings.timeout = 60000
    }
}
