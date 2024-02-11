package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.openapi.components.services
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.jetbrains.php.lang.PhpFileType

@Suppress("UnstableApiUsage")
class PhanLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        project.services<String>(true)

        if (file.fileType != PhpFileType.INSTANCE) {
            return
        }

        serverStarter.ensureServerStarted(PhanLspDescriptor(project))
    }
}
