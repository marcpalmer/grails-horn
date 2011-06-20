modules = {
    'horn' {
        dependsOn 'jquery'
        dependsOn 'jquery-json'
        resource id: 'horn-jquery', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-jquery-1.0.js"]
    }

    'horn-css' {
        dependsOn 'horn'
        resource id: 'horn-jquery-css', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-jquery-css-1.0.js"]
    }

    'horn-html' {
        dependsOn 'horn'
        resource id: 'horn-jquery-html', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-jquery-html-1.0.js"]
    }

    'horn-converters' {
        dependsOn 'horn'
        resource id: 'horn-jquery-converters', url: [ plugin: 'horn', dir:'js/jquery/horn', file:"horn-converters-1.0.js"]
    }
}