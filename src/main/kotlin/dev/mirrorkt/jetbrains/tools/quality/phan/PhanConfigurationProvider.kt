package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.extensions.ExtensionPointName
import com.jetbrains.php.tools.quality.QualityToolConfigurationProvider

abstract class PhanConfigurationProvider : QualityToolConfigurationProvider<PhanConfiguration>() {
    companion object {
        private val LOGGER = thisLogger()
        private val EP_NAME: ExtensionPointName<PhanConfigurationProvider> =
            ExtensionPointName.create("dev.mirror-kt.jetbrains.phan.phanConfigurationProvider")

        fun getInstances(): PhanConfigurationProvider? {
            val extensions = EP_NAME.extensionList
            if (extensions.size > 1) {
                LOGGER.error("Several providers for remote Phan configuration was found")
            }
            return extensions.firstOrNull()
        }
    }
}
