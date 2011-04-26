/**
 *  Grails Horn Plugin
 *
 *  @author Chris Denman
 *  @author Marc Palmer
 */
class HornTagLib {

    static namespace = 'h'


    def div = { attrs, body ->
        def path = attrs.remove( "path")
        attrs.tagName = "div"
        if ( path ) {attrs.path = encodeCSS( path)}
        container.clone().call( attrs, body)
    }

    static def PATTERN_PPN_ARRAY_INDICES = /\[([0-9]+)\]/
    static def PATTERN_PPN_DEREFERENCE = /\./

    static def PATTERN_PP_ARRAY_INDICES = /-([0-9]+)/
    static def PATTERN_PP_DEREFERENCE = /-/

    def span = { attrs, body ->
        def path = attrs.remove( "path")
        if ( !attrs.value ) {
            attrs.tagName = "span"
        }
        if ( path ) {attrs.path = encodeCSS( path)}
        value.clone().call( attrs, body)
    }

    def container = { attrs, body ->

        def path = attrs.remove( "path")
        if ( !path ) {
            throw new RuntimeException( message( code: 'hornTagLib.container.noPath'));
        }

        def tagName = attrs.remove( "tagName")
        if ( !tagName ) {
            throw new RuntimeException( "tagName attribute must be supplied.");  // @todo i18n tagexception
        }

        def newClassAttr = [
            attrs.remove( "class") ?: "",
            attrs.remove( "root") ? "horn" : "",
            path ?: ""].join( " ").trim()

        def sb = new StringBuilder()
        sb.append "<"
        sb.append tagName
        sb.append " class=\""
        sb.append newClassAttr.encodeAsHTML()
        sb.append "\" "
        out << sb.toString()
        out << outputAttributes( attrs)

        sb = new StringBuilder()
        sb.append ">"
        sb.append body()
        sb.append "</$tagName>"
        out << sb.toString()
    }

    def value = { attrs, body ->
        def path = attrs.remove( "path")
        def isJSON = attrs.remove( "json")

        if ( !path && !isJSON ) {
            throw new RuntimeException( "Must declare a path or declare as json.");  // @todo i18n + tagexception
        }

        def tagName = attrs.remove( "tagName")
        def abbrValue = attrs.remove( "value")
        if ( abbrValue ) {
            if ( tagName ) {
                throw new RuntimeException( "Don't supply tagName attribute if value attribute specified.");  // @todo i18n + tagexception
            }
            tagName = 'abbr'
        }

        if ( !tagName ) {
            throw new RuntimeException( "tagName attribute must be supplied if not using value attribute.");  // @todo i18n + tagexception
        }

        def bodyValue = body()

        def newClassAttr = [
            attrs.remove( "class") ?: "",
            path ?: "",
            isJSON ? "hidden data-json" : ""].join( " ").trim()

        def sb = new StringBuilder()
        sb.append "<$tagName class=\"${newClassAttr.encodeAsHTML()}\""
        if ( abbrValue ) {
            sb.append " title=\"${abbrValue.encodeAsHTML()}\""
        }
        sb.append ">"
        sb.append bodyValue
        sb.append "</$tagName>"

        out << sb.toString()
    }

    protected void outputAttributes( attrs ) {
        attrs.each { k,v ->
            out << k << "=\"" << v.encodeAsHTML() << "\" "
        }
    }

    protected def encodeCSS( path ) {
        path = ((path =~
            HornTagLib.PATTERN_PPN_ARRAY_INDICES).replaceAll( "-\$1") =~
            HornTagLib.PATTERN_PPN_DEREFERENCE).replaceAll( "-")

        path = (path.endsWith( "-") ? path.substring( 0, path.length - 1) : path)
        "_" + (path.startsWith( "-") ? path.substring( 1) : path)
    }

    protected def decodeCSS( propertyPath ) {
        propertyPath = "-" + (propertyPath.startsWith( "_") ? propertyPath.substring( 1) : propertyPath)
        ((propertyPath =~ HornTagLib.PATTERN_PP_ARRAY_INDICES).replaceAll( "[\$1]") =~ HornTagLib.PATTERN_PP_DEREFERENCE).replaceAll( ".")
    }
}