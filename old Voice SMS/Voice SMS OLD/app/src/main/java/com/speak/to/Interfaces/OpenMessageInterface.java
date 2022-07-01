package com.speak.to.Interfaces;

import com.speak.to.Adapters.OpenMessageAdapter;

public interface OpenMessageInterface {
    void openMessage(String msg);

    void deleteMessage(String msg, OpenMessageAdapter.notifyUpdate notifyUpdate, int itemCount);
}
