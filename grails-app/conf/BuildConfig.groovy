grails.project.source.level = 1.7
grails.project.target.level = 1.7
grails.servlet.version = '3.0'

grails.project.dependency.resolver = 'maven' // or ivy

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
    }

    plugins {
        build ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}
