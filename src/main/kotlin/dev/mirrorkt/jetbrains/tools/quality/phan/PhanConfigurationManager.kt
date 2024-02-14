package dev.mirrorkt.jetbrains.tools.quality.phan

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager

@Service(Service.Level.PROJECT)
class PhanConfigurationManager(project: Project?) : QualityToolConfigurationManager<PhanConfiguration>(project) {
    companion object {
        fun getInstance(project: Project): PhanConfigurationManager = project.service()
    }

    init {
        if (project != null) {
            myProjectManager = project.service<PhanProjectConfigurationManager>()
        }
        myApplicationManager = ApplicationManager.getApplication().service<PhanAppConfigurationManager>()
    }

    @Service(Service.Level.PROJECT)
    @State(name = "Phan", storages = [Storage("php.xml")])
    class PhanProjectConfigurationManager : PhanConfigurationBaseManager()

    @Service(Service.Level.APP)
    @State(name = "Phan", storages = [Storage("php.xml")])
    class PhanAppConfigurationManager : PhanConfigurationBaseManager()
}
