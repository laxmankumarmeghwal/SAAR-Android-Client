package com.example.saar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter mAdapter;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_timeline);
        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Event>> call = service.getAllEvents();
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                progressDialog.dismiss();
                generateDataList(response.body(),rootView);
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(rootView.getContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void generateDataList(List<Event> eventList, View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_view_timeline);
        mAdapter = new EventAdapter(eventList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(R.string.timeline);
    }
}
