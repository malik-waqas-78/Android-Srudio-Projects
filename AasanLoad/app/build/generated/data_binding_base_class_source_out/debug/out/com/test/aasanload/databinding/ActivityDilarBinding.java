// Generated by view binder compiler. Do not edit!
package com.test.aasanload.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.test.aasanload.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDilarBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout btnDial;

  @NonNull
  public final CardView cvRv;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final ImageView ivCall;

  @NonNull
  public final RecyclerView rcCode;

  @NonNull
  public final TextView tv6;

  private ActivityDilarBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout btnDial, @NonNull CardView cvRv, @NonNull ImageView ivBack,
      @NonNull ImageView ivCall, @NonNull RecyclerView rcCode, @NonNull TextView tv6) {
    this.rootView = rootView;
    this.btnDial = btnDial;
    this.cvRv = cvRv;
    this.ivBack = ivBack;
    this.ivCall = ivCall;
    this.rcCode = rcCode;
    this.tv6 = tv6;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDilarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDilarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_dilar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDilarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_dial;
      ConstraintLayout btnDial = ViewBindings.findChildViewById(rootView, id);
      if (btnDial == null) {
        break missingId;
      }

      id = R.id.cv_rv;
      CardView cvRv = ViewBindings.findChildViewById(rootView, id);
      if (cvRv == null) {
        break missingId;
      }

      id = R.id.iv_back;
      ImageView ivBack = ViewBindings.findChildViewById(rootView, id);
      if (ivBack == null) {
        break missingId;
      }

      id = R.id.iv_call;
      ImageView ivCall = ViewBindings.findChildViewById(rootView, id);
      if (ivCall == null) {
        break missingId;
      }

      id = R.id.rc_code;
      RecyclerView rcCode = ViewBindings.findChildViewById(rootView, id);
      if (rcCode == null) {
        break missingId;
      }

      id = R.id.tv6;
      TextView tv6 = ViewBindings.findChildViewById(rootView, id);
      if (tv6 == null) {
        break missingId;
      }

      return new ActivityDilarBinding((ConstraintLayout) rootView, btnDial, cvRv, ivBack, ivCall,
          rcCode, tv6);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}