package com.example.android.travelogue;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelogue.data.PlacesDatabase;

import static android.app.Activity.RESULT_OK;


public class PlaceDetailViewFragment extends Fragment {

    private static final String TAG = "PlaceDetailViewFragment";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    /*
    // PULL REQUEST 2: get location or image from gallery
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private static final int GRAB_IMAGE_GALLERY = 3;
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationProviderClient client;
    */

    private OnFragmentInteractionListener mListener;

    // TODO Replace with Place class
    Place place;

    private ImageView mPhotoView;

    public PlaceDetailViewFragment() {
        place = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mPhotoView.setImageBitmap(imageBitmap);
            }
            /*
            else if (requestCode == GRAB_IMAGE_GALLERY) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mPhotoView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // String errorString = "Something went wrong trying to load picture " + data.getDataString();
                    // Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
                }
            }
            */
        }
    }

    public void setDetails(Place place) {
        this.place = place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_detail_view, container, false);

        Log.d(TAG, "Running onCreateView now");

        Log.d(TAG, "Details are " + place.placeName + ", " + place.placeLocation + ", " + place.placeNotes);

        // TODO Retrieve details; actually this is never called anymore
        /*
        if (place == null) {
            retrieveDetails();
        }
        */

        // TODO Add instance variables for camera intent
        ImageButton photoButton = (ImageButton) view.findViewById(R.id.place_camera);
        photoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Snap a picture of this place");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        mPhotoView = (ImageView) view.findViewById(R.id.place_photo);

        TextView nameWidget = (TextView) view.findViewById(R.id.place_name);
        nameWidget.setText(place.placeName);

        TextView locationWidget = (TextView) view.findViewById(R.id.place_location);
        locationWidget.setText(place.placeLocation);

        TextView notesWidget = (TextView) view.findViewById(R.id.place_notes);
        notesWidget.setText(place.placeNotes);

        /*
        // PULL REQUEST: this code grabs an image from Google gallery instead of using
        // the camera
        Button grabPictureButton = (Button) view.findViewById(R.id.place_grab_picture);
        grabPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Grab a picture from the gallery");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GRAB_IMAGE_GALLERY);
            }
        });
        */

        /*
        // PULL REQUEST: this code does the work to get current location
        Button getLocationButton = (Button) view.findViewById(R.id.place_get_location);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Get the location of this place");
                if (hasLocationPermission()) {
                    client = LocationServices.getFusedLocationProviderClient(getActivity());
                    client.getLastLocation()
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        Log.i(TAG, "Got location " + location);
                                    }
                                }
                            });
                }
                else {
                    requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSION);
                }
            }
        });
        */

        return view;
    }

    /* not needed anymore with Place passed through Intent
    private void retrieveDetails() {
        // Query the database to get a list of places
        Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.BASE_PATH);
        String[] projection = { "placeName", "placeLocation", "placeNotes" };
        Cursor cursor = null;
        String selection =  "placeName = ?";
        String[] selectionArgs = {place.placeName};
        try {
            cursor = getActivity().getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
            while (cursor.moveToNext()) {
                String place = cursor.getString(cursor.getColumnIndexOrThrow("placeName"));
                Log.d(TAG, "Retrieved place name: " + place);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error found: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    */

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
    // PULL REQUEST 2: get location or image from gallery
    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    */
}
