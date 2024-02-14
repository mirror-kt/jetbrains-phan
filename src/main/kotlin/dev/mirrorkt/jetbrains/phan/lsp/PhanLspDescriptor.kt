package dev.mirrorkt.jetbrains.phan.lsp

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.debug.xdebug.XdebugUtil
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.run.PhpConfigurationOption
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfiguration
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanOptionsConfiguration

@Suppress("UnstableApiUsage")
open class PhanLspDescriptor(
    project: Project,
    protected val interpreter: PhpInterpreter,
    protected val configuration: PhanConfiguration,
    protected val options: PhanOptionsConfiguration,
) : ProjectWideLspServerDescriptor(project, "Phan") {
    override fun isSupportedFile(file: VirtualFile): Boolean = file.fileType == PhpFileType.INSTANCE

    override fun createCommandLine(): GeneralCommandLine {
        val executable =
            interpreter.pathToPhpExecutable ?: throw ExecutionException("PHP interpreter executable is not specified")
        return GeneralCommandLine().apply {
            exePath = executable
            project.basePath?.let { projectBasePath ->
                setWorkDirectory(projectBasePath)
            }

            val xDebugOptions = getXDebugOptions()
            if (xDebugOptions.isNotEmpty()) {
                addParameters(xDebugOptions)
            }

            addParameter(configuration.toolPath)
            addParameters(getLspCommandLineOptions())
        }
    }

    protected fun getLspCommandLineOptions(): List<String> = listOf(
        //            "--quick",
        "--language-server-on-stdin",
        "--allow-polyfill-parser",
        "--language-server-enable-go-to-definition",
        "--language-server-enable-completion",
        // TODO: It should have been defined in 2023.3, but for some reason it isn't.
        //  "--language-server-enable-hover"
        "--language-server-verbose",
    )

    override val lspGoToDefinitionSupport: Boolean = true

    private fun getXDebugOptions(): List<String> =
        XdebugUtil.getXDebugVersion(interpreter, project).disableSettings

    // Snippet from com.jetbrains.php.tools.quality.QualityToolProcessCreator.disableXDebugOptions
    protected fun disableXDebugOptions(command: PhpCommandSettings) {
        val xDebugOptions = getXDebugOptions()
        if (xDebugOptions.isNotEmpty()) {
            command.addConfigurationOptions(xDebugOptions.map { i ->
                val s = i.substringAfter("-d")
                val split = s.split('=')
                assert(split.size == 2)

                PhpConfigurationOption(split[0], split[1])
            })
        }
    }
}
