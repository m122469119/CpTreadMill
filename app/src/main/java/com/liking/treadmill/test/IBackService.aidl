// IBackService.aidl
package com.liking.treadmill.test;

// Declare any non-default types here with import statements

interface IBackService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void rebind();
   void init();
   void bind();
   void unBind();
   void confirm();
   void reportDevices();
   void userLogin(String cardno);
   void reportExerciseCacheData(String data);
   void reportExerciseData(int type, int aimType, int achieve);
}
