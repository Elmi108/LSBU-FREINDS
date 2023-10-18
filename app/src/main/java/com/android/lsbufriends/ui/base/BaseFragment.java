package com.android.lsbufriends.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.android.lsbufriends.ui.dialog.WaitDialog;

public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    private WaitDialog waitDialog;


    protected abstract ViewBinding getBinding(
            LayoutInflater inflater,
            ViewGroup container);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewBinding binding = getBinding(inflater,container);
        return binding.getRoot();
    }

    protected void showWaiting() {
        try{
            if(this.waitDialog != null && this.waitDialog.isShowing())
                return;
            waitDialog = new WaitDialog(mContext);
            waitDialog.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void hideWaiting() {
        if(this.waitDialog != null && this.waitDialog.isShowing())
            this.waitDialog.dismiss();
    }
}
