package edu.cnm.deepdive.animals11.service;

import android.content.Context;
import edu.cnm.deepdive.animals11.model.Animal;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class AnimalsRepository {

  private final Context context;
  private final AnimalService animalService;

  public AnimalsRepository(Context context) {
    this.context = context;
    animalService = AnimalService.getInstance();
  }

  public Single<List<Animal>> loadAnimals() {

    return animalService.getApiKey()
        .flatMap((key) -> animalService.getAnimals(key.getKey()))
        .subscribeOn(Schedulers.io());
  }
}
