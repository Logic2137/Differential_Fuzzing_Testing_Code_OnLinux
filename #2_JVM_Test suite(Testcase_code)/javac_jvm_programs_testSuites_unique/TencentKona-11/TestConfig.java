

package jdk.nashorn.internal.test.framework;


@SuppressWarnings("javadoc")
public interface TestConfig {
    
    public static final String   OPTIONS_RUN                 = "run";
    public static final String   OPTIONS_EXPECT_COMPILE_FAIL = "expect-compile-fail";
    public static final String   OPTIONS_CHECK_COMPILE_MSG   = "check-compile-msg";
    public static final String   OPTIONS_EXPECT_RUN_FAIL     = "expect-run-fail";
    public static final String   OPTIONS_IGNORE_STD_ERROR    = "ignore-std-error";
    public static final String   OPTIONS_COMPARE             = "compare";
    public static final String   OPTIONS_FORK                = "fork";

    

    
    
    static final String  TEST_JS_ROOTS                       = "test.js.roots";

    
    static final String TEST_JS_INCLUDES                    = "test.js.includes";

    
    static final String TEST_JS_LIST                        = "test.js.list";

    
    static final String TEST_JS_FRAMEWORK                   = "test.js.framework";

    
    static final String TEST_JS_EXCLUDE_DIR                 = "test.js.exclude.dir";

    
    static final String TEST_JS_UNCHECKED_DIR               = "test.js.unchecked.dir";

    
    static final String TEST_JS_EXCLUDE_LIST                = "test.js.exclude.list";

    
    static final String TEST_JS_EXCLUDES_FILE               = "test.js.excludes.file";

    
    static final String TEST_JS_ENABLE_STRICT_MODE          = "test.js.enable.strict.mode";

    
    static final String TEST_JS_FAIL_LIST                   = "test.js.fail.list";

    
    static final String TEST_JS_SHARED_CONTEXT              = "test.js.shared.context";

    static final String TEST_FORK_JVM_OPTIONS               = "test.fork.jvm.options";

    
    static final String TEST_FAILED_LIST_FILE = "test.failed.list.file";
}
