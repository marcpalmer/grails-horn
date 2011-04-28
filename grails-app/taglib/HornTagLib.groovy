/**
 *  Grails Horn Plugin
 *
 *  @author Chris Denman
 *  @author Marc Palmer
 */
class HornTagLib {

    static namespace = 'h'

    static def PATTERN_PPN_ARRAY_INDICES = /(\[([0-9]+)\])/
    static def PATTERN_PPN_DEREFERENCE = /\./

    static def PATTERN_PP_ARRAY_INDICES = /(-([0-9]+))/
    static def PATTERN_PP_DEREFERENCE = /-/


    static void outputAttribute( sb, attributeName, attrs ) {
        if ( attrs ) {
            sb.append " "
            sb.append attributeName
            sb.append "=\""
            sb.append attrs.join( " ").encodeAsHTML().trim()
            sb.append "\""
        }
    }

    static void outputAttributes( sb, attrs ) {
        if ( attrs ) {
            attrs.each { k, v ->
                sb.append " "
                sb.append k
                sb.append "=\""
                sb.append v.encodeAsHTML().trim()
                sb.append "\""
            }
        }
    }

    static def encodeCSS( path ) {
        path = path.replaceAll(
            HornTagLib.PATTERN_PPN_ARRAY_INDICES, "-\$2").replaceAll(
            HornTagLib.PATTERN_PPN_DEREFERENCE, "-")
        if ( path.startsWith( "-") ) {
            path = "_${path.substring( "-".length())}" }
        !path.startsWith( "_") ?  "_$path" : path
    }

    static def decodeCSS( path ) {
        path = path.replaceFirst( "_", "-").replaceAll(
            HornTagLib.PATTERN_PP_ARRAY_INDICES, "[\$2]").replaceAll(
            HornTagLib.PATTERN_PP_DEREFERENCE, ".")
        path.startsWith( ".") ? path.substring( ".".length()) : path
    }

    static def attributeWithDefault( attributes, attributeName,
        defaultValue = "" ) {
        def attributeValue = attributes.remove( attributeName)

        attributeValue ? attributeValue.trim() : defaultValue
    }


    static def buildAttributeValue( attributeValue, attrs = []) {
        if ( attributeValue ) { attrs << attributeValue.toString().trim() }

        attrs
    }

    static def isAttributeTruth( attrs, attributeName ) {
        new Boolean( attrs.remove( attributeName))
    }

    def grailsApplication

    def hornTag = { attrs, body ->
        def tag = safeRemoveAttribute( attrs, 'tag', 'value')
        def path = HornTagLib.attributeWithDefault( attrs, "path")
        def isJSON = HornTagLib.isAttributeTruth( attrs, "json")
        if ( !path && !isJSON ) {
            throwTagError( "One of or both of the \"path\" " +
                "or \"json\" attributes must be supplied for the  " +
                "\"<value>\" tag.")
        }
        def sb = new StringBuilder()
        sb.append "<"
        sb.append tag
        def newClassAttrs =
            HornTagLib.buildAttributeValue(
                HornTagLib.attributeWithDefault( attrs, "class"))
        if ( HornTagLib.isAttributeTruth( attrs, "root") ) {
            HornTagLib.buildAttributeValue( "horn", newClassAttrs) }
        if ( isJSON ) { HornTagLib.buildAttributeValue(
            "${grailsApplication.config.hiddenCSSClass} data-json",
            newClassAttrs) }
        HornTagLib.buildAttributeValue( path, newClassAttrs)
        HornTagLib.outputAttribute( sb, "class", newClassAttrs)
        HornTagLib.outputAttributes( sb, attrs)
        sb.append ">"
        sb.append body()
        sb.append "</"
        sb.append tag
        sb.append ">"
        out << sb
    }

    def div = { attrs, body ->
        attrs.tag = "div"
        attrs.path = HornTagLib.encodeCSS( attrs.path)
        hornTag( attrs, body)
    }

    def span = { attrs, body ->
        attrs.tag = "span"
        attrs.path = HornTagLib.encodeCSS( attrs.path)
        hornTag( attrs, body)
    }

    def abbr = { attrs, body ->
        def value = safeRemoveAttribute( attrs, 'value', 'abbr')
        attrs.tag = 'abbr'
        attrs.title = value

        attrs.path = HornTagLib.encodeCSS( attrs.path)
        hornTag( attrs, body)
    }

    protected def safeRemoveAttribute( attributes, attributeName, tag ) {
        def attributeValue = attributes.remove( attributeName)
        if ( !attributeValue ) { throwTagError(
            "Attribute \"${attributeName}\" is required for the \"<${tag}>\" tag.") }
        attributeValue.toString().trim()
    }
}