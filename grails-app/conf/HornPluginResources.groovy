modules = {
    'horn' {
        dependsOn 'jquery'
        dependsOn 'jquery-json'
        resource id: 'horn-jquery', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-jquery.js"]
    }

    'horn-css' {
        dependsOn 'horn'
        resource id: 'horn-jquery-CSS', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-jquery-CSS.js"]
    }

    'horn-html5' {
        dependsOn 'horn'
        resource id: 'horn-jquery-HTML5', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-jquery-HTML5.js"]
    }

    'horn-extract' {
        dependsOn 'horn'
        resource id: 'horn-jquery-extract', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-jquery-extract.js"]
    }

    'horn-converters' {
        dependsOn 'horn'
        resource id: 'horn-jquery-converters', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-converters.js"]
    }
}