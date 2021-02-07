package com.example.awesometimer.Repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import com.example.awesometimer.Data.AppDatabase;
import com.example.awesometimer.Data.ItemDao;
import com.example.awesometimer.Data.SequenceDao;
import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;
import java.util.List;

public class SequenceRepo {

    private SequenceDao mSequenceDao;
    private ItemDao mItemDao;
    private LiveData<List<Sequence>> mAllSequences;

    public SequenceRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mSequenceDao = db.SequenceDao();
        mItemDao = db.ItemDao();
        mAllSequences = mSequenceDao.getAllSequences();
    }

    public LiveData<List<Sequence>> getAllSequences() { return mAllSequences; }

    public LiveData<List<Item>> getItems(int id) {
        return mItemDao.getItems(id);
    }

    public void insert(Sequence seq) { new SequenceRepo.insertSequenceAsyncTask(mSequenceDao).execute(seq);}
    public void update(Sequence seq) { new SequenceRepo.updateSequenceAsyncTask(mSequenceDao).execute(seq);}
    public void delete(Sequence seq) { new SequenceRepo.deleteSeqAsyncTask(mSequenceDao).execute(seq);}
    public LiveData<Sequence> get(int id) {return mSequenceDao.getSequence(id);}

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

    private static class deleteSeqAsyncTask extends AsyncTask<Sequence, Void, Void> {
        private SequenceDao mAsyncTaskDao;

        deleteSeqAsyncTask(SequenceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Sequence... params) {
            mAsyncTaskDao.delete(params[0]);
            mAsyncTaskDao.delete(params[0].id);
            return null;
        }
    }
}
