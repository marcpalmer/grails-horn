import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException

import grails.test.GroovyPagesTestCase

//    void testTemplateBlankValue() {
//        tagLib.templating( [value: ""], { args -> "" })
//        assert !mockRequest[ HornTagLib.KEY_TEMPLATING]
//    }
//
//    void testTemplateTrueValue() {
//        tagLib.templating( [value: "true"], { args -> "" })
//        assert mockRequest[ HornTagLib.KEY_TEMPLATING]
//    }
//
//    void testTemplatetRuEValue() {
//        tagLib.templating( [value: "tRuE"], { args -> "" })
//        assert mockRequest[ HornTagLib.KEY_TEMPLATING]
//    }
//
//    void testTemplateTrueFalseValue() {
//        tagLib.templating( [value: "true"], { args -> "" })
//        assert mockRequest[ HornTagLib.KEY_TEMPLATING]
//        tagLib.templating( [value: "false"], { args -> "" })
//        assert !mockRequest[ HornTagLib.KEY_TEMPLATING]
//    }
//
//    void testTemplateFalseValue() {
//        tagLib.templating( [value: "false"], { args -> "" })
//        assert !mockRequest[ HornTagLib.KEY_TEMPLATING]
//    }
//
//    void testTemplateFaLsEValue() {
//        tagLib.templating( [value: "FaLsE"], { args -> "" })
//        assert !mockRequest[ HornTagLib.KEY_TEMPLATING]
//    }
//
//    void testTemplateFalseTrueValue() {
//        tagLib.templating( [value: "false"], { args -> "" })
//        assert !mockRequest[ HornTagLib.KEY_TEMPLATING]
//        tagLib.templating( [value: "true"], { args -> "" })
//        assert mockRequest[ HornTagLib.KEY_TEMPLATING]
//    }
//
//
//    void testTemplateOmitsHorn() {
//        tagLib.metaClass.getGrailsApplication = { ->
//            [config: [hiddenCSSClass: "hidden"]] }
//        tagLib.templating( [value: "true"], { args -> "" })
//        tagLib.templating( [value: "false"], { args -> "" })
//        tagLib.div(
//            [
//                path: 'x'
//            ],
//            { args -> "bodyValue"})
//
//        assert tagLib.out.toString() == '<div class="horn _x">bodyValue</div>' * 2
//    }
//
//    void testEmptyValueClassWithBodyValue() {
//        NON_JSON_DATA.each { attrRecord ->
//            def passed = false
//            try {
//                tagLib.metaClass.getGrailsApplication = { ->
//                    [config: [hiddenCSSClass: "hidden"]] }
//                def attrs = [:]
//                attrs.putAll( attrRecord.attrs)
//                attrs.= 'emptyBodyClass'
//                tagLib.hornTag( attrs, {args -> "bodyValue"})
//                passed = tagLib.out.toString() == attrRecord.result
//            } catch ( GrailsTagException gte ) {
//                passed = attrRecord.exception == true
//            }
//            assert passed
//            setUp()
//        }
//    }
//
//    void testEmptyValueClassNoBodyValue() {
//        def passed
//        def attrs
//        NON_JSON_DATA.each { attrRecord ->
//            passed = false
//            attrs = [:]
//            try {
//                tagLib.metaClass.getGrailsApplication = { ->
//                    [config: [hiddenCSSClass: "hidden"]] }
//                attrs.putAll( attrRecord.attrs)
//                attrs.= 'emptyBodyClass'
//                tagLib.hornTag( attrs, {args -> ""})
//                passed = tagLib.out.toString() == attrRecord.emptyValueResult
//            } catch ( GrailsTagException gte ) {
//                passed = attrRecord.exception == true
//            }
//            assert passed
//            setUp()
//        }
//    }

/**
 *  Integration Tests for HornTagLib
 *
 *  @author Chris Denman
 *  @author Marc Palmer
 */
class HornTagLibIntegTests extends GroovyPagesTestCase {

    def grailsApplication

    protected test( c, useHTML5 = true ) {
        def cachedValue = grailsApplication.config.useHTML5
        try {
            grailsApplication.config.useHTML5 = useHTML5
            c.clone().call()
        } finally {
            if ( cachedValue ) {
                grailsApplication.config.useHTML5 = cachedValue
            }
        }
    }

    protected assertTemplateResult( value, result, useHTML5 = true ) {
        test({ -> assert applyTemplate( value) == result }, useHTML5)
    }

    protected assertTemplateThrows( templateString,
                                    useHTML5 = true,
                                    exceptionClass = GrailsTagException.class ) {
        test({ ->
            try {
                applyTemplate( templateString)
                assert false
            } catch ( Throwable t ) {
                if ( exceptionClass && (t.class != exceptionClass) ) {
                    fail( "expected " + exceptionClass + ", got " + t.class)
                }
            }
        }, useHTML5)
    }

    void testHornTagNoAttributesHTML() {
        assertTemplateThrows( """<horn:hornTag></horn:hornTag>""")
    }

    void testHornBadTagHTML() {
        assertTemplateThrows( """<horn:hornTag tag="qkw"></horn:hornTag>""")
    }

    void testHornTagTagAttributeOnlyHTML() {
        assertTemplateThrows( """<horn:hornTag tag="div"></horn:hornTag>""")
    }

    void testHornTagPathAttributeOnlyHTML() {
        assertTemplateThrows( """<horn:hornTag path="path"></horn:hornTag>""")
    }

    void testHornTagTagAndPathAttributesHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" path="path"></horn:hornTag>""", """<div data-horn="/path"></div>""")
    }

    void testHornTagRootLevelAbsolutePathHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" path="/path"></horn:hornTag>""", """<div data-horn="/path"></div>""")
    }

    void testHornTagRootLevelRelativePathHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" path="path"></horn:hornTag>""", """<div data-horn="/path"></div>""")
    }

    void testHornTagJSONAttributeOnlyHTML() {
        assertTemplateThrows( """<horn:hornTag json="true"></horn:hornTag>""")
    }

    // @todo maybe trap this case where no body value and json
    void testHornTagTagAndJSONAttributesHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" json="true"></horn:hornTag>""", """<div class="hidden" data-horn-json="true"></div>""")
    }

    void testHornTagPathAndJSONAttributesHTML() {
        assertTemplateThrows( """<horn:hornTag path="path" json="true"></horn:hornTag>""")
    }

    void testHornTagTagPathAndJSONAttributesHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" path="path" json="true"></horn:hornTag>""", """<div class="hidden" data-horn-json="/path"></div>""")
    }

    void testHornTagClassAttributeOnlyHTML() {
        assertTemplateThrows( """<horn:hornTag class="a b c"></horn:hornTag>""")
    }

    void testHornTagTagAndClassAttributesHTML() {
        assertTemplateThrows( """<horn:hornTag tag="div" class="a b c"></horn:hornTag>""")
    }

    void testHornTagClassAndPathAttributesHTML() {
        assertTemplateThrows( """<horn:hornTag class="a b c" path="path"></horn:hornTag>""")
    }

    void testHornTagTagClassAndPathAttributesHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="path"></horn:hornTag>""", """<div class="a b c" data-horn="/path"></div>""")
    }

    void testHornTagTagClassAndJSONAttributesHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" json="true"></horn:hornTag>""", """<div class="a b c hidden" data-horn-json="true"></div>""")
    }

    void testHornTagClassPathAndJSONAttributesHTML() {
        assertTemplateThrows( """<horn:hornTag class="a b c" path="path" json="true"></horn:hornTag>""")
    }

    void testHornTagTagClassPathAndJSONAttributesHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="path" json="true"></horn:hornTag>""", """<div class="a b c hidden" data-horn-json="/path"></div>""")
    }

    void testHornTagPathAttributeOnlyRootHTML() {
        assertTemplateThrows( """<horn:hornTag path="/path"></horn:hornTag>""")
    }

    void testHornTagTagAndPathAttributesRootHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" path="/path"></horn:hornTag>""", """<div data-horn="/path"></div>""")
    }

    void testHornTagPathAndJSONAttributesRootHTML() {
        assertTemplateThrows( """<horn:hornTag path="/path" json="true"></horn:hornTag>""")
    }

    void testHornTagTagPathAndJSONAttributesRootHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" path="/path" json="true"></horn:hornTag>""", """<div class="hidden" data-horn-json="/path"></div>""")
    }

    void testHornTagClassAndPathAttributesRootHTML() {
        assertTemplateThrows( """<horn:hornTag class="a b c" path="/path"></horn:hornTag>""")
    }

    void testHornTagTagClassAndPathAttributesRootHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="/path"></horn:hornTag>""", """<div class="a b c" data-horn="/path"></div>""")
    }
    void testHornTagTagClassPathAndJSONAttributesRootHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="/path" json="true"></horn:hornTag>""", """<div class="a b c hidden" data-horn-json="/path"></div>""")
    }

    void testNestedDivTagsHTML() {
        assertTemplateResult( """<horn:div path="/path"><horn:div path="subpath">bodyValue</horn:div></horn:div>""", """<div data-horn="/path"><div data-horn="subpath">bodyValue</div></div>""")
    }

    void testPassThroughAttributesHMTL() {
        assertTemplateResult( """<horn:hornTag hungry="CaT" tag="div" class="a b c" path="/path" JSON="TRUE" json="true"></horn:hornTag>""", """<div class="a b c hidden" hungry="CaT" JSON="TRUE" data-horn-json="/path"></div>""")
    }

    void testHornTagNoAttributesCSS() {
        assertTemplateThrows( """<horn:hornTag><hornTag:div>""", false)
    }

    void testHornTagBadTagCSS() {
        assertTemplateThrows( """<horn:hornTag tag="qkw"></horn:hornTag>""", false)
    }

    void testHornTagTagOnlyCSS() {
        assertTemplateThrows( """<horn:hornTag tag="div"></horn:hornTag>""", false)
    }

    void testHornTagPathOnlyCSS() {
        assertTemplateThrows( """<horn:hornTag path="path"></horn:hornTag>""", false)
    }

    void testHornTagTagAndPathCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" path="path"></horn:hornTag>""", """<div class="horn _path"></div>""", false)
    }

    void testHornTagJSONOnlyCSS() {
        assertTemplateThrows( """<horn:hornTag json=true"></horn:hornTag>""", false)
    }

    void testHornTagPathAndJSONCSS() {
        assertTemplateThrows( """<horn:hornTag path="path" json="true"></horn:hornTag>""", false)
    }

    void testHornTagTagPathAndJSONCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" path="path" json="true"></horn:hornTag>""", """<div class="horn data-json _path hidden"></div>""", false)
    }

    void testHornTagClassOnlyCSS() {
        assertTemplateThrows( """<horn:hornTag class="a b c"></horn:hornTag>""", false)
    }

    void testHornTagTagAndClassCSS() {
        assertTemplateThrows( """<horn:hornTag tag="div" class="a b c"></horn:hornTag>""", false)
    }

    void testHornTagTagClassAndPathCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="path"></horn:hornTag>""", """<div class="a b c horn _path"></div>""", false)
    }

    void testHornTagClassAndJSONCSS() {
        assertTemplateThrows( """<horn:hornTag class="a b c" json="true"></horn:hornTag>""", false)
    }

    void testHornTagTagClassAndJSONCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" json="true"></horn:hornTag>""", """<div class="a b c horn data-json hidden"></div>""", false)
    }

    void testHornTagClassPathAndJSONCSS() {
        assertTemplateThrows( """<horn:hornTag class="a b c" path="path" json="true"></horn:hornTag>""", false)
    }

    void testHornTagTagClassPathAndJSONCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="path" json="true"></horn:hornTag>""", """<div class="a b c horn data-json _path hidden"></div>""", false)
    }

    void testHornTagPathOnlyAbsoluteCSS() {
        assertTemplateThrows( """<horn:hornTag path="/path"></horn:hornTag>""", false)
    }

    void testHornTagTagAndPathAbsoluteCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" path="/path"></horn:hornTag>""", """<div class="horn _path"></div>""", false)
    }

    void testHornTagPathAndJSONAbsoluteCSS() {
        assertTemplateThrows( """<horn:hornTag path="/path" json="true"></horn:hornTag>""", false)
    }

    void testHornTagTagPathAndJSONAbsoluteCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" path="/path" json="true"></horn:hornTag>""", """<div class="horn data-json _path hidden"></div>""", false)
    }

    void testHornTagTagClassAndPathAbsoluteCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="/path"></horn:hornTag>""", """<div class="a b c horn _path"></div>""", false)
    }

    void testHornTagClassPathAndJSONAbsoluteCSS() {
        assertTemplateThrows( """<horn:hornTag class="a b c" path="/path" json="true"></horn:hornTag>""", false)
    }

    void testHornTagTagClassPathAndJSONAbsoluteCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" class="a b c" path="/path" json="true"></horn:hornTag>""", """<div class="a b c horn data-json _path hidden"></div>""", false)
    }

    void testPassThroughAttributesCSS() {
        assertTemplateResult( """<horn:hornTag hungry="CaT" tag="div" class="a b c" path="/path" JSON="TRUE" json="true"></horn:hornTag>""", """<div class="a b c horn data-json _path hidden" hungry="CaT" JSON="TRUE"></div>""", false)
    }

    void testDivRewritesPathsCSS() {
        assertTemplateResult( """<horn:hornTag tag="div" path="/path[2][3].x.y[3]">bVal</horn:hornTag>""", """<div class="horn _path-2-3-x-y-3">bVal</div>""", false)
    }

    void testDivRewritesPathsHTML() {
        assertTemplateResult( """<horn:hornTag tag="div" path="/path[2][3].x.y[3]">bVal</horn:hornTag>""", """<div data-horn="/path[2][3].x.y[3]">bVal</div>""")
    }

    void testTemplateNoAttributes() {
        assertTemplateThrows( """<horn:template />""")
    }

    void testTemplatingBadValueAttributeDoesNothing() { // @todo
        assertTemplateThrows( """<horn:templating value="sss"/><horn:hornTag tag="div">body</horn:hornTag>""")
    }

    void testTemplatingNoAttributes() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag>body</horn:hornTag>""")
    }

    void testTemplatingTagOnly() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag tag="div">body</horn:hornTag>""")
    }

    void testTemplatingPathOnly() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag path="path">body</horn:hornTag>""")
    }

    void testTemplatingTagPathCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="path" >body</horn:hornTag>""", """<div class="_path hidden">body</div>""", false)
    }

    void testTemplatingTagPathHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="path" >body</horn:hornTag>""", """<div class="hidden" data-horn="/path">body</div>""", true)
    }

    void testTemplatingJSONOnly() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag json="true">body</horn:hornTag>""")
    }

    void testTemplatingTagJSONCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" json="true">body</horn:hornTag>""", """<div class="data-json hidden">body</div>""", false)
    }

    void testTemplatingTagJSONHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" json="true">body</horn:hornTag>""", """<div class="hidden" data-horn-json="true">body</div>""", true)
    }

    void testTemplatingPathJSON() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag path="path" json="true">body</horn:hornTag>""")
    }

    void testTemplatingTagPathJSONCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="path" json="true">body</horn:hornTag>""", """<div class="data-json _path hidden">body</div>""", false)
    }

    void testTemplatingTagPathJSONHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="path" json="true">body</horn:hornTag>""", """<div class="hidden" data-horn-json="/path">body</div>""", true)
    }

    void testTemplatingClassOnly() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag class="a b c">body</horn:hornTag>""")
    }

    void testTemplatingTagClass() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c">body</horn:hornTag>""")
    }

    void testTemplatingClassPath() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag class="a b c" path="path">body</horn:hornTag>""")
    }

    void testTemplatingTagClassPathCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="path">body</horn:hornTag>""", """<div class="a b c _path hidden">body</div>""", false)
    }

    void testTemplatingTagClassPathHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="path">body</horn:hornTag>""", """<div class="a b c hidden" data-horn="/path">body</div>""", true)
    }

    void testTemplatingClassJSON() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag class="a b c" json="true">body</horn:hornTag>""")
    }

    void testTemplatingTagClassJSONCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" json="true">body</horn:hornTag>""", """<div class="a b c data-json hidden">body</div>""", false)
    }

    void testTemplatingTagClassJSONHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" json="true">body</horn:hornTag>""", """<div class="a b c hidden" data-horn-json="true">body</div>""", true)
    }

    void testTemplatingClassPathJSON() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag class="a b c" path="path" json="true">body</horn:hornTag>""")
    }

    void testTemplatingTagClassPathJSONCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="path" json="true">body</horn:hornTag>""", """<div class="a b c data-json _path hidden">body</div>""", false)
    }

    void testTemplatingTagClassPathJSONHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="path" json="true">body</horn:hornTag>""", """<div class="a b c hidden" data-horn-json="/path">body</div>""", true)
    }

    void testTemplatingPathOnlyAbsolute() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag path="/path">body</horn:hornTag>""")
    }

    void testTemplatingTagPathAbsoluteCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="/path">body</horn:hornTag>""", """<div class="_path hidden">body</div>""", false)
    }

    void testTemplatingTagPathAbsoluteHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="/path">body</horn:hornTag>""", """<div class="hidden" data-horn="/path">body</div>""", true)
    }

    void testTemplatingPathJSONAbsolute() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag path="/path" json="true">body</horn:hornTag>""")
    }

    void testTemplatingTagPathJSONAbsoluteCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="/path" json="true">body</horn:hornTag>""", """<div class="data-json _path hidden">body</div>""", false)
    }

    void testTemplatingTagPathJSONAbsoluteHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" path="/path" json="true">body</horn:hornTag>""", """<div class="hidden" data-horn-json="/path">body</div>""", true)
    }

    void testTemplatingClassPathAbsolute() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag class="a b c" path="/path">body</horn:hornTag>""")
    }

    void testTemplatingTagClassPathAbsoluteCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="/path">body</horn:hornTag>""", """<div class="a b c _path hidden">body</div>""", false)
    }

    void testTemplatingTagClassPathAbsoluteHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="/path">body</horn:hornTag>""", """<div class="a b c hidden" data-horn="/path">body</div>""", true)
    }

    void testTemplatingClassPathJSONAbsolute() {
        assertTemplateThrows( """<horn:templating value="true" /><horn:hornTag class="a b c" path="/path" json="true">body</horn:hornTag>""")
    }

    void testTemplatingTagClassPathJSONAbsoluteCSS() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="/path" json="true">body</horn:hornTag>""", """<div class="a b c data-json _path hidden">body</div>""", false)
    }

    void testTemplatingTagClassPathJSONAbsoluteHTML() {
        assertTemplateResult( """<horn:templating value="true" /><horn:hornTag tag="div" class="a b c" path="/path" json="true">body</horn:hornTag>""", """<div class="a b c hidden" data-horn-json="/path">body</div>""", true)
    }

    void testJSONNoTagHTML() {
        assertTemplateResult( """<horn:templating value="false" /><horn:json class="a b c hidden" path="/path">body</horn:json>""", """<span class="a b c hidden" data-horn-json="/path">body</span>""", true)
    }

    void testJSONNoTagCSS() {
        assertTemplateResult( """<horn:templating value="false" /><horn:json class="a b c hidden" path="/path">body</horn:json>""", """<span class="a b c hidden horn data-json _path">body</span>""", false)
    }

    void testJSONNoTagJSONFalseHTML() {
        assertTemplateResult( """<horn:json class="a b c" json="false" path="/path">body</horn:json>""", """<span class="a b c hidden" data-horn-json="/path">body</span>""", true)
    }

    void testJSONNoTagJSONFalseCSS() {
        assertTemplateResult( """<horn:json class="a b c" json="false" path="/path">body</horn:json>""", """<span class="a b c horn data-json _path hidden">body</span>""", false)
    }

    void testJSONOverrideTagHTML() {
        assertTemplateResult( """<horn:json tag="div" class="a b c" path="/path">body</horn:json>""", """<div class="a b c hidden" data-horn-json="/path">body</div>""", true)
    }

    void testJSONOverrideTagCSS() {
        assertTemplateResult( """<horn:json tag="div" class="a b c" path="/path">body</horn:json>""", """<div class="a b c horn data-json _path hidden">body</div>""", false)
    }

    void testJSONHiddenWithNoBodyHTML() {
        assertTemplateResult( """<horn:json class="a b c" path="/path"></horn:json>""", """<span class="a b c hidden" data-horn-json="/path"></span>""", true)
    }

    void testJSONHiddenWithNoBodyCSS() {
        assertTemplateResult( """<horn:json class="a b c" path="/path"></horn:json>""", """<span class="a b c horn data-json _path hidden"></span>""", false)
    }

    void testNestedJSONHTML() {
        assertTemplateResult( """<horn:div path="/root[0]/index"><horn:div path="sub[0]"><horn:json class="a b c" path="path">{"a": 0}</horn:json></horn:div></horn:div>""", """<div data-horn="/root[0]/index"><div data-horn="sub[0]"><span class="a b c hidden" data-horn-json="path">{"a": 0}</span></div></div>""", true)
    }

    void testNestedJSONCSS() {
        assertTemplateResult( """<horn:div path="/root[0].index"><horn:div path="sub[0]"><horn:json class="a b c" path="path">{"a": 0}</horn:json></horn:div></horn:div>""", """<div class="horn _root-0-index"><div class="_sub-0"><span class="a b c data-json _path hidden">{"a": 0}</span></div></div>""", false)
    }

    void testEmptyBodyClassNoneSpecifiedNotJSONHTML() {
        assertTemplateResult( """<horn:div path="/path"></horn:div>""", """<div data-horn="/path"></div>""", true)

    }

    void testEmptyBodyClassNoneSpecifiedNotJSONCSS() {
        assertTemplateResult( """<horn:div path="/path"></horn:div>""", """<div class="horn _path"></div>""", false)
    }

    void testEmptyBodyClassHiddenSpecifiedJSONHTML() {
        assertTemplateResult( """<horn:json emptyBodyClass="hidden" class="a b c hidden" path="/path"></horn:json>""", """<span class="a b c hidden" data-horn-json="/path"></span>""", true)
    }

    void testEmptyBodyClassHiddenSpecifiedJSONCSS() {
        assertTemplateResult( """<horn:json emptyBodyClass="hidden" class="a b c hidden" path="/path"></horn:json>""", """<span class="a b c hidden horn data-json _path"></span>""", false)
    }

    void testEmptyBodyClassEmptySpecifiedHTML() {
        assertTemplateResult( """<horn:div emptyBodyClass="empty" path="/path"></horn:div>""", """<div class="empty" data-horn="/path"></div>""", true)
    }

    void testEmptyBodyClassEmptySpecifiedCSS() {
        assertTemplateResult( """<horn:div emptyBodyClass="empty" path="/path"></horn:div>""", """<div class="horn _path empty"></div>""", false)
    }

    void testEmptyBodyClassEmptySpecifiedWithBodyHTML() {
        assertTemplateResult( """<horn:div emptyBodyClass="empty" path="/path">body</horn:div>""", """<div data-horn="/path">body</div>""", true)
    }

    void testEmptyBodyClassEmptySpecifiedWithBodyCSS() {
        assertTemplateResult( """<horn:div emptyBodyClass="empty" path="/path">body</horn:div>""", """<div class="horn _path">body</div>""", false)
    }

    void testEncodePath() {
        assertTemplateResult( """<horn:encodePath path="/root.sub[0]" />""", """_root-sub-0""", false)
        assertTemplateResult( """<horn:encodePath path="/root.sub[0]" />""", """_root-sub-0""", true)
    }

    void testEncodePathNoPathAttributeThrows() {
        try {
            assertTemplateResult( """<horn:encodePath/>""", """a""", true)
            assert false
        }
        catch ( GrailsTagException gte ) {
            assert true
        }
    }
}
