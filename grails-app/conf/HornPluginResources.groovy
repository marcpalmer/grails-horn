modules = {
    'smutils' {
        resource id: 'smutils', url: [ plugin: 'horn-jquery', dir:'js/jquery/horn', file:"sm-utils.js"]
    }

    'horn' {
        dependsOn 'smutils'
        dependsOn 'jquery'
        dependsOn 'jquery-json'
        resource id: 'horn-jquery', url: [ plugin: 'horn-jquery', dir:'js/jquery/horn', file:"horn-jquery-1.0.js"]
    }

    'horn-css' {
        dependsOn 'horn'
        resource id: 'horn-jquery-css', url: [ plugin: 'horn-jquery', dir:'js/jquery/horn', file:"horn-jquery-css-1.0.js"]
    }

    'horn-html5' {
        dependsOn 'horn'
        resource id: 'horn-jquery-html', url: [ plugin: 'horn-jquery', dir:'js/jquery/horn', file:"horn-jquery-html5-1.0.js"]
    }

    'horn-converters' {
        dependsOn 'horn'
        resource id: 'horn-jquery-converters', url: [ plugin: 'horn-jquery', dir:'js/jquery/horn', file:"horn-converters-1.0.js"]
    }
}