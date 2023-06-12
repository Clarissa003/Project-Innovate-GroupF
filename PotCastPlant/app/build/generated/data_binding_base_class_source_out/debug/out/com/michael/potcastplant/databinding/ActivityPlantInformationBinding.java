// Generated by view binder compiler. Do not edit!
package com.michael.potcastplant.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.michael.potcastplant.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPlantInformationBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final CardView humidity;

  @NonNull
  public final LineChart lineChart;

  @NonNull
  public final CardView moisture;

  @NonNull
  public final TextView nameText;

  @NonNull
  public final ImageView plantImage;

  @NonNull
  public final CardView sunlight;

  @NonNull
  public final SwitchMaterial switch1;

  @NonNull
  public final CardView temperature;

  @NonNull
  public final TextView textViewHumidity;

  @NonNull
  public final TextView textViewSoilMoisture;

  @NonNull
  public final TextView textViewTemperature;

  @NonNull
  public final TextView textViewWaterLevel;

  @NonNull
  public final TextView watering;

  @NonNull
  public final CardView wlevel;

  private ActivityPlantInformationBinding(@NonNull ScrollView rootView, @NonNull CardView humidity,
      @NonNull LineChart lineChart, @NonNull CardView moisture, @NonNull TextView nameText,
      @NonNull ImageView plantImage, @NonNull CardView sunlight, @NonNull SwitchMaterial switch1,
      @NonNull CardView temperature, @NonNull TextView textViewHumidity,
      @NonNull TextView textViewSoilMoisture, @NonNull TextView textViewTemperature,
      @NonNull TextView textViewWaterLevel, @NonNull TextView watering, @NonNull CardView wlevel) {
    this.rootView = rootView;
    this.humidity = humidity;
    this.lineChart = lineChart;
    this.moisture = moisture;
    this.nameText = nameText;
    this.plantImage = plantImage;
    this.sunlight = sunlight;
    this.switch1 = switch1;
    this.temperature = temperature;
    this.textViewHumidity = textViewHumidity;
    this.textViewSoilMoisture = textViewSoilMoisture;
    this.textViewTemperature = textViewTemperature;
    this.textViewWaterLevel = textViewWaterLevel;
    this.watering = watering;
    this.wlevel = wlevel;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPlantInformationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPlantInformationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_plant_information, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPlantInformationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.humidity;
      CardView humidity = ViewBindings.findChildViewById(rootView, id);
      if (humidity == null) {
        break missingId;
      }

      id = R.id.line_chart;
      LineChart lineChart = ViewBindings.findChildViewById(rootView, id);
      if (lineChart == null) {
        break missingId;
      }

      id = R.id.moisture;
      CardView moisture = ViewBindings.findChildViewById(rootView, id);
      if (moisture == null) {
        break missingId;
      }

      id = R.id.name_text;
      TextView nameText = ViewBindings.findChildViewById(rootView, id);
      if (nameText == null) {
        break missingId;
      }

      id = R.id.plant_image;
      ImageView plantImage = ViewBindings.findChildViewById(rootView, id);
      if (plantImage == null) {
        break missingId;
      }

      id = R.id.sunlight;
      CardView sunlight = ViewBindings.findChildViewById(rootView, id);
      if (sunlight == null) {
        break missingId;
      }

      id = R.id.switch1;
      SwitchMaterial switch1 = ViewBindings.findChildViewById(rootView, id);
      if (switch1 == null) {
        break missingId;
      }

      id = R.id.temperature;
      CardView temperature = ViewBindings.findChildViewById(rootView, id);
      if (temperature == null) {
        break missingId;
      }

      id = R.id.text_view_humidity;
      TextView textViewHumidity = ViewBindings.findChildViewById(rootView, id);
      if (textViewHumidity == null) {
        break missingId;
      }

      id = R.id.text_view_soil_moisture;
      TextView textViewSoilMoisture = ViewBindings.findChildViewById(rootView, id);
      if (textViewSoilMoisture == null) {
        break missingId;
      }

      id = R.id.text_view_temperature;
      TextView textViewTemperature = ViewBindings.findChildViewById(rootView, id);
      if (textViewTemperature == null) {
        break missingId;
      }

      id = R.id.text_view_water_level;
      TextView textViewWaterLevel = ViewBindings.findChildViewById(rootView, id);
      if (textViewWaterLevel == null) {
        break missingId;
      }

      id = R.id.watering;
      TextView watering = ViewBindings.findChildViewById(rootView, id);
      if (watering == null) {
        break missingId;
      }

      id = R.id.wlevel;
      CardView wlevel = ViewBindings.findChildViewById(rootView, id);
      if (wlevel == null) {
        break missingId;
      }

      return new ActivityPlantInformationBinding((ScrollView) rootView, humidity, lineChart,
          moisture, nameText, plantImage, sunlight, switch1, temperature, textViewHumidity,
          textViewSoilMoisture, textViewTemperature, textViewWaterLevel, watering, wlevel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
