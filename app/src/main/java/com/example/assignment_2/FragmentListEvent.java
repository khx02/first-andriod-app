package com.example.assignment_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment_2.provider.EventViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EventRecyclerAdapter recyclerAdapter;
    private EventViewModel eventViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListEvent newInstance(String param1, String param2) {
        FragmentListEvent fragment = new FragmentListEvent();
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
        View v = inflater.inflate(R.layout.fragment_list_event, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview_event);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        SharedPreferences sp = requireContext().getSharedPreferences("EVENT_ACTIVITY", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//        String eventCatArrayGson = sp.getString("EVENT_KEY", "No event available");
//        ArrayList<Event> eventList = new ArrayList<>();
//        System.out.println("fragment:" + eventCatArrayGson);
//        try {
//            ArrayList<EventCategory> eventCategoryList = gson.fromJson(eventCatArrayGson, type);
//            for (EventCategory eventCat : eventCategoryList){
////                eventList.addAll(Converters.fromString(eventCat.getEventArrayList()));
//            }
//        }
//        catch (JsonSyntaxException e){
//            System.out.println("failed to convert to Json in fragment");
//        }
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        recyclerAdapter = new EventRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        eventViewModel.getAllEventsLiveData().observe(getViewLifecycleOwner(), newData -> {
            // cast List<Item> to ArrayList<Item>
            recyclerAdapter.setData(new ArrayList<>(newData));
            recyclerAdapter.notifyDataSetChanged();
        });
        return v;
    }
}