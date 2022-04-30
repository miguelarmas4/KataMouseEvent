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
    private boolean isWaitingToTriggerDoubleClick;
    private boolean isWaitingToTriggerTripleClick;
    private long lastTimeMoved;

    public void pressLeftButton(long currentTimeInMilliseconds) {
        /*... implement this method ...*/
        timePressed = currentTimeInMilliseconds;
        pressButton = true;
    }

    public void releaseLeftButton(long currentTimeInMilliseconds) {
        /*... implement this method ...*/
        if (pressButton) {
            resetButtonPressedState();
            if (wasDragging()) {
                handleDropEvent();
            } else {
                handleClickEvent();
            }
        }
    }

    private void handleDropEvent() {
        eventToTrigger = MouseEventType.Drop;
        notifySubscribers();
    }

    private boolean wasDragging() {
        return lastTimeMoved > timePressed;
    }

    private void resetButtonPressedState() {
        pressButton = false;
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
        if (isWaitingToTriggerSingleClick) {
            isWaitingToTriggerSingleClick = false;
        }
        if (isWaitingToTriggerDoubleClick) {
            isWaitingToTriggerDoubleClick = false;
        }
        if (isWaitingToTriggerTripleClick) {
            isWaitingToTriggerTripleClick = false;
        }
    }

    private boolean isFinallyAClickEvent() {
        return isWaitingToTriggerSingleClick ||
                isWaitingToTriggerDoubleClick ||
                isWaitingToTriggerTripleClick;
    }

    private void calculateTypeOfClickEvent() {
        if (!isWaitingToTriggerSingleClick) {
            isWaitingToTriggerSingleClick = true;
            eventToTrigger = MouseEventType.SingleClick;
        } else if (!isWaitingToTriggerDoubleClick) {
            isWaitingToTriggerDoubleClick = true;
            eventToTrigger = MouseEventType.DoubleClick;
        }else {
            isWaitingToTriggerTripleClick = true;
            eventToTrigger = MouseEventType.TripleClick;
        }
    }

    private void afterTheClickTimeWindow(Runnable runnable) {
        CompletableFuture.delayedExecutor(clickTimeWindow, TimeUnit.MILLISECONDS).execute(runnable);
    }

    public void move(MousePointerCoordinates from, MousePointerCoordinates to, long
            currentTimeInMilliseconds) {
        /*... implement this method ...*/
        lastTimeMoved = currentTimeInMilliseconds;
        if (pressButton) {
            eventToTrigger = MouseEventType.Drag;
            notifySubscribers();
        }
    }

    public void subscribe(MouseEventListener listener) {
        listeners.add(listener);
    }

    private void notifySubscribers() {
        listeners.forEach(listener -> listener.handleMouseEvent(eventToTrigger));
    }


}
