
package org.netbeans.jemmy.drivers.scrolling;

public interface ScrollAdjuster {

    public static final int INCREASE_SCROLL_DIRECTION = 1;

    public static final int DECREASE_SCROLL_DIRECTION = -1;

    public static final int DO_NOT_TOUCH_SCROLL_DIRECTION = 0;

    public int getScrollDirection();

    public int getScrollOrientation();

    public String getDescription();
}
