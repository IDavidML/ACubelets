package me.davidml16.acubelets.utils;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.objects.CubeletType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;

public class MessageUtils {

    public enum DefaultFontInfo {

        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PERENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        DEFAULT('a', 4);

        private char character;
        private int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        public char getCharacter() {
            return this.character;
        }

        public int getLength() {
            return this.length;
        }

        public int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) return this.getLength();
            return this.length + 1;
        }

        public static DefaultFontInfo getDefaultFontInfo(char c) {
            for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
                if (dFI.getCharacter() == c) return dFI;
            }
            return DefaultFontInfo.DEFAULT;
        }
    }

    private final static int CENTER_PX = 154;

    public static String centeredMessage(String message){
        String[] lines = ChatColor.translateAlternateColorCodes('&', message).split("\n", 40);
        StringBuilder returnMessage = new StringBuilder();


        for (String line : lines) {
            int messagePxSize = 0;
            boolean previousCode = false;
            boolean isBold = false;

            for (char c : line.toCharArray()) {
                if (c == '§') {
                    previousCode = true;
                } else if (previousCode) {
                    previousCode = false;
                    isBold = c == 'l';
                } else {
                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                    messagePxSize = isBold ? messagePxSize + dFI.getBoldLength() : messagePxSize + dFI.getLength();
                    messagePxSize++;
                }
            }
            int toCompensate = CENTER_PX - messagePxSize / 2;
            int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            int compensated = 0;
            StringBuilder sb = new StringBuilder();
            while(compensated < toCompensate){
                sb.append(" ");
                compensated += spaceLength;
            }
            returnMessage.append(sb.toString()).append(line).append("\n");
        }

        return returnMessage.toString();
    }

    public static void sendLootMessage(CubeletMachine cubeletMachine, CubeletType cubeletType, Reward reward) {
        Player target = Bukkit.getPlayer(cubeletMachine.getPlayerOpening().getUuid());
        if (target != null) {
            if(!Main.get().isDuplicationEnabled()) {
                newLootMessage(target, cubeletType, reward);
            } else if (!Main.get().getCubeletRewardHandler().isDuplicated(cubeletMachine, reward)) {
                newLootMessage(target, cubeletType, reward);
            } else if(Main.get().isDuplicationEnabled() && Main.get().getCubeletRewardHandler().isDuplicated(cubeletMachine, reward)) {
                duplicateLootMessage(target, cubeletType, reward, cubeletMachine.getLastDuplicationPoints());
            }
        }
    }

    private static void newLootMessage(Player target, CubeletType cubeletType, Reward reward) {
        for (String line : Main.get().getLanguageHandler().getMessageList("Cubelet.Reward.New")) {
            if (line.contains("%center%")) {
                line = line.replaceAll("%center%", "");
                target.sendMessage(MessageUtils.centeredMessage(Utils.translate(line
                        .replaceAll("%cubelet_type%", cubeletType.getName())
                        .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                        .replaceAll("%reward_rarity%", reward.getRarity().getName())
                )));
            } else {
                target.sendMessage(Utils.translate(line
                        .replaceAll("%cubelet_type%", cubeletType.getName())
                        .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                        .replaceAll("%reward_rarity%", reward.getRarity().getName())
                ));
            }
        }
    }

    private static void duplicateLootMessage(Player target, CubeletType cubeletType, Reward reward, int duplicatePoints) {

        if(duplicatePoints <= 0) {
            newLootMessage(target, cubeletType, reward);
            return;
        }

        for (String line : Main.get().getLanguageHandler().getMessageList("Cubelet.Reward.Duplicate")) {
            if (line.contains("%center%")) {
                line = line.replaceAll("%center%", "");
                target.sendMessage(MessageUtils.centeredMessage(Utils.translate(line
                        .replaceAll("%cubelet_type%", cubeletType.getName())
                        .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                        .replaceAll("%reward_rarity%", reward.getRarity().getName())
                        .replaceAll("%points%", ""+duplicatePoints)
                )));
            } else {
                target.sendMessage(Utils.translate(line
                        .replaceAll("%cubelet_type%", cubeletType.getName())
                        .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                        .replaceAll("%reward_rarity%", reward.getRarity().getName())
                        .replaceAll("%points%", ""+duplicatePoints)
                ));
            }
        }
    }

    public static void sendShopMessage(Player player) {
        if (player != null) {
            if(Main.get().isCubeletsCommandEnabled()) {
                switch (Main.get().getNoCubeletsCommandExecutor()) {
                    case "player":
                        player.chat("/" + Main.get().getNoCubeletsCommand());
                        break;
                    case "console":
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                                Main.get().getNoCubeletsCommand().replaceAll("%player%", player.getName()));
                        break;
                }
            } else {
                for (String line : Main.get().getLanguageHandler().getMessageList("Cubelet.NoCubelets")) {
                    if (line.contains("%center%")) {
                        line = line.replaceAll("%center%", "");
                        player.sendMessage(MessageUtils.centeredMessage(Utils.translate(line)));
                    } else {
                        player.sendMessage(Utils.translate(line));
                    }
                }
            }
        }
    }

}
