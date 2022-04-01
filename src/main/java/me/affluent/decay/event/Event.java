package me.affluent.decay.event;

public abstract class Event {

    private boolean cancelled = false;
    private String cancelReason;
    private boolean printCancel = true;

    Event() {
    }

    public void setPrintCancel(boolean printCancel) {
        this.printCancel = printCancel;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean printCancel() { return printCancel; }

    public String getCancelReason() {
        return cancelReason;
    }
}