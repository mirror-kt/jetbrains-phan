package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel
import javax.swing.JPanel

class PhanOptionsPanel(
    project: Project,
    comboBox: QualityToolConfigurationComboBox<*>,
    validate: Runnable
) : QualityToolsOptionsPanel(project, validate, PhanQualityToolType) {
    private val data = Data()

    private val panel = panel {
        row {
            label("ここにオプションはないよ").bold()
            cell(PhpTextFieldWithSdkBasedBrowse()).bindText(data::input)
        }
    }

    override fun getOptionsPanel(): JPanel = panel

    override fun apply() {
        panel.apply()
    }
}

private data class Data(
    var input: String = ""
)
