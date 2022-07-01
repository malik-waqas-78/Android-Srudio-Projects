// Generated by view binder compiler. Do not edit!
package com.example.sqliteapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.sqliteapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnAdd;

  @NonNull
  public final Button btnDelAll;

  @NonNull
  public final Button btnDelEng;

  @NonNull
  public final EditText etName;

  @NonNull
  public final ListView listView;

  @NonNull
  public final Spinner spFaculty;

  @NonNull
  public final TextView textView;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnAdd,
      @NonNull Button btnDelAll, @NonNull Button btnDelEng, @NonNull EditText etName,
      @NonNull ListView listView, @NonNull Spinner spFaculty, @NonNull TextView textView) {
    this.rootView = rootView;
    this.btnAdd = btnAdd;
    this.btnDelAll = btnDelAll;
    this.btnDelEng = btnDelEng;
    this.etName = etName;
    this.listView = listView;
    this.spFaculty = spFaculty;
    this.textView = textView;
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
      id = R.id.btn_add;
      Button btnAdd = ViewBindings.findChildViewById(rootView, id);
      if (btnAdd == null) {
        break missingId;
      }

      id = R.id.btn_del_all;
      Button btnDelAll = ViewBindings.findChildViewById(rootView, id);
      if (btnDelAll == null) {
        break missingId;
      }

      id = R.id.btn_del_eng;
      Button btnDelEng = ViewBindings.findChildViewById(rootView, id);
      if (btnDelEng == null) {
        break missingId;
      }

      id = R.id.et_name;
      EditText etName = ViewBindings.findChildViewById(rootView, id);
      if (etName == null) {
        break missingId;
      }

      id = R.id.list_view;
      ListView listView = ViewBindings.findChildViewById(rootView, id);
      if (listView == null) {
        break missingId;
      }

      id = R.id.sp_faculty;
      Spinner spFaculty = ViewBindings.findChildViewById(rootView, id);
      if (spFaculty == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, btnAdd, btnDelAll, btnDelEng,
          etName, listView, spFaculty, textView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}