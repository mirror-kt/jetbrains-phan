package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.remote.PhpRemoteProcessManager
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData
import com.jetbrains.php.run.remote.PhpRemoteInterpreterManager
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfiguration
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanOptionsConfiguration

@Suppress("UnstableApiUsage")
class PhanRemoteLspDescriptor(
    project: Project,
    interpreter: PhpInterpreter,
    configuration: PhanConfiguration,
    options: PhanOptionsConfiguration,
    private val phpRemoteSdkAdditionalData: PhpRemoteSdkAdditionalData,
) : PhanLspDescriptor(project, interpreter, configuration, options) {
    companion object {
        private val LOGGER = logger<PhanRemoteLspDescriptor>()
    }

    override fun createCommandLine(): GeneralCommandLine {
        val manager = getRemoteInterpreterManager()
        val data = interpreter.phpSdkAdditionalData
        val validate = data.validate(project, null)
        if (!validate.isNullOrEmpty()) {
            throw ExecutionException(validate)
        }

        val executable = interpreter.pathToPhpExecutable
        val toolPath = configuration.toolPath
        val command = if (executable != null) {
            PhpCommandSettingsBuilder.create(executable, manager.createPathMapper(project, data), data).apply {
                setScript(toolPath)
                disableXDebugOptions(this)
            }
        } else {
            PhpCommandSettingsBuilder.create(toolPath, manager.createPathMapper(project, data), data)
        }
        project.basePath?.let { projectBasePath ->
            command.setWorkingDir(manager.createPathMapper(project, data).process(projectBasePath))
        }
        command.addArguments(getLspCommandLineOptions())

        return command.createGeneralCommandLine()
    }

    override fun startServerProcess(): OSProcessHandler {
        val startingCommandLine = createCommandLine()
        val process = PhpRemoteProcessManager.getInstance(phpRemoteSdkAdditionalData)
            .getRemoteProcessRunner(phpRemoteSdkAdditionalData)
            .getRemoteBackgroundProcess(
                project,
                phpRemoteSdkAdditionalData,
                startingCommandLine,
                *getPathMappings().pathMappings.toTypedArray()
            )
        LOG.info("$this: starting remote LSP server: $startingCommandLine")
        return OSProcessHandler(process, startingCommandLine.commandLineString)
    }

    override fun getFilePath(file: VirtualFile): String {
        val mappings = getPathMappings()
        return mappings.convertToRemote(file.path)
    }

    override fun findLocalFileByPath(path: String): VirtualFile? {
        val mappings = getPathMappings()
        val localPath = mappings.convertToLocal(path)
        return LocalFileSystem.getInstance()
            .findFileByPath(localPath)
    }

    private fun getRemoteInterpreterManager(): PhpRemoteInterpreterManager =
        PhpRemoteInterpreterManager.getInstance()
            ?: throw IllegalStateException("Remote php interpreter specified but could not get the PhpRemoteInterpreterManager")

    private fun getPathMappings() = getRemoteInterpreterManager()
        .createPathMappings(project, phpRemoteSdkAdditionalData)
}
