/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8044537
 * @summary Checking ACC_SYNTHETIC flag is generated for bridge method
 *          generated for lambda expressions and method references.
 * @modules jdk.compiler/com.sun.tools.javac.api
 *          jdk.compiler/com.sun.tools.javac.file
 *          jdk.compiler/com.sun.tools.javac.main
 *          jdk.jdeps/com.sun.tools.classfile
 * @library /tools/lib /tools/javac/lib ../lib
 * @build TestBase TestResult InMemoryFileManager ToolBox
 * @build BridgeMethodsForLambdaTest SyntheticTestDriver ExpectedClass ExpectedClasses
 * @run main SyntheticTestDriver BridgeMethodsForLambdaTest 1
 */

import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * Synthetic members:
 * 1. inner class for Inner1.
 * 2. method for () -> {} in Inner1
 * 3. method for () -> {} in Inner2
 * 4. method references to private methods.
 * 5. method for super::function()
 * 6. method references to private static methods.
 * 7. access method for private method function().
 * 8. access method for private static method staticFunction().
 * 9. method reference to vararg method.
 * 10. method reference to array's method.
 * 11. constructors for Inner1 and Inner2.
 */
@ExpectedClass(className = "BridgeMethodsForLambdaTest",
        expectedMethods = {"<init>()", "<clinit>()", "function(java.lang.Integer[])"},
        expectedNumberOfSyntheticMethods = 6)
@ExpectedClass(className = "BridgeMethodsForLambdaTest$Inner1",
        expectedMethods = {"<init>(BridgeMethodsForLambdaTest)", "function()", "run()"},
        expectedFields = "lambda1",
        expectedNumberOfSyntheticMethods = 4,
        expectedNumberOfSyntheticFields = 1)
@ExpectedClass(className = "BridgeMethodsForLambdaTest$Inner2",
        expectedMethods = {"<init>()", "staticFunction()"},
        expectedFields = "lambda1",
        expectedNumberOfSyntheticMethods = 3)
@ExpectedClass(className = "BridgeMethodsForLambdaTest$Inner3",
        expectedMethods = {"<init>(BridgeMethodsForLambdaTest)", "function()"},
        expectedNumberOfSyntheticMethods = 1,
        expectedNumberOfSyntheticFields = 1)
@ExpectedClass(className = "BridgeMethodsForLambdaTest$Inner4",
        expectedMethods = {"<init>(BridgeMethodsForLambdaTest)", "function()"},
        expectedNumberOfSyntheticMethods = 1,
        expectedNumberOfSyntheticFields = 1)
public class BridgeMethodsForLambdaTest {

    private class Inner1 implements Runnable {
        private Inner1() {
        }
        private Runnable lambda1 = () -> {
        };
        private void function() {
        }
        @Override
        public void run() {
        }
    }

    private static class Inner2 {
        private Runnable lambda1 = () -> {
        };
        private static void staticFunction() {
        }
    }

    private class Inner3 {
        public void function() {
        }
    }

    private class Inner4 extends Inner3 {
        @Override
        public void function() {
            Runnable r = super::function;
        }
    }

    private static int function(Integer...vararg) {
        return 0;
    }

    {
        Inner1 inner = new Inner1();
        Runnable l1 = inner::function;
        Runnable l2 = Inner1::new;
        inner.lambda1 = inner::function;
        Comparator<Integer> c = BridgeMethodsForLambdaTest::function;
        IntStream.of(2).mapToObj(int[]::new);
    }

    static {
        Inner2 inner = new Inner2();
        Runnable l1 = Inner2::staticFunction;
    }
}
