class HornJqueryGrailsPlugin {
    def version = "1.0.53"
    def grailsVersion = "1.3.1 > *"
    def dependsOn = [jquery: "1.4 > *", jqueryJson: "2.2.2 > *"]
    def pluginExcludes = []
    def author = "Chris Denman"
    def authorEmail = "chrisdenman@me.com"
    def description = 'Provides the JS libraries and tags for embedding data in your HTML content.'
    def documentation = "http://grails.org/plugin/horn"
    def title = "Horn"
}