package me.affluent.decay.patreon;

import me.affluent.decay.Constants;
import me.affluent.decay.Withering;
import me.affluent.decay.entity.Player;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PatreonClient {

    private static final List<String> pledgeQueue = new ArrayList<>();

    PatreonClient(final Socket socket, final InputStream in, final long id) {
        final DataInputStream dis = new DataInputStream(in);
        try {
            boolean authorized = false;
            System.out.println("Processing client...");
            while (socket != null && !socket.isClosed()) {
                @SuppressWarnings("deprecation") final String line = dis.readLine();
                if (line == null) continue;
                if (line.startsWith("Authorization: ")) {
                    if (line.substring("Authorization: ".length()).equals(Constants.dbl_vote_auth)) {
                        authorized = true;
                    }
                }
                boolean process = false;
                System.out.println(line);
                if (process) {
                    final JSONObject json = new JSONObject(line);
                    final String uid = json.getString("user");
                    pledge(uid, authorized);
                    //
                    dis.close();
                    socket.close();
                    PatreonServer.close(id);
                    break;
                }
            }
        } catch (SocketException | SocketTimeoutException ignored) {
            PatreonServer.close(id);
        } catch (Exception ex) {
            PatreonServer.close(id);
            ex.printStackTrace();
        }
    }

    public static void pledge(final String uid, final boolean authorized) {
        if (!authorized) {
            return;
        }
        final User u = Withering.getBot().getShardManager().retrieveUserById(uid).complete();
        if (u != null) {
            if (pledgeQueue.contains(uid)) return;
            pledgeQueue.add(uid);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pledgeQueue.remove(uid);
                }
            }, 7544);
            if (!Player.playerExists(uid)) {
                return;
            }
            final Player p = Player.getPlayer(uid);
            long now = System.currentTimeMillis();
            //long until = -1;
            //PatreonSystem.addPladgers(u.getId(), until);
            //u.openPrivateChannel..blabla..sendMessage
        }
    }
}