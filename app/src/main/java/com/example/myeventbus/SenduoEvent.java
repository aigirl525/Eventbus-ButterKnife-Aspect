package com.example.myeventbus;

import java.io.Serializable;

/**
 * 通过这个类来确认该由哪些事件接收者来处理事件
 * 方法传递的参数
 */
public class SenduoEvent implements Serializable {
    private String eventType;
    private String eventContent;

    public SenduoEvent(String eventType, String eventContent) {
        this.eventType = eventType;
        this.eventContent = eventContent;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    @Override
    public String toString() {
        return "SenduoEvent{" +
                "eventType='" + eventType + '\'' +
                ", eventContent='" + eventContent + '\'' +
                '}';
    }
}
