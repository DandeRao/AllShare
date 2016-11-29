package com.allshare_back4app.Fragments;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.allshare_back4app.MainActivity_1;
import com.allshare_back4app.R;
import com.parse.ParseUser;

/**
 * Created by mkrao on 11/17/2016.
 */

public class ActionBarItemsHandler extends Fragment {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_sign_out:
               // Logout Line
                ParseUser.logOut();
                ((MainActivity_1) getActivity()).replaceFragment(new LoginFragment(),true);

                return true;

            case R.id.menu_profile:
                // Logout Line

                ((MainActivity_1) getActivity()).replaceFragment(new UserProfile(),true);

                return true;

            case R.id.add_request:
                ((MainActivity_1) getActivity()).replaceFragment(new AddRequest(), true);
                return (true);

            case R.id.refresh_request:
            //    Call Load Request Method
                Toast toast = Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_LONG);
                toast.show();
                return (true);
        }

        return super.onOptionsItemSelected(item);
    }

    // Action Bar Items for Menu items like Sign Out
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_list_items, menu);
    }
}
