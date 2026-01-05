import de.florianmichael.baseproject.configureApplication
import de.florianmichael.baseproject.configureExampleSourceSet
import de.florianmichael.baseproject.configureShadedDependencies
import de.florianmichael.baseproject.setupProject
import de.florianmichael.baseproject.setupPublishing

plugins {
    id("de.florianmichael.baseproject.BaseProject")
}

setupProject()
setupPublishing()
configureExampleSourceSet()
configureApplication()

val shade = configureShadedDependencies()

dependencies {
    shade("com.fifesoft:rsyntaxtextarea:3.6.1")
    shade("com.formdev:flatlaf:3.7")
}
