package com.mjcdouai.go4lunch;

import com.mjcdouai.go4lunch.model.ChatMessage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ChatMessageTest {

    private ChatMessage mChatMessage;
    private long mCreationTime;

    @Before
    public void setup() {
        mChatMessage = new ChatMessage("Message", "User");
        mCreationTime = new Date().getTime();
    }

    @Test
    public void getMessageTextWithSuccess() {
        Assert.assertEquals("Message", mChatMessage.getMessageText());
    }

    @Test
    public void getMessageUserWithSuccess() {
        Assert.assertEquals("User", mChatMessage.getMessageUser());

    }

    @Test
    public void getMessageTimeWithSuccess() {
        Assert.assertEquals(mCreationTime, mChatMessage.getMessageTime());
    }

    @Test
    public void setMessageTextWithSuccess() {
        mChatMessage.setMessageText("newText");
        Assert.assertEquals("newText", mChatMessage.getMessageText());
    }

    @Test
    public void setMessageUserWithSuccess() {
        mChatMessage.setMessageUser("newUser");
        Assert.assertEquals("newUser", mChatMessage.getMessageUser());
    }

    @Test
    public void setMessageTimeWithSuccess() {
        mChatMessage.setMessageTime(1000);
        Assert.assertEquals(1000, mChatMessage.getMessageTime());
    }

}
