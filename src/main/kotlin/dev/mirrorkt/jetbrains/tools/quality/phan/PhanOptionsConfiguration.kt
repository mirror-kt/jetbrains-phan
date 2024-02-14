package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.jetbrains.php.tools.quality.QualityToolsOptionsConfiguration

@Service(Service.Level.PROJECT)
@State(name = "PhanOptionsConfiguration", storages = [Storage("php.xml")])
class PhanOptionsConfiguration : QualityToolsOptionsConfiguration(),
    PersistentStateComponent<PhanOptionsConfiguration> {
    companion object {
        fun getInstance(project: Project): PhanOptionsConfiguration = project.service()
    }

    override fun getState(): PhanOptionsConfiguration = this

    override fun loadState(state: PhanOptionsConfiguration) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
