package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.openapi.project.Project
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfiguration
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanOptionsConfiguration

class PhanRemoteLspServerSupportProvider : PhanLspServerSupportProvider() {
    override fun getLspDescriptor(
        project: Project,
        interpreter: PhpInterpreter,
        configuration: PhanConfiguration,
        options: PhanOptionsConfiguration
    ): PhanLspDescriptor = PhanRemoteLspDescriptor(
        project,
        interpreter,
        configuration,
        options,
        interpreter.phpSdkAdditionalData as PhpRemoteSdkAdditionalData
    )

    override fun canStart(interpreter: PhpInterpreter): Boolean = interpreter.isRemote
}
