// Generated by view binder compiler. Do not edit!
package com.example.hissabbookapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.example.hissabbookapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CustomDialogBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnByCashIn;

  @NonNull
  public final Button btnByCashOut;

  @NonNull
  public final Button btnNone;

  @NonNull
  public final TextView textInstr;

  private CustomDialogBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnByCashIn,
      @NonNull Button btnByCashOut, @NonNull Button btnNone, @NonNull TextView textInstr) {
    this.rootView = rootView;
    this.btnByCashIn = btnByCashIn;
    this.btnByCashOut = btnByCashOut;
    this.btnNone = btnNone;
    this.textInstr = textInstr;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CustomDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CustomDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.custom_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CustomDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnByCashIn;
      Button btnByCashIn = rootView.findViewById(id);
      if (btnByCashIn == null) {
        break missingId;
      }

      id = R.id.btnByCashOut;
      Button btnByCashOut = rootView.findViewById(id);
      if (btnByCashOut == null) {
        break missingId;
      }

      id = R.id.btnNone;
      Button btnNone = rootView.findViewById(id);
      if (btnNone == null) {
        break missingId;
      }

      id = R.id.textInstr;
      TextView textInstr = rootView.findViewById(id);
      if (textInstr == null) {
        break missingId;
      }

      return new CustomDialogBinding((ConstraintLayout) rootView, btnByCashIn, btnByCashOut,
          btnNone, textInstr);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
