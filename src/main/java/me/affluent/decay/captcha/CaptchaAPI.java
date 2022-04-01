package me.affluent.decay.captcha;

import me.affluent.decay.Constants;
import me.affluent.decay.util.system.FormatUtil;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;

public class CaptchaAPI {

    private static final HashMap<String, CaptchaData> pending = new HashMap<>();
    private static final HashMap<String, Integer> captchaActions = new HashMap<>();

    public static void doCaptchaAction(TextChannel tc, String userId, String mention) {
        if (userId.equalsIgnoreCase("429307019229397002")) return;
        if (pending.containsKey(userId)) return;
        int captchaActions1 = captchaActions.getOrDefault(userId, 0) + 1;
        captchaActions.put(userId, captchaActions1);
        int min = 45;
        int max = 60;
        if (captchaActions1 >= min) {
            if (captchaActions1 >= FormatUtil.getBetween(min, max)) {
                String verifyText = Captcha.generateText();
                boolean sent = sendCaptcha(tc, verifyText, userId, mention);
                captchaActions.remove(userId);
                if (!sent) pending.remove(userId);
            }
        }
    }

    public static void remove(String userId) {
        if (userId.equalsIgnoreCase("429307019229397002")) return;
        pending.remove(userId);
    }

    public static boolean isPending(String userId) {
        if (userId.equalsIgnoreCase("429307019229397002")) return false;
        return pending.containsKey(userId);
    }

    private static boolean sendCaptcha(TextChannel tc, String text, String userId, String mention) {
        if (userId.equalsIgnoreCase("429307019229397002")) return true;
        final byte[] data = Captcha.generateImage(text);
        final boolean[] success = new boolean[]{true};
        tc.sendMessage("**Verify Captcha**\n" + mention + " > `" + Constants.PREFIX + "verify <code>`")
                .addFile(data, "verify_" + userId + ".jpg").queue(s -> success[0] = true, f -> success[0] = false);
        pending.put(userId, new CaptchaData(text, data));
        return success[0];
    }

    public static CaptchaData getCaptchaData(String userId) {
        return pending.get(userId);
    }

    public static class CaptchaData {

        private final String text;
        private final byte[] data;

        CaptchaData(String text, byte[] data) {
            this.text = text;
            this.data = data;
        }

        public String getText() {
            return text;
        }

        public byte[] getData() {
            return data;
        }
    }
}