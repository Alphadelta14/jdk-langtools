/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 6257087
 * @summary javah doesn't produce proper signatures for inner class native methods
 * @library /tools/lib
 * @build ToolBox
 * @run main T6257087
 */

import java.util.List;

public class T6257087 {

    private static final String fooBarGoldenFile =
        "/* DO NOT EDIT THIS FILE - it is machine generated */\n" +
        "#include <jni.h>\n" +
        "/* Header for class foo_bar */\n" +
        "\n" +
        "#ifndef _Included_foo_bar\n" +
        "#define _Included_foo_bar\n" +
        "#ifdef __cplusplus\n" +
        "extern \"C\" {\n" +
        "#endif\n" +
        "/*\n" +
        " * Class:     foo_bar\n" +
        " * Method:    aardvark\n" +
        " * Signature: ()V\n" +
        " */\n" +
        "JNIEXPORT void JNICALL Java_foo_00024bar_aardvark\n" +
        "  (JNIEnv *, jobject);\n" +
        "\n" +
        "#ifdef __cplusplus\n" +
        "}\n" +
        "#endif\n" +
        "#endif";

    public static void main(String[] args) throws Exception {
        ToolBox tb = new ToolBox();
        tb.new JavahTask()
                .classpath(ToolBox.testClasses)
                .classes("foo")
                .run();

        List<String> fooBarFile = tb.readAllLines("foo_bar.h");
        tb.checkEqual(fooBarFile, tb.split(fooBarGoldenFile, "\n"));
    }

}

class foo {
    class bar {
        public native void aardvark();
    }
}
