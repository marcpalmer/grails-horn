class HornGrailsPlugin {
    def version = "1.0.28"
    def grailsVersion = "1.3.1 > *"
    def dependsOn = [jquery: "1.4 > *", jqueryJson: "2.2.2 > *"]
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/views/example.gsp",
        "grails-app/views/example_html5.gsp",
        "grails-app/views/layouts/hornded.gsp",
        "grails-app/views/layouts/hornded_html5.gsp"]
    def author = "Chris Denman"
    def authorEmail = "chrisdenman@me.com"
    def title = "Horn"
    def description = 'Easy HTML data embedding.'
    def documentation = "http://grails.org/plugin/horn"
}
