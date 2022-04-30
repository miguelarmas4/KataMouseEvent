import mouse.Mouse;
import mouse.MouseEventListener;
import mouse.MouseEventType;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MouseEventTests {

    private Mouse mouse;
    private SpyEventListener listener;

    class SpyEventListener implements MouseEventListener {

        public MouseEventType receivedEventType;
        public boolean wasEventTriggered;
        public int eventCount;

        @Override
        public void handleMouseEvent(MouseEventType eventType) {
            this.receivedEventType = eventType;
            wasEventTriggered = true;
            eventCount++;
        }
    }

    @Before
    public void initMouse() {
        mouse = new Mouse();
        listener = new SpyEventListener();
        mouse.subscribe(listener);
    }

    /*
    press left mouse button
    release left mouse button
    move mouse
     */

    @Test
    public void single_click_means_pressing_and_releasing_mouse_button() throws InterruptedException {

        mouse.pressLeftButton(System.currentTimeMillis());
        mouse.releaseLeftButton(System.currentTimeMillis() + 10);

        delaySimulatingHumanUser();
        assertThat(listener.receivedEventType).isEqualTo(MouseEventType.SingleClick);

    }

    private void delaySimulatingHumanUser() throws InterruptedException {
        Thread.sleep(Mouse.clickTimeWindow + 100);
    }
}
