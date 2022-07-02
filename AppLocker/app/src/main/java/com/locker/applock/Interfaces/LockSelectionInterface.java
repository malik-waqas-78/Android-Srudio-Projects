package com.locker.applock.Interfaces;

import android.app.Dialog;

public interface LockSelectionInterface {
    void SelectLock(Dialog dialog, int radioId);
    void Dismiss(Dialog dialog);
}
