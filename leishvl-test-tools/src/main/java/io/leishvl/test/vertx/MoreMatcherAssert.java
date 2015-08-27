package io.leishvl.test.vertx;

import io.vertx.ext.unit.TestContext;
import org.hamcrest.Matcher;

import static java.util.Objects.requireNonNull;

/**
 * Extends {@link TestContext} with more assertion methods.
 * @author Erik Torres <ertorser@upv.es>
 */
public class MoreMatcherAssert {

    private final TestContext context;

    public MoreMatcherAssert(final TestContext context) {
        this.context = requireNonNull(context, "Valid test context expected.");
    }

    public <T> void assertThat(final String reason, final T actual, final Matcher<? super T> matcher) {
        try {
            org.hamcrest.MatcherAssert.assertThat(reason, actual, matcher);
        } catch (AssertionError e) {
            context.fail(e);
        }
    }

}