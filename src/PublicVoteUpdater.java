import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class PublicVoteUpdater {

    public static void updateAll() {
        for (Episode e : Episode.episodes) {
            if (e.getPublicVoteLink().contains("strawpoll")) {
                update(e);
            }
        }
    }

    public static void update(final Episode e) {
        double[] votePercentages = getPublicVotes(e);
        String qry = "update wahlkrampf set PublicVoteOne = '" + votePercentages[0] + "', PublicVoteTwo = '" + votePercentages[1] + "', PublicVoteThree = '" + votePercentages[2] + "' where PublicVoteLink = '" + e.getPublicVoteLink() + "'";
        MySQL.update(qry);
    }

    public static double[] getPublicVotes(Episode episode) {
        URL url;
        InputStreamReader reader;
        try {
            url = new URI(episode.getPublicVoteLink()).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = null;

        int[] votes = new int[3];

        while(true) {
            try {
                if ((line = bufferedReader.readLine()) == null) {
                    break;
                } else {
                    if (line.contains(updateCase(episode.getParticipantOne().getCase())) && line.contains("vote_count")) {
                        votes[0] = getVotesByLine(line, episode.getParticipantOne().getCase());
                        votes[1] = getVotesByLine(line, episode.getParticipantTwo().getCase());
                        votes[2] = getVotesByLine(line, episode.getParticipantThree().getCase());
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        double[] percentages = new double[3];
        int sum = getParticipantCount(line);
        percentages[0] = (double) Math.round(((double) votes[0] / sum) * 10000) /100;
        percentages[1] = (double) Math.round(((double) votes[1] / sum) * 10000) /100;
        percentages[2] = (double) Math.round(((double) votes[2] / sum) * 10000) /100;

        return percentages;
    }

    private static int getParticipantCount(final String s) {
        String line = s;
        while (!line.startsWith("participant_count")) {
            line = line.substring(1);
        }

        line = line.substring(19, 19+15);

        return toNumber(line);
    }

    private static int getVotesByLine(final String s, final String case_) {
        String caseString = updateCase(case_);
        String line = s;
        while (!line.startsWith(caseString)) {
            line = line.substring(1);
        }

        while (!line.startsWith("vote_count")) {
            line = line.substring(1);
        }

        line = line.substring(12, 22);

        return toNumber(line);
    }

    private static String updateCase(String aCase) {
        return aCase.replaceAll("/", "\\\\/");
    }

    private static int toNumber (final String s) {
        int x = -1;

        for (int i = 10; i > 0; i--) {
            try {
                x = Integer.parseInt(s.substring(0, i));
                break;
            } catch (NumberFormatException ignored) {}
        }

        return x;
    }

}