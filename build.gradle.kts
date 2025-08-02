import de.florianmichael.baseproject.configureExampleSourceSet
import de.florianmichael.baseproject.setupProject
import de.florianmichael.baseproject.setupPublishing

plugins {
    id("de.florianmichael.baseproject.BaseProject")
}

setupProject()
setupPublishing()
configureExampleSourceSet()
