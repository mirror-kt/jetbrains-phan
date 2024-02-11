package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.lang.PhpFileType

// settings.php.quality.tools

@Suppress("UnstableApiUsage")
class PhanLspDescriptor(
    project: Project,
    private val command: PhpCommandSettings
) : ProjectWideLspServerDescriptor(project, "Phan") {

    override fun createCommandLine(): GeneralCommandLine = command.apply {
        LOG.warn("createCommandLine")
        addArguments(
            listOf(
                "--quick",
                "--language-server-on-stdin",
                "--allow-polyfill-parser",
                "--language-server-enable-go-to-definition",
                "--language-server-enable-completion",
                // TODO: It should have been defined in 2023.3, but for some reason it isn't.
                //  "--language-server-enable-hover"
                "--language-server-verbose"
            )
        )
    }.createGeneralCommandLine()

    override fun isSupportedFile(file: VirtualFile): Boolean = file.fileType == PhpFileType.INSTANCE

    override val lspGoToDefinitionSupport: Boolean = true

//    override val lspCompletionSupport: LspCompletionSupport = object : LspCompletionSupport() {
//        override fun getIcon(item: CompletionItem): Icon? {
//
//        }
//    }

    // TODO: It should have been defined in 2023.3, but for some reason it isn't.
    //  override val lspHoverSupport = false
}
