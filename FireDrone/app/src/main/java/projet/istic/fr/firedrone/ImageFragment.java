package projet.istic.fr.firedrone;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.ImageAPI;
import projet.istic.fr.firedrone.adapter.GridViewAdapter;
import projet.istic.fr.firedrone.model.ImageItem;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.synchro.MyObservable;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ImageFragment extends Fragment implements Observateur {

    private static ImageFragment INSTANCE;

    View view = null;

    public static ImageFragment getInstance() {
        if(INSTANCE == null){
            INSTANCE = new ImageFragment();
        }
        return INSTANCE;
    }
    List<ImageItem> listImage;
    ListView lv1 = null;
    ArrayAdapter<Intervention> listAdapter;
    //Map<String, Intervention> mapIntervention;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){

        MyObservable p = MyObservable.getInstance();
        p.ajouterObservateur(this);

        view = inflater.inflate(R.layout.image_gallery, container, false);

        //Création de la liste et affichage dans la gridView
        getListData(true);


        final Button btnAll = (Button) view.findViewById(R.id.btnAllImages);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListData(false);
            }
        });

        return view;
    }

    /**
     *
     * @return list data of in progress interventions
     */
    private List<ImageItem> getListData(final boolean generateMapBool) {

        final List<ImageItem> imageItems = new ArrayList<ImageItem>();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ImageAPI imageAPI = restAdapter.create(ImageAPI.class);

        String idIntervention= InterventionSingleton.getInstance().getIntervention().getId();

        imageAPI.getImagesByIntervention(idIntervention, new Callback<List<ImageItem>>() {

            @Override
            public void success(List<ImageItem> images, Response response) {
                imageItems.addAll(images);

                GridView gridview = (GridView) view.findViewById(R.id.girdPicture);
                GridViewAdapter gridAdapter = new GridViewAdapter(getContext(), R.layout.image_grid_item_layout, images);
                gridview.setAdapter(gridAdapter);

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                        //Create intent
                        Intent myIntent = new Intent(getActivity(), ImageFullScreenActivity.class);

                        myIntent.putExtra("date", item.getDate());
                        System.out.println(item.getImageUrl());
                        myIntent.putExtra("imageUrl", item.getImageUrl());

                        //Start fullscreen activity
                        startActivity(myIntent);
                    }
                });

                if(generateMapBool){
                    generateMap(images,view);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(getContext()).show();
            }
        });

         return imageItems;
    }

    //Méthode de génération de la google map à la place du FrameLayout de la liste d'intervention
    public void generateMap(List<ImageItem> imageList, View view){

        FrameLayout layoutId = (FrameLayout) view.findViewById(R.id.pictureMapLocation);

        //Test de la connexion de la tablette au réseau
        if (isOnline()) {
            //Replace de la frame par google map
            ImageMapFragment imageMapFragment = new ImageMapFragment();
            //On envoi la liste des interventions que l'on a récupérée de la base
            imageMapFragment.setListImage(imageList);

            if (layoutId.getVisibility() == View.VISIBLE) {
                // Its visible
                FragmentTransaction transactionMap = getFragmentManager().beginTransaction();
                transactionMap.replace(R.id.pictureMapLocation, imageMapFragment).commit();
            }
        }
        //Si pas de connexion wifi
        else {
            TextView notConnected = new TextView(getActivity());
            notConnected.setText("Vous n'avez pas de connexion internet, la map ne peut pas s'afficher");
            //layoutId.add(notConnected);
        }
    }

    //Méthode de vérification de la connexion wifi
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * refresh list image with only image at markerPosition
     * @param markerPosition
     */
    public void updateListImage(LatLng markerPosition){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ImageAPI imageAPI = restAdapter.create(ImageAPI.class);

        String idIntervention= InterventionSingleton.getInstance().getIntervention().getId();

        String latitude = String.valueOf(markerPosition.latitude);
        String longitude = String.valueOf(markerPosition.longitude);

        imageAPI.getImagesByLatLong(
                idIntervention,
                latitude,
                longitude,
                new Callback<List<ImageItem>>() {

            @Override
            public void success(List<ImageItem> images, Response response) {

                GridView gridview = (GridView) view.findViewById(R.id.girdPicture);
                GridViewAdapter gridAdapter = new GridViewAdapter(getContext(), R.layout.image_grid_item_layout, images);
                gridview.setAdapter(gridAdapter);

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                        //Create intent
                        Intent myIntent = new Intent(getActivity(), ImageFullScreenActivity.class);

                        myIntent.putExtra("date", item.getDate());
                        System.out.println(item.getImageUrl());
                        myIntent.putExtra("imageUrl", item.getImageUrl());

                        //Start fullscreen activity
                        startActivity(myIntent);
                    }
                });
                //generateMap(images,view);
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(getContext()).show();
            }
        });
    }


    @Override
    public void actualiser(Observable o) {

    }

}
