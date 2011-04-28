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

    'horn-css' {
        dependsOn 'horn'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-jquery-CSS.js"]
            nominify: false
    }

    'horn-html5' {
        dependsOn 'horn'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-jquery-HTML5.js"]
            nominify: false
    }


    'horn-create' {
        dependsOn 'horn'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-create.js"]
            nominify: false
    }

    'horn-extract' {
        dependsOn 'horn, horn-create'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-extract.js"]
            nominify: false
    }

    'horn-converters' {
        dependsOn 'horn-extract'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-converters.js"]
            nominify: false
    }
}
