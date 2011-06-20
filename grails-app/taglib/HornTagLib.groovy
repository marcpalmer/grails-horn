/**
 *  Grails Horn Plugin Tag Library
 *  <p>
 *  Horn-domain specific tags and utility closures.
 *
 *  @author Chris Denman
 *  @author Marc Palmer
 */
class HornTagLib {

    static namespace = 'horn'

    /**
     *  Prefix for request attributes to hopefully make them unique.
     */
    static KEY_PREFIX_HORNTAGLIB = "com.grailsrocks.horn.hornTagLib"

    /**
     *  Request key for the templating option.
     */
    static KEY_TEMPLATING = HornTagLib.KEY_PREFIX_HORNTAGLIB + ".templating"

    static PATTERN_PPN_ARRAY_INDICES = /(\[([0-9X]+)\])/
    static PATTERN_PPN_DEREFERENCE = /\./

    static PATTERN_PP_ARRAY_INDICES = /(-([0-9X]+))/
    static PATTERN_PP_DEREFERENCE = /-/

    protected depth = 0

    /**
     *  Remove a named attribute from a collection of attributes 
     *  (name/value pairs).
     *  <p>
     *  If the attriubte was located and removed, its former value is returned
     *  else, the <code>defaultValue</code> supplied is returned, else 
     *  <code>""</code>.
     *
     *  @param attributes the attributes from which to remove the given named 
     *      attriubte
     *  @param attributeName the name of the attriubute to remove 
     *  @param defaultValue the default value returned if an attribute was not 
     *      removed (default value <code>""</code>)
     *      
     *  @return the value of the attribute just removed, else 
     *      <code>defaultValue</code> is returned
     */
    static removeAttribute( attributes, attributeName, defaultValue = "" ) {
        def attributeValue = attributes.remove( attributeName)

        attributeValue ? attributeValue.toString() : defaultValue
    }

    /**
     *  Add an attriubte to a given collection if <code>condition == true</code>.
     * 
     *  @param attributeName the name of the attribute to add 
     *  @param attributeValue the value of the attribute to add
     *  @param attrs the attribute collection to add to
     *  @param condition if <code>true</code> the given attribute will be added, 
     *      false otherwise 
     *  
     *  @return the attribute collection <code>attrs</code> passed in will be 
     *      returned
     */
    static addAttributeIf( attributeName, attributeValue, attrs = [],
        condition) {
        if ( condition ) { attrs[ attributeName] = attributeValue }

        attrs
    }

    /**
     *  Add an attribute value to a collection of attribute values.
     *  <p>
     *  The value is not added if it is already contained in 
     *  <code>attributeValues</code>.    
     * 
     *  @param attributeValue the attriubte value to add 
     *      (if not already present)
     *  @param attributeValues the attribute value collection (optional with 
     *      default value <code>[]></code>
     *  
     *  @return the possibly altered attribute value collection 
     *      <code>attributeValues</code>
     */
    static addAttributeValue( attributeValue, attributeValues = []) {
        if ( attributeValue &&
            !attributeValues.contains( attributeValue.trim()) ) {
            attributeValues << attributeValue.toString().trim()
        }

        attributeValues
    }

    /**
     *  Add an attribute value to a collection of attriubte values if
     *  <code>condition</code> is <code>true</code>.
     *  <p>
     *  If the attribute value is already present in the collection, calling
     *  this method will have no effect, regardless of the value of
     *  <code>condition</code>.
     *
     *  @param attributeValue the attribute value to add
     *  @param attributeValues the attrituve value collection to add to
     *  @param condition if <code>true</code>, add the attribute value,
     *      otherwise don't.
     *
     *  @return the possibly altered attribute value collection
     *      <code>attributeValues</code>
     */
    static addAttributeValueIf( attributeValue, attributeValues = [],
        condition = false ) {
        if ( condition ) {
            addAttributeValue( attributeValue, attributeValues)
        }

        attributeValues
    }

    /**
     *  Split a value (converted to a <code>String</code>) and add the trimmed 
     *  tokens to an attribute value colleciton.   
     *  <p>
     *  Value is split using <code>" "</code> as a delimiter.    
     *
     *  @param value the value to split
     *  @param attributeValues the attribute value collection to add to
     * 
     *  @return the possibly altered attribute value collection
     *      <code>attributeValues</code>
     */
    static splitAddAttributeValues( value, attributeValues = [] ) {
        value.toString().split( " ").each { token ->
            addAttributeValue( token.trim(), attributeValues)
        }

        attributeValues
    }

    /**
     *  Output the given 'multi-valued' attribute.
     *  <p>
     *  Nothing is written if <code>attributeValues</code> is empty.         
     *
     *  @param out the <code>PrintWriter<code> to write to @todo check this
     *  @param attributeName the name of the html tag's attribute to write
     *  @param attributeValues the attribute values to output 
     */
    static void outputAttribute( out, attributeName, attributeValues ) {
        if ( attributeValues ) {
            out << " ${attributeName}" +
                "=\"${attributeValues.join( " ").encodeAsHTML().trim()}\""
        }
    }

    /**
     *  Output the given set of attributes.
     *  <p>
     *  Nothing is done if <code>attrs</code> is empty.    
     * 
     *  @param out the <code>PrintWriter<code> to write to @todo check this
     *  @param attrs the attributes to output
     */
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

    /**
     *  Remove and determine the <code>Boolean</code> truth value of an
     *  attribute named within a colleciton.
     *
     *  @param attrs the the attribute collection
     *  @param attributeName the name of the attribute to insepct
     *
     *  @return <code>true</code> if the given named attribute represented
     *      a Groovy <code>true</code> value, false otherwise
     */
    static isAttributeTruth( attrs, attributeName ) {
        new Boolean( attrs.remove( attributeName))
    }

    /**
     *  Converts a Horn path in JavaScript form to CSS form.
     *
     *  @param path the horn path in JavaScript form
     *
     *  @return <code>path</code> converted to CSS form
     */
    static jsToCSSForm( path ) {
        if ( path.startsWith( "/") ) { path = path.substring( 1) }
        path = path.replaceAll(
            HornTagLib.PATTERN_PPN_ARRAY_INDICES, "-\$2").replaceAll(
            HornTagLib.PATTERN_PPN_DEREFERENCE, "-")
        if ( path.startsWith( "-") ) { path = path.substring( 1) }
        !path.startsWith( "_") ?  "_$path" : path
    }

    /**
     *  Converts a Horn path in CSS form to JavaScript form.
     *
     *  @param path the horn path in CSS form
     *
     *  @return <code>path</code> converted to JavaScript form
     */
    static cssToJSForm( path ) {
        path = path.replaceFirst( "_", "-").replaceAll(
            HornTagLib.PATTERN_PP_ARRAY_INDICES, "[\$2]").replaceAll(
            HornTagLib.PATTERN_PP_DEREFERENCE, ".")

        path.startsWith( ".") ? path.substring( ".".length()) : path
    }

    def grailsApplication

    /**
     *  Remove a named attribute from an attribute collection and throw an
     *  exception upon failure.
     *
     *  @param attributes the attribute collection to remove from
     *  @param attributeName the named attribute to remove
     *  @param tag the name of the tag we are currently processing
     *
     *  @return the given attribute's (trimmed) value.
     */
    protected def safeRemoveAttribute( attributes, attributeName, tag ) {
        def attributeValue = attributes.remove( attributeName)
        if ( !attributeValue ) {
            throwTagError( "The \"${attributeName}\" " +
                "attribute is required for the \"<${tag}>\" tag.") }

        attributeValue.toString().trim()
    }

    /**
     *  Convert an Horn path in JavaScript representation into CSS compatible
     *  representation.
     *  <p>
     *  Writes out the converted tag.
     *
     *  @param attrs.path the value to convert
     *
     *  @throws GrailsTagException if the <code>path</code> attribute is not
     *      supplied
     *
     */
    def encodePath = { attrs, body ->
        out << HornTagLib.jsToCSSForm(
            safeRemoveAttribute( attrs, 'path', 'encodePath'))
    }

    def hornTag = { attrs, body ->
        def html5 = grailsApplication.config.useHTML5 == true
        def hiddenCSSClass = grailsApplication.config.hiddenCSSClass ?: 'hidden'
        def templating = request[ HornTagLib.KEY_TEMPLATING] == true
        def tag = safeRemoveAttribute( attrs, 'tag', 'hornTag')
        def path = HornTagLib.removeAttribute( attrs, "path")
        def isJSON = HornTagLib.isAttributeTruth( attrs, "json")
        if ( !path && !isJSON ) {
            throwTagError( """One of, or both of the "path" and "json" attributes must be supplied for the "<hornTag>" tag when not in templating mode.""")
        }
        def isLevel0 = depth == 0
        def isAbsolutePath = path && path.startsWith( "/")
        if ( isAbsolutePath ) { if ( !html5 ) { path = path.substring( 1) } }
            else {
                if ( isLevel0 && path ) {
                    if ( html5 ) { path = "/$path" }
                    isAbsolutePath = true
                }
            }

        depth++
        def bodyValue = body()
        depth--
        out << "<"
        out << tag

        def newClassAttrs =
            splitAddAttributeValues( HornTagLib.removeAttribute( attrs, "class"))

        if ( html5 ) {
            HornTagLib.addAttributeIf( "data-horn", path, attrs,
               !isJSON && path)
            HornTagLib.addAttributeIf( "data-horn-json", path, attrs,
               path && isJSON)
            HornTagLib.addAttributeIf( "data-horn-json", "true", attrs,
               !path && isJSON)
        } else {
            HornTagLib.addAttributeValueIf( "horn", newClassAttrs,
                !templating && (isAbsolutePath || isLevel0));
            HornTagLib.addAttributeValueIf( "data-json", newClassAttrs, isJSON)
            HornTagLib.addAttributeValueIf( HornTagLib.jsToCSSForm( path),
                newClassAttrs, path)
            HornTagLib.addAttributeValueIf( "data-json", newClassAttrs, isJSON)
        }

        def emptyBodyClass = attrs.remove('emptyBodyClass')?.trim()
        HornTagLib.addAttributeValueIf( emptyBodyClass, newClassAttrs,
            (bodyValue?.trim() == '') && emptyBodyClass && !templating)
        HornTagLib.addAttributeValueIf( hiddenCSSClass, newClassAttrs,
            isJSON || (isLevel0 && templating))

        HornTagLib.outputAttribute( out, "class", newClassAttrs)
        HornTagLib.outputAttributes( out, attrs)
        out << ">"

        out << bodyValue

        out << "</"
        out << tag
        out << ">"
    }

    /**
     *
     */
    def json = { attrs, body ->
        if ( !attrs.tag ) { attrs.tag = "span" }
        attrs.json = true
        out << hornTag( attrs, body)
    }

    /**
     *
     */
    def templating = { attrs, body ->
        request[ HornTagLib.KEY_TEMPLATING] =
            HornTagLib.isAttributeTruth( attrs, "value")
    }

    def a =         { attrs, body ->
                        attrs.tag = "a"
                        out << hornTag( attrs, body) }
    def abbr =      { attrs, body ->
                        attrs.tag = 'abbr'
                        attrs.title =
                            safeRemoveAttribute( attrs, 'value', 'abbr')
                        out << hornTag( attrs, body) }
    def b =         { attrs, body ->
                        attrs.tag = "b"
                        out << hornTag( attrs, body) }
    def base =      { attrs, body ->
                        attrs.tag = "base"
                        out << hornTag( attrs, body) }
    def body =      { attrs, body ->
                        attrs.tag = "body"
                        out << hornTag( attrs, body) }
    def br =        { attrs, body ->
                        attrs.tag = "br"
                        out << hornTag( attrs, body) }
    def button =    { attrs, body ->
                        attrs.tag = "button"
                        out << hornTag( attrs, body) }
    def caption =   { attrs, body ->
                        attrs.tag = "caption"
                        out << hornTag( attrs, body) }
    def col =       { attrs, body ->
                        attrs.tag = "col"
                        out << hornTag( attrs, body) }
    def colgroup =  { attrs, body ->
                        attrs.tag = "colgroup"
                        out << hornTag( attrs, body) }
    def div =       { attrs, body ->
                        attrs.tag = "div"
                        out << hornTag( attrs, body) }
    def fieldset =  { attrs, body ->
                        attrs.tag = "fieldset"
                        out << hornTag( attrs, body) }
    def form =      { attrs, body ->
                        attrs.tag = "form"
                        out << hornTag( attrs, body) }
    def head =      { attrs, body ->
                        attrs.tag = "head"
                        out << hornTag( attrs, body) }
    def html =      { attrs, body ->
                        attrs.tag = "html"
                        out << hornTag( attrs, body) }
    def h1 =        { attrs, body ->
                        attrs.tag = "h1"
                        out << hornTag( attrs, body) }
    def h2 =        { attrs, body ->
                        attrs.tag = "h2"
                        out << hornTag( attrs, body) }
    def h3 =        { attrs, body ->
                        attrs.tag = "h3"
                        out << hornTag( attrs, body) }
    def h4 =        { attrs, body ->
                        attrs.tag = "h4"
                        out << hornTag( attrs, body) }
    def h5 =        { attrs, body ->
                        attrs.tag = "h5"
                        out << hornTag( attrs, body) }
    def h6 =        { attrs, body ->
                        attrs.tag = "h6"
                        out << hornTag( attrs, body) }
    def input =     { attrs, body ->
                        attrs.tag = "input"
                        out << hornTag( attrs, body) }
    def label =     { attrs, body ->
                        attrs.tag = "label"
                        out << hornTag( attrs, body) }
    def legend =    { attrs, body ->
                        attrs.tag = "legend"
                        out << hornTag( attrs, body) }
    def li =        { attrs, body ->
                        attrs.tag = "li"
                        out << hornTag( attrs, body) }
    def link =      { attrs, body ->
                        attrs.tag = "link"
                        out << hornTag( attrs, body) }
    def object =    { attrs, body ->
                        attrs.tag = "object"
                        out << hornTag( attrs, body) }
    def ol =        { attrs, body ->
                        attrs.tag = "ol"
                        out << hornTag( attrs, body) }
    def optgroup =  { attrs, body ->
                        attrs.tag = "optgroup"
                        out << hornTag( attrs, body) }
    def option =    { attrs, body ->
                        attrs.tag = "option"
                        out << hornTag( attrs, body) }
    def p =         { attrs, body ->
                        attrs.tag = "p"
                        out << hornTag( attrs, body) }
    def pre =       { attrs, body ->
                        attrs.tag = "pre"
                        out << hornTag( attrs, body) }
    def script =    { attrs, body ->
                        attrs.tag = "script"
                        out << hornTag( attrs, body) }
    def select =    { attrs, body ->
                        attrs.tag = "select"
                        out << hornTag( attrs, body) }
    def span =      { attrs, body ->
                        attrs.tag = "span"
                        out << hornTag( attrs, body) }
    def strong =    { attrs, body ->
                        attrs.tag = "strong"
                        out << hornTag( attrs, body) }
    def style =     { attrs, body ->
                        attrs.tag = "style"
                        out << hornTag( attrs, body) }
    def sub =       { attrs, body ->
                        attrs.tag = "sub"
                        out << hornTag( attrs, body) }
    def sup =       { attrs, body ->
                        attrs.tag = "sup"
                        out << hornTag( attrs, body) }
    def table =     { attrs, body ->
                        attrs.tag = "table"
                        out << hornTag( attrs, body) }
    def tbody =     { attrs, body ->
                        attrs.tag = "tbody"
                        out << hornTag( attrs, body) }
    def textarea =  { attrs, body ->
                        attrs.tag = "textarea"
                        out << hornTag( attrs, body) }
    def tfoot =     { attrs, body ->
                        attrs.tag = "tfoot"
                        out << hornTag( attrs, body) }
    def thead =     { attrs, body ->
                        attrs.tag = "thead"
                        out << hornTag( attrs, body) }
    def td =        { attrs, body ->
                        attrs.tag = "td"
                        out << hornTag( attrs, body) }
    def th =        { attrs, body ->
                        attrs.tag = "th"
                        out << hornTag( attrs, body) }
    def tr =        { attrs, body ->
                        attrs.tag = "tr"
                        out << hornTag( attrs, body) }
    def tt =        { attrs, body ->
                        attrs.tag = "tt"
                        out << hornTag( attrs, body) }
    def ul =        { attrs, body ->
                        attrs.tag = "ul"
                        out << hornTag( attrs, body) }
}