package com.voicesms.voice.Interfaces;

import com.voicesms.voice.Adapters.OpenMessageAdapter;

public interface OpenMessageInterface {
    void openMessage(String msg);

    void deleteMessage(String msg, OpenMessageAdapter.notifyUpdate notifyUpdate, int itemCount);
}
