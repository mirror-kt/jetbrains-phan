package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.util.NlsSafe
import com.intellij.util.xmlb.XmlSerializer
import com.jetbrains.php.tools.quality.QualityToolConfigurationBaseManager
import com.jetbrains.php.tools.quality.QualityToolType
import org.jdom.Element

@Suppress("UnstableApiUsage")
open class PhanConfigurationBaseManager : QualityToolConfigurationBaseManager<PhanConfiguration>() {
    companion object {
        @NlsSafe
        private const val PHAN_PATH = "PhanPath"

        @NlsSafe
        private const val ROOT_NAME = "Phan_settings"
    }

    override fun getQualityToolType(): QualityToolType<PhanConfiguration> = PhanQualityToolType

    override fun getOldStyleToolPathName(): String = PHAN_PATH

    override fun getConfigurationRootName(): String = ROOT_NAME

    override fun loadLocal(element: Element): PhanConfiguration? =
        XmlSerializer.deserialize(element, PhanConfiguration::class.java)
}
