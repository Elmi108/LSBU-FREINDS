package com.android.lsbufriends.ui.post.filter;

import android.widget.Filter;

import com.android.lsbufriends.data.adapter.PostAdapter;
import com.android.lsbufriends.data.model.post.PostRequest;

import java.util.ArrayList;
import java.util.List;

public class FilterPosts extends Filter {

    private PostAdapter adapter;
    private List<PostRequest> filterList;

    public FilterPosts(PostAdapter adapter, List<PostRequest> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //validate data for search query
        if(constraint !=null && constraint.length() > 0)
        {
            //search field not empty, searching something, perform search

            //change to upper case, to make case insensitive
            constraint = constraint.toString().toUpperCase();
            //store our filtered list
            List<PostRequest> filteredModels = new ArrayList<>();
            for(int i=0;i<filterList.size();i++)
            {
                //check search by category
                if(filterList.get(i).getFaculty().toUpperCase().contains(constraint))
                {
                    //add filtered data to list
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;

        }
        else{
            //search filed empty, not searching return original/all/complete list
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.postRequestList = (ArrayList<PostRequest>) results.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
