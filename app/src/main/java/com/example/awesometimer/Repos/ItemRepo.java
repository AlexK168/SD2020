package com.example.awesometimer.Repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.awesometimer.Data.AppDatabase;
import com.example.awesometimer.Data.ItemDao;
import com.example.awesometimer.Models.Item;

import java.util.List;

public class ItemRepo {
    private ItemDao mItemDao;
    private LiveData<List<Item>> mAllItems;
    private int id_sequence;

    public ItemRepo(Application application, int param) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mItemDao = db.ItemDao();
        id_sequence = param;
        mAllItems = mItemDao.getItems(param);
    }

    public LiveData<List<Item>> getItems() { return mAllItems; }

    public void insert(Item item) { new ItemRepo.insertItemAsyncTask(mItemDao).execute(item);}
    public void update(Item item) { new ItemRepo.updateItemAsyncTask(mItemDao).execute(item);}
    public void delete(Item item) { new ItemRepo.deleteItemAsyncTask(mItemDao).execute(item);}

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

    private static class updateItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao mAsyncTaskDao;

        updateItemAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao mAsyncTaskDao;

        deleteItemAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
