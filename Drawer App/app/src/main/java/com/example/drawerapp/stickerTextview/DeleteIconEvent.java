package com.example.drawerapp.stickerTextview;

import android.view.MotionEvent;

import com.example.drawerapp.stickerTextview.stickerInterface.StickerIconEvent;

/**
 * @author wupanjie
 */

public class DeleteIconEvent implements StickerIconEvent {
  @Override
  public void onActionDown(StickerView stickerView, MotionEvent event) {

  }

  @Override
  public void onActionMove(StickerView stickerView, MotionEvent event) {

  }

  @Override
  public void onActionUp(StickerView stickerView, MotionEvent event) {
    stickerView.removeCurrentSticker();
  }
}
