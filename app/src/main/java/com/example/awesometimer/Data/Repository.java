package com.example.awesometimer.Data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.awesometimer.Models.Phase;
import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.Models.SequenceWithItems;

import java.util.List;

public class Repository {

    private SequenceDao mSequenceDao;
    private PhaseDao mPhaseDao;
    private ItemDao mItemDao;

    private LiveData<List<Sequence>> mAllSequences;
    private LiveData<List<Phase>> mAllPhases;
    private LiveData<List<SequenceWithItems>> mSequencesWithItems;

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mSequenceDao = db.SequenceDao();
        mPhaseDao = db.PhaseDao();

        mAllSequences = mSequenceDao.getAllSequences();
        mSequencesWithItems = mSequenceDao.getSequencesWithItems();
        mAllPhases = mPhaseDao.getAllPhases();
    }

    public LiveData<List<Sequence>> getAllSequences() {
        return mAllSequences;
    }
    public LiveData<List<Phase>> getAllPhases() {
        return mAllPhases;
    }
    public LiveData<List<SequenceWithItems>> getSequencesWithItems() {
        return mSequencesWithItems;
    }

    public void insert(Sequence seq) { new insertSequenceAsyncTask(mSequenceDao).execute(seq);}
    public void update(Sequence seq) { new updateSequenceAsyncTask(mSequenceDao).execute(seq);}
    public void delete(Sequence seq) { new deleteSeqAsyncTask(mSequenceDao).execute(seq);}
    public void insert(Phase phase) { new insertPhaseAsyncTask(mPhaseDao).execute(phase);}
    public void update(Phase phase) { new updatePhaseAsyncTask(mPhaseDao).execute(phase);}
    public void delete(Phase phase) { new deletePhaseAsyncTask(mPhaseDao).execute(phase);}


    private static class insertSequenceAsyncTask extends AsyncTask<Sequence, Void, Void> {

        private SequenceDao mAsyncTaskDao;

        insertSequenceAsyncTask(SequenceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Sequence... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertPhaseAsyncTask extends AsyncTask<Phase, Void, Void> {

        private PhaseDao mAsyncTaskDao;

        insertPhaseAsyncTask(PhaseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Phase... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updatePhaseAsyncTask extends AsyncTask<Phase, Void, Void> {

        private PhaseDao mAsyncTaskDao;

        updatePhaseAsyncTask(PhaseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Phase... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class updateSequenceAsyncTask extends AsyncTask<Sequence, Void, Void> {

        private SequenceDao mAsyncTaskDao;

        updateSequenceAsyncTask(SequenceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Sequence... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deletePhaseAsyncTask extends AsyncTask<Phase, Void, Void> {
        private PhaseDao mAsyncTaskDao;

        deletePhaseAsyncTask(PhaseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Phase... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class deleteSeqAsyncTask extends AsyncTask<Sequence, Void, Void> {
        private SequenceDao mAsyncTaskDao;

        deleteSeqAsyncTask(SequenceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Sequence... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}