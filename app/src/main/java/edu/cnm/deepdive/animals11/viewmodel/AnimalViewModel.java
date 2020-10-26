package edu.cnm.deepdive.animals11.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.animals11.model.Animal;
import io.reactivex.disposables.CompositeDisposable;
import edu.cnm.deepdive.animals11.service.AnimalsRepository;
import java.util.List;

public class AnimalViewModel extends AndroidViewModel {

  private final MutableLiveData<List<Animal>> animals;
  private final MutableLiveData<Integer> selectedItem;
  private final MutableLiveData<Throwable> throwable;
  private final AnimalsRepository animalsRepository;
  private final CompositeDisposable pending;

  public AnimalViewModel(
      @NonNull Application application) {
    super(application);
    animals = new MutableLiveData<>();
    selectedItem = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
//    animalService = AnimalService.getInstance();
    animalsRepository = new AnimalsRepository(application);
    pending = new CompositeDisposable();
    loadAnimals();
  }

  public LiveData<List<Animal>> getAnimals() {
    return animals;
  }

  public LiveData<Integer> getSelectedItem() {
    return selectedItem;
  }

  public void select(int index) {
    selectedItem.setValue(index);
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  @SuppressLint("CheckResult")
  private void loadAnimals() {
    pending.add(
        animalsRepository.loadAnimals()
            .subscribe(
                animals::postValue,
                throwable::postValue
            )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }
}