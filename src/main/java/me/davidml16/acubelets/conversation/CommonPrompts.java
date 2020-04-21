package me.davidml16.acubelets.conversation;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public interface CommonPrompts  {

    public static class CommonStringPrompt extends StringPrompt {
        private Prompt parentPrompt;
        private String text;
        private String storeValue;
        private boolean allowSpaces;
        private Main main;

        public CommonStringPrompt(Main main, Prompt param1Prompt, boolean param1Boolean, String param1String1, String param1String2) {
            this.main = main;
            this.parentPrompt = param1Prompt;
            this.allowSpaces = param1Boolean;
            this.text = param1String1;
            this.storeValue = param1String2;
        }

        public CommonStringPrompt(Prompt param1Prompt, String param1String1, String param1String2) {
            this(null, param1Prompt, true, param1String1, param1String2);
        }

        public String getPromptText(ConversationContext param1ConversationContext) {
            return this.text;
        }

        public Prompt acceptInput(ConversationContext param1ConversationContext, String param1String) {
            if (param1String.trim().equalsIgnoreCase("cancel")) {
                return this.parentPrompt;
            }
            if (!this.allowSpaces && param1String.contains(" ")) {
                param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  Spaces are not allowed!\n ");
                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                return this;
            }

            param1ConversationContext.setSessionData(this.storeValue, param1String);
            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.CLICK, 10, 2);
            return this.parentPrompt;
        }
    }

    public static class UncoloredStringPrompt extends StringPrompt {
        private Prompt parentPrompt;
        private String text;
        private String storeValue;
        private boolean allowSpaces;
        private Main main;

        public UncoloredStringPrompt(Main main, Prompt param1Prompt, boolean param1Boolean, String param1String1, String param1String2) {
            this.main = main;
            this.parentPrompt = param1Prompt;
            this.allowSpaces = param1Boolean;
            this.text = param1String1;
            this.storeValue = param1String2;
        }

        public UncoloredStringPrompt(Prompt param1Prompt, String param1String1, String param1String2) {
            this(null, param1Prompt, true, param1String1, param1String2);
        }

        public String getPromptText(ConversationContext param1ConversationContext) {
            return this.text;
        }

        public Prompt acceptInput(ConversationContext param1ConversationContext, String param1String) {
            if (param1String.trim().equalsIgnoreCase("cancel")) {
                return this.parentPrompt;
            }
            if (!this.allowSpaces && param1String.contains(" ")) {
                param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  Spaces are not allowed!\n ");
                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                return this;
            }
            if (param1String.contains("&") || param1String.contains("§")) {
                param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  Color codes are not allowed!\n ");
                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                return this;
            }

            param1ConversationContext.setSessionData(this.storeValue, param1String);
            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.CLICK, 10, 2);
            return this.parentPrompt;
        }
    }

    public static class SkullStringPrompt extends StringPrompt {
        private Prompt parentPrompt;
        private String text;
        private String storeValue;
        private boolean allowSpaces;
        private Main main;

        public SkullStringPrompt(Main main, Prompt param1Prompt, boolean param1Boolean, String param1String1, String param1String2) {
            this.main = main;
            this.parentPrompt = param1Prompt;
            this.allowSpaces = param1Boolean;
            this.text = param1String1;
            this.storeValue = param1String2;
        }

        public SkullStringPrompt(Prompt param1Prompt, String param1String1, String param1String2) {
            this(null, param1Prompt, true, param1String1, param1String2);
        }

        public String getPromptText(ConversationContext param1ConversationContext) {
            return this.text;
        }

        public Prompt acceptInput(ConversationContext param1ConversationContext, String param1String) {
            if (param1String.trim().equalsIgnoreCase("cancel")) {
                return this.parentPrompt;
            }
            if (!this.allowSpaces && param1String.contains(" ")) {
                param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  Spaces are not allowed!\n ");
                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                return this;
            }
            if (!param1String.startsWith("base64:") && !param1String.startsWith("uuid:") && !param1String.startsWith("url:") && !param1String.startsWith("name:")) {
                param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  Texture string needs to start with one of specified options!\n  'name:', 'url:', 'uuid:' or 'base64:'\n ");
                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                return this;
            }

            param1ConversationContext.setSessionData(this.storeValue, param1String);
            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.CLICK, 10, 2);
            return this.parentPrompt;
        }
    }

    public static class BooleanPrompt extends StringPrompt {
        private Prompt parentPrompt;
        private String text;
        private String storeValue;
        private Main main;

        public BooleanPrompt(Main main, Prompt param1Prompt, String param1String1, String param1String2) {
            this.main = main;
            this.parentPrompt = param1Prompt;
            this.text = param1String1;
            this.storeValue = param1String2;
        }

        public String getPromptText(ConversationContext param1ConversationContext) {
            return this.text;
        }

        public Prompt acceptInput(ConversationContext param1ConversationContext, String param1String) {
            if (param1String.trim().equalsIgnoreCase("cancel")) {
                return this.parentPrompt;
            }

            if (param1String.contains(" ")) {
                param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  Spaces are not allowed!\n ");
                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                return this;
            }

            if (param1String.equalsIgnoreCase("true") || param1String.equalsIgnoreCase("false")) {
                param1ConversationContext.setSessionData(this.storeValue, param1String.toLowerCase());
                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.CLICK, 10, 2);
                return parentPrompt;
            }

            param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  That's not a valid option!\n ");
            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
            return this;
        }
    }

    public static class ConfirmExitPrompt extends StringPrompt {
        private Prompt parent;
        private Main main;

        ConfirmExitPrompt(Main main, Prompt param1Prompt) {
            this.main = main;
            this.parent = param1Prompt;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String str = ChatColor.GREEN + "  1 " + ChatColor.GRAY + "- Yes\n" + ChatColor.RED + "  2 " + ChatColor.GRAY + "- No\n ";

            return ChatColor.YELLOW + "\n Are you sure you want to exit without saving?\n \n" + str;
        }


        public Prompt acceptInput(ConversationContext param1ConversationContext, String param1String) {
            if (param1String.equals("1") || param1String.equalsIgnoreCase("Yes")) {

                param1ConversationContext.getForWhom().sendRawMessage("\n" + ColorUtil.translate(main.getLanguageHandler().getPrefix()
                        + " &cYou leave type setup menu!"));
                return Prompt.END_OF_CONVERSATION;
            }
            if (param1String.equals("2") || param1String.equalsIgnoreCase("No")) {
                return this.parent;
            }
            param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  That's not a valid option!\n");
            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
            return this;
        }
    }

    public static class ErrorPrompt extends StringPrompt {
        private Prompt parent;
        private String text;
        private Main main;

        ErrorPrompt(Main main, Prompt param1Prompt, String param1String1) {
            this.main = main;
            this.parent = param1Prompt;
            this.text = param1String1;
        }

        public String getPromptText(ConversationContext param1ConversationContext) {
            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
            return text;
        }


        public Prompt acceptInput(ConversationContext param1ConversationContext, String param1String) {
            param1ConversationContext.getForWhom().sendRawMessage(text);
            return parent;
        }
    }

}