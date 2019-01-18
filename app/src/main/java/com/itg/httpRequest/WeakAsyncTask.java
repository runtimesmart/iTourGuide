/**
* @FileName WeakAsyncTask.java
* @Package com.itg.httpRequest
* @Description TODO
* @Author Alpha
* @Date 2015-11-10 下午7:37:22 
* @Version V1.0

*/
package com.itg.httpRequest;

import java.lang.ref.WeakReference;

import android.os.AsyncTask;

public abstract class WeakAsyncTask<Params, Progress, Result, WeakTarget> extends AsyncTask<Params, Progress, Result> {

	 protected WeakReference<WeakTarget> mTarget;  
	  
	    public WeakAsyncTask(WeakTarget target) {  
	        mTarget = new WeakReference<WeakTarget>(target);  
	    }  
	  
	    /** {@inheritDoc} */  
	    @Override  
	    protected void onPreExecute() {  
	        final WeakTarget target = mTarget.get();  
	        if (target != null) {  
	            this.onPreExecute(target);  
	        }  
	    }  
	  
	    /** {@inheritDoc} */  
	    @Override  
	    protected final Result doInBackground(Params... params) {  
	        final WeakTarget target = mTarget.get();  
	        if (target != null) {  
	            return this.doInBackground(target, params);  
	        } else {  
	            return null;  
	        }  
	    }  
	  
	    /** {@inheritDoc} */  
	    @Override  
	    protected final void onPostExecute(Result result) {  
	        final WeakTarget target = mTarget.get();  
	        if (target != null) {  
	            this.onPostExecute(target, result);  
	        }  
	    } 
	    protected void onPreExecute(WeakTarget target) {  
	        // No default action  
	    }  
	  
	    protected abstract Result doInBackground(WeakTarget target, Params... params);  
	  
	    protected void onPostExecute(WeakTarget target, Result result) {  
	        // No default action  
	    }  

}
