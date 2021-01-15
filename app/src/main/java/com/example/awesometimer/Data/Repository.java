package com.example.awesometimer.Data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.awesometimer.Models.Item;
import com.example.awesometimer.Models.Sequence;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private SequenceDao mSequenceDao;
    private ItemDao mItemDao;

    private LiveData<List<Sequence>> mAllSequences;


    public Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mSequenceDao = db.SequenceDao();
        mItemDao = db.ItemDao();

        mAllSequences = mSequenceDao.getAllSequences();
    }

    public LiveData<List<Sequence>> getAllSequences() { return mAllSequences; }

    public LiveData<List<Item>> getItems(int id) {
        return mItemDao.getItems(id);
    }

    public void insert(Sequence seq) { new insertSequenceAsyncTask(mSequenceDao).execute(seq);}
    public void update(Sequence seq) { new updateSequenceAsyncTask(mSequenceDao).execute(seq);}
    public void delete(Sequence seq) { new deleteSeqAsyncTask(mSequenceDao).execute(seq);}
    public void insert(ArrayList<Item> items) {new insertItemsAsyncTask(mItemDao).execute(items);}
    public void insert(Item item) { new insertItemAsyncTask(mItemDao).execute(item);}

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

    private static class insertItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao mAsyncTaskDao;

        insertItemAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertItemsAsyncTask extends AsyncTask<ArrayList<Item>, Void, Void> {

        private ItemDao mAsyncTaskDao;

        insertItemsAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ArrayList<Item>... params) {
            for (Item i : params[0]) {
                mAsyncTaskDao.insert(i);
            }
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
            return null;
        }
    }
}