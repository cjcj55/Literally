package com.cjcj55.literallynot;

import android.content.Context;

import org.vosk.Model;
import org.vosk.android.StorageService;

public class ModelManager {

    private static ModelManager instance;
    private Model model;

    private ModelManager() {
        // private constructor to prevent direct instantiation
    }

    public static synchronized ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    public void initModel(Context context, Callback callback) {
        StorageService.unpack(context, "model-en-us", "model",
                (model) -> {
                    this.model = model;
                    callback.onSuccess();
                },
                (exception) -> {
                    callback.onFailure(exception);
                });
    }

    public Model getModel() {
        return model;
    }

    public interface Callback {
        void onSuccess();
        void onFailure(Exception exception);
    }
}
