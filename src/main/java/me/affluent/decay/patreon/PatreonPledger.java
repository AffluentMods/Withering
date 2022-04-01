package me.affluent.decay.patreon;

public class PatreonPledger {

    private final String userId;
    private final String pledge;
    private final long end;

    PatreonPledger(String userId, String pledge, long end) {
        this.userId = userId;
        this.pledge = pledge;
        this.end = end;
    }

    public String getUserId() {
        return userId;
    }

    public String getPledge() {
        return pledge;
    }

    public long getEnd() {
        return end;
    }
}
