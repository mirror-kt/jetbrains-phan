package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.util.xmlb.XmlSerializerUtil
import com.jetbrains.php.tools.quality.QualityToolProjectConfiguration
import com.jetbrains.php.tools.quality.QualityToolType

@Service(Service.Level.PROJECT)
@State(name = "PhanProjectConfiguration", storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
class PhanProjectConfiguration : QualityToolProjectConfiguration<PhanConfiguration>(),
    PersistentStateComponent<PhanProjectConfiguration> {
    override fun getState(): PhanProjectConfiguration = this

    override fun loadState(state: PhanProjectConfiguration) {
        XmlSerializerUtil.copyBean(state, this)
    }

    override fun getQualityToolType(): QualityToolType<PhanConfiguration> = PhanQualityToolType
}
