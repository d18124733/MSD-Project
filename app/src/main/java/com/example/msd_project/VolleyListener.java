package com.example.msd_project;


/*An activity must implement this method in order to update a view with new data from Volley request.
requestedFinished() is called within the onResponse() method of the corresponding JSONHandler method
and updates the view after request completion
 */

//Reference: https://stackoverflow.com/questions/23833977/android-wait-for-volley-response-to-return
public interface VolleyListener{

    public void requestFinished(boolean exsitance);

}//End Reference
