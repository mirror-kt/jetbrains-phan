package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.jetbrains.php.tools.quality.QualityToolsOptionsConfiguration

@Service(Service.Level.PROJECT)
@State(name = "PhanOptionsConfiguration", storages = [Storage("php.xml")])
class PhanOptionsConfiguration : QualityToolsOptionsConfiguration(),
    PersistentStateComponent<PhanOptionsConfiguration> {
    override fun getState(): PhanOptionsConfiguration = this

    override fun loadState(state: PhanOptionsConfiguration) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
