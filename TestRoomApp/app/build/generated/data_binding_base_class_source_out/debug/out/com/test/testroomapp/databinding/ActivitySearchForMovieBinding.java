// Generated by view binder compiler. Do not edit!
package com.test.testroomapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.test.testroomapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySearchForMovieBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnRetrieve;

  @NonNull
  public final Button btnSave;

  @NonNull
  public final EditText etMovieTitle;

  @NonNull
  public final ProgressBar pBar;

  @NonNull
  public final TextView tvDetails;

  private ActivitySearchForMovieBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnRetrieve, @NonNull Button btnSave, @NonNull EditText etMovieTitle,
      @NonNull ProgressBar pBar, @NonNull TextView tvDetails) {
    this.rootView = rootView;
    this.btnRetrieve = btnRetrieve;
    this.btnSave = btnSave;
    this.etMovieTitle = etMovieTitle;
    this.pBar = pBar;
    this.tvDetails = tvDetails;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySearchForMovieBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySearchForMovieBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_search_for_movie, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySearchForMovieBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_retrieve;
      Button btnRetrieve = ViewBindings.findChildViewById(rootView, id);
      if (btnRetrieve == null) {
        break missingId;
      }

      id = R.id.btn_save;
      Button btnSave = ViewBindings.findChildViewById(rootView, id);
      if (btnSave == null) {
        break missingId;
      }

      id = R.id.et_movie_title;
      EditText etMovieTitle = ViewBindings.findChildViewById(rootView, id);
      if (etMovieTitle == null) {
        break missingId;
      }

      id = R.id.p_bar;
      ProgressBar pBar = ViewBindings.findChildViewById(rootView, id);
      if (pBar == null) {
        break missingId;
      }

      id = R.id.tv_details;
      TextView tvDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvDetails == null) {
        break missingId;
      }

      return new ActivitySearchForMovieBinding((ConstraintLayout) rootView, btnRetrieve, btnSave,
          etMovieTitle, pBar, tvDetails);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}