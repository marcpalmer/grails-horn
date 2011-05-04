/**
 *  Grails Horn Plugin
 *
 *  @author Chris Denman
 *  @author Marc Palmer
 *
 *  @todo custom <json> tag?
 */
class HornTagLib {

    static namespace = 'horn'

    static def KEY_PREFIX_HORNTAGLIB = "com.grailsrocks.horn.hornTagLib"
    static def KEY_TEMPLATING = HornTagLib.KEY_PREFIX_HORNTAGLIB + ".templating"

    static def PATTERN_PPN_ARRAY_INDICES = /(\[([0-9]+)\])/
    static def PATTERN_PPN_DEREFERENCE = /\./

    static def PATTERN_PP_ARRAY_INDICES = /(-([0-9]+))/
    static def PATTERN_PP_DEREFERENCE = /-/

    protected depth = 0

    static def removeAttribute( attributes, attributeName,
        defaultValue = "" ) {
        def attributeValue = attributes.remove( attributeName)

        attributeValue ? (attributeValue instanceof String ?
            attributeValue.trim() : attributeValue) : defaultValue
    }


    static def addAttributeValue( attributeValue, attrs = []) {
        if ( attributeValue && !attrs.contains( attributeValue.trim()) ) {
            attrs << attributeValue.toString().trim()
        }

        attrs
    }

    static def addAttributeValues( existingAttributeValue, attrs = [] ) {
        existingAttributeValue.split( " ").each { token ->
            addAttributeValue( token.trim(), attrs)
        }

        attrs
    }

    static void outputAttribute( out, attributeName, attrs ) {
        if ( attrs ) {
            out << " "
            out << attributeName
            out << "=\""
            out << attrs.join( " ").encodeAsHTML().trim()
            out << "\""
        }
    }

    static void outputAttributes( out, attrs ) {
        if ( attrs ) {
            attrs.each { k, v ->
                out << " "
                out << k
                out << "=\""
                out << v.encodeAsHTML().trim()
                out << "\""
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

    static def isAttributeTruth( attrs, attributeName ) {
        new Boolean( attrs.remove( attributeName))
    }

    def grailsApplication

    protected def safeRemoveAttribute( attributes, attributeName, tag ) {
        def attributeValue = attributes.remove( attributeName)
        if ( !attributeValue ) { throwTagError(
            "The \"${attributeName}\" attribute is required for the \"<${tag}>\" tag.") }

        attributeValue.toString().trim()
    }

    def templating = { attrs, body ->
        request[ HornTagLib.KEY_TEMPLATING] =
            HornTagLib.isAttributeTruth( attrs, "value")
    }

    def hornTag = { attrs, body ->
        def templating = request[ HornTagLib.KEY_TEMPLATING] == true
        def tag = safeRemoveAttribute( attrs, 'tag', 'hornTag')
        def path = HornTagLib.removeAttribute( attrs, "path")
        def isJSON = HornTagLib.isAttributeTruth( attrs, "json")
        def isLevel0 = depth == 0
        if ( !templating && (!path && !isJSON) ) {
            throwTagError( "One of or both of the \"path\" " +
                "or \"json\" attributes must be supplied for the  " +
                "\"<hornTag>\" tag when not in templating mode.")
        }
        def userSuppliedRoot = HornTagLib.isAttributeTruth( attrs, "root")
        if ( !userSuppliedRoot ) { depth++ }
        def bodyValue = body()
        if ( !userSuppliedRoot ) { depth-- }
        out << "<"
        out << tag
        def newClassAttrs = addAttributeValues(
            HornTagLib.removeAttribute( attrs, "class"))


        def emptyBodyClass = attrs.remove('emptyBodyClass')?.trim()
        if ( !templating ) {
            if ( (bodyValue?.trim() == '') && (emptyBodyClass != null) && (emptyBodyClass != "") ) {
                HornTagLib.addAttributeValue( emptyBodyClass, newClassAttrs)
            }
        }

        if ( userSuppliedRoot || (!templating && isLevel0) ) {
            HornTagLib.addAttributeValue( "horn", newClassAttrs)
        }

        if ( isLevel0 && templating ) {
            HornTagLib.addAttributeValue(
                "${grailsApplication.config.hiddenCSSClass ?: 'hidden'}",
                newClassAttrs)
        }

        if ( isJSON ) {
            HornTagLib.addAttributeValue(
                "${grailsApplication.config.hiddenCSSClass ?: 'hidden'}",
                newClassAttrs)
            HornTagLib.addAttributeValue(
                "data-json",
                newClassAttrs)
        }

        if ( !templating && path ) {
            path = HornTagLib.encodeCSS( path)
            HornTagLib.addAttributeValue( path, newClassAttrs)
        }

        HornTagLib.outputAttribute( out, "class", newClassAttrs)
        HornTagLib.outputAttributes( out, attrs)
        out << ">"
        out << bodyValue
        out << "</"
        out << tag
        out << ">"
    }

    def a =         { attrs, body ->
                        attrs.tag = "a"
                        hornTag( attrs, body) }
    def abbr =      { attrs, body ->
                        def value = safeRemoveAttribute(
                            attrs, 'value', 'abbr')
                        attrs.tag = 'abbr'
                        attrs.title = value
                        hornTag( attrs, body) }
    def b =         { attrs, body ->
                        attrs.tag = "b"
                        hornTag( attrs, body) }
    def base =      { attrs, body ->
                        attrs.tag = "base"
                        hornTag( attrs, body) }
    def body =      { attrs, body ->
                        attrs.tag = "body"
                        hornTag( attrs, body) }
    def br =        { attrs, body ->
                        attrs.tag = "br"
                        hornTag( attrs, body) }
    def button =    { attrs, body ->
                        attrs.tag = "button"
                        hornTag( attrs, body) }
    def caption =   { attrs, body ->
                        attrs.tag = "caption"
                        hornTag( attrs, body) }
    def col =       { attrs, body ->
                        attrs.tag = "col"
                        hornTag( attrs, body) }
    def colgroup =  { attrs, body ->
                        attrs.tag = "colgroup"
                        hornTag( attrs, body) }
    def div =       { attrs, body ->
                        attrs.tag = "div"
                        hornTag( attrs, body) }
    def fieldset =  { attrs, body ->
                        attrs.tag = "fieldset"
                        hornTag( attrs, body) }
    def form =      { attrs, body ->
                        attrs.tag = "form"
                        hornTag( attrs, body) }
    def head =      { attrs, body ->
                        attrs.tag = "head"
                        hornTag( attrs, body) }
    def html =      { attrs, body ->
                        attrs.tag = "html"
                        hornTag( attrs, body) }
    def h1 =        { attrs, body ->
                        attrs.tag = "h1"
                        hornTag( attrs, body) }
    def h2 =        { attrs, body ->
                        attrs.tag = "h2"
                        hornTag( attrs, body) }
    def h3 =        { attrs, body ->
                        attrs.tag = "h3"
                        hornTag( attrs, body) }
    def h4 =        { attrs, body ->
                        attrs.tag = "h4"
                        hornTag( attrs, body) }
    def h5 =        { attrs, body ->
                        attrs.tag = "h5"
                        hornTag( attrs, body) }
    def h6 =        { attrs, body ->
                        attrs.tag = "h6"
                        hornTag( attrs, body) }
    def input =     { attrs, body ->
                        attrs.tag = "input"
                        hornTag( attrs, body) }
    def label =     { attrs, body ->
                        attrs.tag = "label"
                        hornTag( attrs, body) }
    def legend =    { attrs, body ->
                        attrs.tag = "legend"
                        hornTag( attrs, body) }
    def li =        { attrs, body ->
                        attrs.tag = "li"
                        hornTag( attrs, body) }
    def link =      { attrs, body ->
                        attrs.tag = "link"
                        hornTag( attrs, body) }
    def object =    { attrs, body ->
                        attrs.tag = "object"
                        hornTag( attrs, body) }
    def ol =        { attrs, body ->
                        attrs.tag = "ol"
                        hornTag( attrs, body) }
    def optgroup =  { attrs, body ->
                        attrs.tag = "optgroup"
                        hornTag( attrs, body) }
    def option =    { attrs, body ->
                        attrs.tag = "option"
                        hornTag( attrs, body) }
    def p =         { attrs, body ->
                        attrs.tag = "p"
                        hornTag( attrs, body) }
    def pre =       { attrs, body ->
                        attrs.tag = "pre"
                        hornTag( attrs, body) }
    def script =    { attrs, body ->
                        attrs.tag = "script"
                        hornTag( attrs, body) }
    def select =    { attrs, body ->
                        attrs.tag = "select"
                        hornTag( attrs, body) }
    def span =      { attrs, body ->
                        attrs.tag = "span"
                        hornTag( attrs, body) }
    def strong =    { attrs, body ->
                        attrs.tag = "strong"
                        hornTag( attrs, body) }
    def style =     { attrs, body ->
                        attrs.tag = "style"
                        hornTag( attrs, body) }
    def sub =       { attrs, body ->
                        attrs.tag = "sub"
                        hornTag( attrs, body) }
    def sup =       { attrs, body ->
                        attrs.tag = "sup"
                        hornTag( attrs, body) }
    def table =     { attrs, body ->
                        attrs.tag = "table"
                        hornTag( attrs, body) }
    def tbody =     { attrs, body ->
                        attrs.tag = "tbody"
                        hornTag( attrs, body) }
    def textares =  { attrs, body ->
                        attrs.tag = "textares"
                        hornTag( attrs, body) }
    def tfoot =     { attrs, body ->
                        attrs.tag = "tfoot"
                        hornTag( attrs, body) }
    def thead =     { attrs, body ->
                        attrs.tag = "thead"
                        hornTag( attrs, body) }
    def td =        { attrs, body ->
                        attrs.tag = "td"
                        hornTag( attrs, body) }
    def th =        { attrs, body ->
                        attrs.tag = "th"
                        hornTag( attrs, body) }
    def tr =        { attrs, body ->
                        attrs.tag = "tr"
                        hornTag( attrs, body) }
    def tt =        { attrs, body ->
                        attrs.tag = "tt"
                        hornTag( attrs, body) }
    def ul =        { attrs, body ->
                        attrs.tag = "ul"
                        hornTag( attrs, body) }
}