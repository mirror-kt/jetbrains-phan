package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager

class PhanConfigurationManager(project: Project?) : QualityToolConfigurationManager<PhanConfiguration>(project) {
    init {
        if (project != null) {
            myProjectManager = project.service<PhanProjectConfigurationManager>()
        }
        myApplicationManager = ApplicationManager.getApplication().service<PhanAppConfigurationManager>()
    }

    @State(name = "Phan", storages = [Storage("php.xml")])
    class PhanProjectConfigurationManager : PhanConfigurationBaseManager()

    @State(name = "Phan", storages = [Storage("php.xml")])
    class PhanAppConfigurationManager : PhanConfigurationBaseManager()
}
