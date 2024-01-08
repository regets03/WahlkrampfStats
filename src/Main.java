public class Main {

    public static void main(String[] args) {
        MySQL.connect();
        MySQL.load();

        Stats.printHighestLosestreaks(5);

        MySQL.disconnect();
    }

}