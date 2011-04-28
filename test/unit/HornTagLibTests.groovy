import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib

class HornTagLibTests extends TagLibUnitTestCase {

    protected void setUp() {
        super.setUp()
        loadCodec( HTMLCodec)
    }

    protected def noddyBodyClosure = { it -> "" }


    void testDivRootContext() {
        tagLib.div(
            [
                path: 'path',
                root: 'true',
                class: 'class'
            ],
            { args -> "bodyValue"})

        assert tagLib.out.toString() == '<div class="class horn _path">bodyValue</div>'
    }

    void testDivPassthroughAttributes() {
        tagLib.div(
            [
                path: 'path',
                root: 'true',
                class: 'class',
                hungry: 'cats',
                green: 'fields'
            ],
            { args -> "bodyValue"})

        assert tagLib.out.toString() == '<div class="class horn _path" hungry="cats" green="fields">bodyValue</div>'
    }

    void testDivRewritesPaths() {
        tagLib.div(
            [
                path: 'x.y.z[10][20][30].a.b'
            ],
            { args -> "bodyValue"})

        assert tagLib.out.toString() == '<div class="horn _x-y-z-10-20-30-a-b">bodyValue</div>'
    }

    void testNestedHornTags() {
        tagLib.div( [path: 'outer'], { argsOuter ->
            tagLib.div( [path: 'inner'], { argsInner ->
                "bodyValue"
            })
        })

        assert tagLib.out.toString() == '<div class="horn _outer"><div class="_inner">bodyValue</div></div>'
    }

    void testHornTag() {
        [   [   attrs: [                                                                        ],  exception: true                                                                             ],
            [   attrs: [tag: 'div',                                                             ],  exception: true                                                                             ],
            [   attrs: [                            path: '_path'                               ],  exception: true                                                                             ],
            [   attrs: [tag: 'div',                 path: '_path'                               ],                      result: '<div class="horn _path">bodyValue</div>'                            ],
            [   attrs: [                                            json: 'true'                ],  exception: true                                                                             ],
            [   attrs: [tag: 'div',                                 json: 'true'                ],                      result: '<div class="horn hidden data-json">bodyValue</div>'                 ],
            [   attrs: [                            path: '_path',  json: 'true'                ],  exception: true                                                                             ],
            [   attrs: [tag: 'div',                 path: '_path',  json: 'true'                ],                      result: '<div class="horn hidden data-json _path">bodyValue</div>'           ],
            [   attrs: [            class: "a b c",                                             ],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c",                                             ],  exception: true                                                                             ],
            [   attrs: [            class: "a b c", path: '_path'                               ],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c", path: '_path'                               ],                      result: '<div class="a b c horn _path">bodyValue</div>'                      ],
            [   attrs: [            class: "a b c",                 json: 'true'                ],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c",                 json: 'true'                ],                      result: '<div class="a b c horn hidden data-json">bodyValue</div>'           ],
            [   attrs: [            class: "a b c", path: '_path',  json: 'true'                ],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c", path: '_path',  json: 'true'                ],                      result: '<div class="a b c horn hidden data-json _path">bodyValue</div>'     ],
            [   attrs: [                                                                        ],  exception: true                                                                             ],

            [   attrs: [tag: 'div',                                                 root: 'true'],  exception: true                                                                             ],
            [   attrs: [                            path: '_path',                  root: 'true'],  exception: true                                                                             ],
            [   attrs: [tag: 'div',                 path: '_path',                  root: 'true'],                      result: '<div class="horn _path">bodyValue</div>'                       ],
            [   attrs: [                                            json: 'true',   root: 'true'],  exception: true                                                                             ],
            [   attrs: [tag: 'div',                                 json: 'true',   root: 'true'],                      result: '<div class="horn hidden data-json">bodyValue</div>'            ],
            [   attrs: [                            path: '_path',  json: 'true',   root: 'true'],  exception: true                                                                             ],
            [   attrs: [tag: 'div',                 path: '_path',  json: 'true',   root: 'true'],                      result: '<div class="horn hidden data-json _path">bodyValue</div>'      ],
            [   attrs: [            class: "a b c",                                 root: 'true'],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c",                                 root: 'true'],  exception: true                                                                             ],
            [   attrs: [            class: "a b c", path: '_path',                  root: 'true'],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c", path: '_path',                  root: 'true'],                      result: '<div class="a b c horn _path">bodyValue</div>'                 ],
            [   attrs: [            class: "a b c",                 json: 'true',   root: 'true'],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c",                 json: 'true',   root: 'true'],                      result: '<div class="a b c horn hidden data-json">bodyValue</div>'      ],
            [   attrs: [            class: "a b c", path: '_path',  json: 'true',   root: 'true'],  exception: true                                                                             ],
            [   attrs: [tag: 'div', class: "a b c", path: '_path',  json: 'true',   root: 'true'],                      result: '<div class="a b c horn hidden data-json _path">bodyValue</div>']

        ].each { attrRecord ->
            def passed = false
            try {
                tagLib.metaClass.getGrailsApplication = { -> [config: [hiddenCSSClass: "hidden"]] }
                def attrs = [:]
                attrs.putAll( attrRecord.attrs)
                tagLib.hornTag( attrs, {args -> "bodyValue"})
                passed = tagLib.out.toString() == attrRecord.result
            } catch ( GrailsTagException gte ) {
                passed = attrRecord.exception == true
            }
            assert passed
            setUp()
        }
    }

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
        "_e-j-p": "e.j.p"
    ]

    void testDecodeCSS() {
        HornTagLibTests.CSS_JAVA_PROPERTIES.each { k, v ->
            assert tagLib.decodeCSS( k) == v
        }
    }

    void testEncodeCSS() {
        HornTagLibTests.CSS_JAVA_PROPERTIES.each { k, v ->
            assert tagLib.encodeCSS( v) == k
        }
    }

    void testEncodeDecodeCSS() {
        HornTagLibTests.CSS_JAVA_PROPERTIES.each { k, v ->
            assert tagLib.decodeCSS( tagLib.encodeCSS( v)) == v
        }
    }

    void testDecodeEncodeCSS() {
        HornTagLibTests.CSS_JAVA_PROPERTIES.each { k, v ->
            assert tagLib.encodeCSS( tagLib.decodeCSS( k)) == k
        }
    }
}