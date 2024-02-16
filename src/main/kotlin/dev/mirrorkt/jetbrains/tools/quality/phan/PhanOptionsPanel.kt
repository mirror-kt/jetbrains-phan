package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.ui.dsl.builder.COLUMNS_MEDIUM
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel
import dev.mirrorkt.jetbrains.phan.PhanBundle
import dev.mirrorkt.jetbrains.phan.lsp.PhanLspServerSupportProvider
import dev.mirrorkt.jetbrains.phan.lsp.PhanRemoteLspServerSupportProvider
import javax.swing.JPanel

class PhanOptionsPanel(
    project: Project,
    comboBox: QualityToolConfigurationComboBox<*>,
    validate: Runnable
) : QualityToolsOptionsPanel(project, validate, PhanQualityToolType) {
    private val panel by lazy {
        panel {
            val options = PhanOptionsConfiguration.getInstance(project)

            row {
                checkBox(PhanBundle.message("label.phan.options.quick.mode"))
                    .bindSelected(options::quickModeEnabled)
            }
            row {
                checkBox(PhanBundle.message("label.phan.options.allow.polyfill.parser"))
                    .bindSelected(options::allowPolyfillParser)
            }
            row {
                label(PhanBundle.message("label.phan.options.config.file"))
                cell(PhpTextFieldWithSdkBasedBrowse().apply {
                    init(
                        project,
                        getSdkAdditionalData(project, comboBox),
                        PhanBundle.message("label.phan.options.config.file.dialog.title"),
                        true,
                        false
                    )
                }).columns(COLUMNS_MEDIUM)
                    .bindText(options::configFile)
            }

            onApply {
                @Suppress("UnstableApiUsage")
                with(LspServerManager.getInstance(project)) {
                    stopAndRestartIfNeeded(PhanLspServerSupportProvider::class.java)
                    stopAndRestartIfNeeded(PhanRemoteLspServerSupportProvider::class.java)
                }
            }
        }
    }

    override fun getOptionsPanel(): JPanel = panel

    override fun apply() {
        panel.apply()
    }

    override fun isModified(): Boolean = panel.isModified()
}
