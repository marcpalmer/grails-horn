package com.spottymushroom.grails.horn.jquery

import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib

/**
 *  Unit Tests for HornTagLib
 *
 *  @author Chris Denman
 *  @author Marc Palmer
 */
class HornTagLibTests extends TagLibUnitTestCase {

    static CSS_JAVA_PROPERTIES = [
        "_0": "[0]",
        "_1": "[1]",
        "_2": "[2]",

        "_3-0": "[3][0]",
        "_3-1": "[3][1]",
        "_3-2": "[3][2]",

        "_3-3-0": "[3][3][0]",
        "_3-3-1": "[3][3][1]",
        "_3-3-2": "[3][3][2]",

        "_3-4-k": "[3][4].k",
        "_3-4-l": "[3][4].l",
        "_3-4-m": "[3][4].m",

        "_4-f": "[4].f",
        "_4-g": "[4].g",
        "_4-h": "[4].h",

        "_4-i-0": "[4].i[0]",
        "_4-i-1": "[4].i[1]",
        "_4-i-2": "[4].i[2]",

        "_4-j-n": "[4].j.n",
        "_4-j-o": "[4].j.o",
        "_4-j-p": "[4].j.p",

        "_a": "a",
        "_b": "b",
        "_c": "c",

        "_d-0": "d[0]",
        "_d-1": "d[1]",
        "_d-2": "d[2]",

        "_d-3-0": "d[3][0]",
        "_d-3-1": "d[3][1]",
        "_d-3-2": "d[3][2]",

        "_d-4-k": "d[4].k",
        "_d-4-l": "d[4].l",
        "_d-4-m": "d[4].m",

        "_e-f": "e.f",
        "_e-g": "e.g",
        "_e-h": "e.h",

        "_e-i-0": "e.i[0]",
        "_e-i-1": "e.i[1]",
        "_e-i-2": "e.i[2]",

        "_e-j-n": "e.j.n",
        "_e-j-o": "e.j.o",
        "_e-j-p": "e.j.p",

        "_x-2-y-X": "x[2].y[X]"
    ]

    void testDecodeEncodeCSS() {
        HornTagLibTests.CSS_JAVA_PROPERTIES.each { k, v ->
            assertEquals tagLib.cssToJSForm( tagLib.jsToCSSForm( v)), v
            assertEquals tagLib.jsToCSSForm( tagLib.cssToJSForm( k)), k
        }
    }

    void testIsAttributeTruthNoAttribute() {
        assert HornTagLib.isAttributeTruth( [:], "key") == false
    }

    void testIsAttributeTruth() {
        assert HornTagLib.isAttributeTruth( [key:"true"], "key") == true
    }

    void testIsAttributeTruthCaseSensitivity() {
        assert HornTagLib.isAttributeTruth( [key:"TRUE"], "key") == true
    }

    void testRemoveAttriubteNotPresent() {
        assert HornTagLib.removeAttribute(
            [key:"TRUE"], "notpresent", "a") == "a"
    }

    void testRemoveAttriubteNotPresentNoDefault() {
        assert HornTagLib.removeAttribute( [key:"TRUE"], "notpresent") != "a"
    }

    void testRemoveAttriubteNotPresentEmptyCollection() {
        assert HornTagLib.removeAttribute( [:], "notpresent", "b") == "b"
    }

    void testRemoveAttriubte() {
        assert HornTagLib.removeAttribute( [key:"TRUE"], "key", "a") == "TRUE"
    }

    void testAddAttributeIfFalse() {
        def attrs = [:]
        def key = "key"
        HornTagLib.addAttributeIf( key, "value", attrs, false)
        assert !attrs.containsKey( key)
    }

    void testAddAttributeIfTrue() {
        def attrs = [:]
        def key = "key"
        HornTagLib.addAttributeIf( key, "value", attrs, true)
        assert attrs.containsKey( key)
    }

    void testAddAttributeIfOverride() {
        def attrs = [key:"a"]
        def key = "key"
        HornTagLib.addAttributeIf( key, "value", attrs, true)
        assert attrs[ key] == "value"
    }

    void testAddAttributeIfGroovyTruth() {
        def attrs = [:]
        def key = "key"
        HornTagLib.addAttributeIf( key, "value", attrs, "")
        assert !attrs.containsKey( key)
    }

    void testAddAttributeReturnValue() {
        def attrs = [:]
        assert HornTagLib.addAttributeIf( "key", "value", attrs, true) == attrs
    }

    void testAddAttributeValue() {
        def attrs = []
        HornTagLib.addAttributeValue( "value", attrs)
        assert attrs.contains( "value")
    }

    void testAddAttributeValueTrimming() {
        def attrs = ["value"]
        HornTagLib.addAttributeValue( " value ", attrs)
        assert !attrs.contains( " value ")
    }

    void testAddAttributeValueReturnValue() {
        def attrs = []
        def rv = HornTagLib.addAttributeValue( "value", attrs)
        assert attrs == rv
    }

    void testAddAttributeValueIfFalseDefault() {
        def attrs = []
        HornTagLib.addAttributeValueIf( "value", attrs)
        assert !attrs.contains( "value")
    }

    void testAddAttributeValueIfFalse() {
        def attrs = []
        HornTagLib.addAttributeValueIf( "value", attrs, false)
        assert !attrs.contains( "value")
    }

    void testAddAttributeValueIfTrue() {
        def attrs = []
        HornTagLib.addAttributeValueIf( "value", attrs, true)
        assert attrs.contains( "value")
    }

    void testAddAttributeValueIfReturnValue() {
        def attrs = []
        def rv = HornTagLib.addAttributeValueIf( "value", attrs, true)
        assert attrs == rv
    }

    void testSplitAddAttributeValuesEmptyBoth() {
        def value = ""
        def values = []
        assert !HornTagLib.splitAddAttributeValues(
            value, values).contains( value)
    }

    void testSplitAddAttributeValue() {
        def value = "added"
        def values = []
        assert HornTagLib.splitAddAttributeValues(
            value, values).contains( value)
    }

    void testSplitAddAttributeValueDuped() {
        def value = "added"
        def values = ["added"]
        def rv = HornTagLib.splitAddAttributeValues( value, values)
        assert rv.size() == 1
        assert rv.contains( value)
    }

    void testSplitAddAttributeValueOrder() {
        def value = "added"
        def values = ["one", "two"]
        def rv = HornTagLib.splitAddAttributeValues( value, values)
        assert rv.size() == 3
        assert rv[ 0] == "one"
        assert rv[ 1] == "two"
        assert rv[ 2] == "added"
    }

    void testSplitAddAttributeValueCase() {
        def value = "ADDED"
        def values = ["added"]
        def rv = HornTagLib.splitAddAttributeValues( value, values)
        assert rv.size() == 2
        assert rv[ 0] == "added"
        assert rv[ 1] == "ADDED"
    }

    void safeRemoveAttributeThrows() {
        try {
            HornTagLib.safeRemoveAttribute( [:], "a", "a")
            assert false
        } catch ( GrailsTagException gte ) {
            assert true
        }
    }

    void safeRemoveAttribute() {
        try {
            HornTagLib.safeRemoveAttribute( [a:0], "a", "a")
            assert true
        } catch ( GrailsTagException gte ) {
            assert false
        }
    }

    void safeRemoveAttributeRemoves() {
        def attrs = [a:0]
        try {
            HornTagLib.safeRemoveAttribute( attrs, "a", "a")
            assert !attrs.containsKey( "a")
        } catch ( GrailsTagException gte ) {
            assert false
        }
    }

    void safeRemoveAttributeReturnValue() {
        def attrs = [a:0]
        try {

            assert HornTagLib.safeRemoveAttribute( attrs, "a", "a") == attrs
        } catch ( GrailsTagException gte ) {
            assert false
        }
    }
}