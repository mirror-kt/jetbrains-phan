package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.lang.PhpFileType
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfiguration
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanOptionsConfiguration
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanProjectConfiguration

@Suppress("UnstableApiUsage")
open class PhanLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        if (file.fileType != PhpFileType.INSTANCE) {
            return
        }

        val configuration = PhanProjectConfiguration.getInstance(project)
            .findSelectedConfiguration(project)
            ?: return
        val toolPath = configuration.toolPath
        if (toolPath.isEmpty()) {
            return
        }
        val options = PhanOptionsConfiguration.getInstance(project)
        val interpreter = PhpInterpretersManagerImpl.getInstance(project)
            .findInterpreterById(configuration.interpreterId)
            ?: return
        if (!canStart(interpreter)) {
            return
        }

        val lspDescriptor = getLspDescriptor(project, interpreter, configuration, options)
        serverStarter.ensureServerStarted(lspDescriptor)
    }

    protected open fun getLspDescriptor(
        project: Project,
        interpreter: PhpInterpreter,
        configuration: PhanConfiguration,
        options: PhanOptionsConfiguration
    ): PhanLspDescriptor = PhanLspDescriptor(project, interpreter, configuration, options)

    protected open fun canStart(interpreter: PhpInterpreter): Boolean = !interpreter.isRemote
}
