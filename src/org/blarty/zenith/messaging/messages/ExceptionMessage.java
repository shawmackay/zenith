package org.jini.projects.zenith.messaging.messages;

public class ExceptionMessage implements Message {

    private MessageHeader header = null;
    private Exception ex = null;
    private Message originalMessage = null;
    
    public ExceptionMessage(MessageHeader header, Exception ex, Message originalMessage){
        this.header = header;
        this.ex = ex;
        this.originalMessage = originalMessage;
    }
    
    public MessageHeader getHeader() {
        // TODO Auto-generated method stub
        return header;
    }

    public Object getMessageContent() {
        // TODO Auto-generated method stub
        return ex;
    }

    public String getMessageContentAsString() {
        // TODO Auto-generated method stub
        return ex.getMessage();
    }

    public Message getOriginalMessage(){
        return originalMessage;
    }
}
