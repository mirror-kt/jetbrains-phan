package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.lang.PhpFileType

// settings.php.quality.tools

@Suppress("UnstableApiUsage")
class PhanLspDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Phan") {
    companion object {
        private val LOGGER = logger<PhanLspDescriptor>()
    }

    override fun createCommandLine(): GeneralCommandLine {
        val manager = PhpInterpretersManagerImpl.getInstance(project)
        val interpreter = project.service<PhpInterpreter>()
        LOGGER.warn("$interpreter")

        return GeneralCommandLine().apply {
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            withCharset(Charsets.UTF_8)
            addParameters(
                "php",
                "vendor/bin/phan",
                "--quick",
                "--language-server-on-stdin",
                "--language-server-disable-completion",
                "--language-server-disable-go-to-definition",
            )
        }
    }

    override fun isSupportedFile(file: VirtualFile): Boolean = file.fileType == PhpFileType.INSTANCE
}