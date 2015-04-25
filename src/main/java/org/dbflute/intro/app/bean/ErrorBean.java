package org.dbflute.intro.app.bean;

import java.util.List;
import java.util.Map;

public class ErrorBean implements MessageBean {

    private Map<String, List<String>> messages;

    @Override
    public Map<String, List<String>> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, List<String>> messages) {
        this.messages = messages;
    }
}
