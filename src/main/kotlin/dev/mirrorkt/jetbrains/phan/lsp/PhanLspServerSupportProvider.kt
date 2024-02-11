package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.lang.PhpFileType
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanProjectConfiguration

@Suppress("UnstableApiUsage")
class PhanLspServerSupportProvider : LspServerSupportProvider {
    companion object {
        private val LOGGER = thisLogger()
    }

    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        if (file.fileType != PhpFileType.INSTANCE) {
            return
        }

        val configuration = project.service<PhanProjectConfiguration>()
            .findSelectedConfiguration(project)
            ?: return
        val toolPath = configuration.toolPath
        if (toolPath.isEmpty()) {
            return
        }
        val interpreter = PhpInterpretersManagerImpl.getInstance(project)
            .findInterpreterById(configuration.interpreterId)
            ?: return
        val command = PhpCommandSettings.createHelperCommand(
            project,
            toolPath,
            interpreter.isRemote,
            interpreter
        )

        serverStarter.ensureServerStarted(PhanLspDescriptor(project, command))
    }
}
