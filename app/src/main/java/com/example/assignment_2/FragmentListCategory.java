package com.example.assignment_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_2.provider.EventViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EventCategoryRecyclerAdapter recyclerAdapter;
    private EventViewModel eventViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentListCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
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
        View v = inflater.inflate(R.layout.fragment_list_category, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
//        SharedPreferences sp = requireContext().getSharedPreferences("EVENT_ACTIVITY", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<EventCategory>>() {
//        }.getType();
//        String eventCatArrayGson = sp.getString("EVENT_KEY", "No event available");
//        try {
//            eventCategoryList = gson.fromJson(eventCatArrayGson, type);
//        } catch (JsonSyntaxException e) {
//            System.out.println("failed to convert to Json in fragment");
//        }
//        System.out.println(eventCategoryList);


//        ArrayList<EventCategory> eventCategoryArrayList = new ArrayList<>();
//        for (EventCategory eventCategory : eventCategoryList) {
//            if (eventCategory.getCategoryName() != null && eventCategory.getCategoryId() != null) {
//                eventCategoryArrayList.add(eventCategory);
//            }
//        }
        recyclerAdapter = new EventCategoryRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        eventViewModel.getAllEventCategoriesLiveData().observe(getViewLifecycleOwner(), newData -> {
            // cast List<Item> to ArrayList<Item>
            recyclerAdapter.setData(new ArrayList<>(newData));
            recyclerAdapter.notifyDataSetChanged();
        });
        return v;
        }

}