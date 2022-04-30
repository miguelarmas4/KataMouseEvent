package mouse;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Mouse {
    private List<MouseEventListener> listeners= new ArrayList<>();
    private final long timeWindowInMillisecondsForDoubleClick = 500;
    public static long clickTimeWindow = 500;
    private static MouseEventType eventToTrigger;

    private long timePressed;
    private boolean pressButton;
    private boolean isWaitingToTriggerSingleClick;

    public void pressLeftButton(long currentTimeInMilliseconds) {
        /*... implement this method ...*/
        timePressed = timeWindowInMillisecondsForDoubleClick;
        pressButton = true;
    }

    public void releaseLeftButton(long currentTimeInMilliseconds) {
        /*... implement this method ...*/
        if (pressButton) {
            pressButton = false;
            handleClickEvent();
        }
    }

    private void handleClickEvent() {
        calculateTypeOfClickEvent();
        afterTheClickTimeWindow(() -> {
            boolean isClick = isFinallyAClickEvent();
            resetClickState();
            if (isClick) {
                notifySubscribers();
            }
        });
    }

    private void resetClickState() {
        isWaitingToTriggerSingleClick = false;
    }

    private boolean isFinallyAClickEvent() {
        return isWaitingToTriggerSingleClick;
    }

    private void calculateTypeOfClickEvent() {
        if (!isWaitingToTriggerSingleClick) {
            isWaitingToTriggerSingleClick = true;
            eventToTrigger = MouseEventType.SingleClick;
        }
    }

    private void afterTheClickTimeWindow(Runnable runnable) {
        CompletableFuture.delayedExecutor(clickTimeWindow, TimeUnit.MILLISECONDS).execute(runnable);
    }
    public void move(MousePointerCoordinates from, MousePointerCoordinates to, long
            currentTimeInMilliseconds) {
        /*... implement this method ...*/
    }

    public void subscribe(MouseEventListener listener) {
        listeners.add(listener);
    }

    private void notifySubscribers() {
        listeners.forEach(listener -> listener.handleMouseEvent(eventToTrigger));
    }


}
