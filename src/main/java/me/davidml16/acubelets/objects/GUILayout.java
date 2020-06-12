package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.utils.ColorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUILayout {

    private HashMap<String, String> messages;
    private HashMap<String, List<String>> messageList;

    public GUILayout(HashMap<String, String> messages, HashMap<String, List<String>> messageList) {
        this.messages = messages;
        this.messageList = messageList;
    }

    public GUILayout() {
        this(new HashMap<String, String>(), new HashMap<String, List<String>>());
    }

    public HashMap<String, String> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, String> messages) {
        this.messages = messages;
    }

    public HashMap<String, List<String>> getMessageList() {
        return messageList;
    }

    public void setMessageList(HashMap<String, List<String>> messageList) {
        this.messageList = messageList;
    }

    public String getMessage(String message) {
        return ColorUtil.translate(messages.get("Layout." + message));
    }

    public List<String> getMessageList(String message) {
        List<String> lines = new ArrayList<>();
        for(String line : messageList.get("Layout." + message)) {
            lines.add(ColorUtil.translate(line));
        }
        return lines;
    }

}
