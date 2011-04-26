<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="horned_html5" />
    </head>
    <body>
        <h:div root="yes" path="x.y.z[10]">
            <h:container    class="passthrough" tagName="div"   path="key">
                <h:value        class="passthrough" tagName="span"  path="key"                               >value</h:value>
            </h:container>

            <h:container    class="passthrough" tagName="div"   path="key">
                <h:value        class="passthrough" tagName="span"  path="key"   json="true"                 >{"key": value}</h:value>
            </h:container>

            <h:container    class="passthrough" tagName="div"   path="key">
                <h:value        class="passthrough"                 path="key"               value="value"   >Display Value</h:value>
            </h:container>

            <h:container    class="passthrough" tagName="div"   path="key">
                <h:span         path="[0][0].i[10].j.k">value</h:span>
            </h:container>

            <h:div path="prop[10].x[20][30]">
                <h:span         path="[0][0].i[10].j.k">value</h:span>
            </h:div>

            <h:div root="yes" path="prop[10].x[20][30]">
                <h:span         path="[0][0].i[10].j.k">value</h:span>
                <h:span         path="[1][4].kk[110].a.vvvv[10][20].a" json="true">true</h:span>
                <h:span         path="a" value="hornValue">true</h:span>
            </h:div>
        </h:div>
    </body>
</html>