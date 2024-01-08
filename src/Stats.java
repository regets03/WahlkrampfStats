import java.util.*;

public class Stats {

    public static void printHighestPercentages(final int amount) {
        List<Participant> list = getSortedPercentages();
        list = list.reversed();
        for (int i = 0; i < amount; i++) {
            Episode e = Episode.getEpisodeByParticipant(list.get(i));
            System.out.println("In der Folge " + e.getTitle() + " hat " + list.get(i).getGenitiveName() + " Case " + list.get(i).getCase() + " " + list.get(i).getPublicVotePercent() + "% bekommen");
        }
    }

    public static void printOrders() {
        Episode.episodes.forEach(e -> System.out.printf("| %-10s | %-10s | %-10s | %-10s |%n", e.getReleaseDate(), e.getParticipantOne().getName(), e.getParticipantTwo().getName(), e.getParticipantThree().getName()));
    }

    public static void printOrdersAmount() {
        Map<List<String>, Integer> map = new HashMap<>();

        for (Episode e : Episode.episodes) {
            if (map.containsKey(e.getParticipantsNames())) {
                map.put(e.getParticipantsNames(), map.get(e.getParticipantsNames()) + 1);
            } else {
                map.put(e.getParticipantsNames(), 1);
            }
        }

        List<Map.Entry<List<String>, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        list = list.reversed();

        for (Map.Entry<List<String>, Integer> entry : list) {
            System.out.printf("| %-10s | %-10s | %-10s | %-3s |%n", entry.getKey().get(0), entry.getKey().get(1), entry.getKey().get(2), entry.getValue());
        }
    }


    public static void printLowestPercentages(final int amount) {
        List<Participant> list = getSortedPercentages();
        for (int i = 0; i < amount; i++) {
            Episode e = Episode.getEpisodeByParticipant(list.get(i));
            System.out.println("In der Folge " + e.getTitle() + " hat " + list.get(i).getGenitiveName() + " Case " + list.get(i).getCase() + " nur " + list.get(i).getPublicVotePercent() + "% bekommen");
        }
    }


    private static List<Participant> getSortedPercentages() {
        List<Participant> list = new ArrayList<>();
        Episode.episodes.forEach(n -> list.addAll(n.getParticipants()));
        list.sort(Comparator.comparing(Participant::getPublicVotePercent));
        return list;
    }


    public static void printLowestVotedWinningCases(final int amount) {
        List<Participant> list = new ArrayList<>();
        Episode.episodes.forEach(n -> list.add(n.getWinner()));
        list.sort(Comparator.comparing(Participant::getPublicVotePercent));

        for (int i = 0; i < amount; i++) {
            Episode episode = Episode.getEpisodeByParticipant(list.get(i));
            System.out.println("In der Folge " + episode.getTitle() + " hat " + episode.getWinner().getGenitiveName() + " Case " + episode.getWinner().getCase() + " gewonnen, der im Public Vote nur " + episode.getWinner().getPublicVotePercent() + "% bekommen hat");
        }
    }

    public static void printNumbersAmountByName(final String name) {
        int[] amounts = new int[3];

        for (int i = 0; i < 3; i++) {
            List<Participant> list = new ArrayList<>();
            int finalI = i;
            Episode.episodes.forEach(n -> list.add(n.getParticipants().get(finalI)));
            amounts[i] = (int) list.stream().filter(n -> n.getName().equals(name)).count();
        }

        System.out.println(name + " war in " + getEpisodesAmountByName(name) + " Folgen " + amounts[0] + " mal an 1. Stelle, " + amounts[1] + " mal an 2. Stelle und " + amounts[2] + " mal an 3. Stelle");
    }

    public static int getEpisodesAmountByName(final String name) {
        List<Participant> list = new ArrayList<>();
        Episode.episodes.forEach(n -> list.addAll(n.getParticipants()));
        return (int) list.stream().filter(n -> n.getName().equals(name)).count();
    }

    private static List<Streak> getHighestWinningStreaks() {
        List<Streak> list = new ArrayList<>();
        Streak current = null;
        for (Episode e : Episode.episodes) {
            if (current == null) {
                current = new Streak(e, e.getWinner().getName());
            } else if (current.element.equals(e.getWinner().getName())){
                current.addEpisode(e);
            } else {
                list.add(current);
                current = new Streak(e, e.getWinner().getName());
            }
        }
        list.sort(Comparator.comparing(Streak::getStreak));
        list = list.reversed();
        return list;
    }

    private static List<Streak> getHighestLosingStreaks() {
        List<Streak> list = new ArrayList<>();
        Streak one = null;
        Streak two = null;
        for (Episode e : Episode.episodes) {
            for (Participant p : e.getLosers()) {
                if (one == null) {
                    one = new Streak(e, p.getName());
                } else if (two == null) {
                    two = new Streak(e, p.getName());
                } else if (one.element.equals(p.getName())) {
                    one.addEpisode(e);
                } else if (two.element.equals(p.getName())) {
                    two.addEpisode(e);
                } else {
                    if (!(one.element.equals(e.getLosers().get(0).getName()) || one.element.equals(e.getLosers().get(1).getName()))) {
                        list.add(one);
                        one = new Streak(e, p.getName());
                    } else {
                        list.add(two);
                        two = new Streak(e, p.getName());
                    }
                }
            }
        }
        list.sort(Comparator.comparing(Streak::getStreak));
        list = list.reversed();
        return list;
    }

    public static void printHighestLosestreaks(final int amount) {
        List<Streak> list = getHighestLosingStreaks();
        for (int i = 0; i < amount; i++) {
            Streak streak = list.get(i);
            System.out.println(streak.element + " hat in den Folgen ");
            streak.episodes.forEach(n -> System.out.println(n.getTitle()));
            System.out.println("keinen Sieg geholt und damit ein Streak von " + streak.streak + "\n\n");
        }
    }

    public static void printHighestWinstreaks(final int amount) {
        List<Streak> list = getHighestWinningStreaks();
        for (int i = 0; i < amount; i++) {
            Streak streak = list.get(i);
            System.out.print(streak.element + " hat die Folgen ");
            streak.episodes.forEach(n -> System.out.print(n.getTitle() + " "));
            System.out.println("gewonnen und damit ein Streak von " + streak.streak);
        }
    }

    private static class Streak {

        List<Episode> episodes = new ArrayList<>();
        String element;
        int streak = 0;

        public int getStreak() {
            return streak;
        }

        public Streak (final Episode startEpisode, final String element) {
            this.element = element;
            addEpisode(startEpisode);
        }

        public void addEpisode(final Episode episode) {
            episodes.add(episode);
            streak++;
        }

    }
}
