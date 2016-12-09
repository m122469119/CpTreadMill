package com.aaron.android.framework.library.thread;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Looper;

import com.aaron.android.codelibrary.utils.LogUtils;

/**
 * 任务调度器
 * @author xiaoming.liu
 * @version 1.0.0
 */
public class TaskScheduler {

    private static final String TAG = "TaskScheduler";

    /**
     * 以轻量级的方式执行一个异步任务
     * @param runnable 异步任务
     */
    public static void execute(Runnable runnable) {
        executeAtUI(null, runnable, null);
    }

    /**
     * 以轻量级的方式执行一个异步任务
     * @param backgroundRunnable  backgroundRunnable
     * @param postExecuteRunnable 回调任务
     */
    public static void execute(final Runnable backgroundRunnable, final Runnable postExecuteRunnable) {
        executeAtUI(null, backgroundRunnable, postExecuteRunnable);
    }

    /**
     * 以轻量级的方式执行一个异步任务
     * @param object 所在的页面
     * @param runnable 异步任务
     */
    public static void executeAtUI(final Object object, Runnable runnable) {
        executeAtUI(object, runnable, null);
    }

    /**
     * 以轻量级的方式执行一个异步任务
     * @param object 所在的页面
     * @param backgroundRunnable  backgroundRunnable
     * @param postExecuteRunnable 回调任务
     */
    public static void executeAtUI(final Object object, final Runnable backgroundRunnable, final Runnable postExecuteRunnable) {
        if (backgroundRunnable == null) {
            if (postExecuteRunnable != null) {
                postExecuteRunnable.run();
            }
            return;
        }
        // 如果不在主线程，下面的TASK不能执行，且不需要执行
        if (Looper.myLooper() != Looper.getMainLooper()) {
            backgroundRunnable.run();
            if (postExecuteRunnable != null) {
                postExecuteRunnable.run();
            }
            return;
        }
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                backgroundRunnable.run();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (postExecuteRunnable != null) {
                    postExecuteRunnable.run();
                }
            }

            @Override
            protected void onCancelled() {
                LogUtils.d(TAG, "AsyncTask onCancelled class=%s", backgroundRunnable.getClass().getName());
                if (postExecuteRunnable != null) {
                    postExecuteRunnable.run();
                }
            }
        }
        .execute();
    }

    /**
     * 执行一个Task
     * @param object 所在的页面
     * @param task Task
     */
    public static void executeAtUI(Object object, Task task) {
        task.executeAtUI(object);
    }

    /**
     * 执行一个Task
     * @param task Task
     */
    public static void execute(Task task) {
        task.execute();
    }

    /**
     * Task
     * @param <Input> 输入参数类型
     * @param <Output> 输出参数类型
     */
    public static abstract class Task<Input, Output> extends AsyncTask<Input, Object, Output> {

        private Input mInput;
        private Object mObject;

        /**
         * 以输入参数构造一个Task，
         * @param Input 输入参数
         */
        public Task(Input Input) {
            mInput = Input;
        }

        @Override
        protected final Output doInBackground(Input... params) {
            try {
                return onDoInBackground(params[0]);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected final void onPostExecute(Output result) {
            onPostExecuteForeground(result);
            releaseData();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            releaseData();
        }

        private void releaseData() {
            mObject = null;
            if (!(mInput instanceof Number || mInput instanceof Boolean || mInput instanceof Character || mInput instanceof Enum)) {
                mInput = null;
            }
        }

        /**
         * 执行
         */
        private void execute() {
            execute(mInput);
        }

        /**
         * 执行
         * @param object 所在的页面
         */
        private void executeAtUI(Object object) {
            mObject = object;
            execute(mInput);
        }

        /**
         * 后台任务执行回调
         * @param param 输入参数
         * @return 返回结果
         */
        protected abstract Output onDoInBackground(Input param);

        /**
         * 前台任务执行回调
         * @param result 输入参数
         */
        protected abstract void onPostExecuteForeground(Output result);
    }
}
