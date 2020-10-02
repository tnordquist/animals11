package edu.cnm.deepdive.animals11.controller;

import android.os.Bundle;
import android.util.Log;
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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_image, container, false);
  }

  private class Retriever extends Thread {

    @Override
    public void run() {

      Gson gson = new GsonBuilder()
          .create();

      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("https://us-central1-apis-4674e.cloudfunctions.net/")
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();

      AnimalService animalService = retrofit.create(AnimalService.class);

      try {

        Response<ApiKey> keyResponse = animalService.getApiKey().execute();
        ApiKey key = keyResponse.body();
        assert key != null;
        final String clientKey = key.getKey();

        Response<List<Animal>> listResponse = animalService.getAnimals(clientKey).execute();
        List<Animal> animalList = listResponse.body();
        assert animalList != null;
        final String imageUrl = animalList.get(0).getImageUrl();

      } catch (IOException e) {
        Log.e("AnimalService", e.getMessage(), e);
      }

    }

  }
}