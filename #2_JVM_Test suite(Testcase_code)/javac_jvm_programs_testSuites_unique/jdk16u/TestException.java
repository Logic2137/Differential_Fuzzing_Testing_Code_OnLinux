

package org.reactivestreams.tck.flow.support;


public final class TestException extends RuntimeException {
  public TestException() {
    super("Test Exception: Boom!");
  }
}
