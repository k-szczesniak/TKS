package com.mycompany.firstapplication.Controllers;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

public abstract class Conversational {
    @Inject
    protected Conversation conversation;

    protected void endCurrentConversation() {
        if (!conversation.isTransient()) conversation.end();
    }

    protected void beginNewConversation() {
        endCurrentConversation();
        conversation.begin();
    }
}
