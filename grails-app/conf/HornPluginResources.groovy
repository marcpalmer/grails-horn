modules = {
    'horn' {
        dependsOn 'jquery'
        dependsOn 'jquery-json'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-jquery.js"]
    }

    'horn-css' {
        dependsOn 'horn'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-jquery-CSS.js"]
    }

    'horn-html5' {
        dependsOn 'horn'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-jquery-HTML5.js"]
    }

    'horn-extract' {
        dependsOn 'horn'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-extract.js"]
    }

    'horn-converters' {
        dependsOn 'horn-extract'
        resource
            url: [
                dir:'js/jquery/horn',
                file:"horn-converters.js"]
    }
}
