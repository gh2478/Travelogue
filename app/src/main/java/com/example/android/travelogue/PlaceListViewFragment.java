package com.example.android.travelogue;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelogue.data.PlacesDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PlaceListViewFragment extends Fragment {

    private static final String TAG = "PlaceListViewFragment";

    private final List<Place> placeList = new ArrayList<>();
    private final List<String> keyList = new ArrayList<>();

    private OnListFragmentInteractionListener mListener;
    private PlaceListAdapter placeListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaceListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_list, container, false);

        Log.d(TAG, "onCreateView is running");

        retrievePlaces();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        placeListAdapter = new PlaceListAdapter();
        recyclerView.setAdapter(placeListAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Place place);
    }

    private class PlaceListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView placeNameTextView;
        private int position;
        private Place place;

        public PlaceListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_place, parent, false));
            placeNameTextView = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            this.position = position;

            place = placeList.get(position);
            placeNameTextView.setText(place.placeName);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "This has been clicked!");

            mListener.onListFragmentInteraction(place);
        }
    }

    // RecyclerView Adapter
    private class PlaceListAdapter extends RecyclerView.Adapter<PlaceListHolder> {
        public PlaceListAdapter() {
        }

        @Override
        public PlaceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new PlaceListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PlaceListHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "Number of place = " + placeList.size());
            return placeList.size();
        }
    }

    // Retrieve the list of places from the database
    private void retrievePlaces() {
        // Query the database to get a list of places
        Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.BASE_PATH);
        String[] projection = { "placeName", "placeLocation", "placeNotes" };
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(contentUri, projection, null, null, null);
            while (cursor.moveToNext()) {
                String placeName = cursor.getString(cursor.getColumnIndexOrThrow("placeName"));
                String placeLocation = cursor.getString(cursor.getColumnIndexOrThrow("placeLocation"));
                String placeNotes = cursor.getString(cursor.getColumnIndexOrThrow("placeNotes"));
                Place place = new Place(placeName, placeLocation, placeNotes);
                Log.d(TAG, "Retrieved place name: " + place.placeName);
                placeList.add(place);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error found: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
