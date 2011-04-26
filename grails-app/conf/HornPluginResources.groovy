modules = {
    'jquery-json' {
        dependsOn 'jquery'
        resource
            url:[
                dir:'js/jquery/plugins',
                file:'jquery.json-2.2.min.js']
    }

    'horn' {
        dependsOn 'jquery-json'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-jquery.js"]
            nominify: false
    }

    'horn-converters' {
        dependsOn 'horn'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-converters.js"]
            nominify: false
    }
}
