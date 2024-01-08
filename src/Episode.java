import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Episode {

    public static List<Episode> episodes = new ArrayList<>();

    public static void sortByReleaseDate() {
        episodes.sort(Comparator.comparing(Episode::getDate));
    }

    public static Episode getEpisodeByTitle(final String title) {
        return episodes.stream().filter(n -> n.getTitle().equals(title)).findFirst().orElseThrow();
    }

    public List<String> getParticipantsNames() {
        List<String> names = new ArrayList<>();
        names.add(participantOne.getName());
        names.add(participantTwo.getName());
        names.add(participantThree.getName());
        return names;
    }

    private final String releaseDate;
    private final String title;
    private final Participant participantOne;
    private final Participant participantTwo;
    private final Participant participantThree;
    private final String publicVoteLink;

    public Participant getParticipantOne() {
        return participantOne;
    }

    public Participant getParticipantTwo() {
        return participantTwo;
    }

    public Participant getParticipantThree() {
        return participantThree;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPublicVoteLink() {
        return publicVoteLink;
    }

    public Date getDate() {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(getReleaseDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Participant> getParticipants() {
        List<Participant> list = new ArrayList<>();
        list.add(participantOne);
        list.add(participantTwo);
        list.add(participantThree);
        return list;
    }

    public String getTitle() {
        return title;
    }

    public Participant getWinner() {
        if (participantOne.getCase().equals(participantTwo.getVote()) && participantOne.getCase().equals(participantThree.getVote())) {
            return participantOne;
        } else if (participantTwo.getCase().equals(participantOne.getVote()) && participantTwo.getCase().equals(participantThree.getVote())) {
            return participantTwo;
        } else if (participantThree.getCase().equals(participantOne.getVote()) && participantThree.getCase().equals(participantTwo.getVote())) {
            return participantThree;
        }
        return null;
    }

    public Participant getPublicVoteWinner() {
        if (participantOne.getPublicVotePercent() > participantTwo.getPublicVotePercent() && participantOne.getPublicVotePercent() > participantThree.getPublicVotePercent()) {
            return participantOne;
        } else if (participantTwo.getPublicVotePercent() > participantOne.getPublicVotePercent() && participantTwo.getPublicVotePercent() > participantThree.getPublicVotePercent()) {
            return participantTwo;
        } else if (participantThree.getPublicVotePercent() > participantOne.getPublicVotePercent() && participantThree.getPublicVotePercent() > participantTwo.getPublicVotePercent()) {
            return participantThree;
        }
        return null;
    }

    public Episode(final String releaseDate, final String title, final String participantOne, final String caseOne, final String voteOne, final String publicVoteOne, final String participantTwo, final String caseTwo, final String voteTwo, final String publicVoteTwo, final String participantThree, final String caseThree, final String voteThree, final String publicVoteThree, final String publicVoteLink) {
        this.releaseDate = releaseDate;
        this.title = title;
        this.participantOne = new Participant(participantOne, caseOne, voteOne, Double.parseDouble(publicVoteOne));
        this.participantTwo = new Participant(participantTwo, caseTwo, voteTwo, Double.parseDouble(publicVoteTwo));
        this.participantThree = new Participant(participantThree, caseThree, voteThree, Double.parseDouble(publicVoteThree));
        this.publicVoteLink = publicVoteLink;
    }

    public double getPercentageByCase(final String case_) {
        if (participantOne.getCase().equals(case_)) {
            return participantOne.getPublicVotePercent();
        } else if (participantTwo.getCase().equals(case_)) {
            return participantTwo.getPublicVotePercent();
        } else if (participantThree.getCase().equals(case_)) {
            return participantThree.getPublicVotePercent();
        }
        return -1;
    }

    public List<Participant> getLosers() {
        List<Participant> list = new ArrayList<>();
        getParticipants().stream().filter(n -> !n.equals(getWinner())).forEach(list::add);
        return list;
    }

    public static Episode getEpisodeByParticipant(Participant p) {
        return episodes.stream().filter(n -> n.getParticipants().contains(p)).findFirst().orElseThrow();
    }

}