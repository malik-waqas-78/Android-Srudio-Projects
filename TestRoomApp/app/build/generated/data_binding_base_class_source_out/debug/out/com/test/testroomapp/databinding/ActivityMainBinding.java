// Generated by view binder compiler. Do not edit!
package com.test.testroomapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.test.testroomapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnAddMoviesToDb;

  @NonNull
  public final Button btnSearchActor;

  @NonNull
  public final Button btnSearchByString;

  @NonNull
  public final Button btnSearchMovie;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnAddMoviesToDb,
      @NonNull Button btnSearchActor, @NonNull Button btnSearchByString,
      @NonNull Button btnSearchMovie) {
    this.rootView = rootView;
    this.btnAddMoviesToDb = btnAddMoviesToDb;
    this.btnSearchActor = btnSearchActor;
    this.btnSearchByString = btnSearchByString;
    this.btnSearchMovie = btnSearchMovie;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_add_movies_to_db;
      Button btnAddMoviesToDb = ViewBindings.findChildViewById(rootView, id);
      if (btnAddMoviesToDb == null) {
        break missingId;
      }

      id = R.id.btn_search_actor;
      Button btnSearchActor = ViewBindings.findChildViewById(rootView, id);
      if (btnSearchActor == null) {
        break missingId;
      }

      id = R.id.btn_search_by_string;
      Button btnSearchByString = ViewBindings.findChildViewById(rootView, id);
      if (btnSearchByString == null) {
        break missingId;
      }

      id = R.id.btn_search_movie;
      Button btnSearchMovie = ViewBindings.findChildViewById(rootView, id);
      if (btnSearchMovie == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, btnAddMoviesToDb, btnSearchActor,
          btnSearchByString, btnSearchMovie);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
