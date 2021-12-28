package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConversationHandler {

    private Set<UUID> conversations;

    private Main main;

    public ConversationHandler(Main main) {
        this.main = main;
        this.conversations = new HashSet<>();
    }

    public void addConversation(Player player) {
        conversations.add(player.getUniqueId());
    }

    public boolean haveConversation(Player player) {
        return conversations.contains(player.getUniqueId());
    }

    public void removeConversation(Player player) {
        conversations.remove(player.getUniqueId());
    }

}
