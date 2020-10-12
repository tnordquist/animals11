package edu.cnm.deepdive.animals11.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.animals11.R;
import edu.cnm.deepdive.animals11.model.Animal;
import edu.cnm.deepdive.animals11.viewmodel.AnimalViewModel;
import java.util.List;
import java.util.Objects;

public class ImageFragment extends Fragment implements OnItemSelectedListener {

  private WebView contentView;
  private AnimalViewModel animalViewModel;
  private Spinner spinner;
  private List<Animal> animals;
  private Toolbar toolbar;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image, container, false);
    setupWebView(root);
    toolbar = root.findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.app_name);
    spinner = root.findViewById(R.id.animals_spinner);
    spinner.setOnItemSelectedListener(this);
    ((MainActivity) getActivity()).setSupportActionBar(toolbar);
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    animalViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
        .get(AnimalViewModel.class);
    animalViewModel.getAnimals().observe(getViewLifecycleOwner(), new Observer<List<Animal>>() {
      @Override
      public void onChanged(List<Animal> animals) {
        ImageFragment.this.animals = animals;
        ArrayAdapter<Animal> adapter = new ArrayAdapter<>(
            ImageFragment.this.getContext(), R.layout.custom_spinner_item, animals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

      }
    });
  }

  private void setupWebView(View root) {
    contentView = root.findViewById(R.id.content_view);
    contentView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
      }
    });
    WebSettings settings = contentView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
    contentView.loadUrl(animals.get(pos).getImageUrl());
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }
}