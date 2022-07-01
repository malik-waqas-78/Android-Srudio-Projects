package com.example.drawerapp.stickerTextview.stickerInterface;

import android.view.MotionEvent;

import com.example.drawerapp.stickerTextview.StickerView;

/**
 * @author wupanjie
 */

public interface StickerIconEvent {
  void onActionDown(StickerView stickerView, MotionEvent event);

  void onActionMove(StickerView stickerView, MotionEvent event);

  void onActionUp(StickerView stickerView, MotionEvent event);
}
