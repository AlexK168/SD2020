package com.example.awesometimer.Repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.awesometimer.Data.AppDatabase;
import com.example.awesometimer.Data.StageDao;
import com.example.awesometimer.Models.Stage;

import java.util.List;

public class StageRepo {
    private StageDao mStageDao;
    private LiveData<List<Stage>> mAllStages;

    public StageRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mStageDao = db.StageDao();
        mAllStages = mStageDao.getAllStages();
    }

    public LiveData<List<Stage>> getAllStages() { return mAllStages; }

    public void insert(Stage seq) { new StageRepo.insertStageAsyncTask(mStageDao).execute(seq);}
    public void update(Stage seq) { new StageRepo.updateStageAsyncTask(mStageDao).execute(seq);}
    public void delete(Stage seq) { new StageRepo.deleteStageAsyncTask(mStageDao).execute(seq);}

    private static class insertStageAsyncTask extends AsyncTask<Stage, Void, Void> {

        private StageDao mAsyncTaskDao;

        insertStageAsyncTask(StageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stage... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateStageAsyncTask extends android.os.AsyncTask<Stage, Void, Void> {

        private StageDao mAsyncTaskDao;

        updateStageAsyncTask(StageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stage... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteStageAsyncTask extends android.os.AsyncTask<Stage, Void, Void> {
        private StageDao mAsyncTaskDao;

        deleteStageAsyncTask(StageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stage... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
