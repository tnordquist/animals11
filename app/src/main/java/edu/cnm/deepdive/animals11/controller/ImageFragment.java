package edu.cnm.deepdive.animals11.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.animals11.R;
import edu.cnm.deepdive.animals11.model.Animal;
import edu.cnm.deepdive.animals11.model.ApiKey;
import edu.cnm.deepdive.animals11.service.AnimalService;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFragment extends Fragment {

  private WebView contentView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image, container, false);
    setupWebView(root);
    return root;
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
    new RetrieverTask().execute();
  }

  private class RetrieverTask extends AsyncTask<Void, Void, List<Animal>> {

    private AnimalService animalService;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Gson gson = new GsonBuilder()
          .create();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("https://us-central1-apis-4674e.cloudfunctions.net/")
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();
      animalService = retrofit.create(AnimalService.class);
    }

    @Override
    protected List<Animal> doInBackground(Void... voids) {
      try {
        Response<ApiKey> keyResponse = animalService.getApiKey().execute();
        ApiKey key = keyResponse.body();
        assert key != null;
        final String clientKey = key.getKey();

        Response<List<Animal>> listResponse = animalService.getAnimals(clientKey).execute();
        List<Animal> animalList = listResponse.body();
        assert animalList != null;
        return animalList;
      } catch (
          IOException e) {
        Log.e("AnimalService", e.getMessage(), e);
        cancel(true);
      }
      return null;
    }

    @Override
    protected void onPostExecute(List<Animal> animalList) {
      final String imageUrl = animalList.get(22).getImageUrl();

      Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
        @Override
        public void run() {
          contentView.loadUrl(imageUrl);
        }
      });

    }
  }
}