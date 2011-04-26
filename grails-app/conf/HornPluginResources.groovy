modules = {
    'jquery-json' {
        dependsOn 'jquery'
        resource
            url:[
                dir:'js/jquery/plugins/json',
                file:'jquery.json-2.2.min.js']
    }

    'horn' {
        dependsOn 'jquery-json'
        resource
            url: [
                dir:'js/horn/jquery',
                file:"horn.js"]
            nominify: false
    }
}
