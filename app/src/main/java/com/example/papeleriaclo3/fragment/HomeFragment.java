package com.example.papeleriaclo3.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.papeleriaclo3.Activities.MainActivity;
import com.example.papeleriaclo3.Activities.PostActivity;
import com.example.papeleriaclo3.R;
import com.example.papeleriaclo3.adapters.PostAdapter;
import com.example.papeleriaclo3.models.Post;
import com.example.papeleriaclo3.providers.AuthProviders;
import com.example.papeleriaclo3.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    View mView;
    FloatingActionButton mFab;
    Toolbar mtoolbar;
    AuthProviders mAuthProvider;

    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostAdapter mPostAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_home, container, false);
        mFab=mView.findViewById(R.id.fab);
        mtoolbar=mView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mtoolbar);
        mRecyclerView=mView.findViewById(R.id.recycleviewhome);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Publicaciones");
        setHasOptionsMenu(true);
        mAuthProvider=new AuthProviders();
        mPostProvider= new PostProvider();


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToPost();
            }
        });
        return mView;
    }



    private void goToPost() {
        Intent intent=new Intent(getContext(), PostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query= mPostProvider.getAll();
        FirestoreRecyclerOptions<Post>options=new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query,Post.class).build();

        mPostAdapter=new PostAdapter(options,getContext());
        mRecyclerView.setAdapter(mPostAdapter);
        mPostAdapter.startListening();
    }





    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.itemLogout){
            logout();
        }
        //return super.onOptionsItemSelected(item);
        return true;
    }

    private void logout() {
        mAuthProvider.logout();
        Intent intent=new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}