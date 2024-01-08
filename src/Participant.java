public class Participant {

    private final String name;
    private final String case_;
    private final String vote;
    private final double publicVotePercent;

    public String getName() {
        return name;
    }

    public String getCase() {
        return case_;
    }

    public String getVote() {
        return vote;
    }

    public double getPublicVotePercent() {
        return publicVotePercent;
    }

    public String getGenitiveName() {
        if (name.endsWith("s")) {
            return name + "'";
        } else {
            return name + "s";
        }
    }

    public Participant(final String name, final String case_, final String vote, final double publicVotePercent) {
        this.name = name;
        this.case_ = case_;
        this.vote = vote;
        this.publicVotePercent = publicVotePercent;
    }

}