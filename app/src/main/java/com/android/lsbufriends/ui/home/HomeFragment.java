package com.android.lsbufriends.ui.home;

import static com.android.lsbufriends.utils.constants.NameConstants.POSTS_REF;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.adapter.PostAdapter;
import com.android.lsbufriends.data.model.post.PostRequest;
import com.android.lsbufriends.databinding.FragmentHomeBinding;
import com.android.lsbufriends.ui.base.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private FragmentHomeBinding binding;
    private List<PostRequest> postList;
    PostAdapter adapter;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        setListener();
    }

    private void setListener() {
        binding.toolbar.inflateMenu(R.menu.home_menu);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.item_filter){
                    String[] options = getResources().getStringArray(R.array.faculty_filter);
                    Log.d("PAKSOFT", "onOptionsItemSelected: called");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Filter Post By Faculty:")
                            .setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //handle item clicks
                                    if(which==0)
                                    {
                                        //All clicked
                                        String str ="Showing All Posts";
                                        adapter.getFilter().filter("");// show all order
                                    }
                                    else
                                    {
                                        String optionClicked = options[which];
                                        adapter.getFilter().filter(optionClicked);
                                    }
                                }
                            })
                            .show();

                }
                return false;
            }
        });
    }

    private void getPost() {
        postList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    PostRequest post = dataSnapshot.getValue(PostRequest.class);
                    postList.add(post);
                }

                adapter = new PostAdapter(getContext(),postList);
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpView() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child(POSTS_REF);

        getPost();
    }

   /* @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}