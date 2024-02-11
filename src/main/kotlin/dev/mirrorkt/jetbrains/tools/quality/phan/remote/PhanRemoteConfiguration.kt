package dev.mirrorkt.jetbrains.tools.quality.phan.remote

import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Tag
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.config.interpreters.PhpSdkDependentConfiguration
import com.jetbrains.php.tools.quality.QualityToolConfiguration
import dev.mirrorkt.jetbrains.phan.PhanBundle
import dev.mirrorkt.jetbrains.tools.quality.phan.PhanConfiguration
import org.jetbrains.annotations.Nls

@Tag("phan_by_interpreter")
class PhanRemoteConfiguration : PhanConfiguration(), PhpSdkDependentConfiguration {
    companion object {
        fun getDefaultName(@Nls interpreterName: String?): @Nls String = if (interpreterName.isNullOrEmpty()) {
            PhanBundle.message("undefined.interpreter")
        } else {
            interpreterName
        }
    }

    private var myInterpreterId: String? = null

    override fun getInterpreterId(): String? = myInterpreterId

    override fun setInterpreterId(interpreterId: String) {
        myInterpreterId = interpreterId
    }

    override fun getPresentableName(project: Project?): String = getDefaultName(
        PhpInterpretersManagerImpl.getInstance(project).findInterpreterName(getInterpreterId())
    )

    override fun getId(): String {
        val interpreterId = myInterpreterId
        return if (interpreterId.isNullOrEmpty()) {
            PhanBundle.message("undefined.interpreter")
        } else {
            interpreterId
        }
    }

    override fun clone(): QualityToolConfiguration {
        val settings = PhanRemoteConfiguration()
        settings.myInterpreterId = myInterpreterId
        clone(settings)

        return settings
    }

    override fun serialize(path: String?): String? = path

    override fun deserialize(path: String?): String? = path
}
