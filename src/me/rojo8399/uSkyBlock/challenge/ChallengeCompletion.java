package me.rojo8399.uSkyBlock.challenge;

public class ChallengeCompletion {
    private String name;
    private long firstCompleted;
    private int timesCompleted;
    private int timesCompletedSinceTimer;

    public ChallengeCompletion(final String name) {
        super();
        this.name = name;
        this.firstCompleted = 0L;
        this.timesCompleted = 0;
    }

    public ChallengeCompletion(final String name, final long firstCompleted, final int timesCompleted, final int timesCompletedSinceTimer) {
        super();
        this.name = name;
        this.firstCompleted = firstCompleted;
        this.timesCompleted = timesCompleted;
        this.timesCompletedSinceTimer = timesCompletedSinceTimer;
    }

    public String getName() {
        return this.name;
    }

    public long getFirstCompleted() {
        return this.firstCompleted;
    }

    public boolean isOnCooldown() {
        return firstCompleted > System.currentTimeMillis();
    }

    public long getCooldownInMillis() {
        long now = System.currentTimeMillis();
        return firstCompleted > now ? firstCompleted - now : 0;
    }

    public int getTimesCompleted() {
        return this.timesCompleted;
    }

    public int getTimesCompletedSinceTimer() {
        return isOnCooldown() ? this.timesCompletedSinceTimer : timesCompleted > 0 ? 1 : 0;
    }

    public void setFirstCompleted(final long newCompleted) {
        this.firstCompleted = newCompleted;
        this.timesCompletedSinceTimer = 0;
    }

    public void setTimesCompleted(final int newCompleted) {
        this.timesCompleted = newCompleted;
        this.timesCompletedSinceTimer = newCompleted;
    }

    public void addTimesCompleted() {
        ++this.timesCompleted;
        ++this.timesCompletedSinceTimer;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
