package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.codeInspection.InspectionProfile
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.jetbrains.php.tools.quality.QualityToolAnnotator
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo
import com.jetbrains.php.tools.quality.QualityToolMessageProcessor
import com.jetbrains.php.tools.quality.QualityToolType

object PhanAnnotator : QualityToolAnnotator<PhanValidationInspection>() {
    private val LOGGER = thisLogger()

    override fun getQualityToolType(): QualityToolType<*> = PhanQualityToolType

    override fun getOptions(
        filePath: String?,
        inspection: PhanValidationInspection,
        profile: InspectionProfile?,
        project: Project
    ): List<String> = emptyList()

    override fun createMessageProcessor(collectedInfo: QualityToolAnnotatorInfo<*>): QualityToolMessageProcessor =
        object : QualityToolMessageProcessor(collectedInfo) {
            override fun parseLine(line: String?) {
                // nothing to do
                // TODO
                LOGGER.warn(line)
            }

            override fun done() {
                // nothing to do
                LOGGER.warn("done")
            }

            override fun getQualityToolType(): QualityToolType<*> = PhanQualityToolType
        }
}
