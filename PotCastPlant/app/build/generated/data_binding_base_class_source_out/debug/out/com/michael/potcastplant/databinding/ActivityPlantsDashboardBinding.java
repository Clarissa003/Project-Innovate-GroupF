// Generated by view binder compiler. Do not edit!
package com.michael.potcastplant.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.michael.potcastplant.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPlantsDashboardBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button buttonAddPlant;

  @NonNull
  public final RecyclerView recyclerViewPlants;

  @NonNull
  public final TextView textViewNoPlantsYet;

  @NonNull
  public final TextView textViewTitle;

  private ActivityPlantsDashboardBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button buttonAddPlant, @NonNull RecyclerView recyclerViewPlants,
      @NonNull TextView textViewNoPlantsYet, @NonNull TextView textViewTitle) {
    this.rootView = rootView;
    this.buttonAddPlant = buttonAddPlant;
    this.recyclerViewPlants = recyclerViewPlants;
    this.textViewNoPlantsYet = textViewNoPlantsYet;
    this.textViewTitle = textViewTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPlantsDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPlantsDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_plants_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPlantsDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button_addPlant;
      Button buttonAddPlant = ViewBindings.findChildViewById(rootView, id);
      if (buttonAddPlant == null) {
        break missingId;
      }

      id = R.id.recycler_view_plants;
      RecyclerView recyclerViewPlants = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewPlants == null) {
        break missingId;
      }

      id = R.id.text_view_no_plants_yet;
      TextView textViewNoPlantsYet = ViewBindings.findChildViewById(rootView, id);
      if (textViewNoPlantsYet == null) {
        break missingId;
      }

      id = R.id.text_view_title;
      TextView textViewTitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewTitle == null) {
        break missingId;
      }

      return new ActivityPlantsDashboardBinding((ConstraintLayout) rootView, buttonAddPlant,
          recyclerViewPlants, textViewNoPlantsYet, textViewTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
