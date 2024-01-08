import java.sql.*;

public class MySQL {

    private static final String host = "localhost";
    private static final String port = "3306";
    private static final String database = "wahlkrampf";
    private static final String username = "root";
    private static final String password = "wahlkrampf123";

    private static Connection connection;

    public static boolean isConnected() {
        return connection != null;
    }

    public static void connect() {
        if (!isConnected()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("\n[MySQL] connected\n");
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
                System.out.println("\n[MySQL] disconnected\n");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void update(final String qry) {
        try {
            connection.prepareStatement(qry).execute();
            System.out.println("\n[MySQL] update\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static ResultSet query(final String qry) {
        try {
            return connection.createStatement().executeQuery(qry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {
        System.out.println("\n[MySQL] loading\n");
        ResultSet rs = query("select * from wahlkrampf order by ID asc");

        while (true) {
            try {
                if (!rs.next()) {
                    break;
                } else {
                    String releaseDate = rs.getString("releaseDate");
                    String title = rs.getString("title");
                    String participantOne = rs.getString("participantOne");
                    String caseOne = rs.getString("caseOne");
                    String voteOne = rs.getString("voteOne");
                    String publicVoteOne = rs.getString("publicVoteOne");
                    String participantTwo = rs.getString("participantTwo");
                    String caseTwo = rs.getString("caseTwo");
                    String voteTwo = rs.getString("voteTwo");
                    String publicVoteTwo = rs.getString("publicVoteTwo");
                    String participantThree = rs.getString("participantThree");
                    String caseThree = rs.getString("caseThree");
                    String voteThree = rs.getString("voteThree");
                    String publicVoteThree = rs.getString("publicVoteThree");
                    String publicVoteLink = rs.getString("publicVoteLink");
                    Episode episode = new Episode(releaseDate, title, participantOne, caseOne, voteOne, publicVoteOne, participantTwo, caseTwo, voteTwo, publicVoteTwo, participantThree, caseThree, voteThree, publicVoteThree, publicVoteLink);
                    Episode.episodes.add(episode);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
