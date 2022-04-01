package me.affluent.decay.util;

import me.affluent.decay.Withering;
import me.affluent.decay.entity.BadgeUser;
import me.affluent.decay.entity.Player;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class DonatorUtil {

    public static boolean isDonator(String uid) {
        return BadgeUser.getBadgeUser(uid).getBadgeList().contains("donator"); //TODO check if they have donator role
    }

    public static boolean isBoosting(String uid) {
        Guild hub = Withering.getHub();
        User u = Player.getUser(uid);
        if (u == null) return false;
        if (hub.isMember(u)) {
            Member m = hub.getMemberById(uid);
            if (m != null) return m.getTimeBoosted() != null;
        }
        return false;
    }
}