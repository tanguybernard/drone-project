package projet.istic.fr.firedrone;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.ImageAPI;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.adapter.CustomListAdapter;
import projet.istic.fr.firedrone.adapter.GridViewAdapter;
import projet.istic.fr.firedrone.adapter.ImageAdapter;
import projet.istic.fr.firedrone.map.TabMapFragment;
import projet.istic.fr.firedrone.model.ImageItem;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;
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
        listImage = getListData(view);


        GridView gridview = (GridView) view.findViewById(R.id.girdPicture);
        GridViewAdapter gridAdapter = new GridViewAdapter(this.getContext(), R.layout.image_grid_item_layout, listImage);
        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                                                //Create intent
                                                Intent myIntent = new Intent(getActivity(), ImageFullScreenActivity.class);
                                                getActivity().startActivity(myIntent);

                                                myIntent.putExtra("date", item.getDate());
                                                myIntent.putExtra("image", item.getImage());

                                                //Start fullscreen activity
                                                getActivity().startActivity(myIntent);
                                            }
        });


        return view;
    }


    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        /**for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
         return imageItems;*/
        Bitmap bitmap=null;
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.accident_rouge);
        ImageItem i = new ImageItem();
        i.setImage(largeIcon);
        i.setDate("Osef3");
        i.setLatitude("48.2292016");
        i.setLongitude("-1.5300694999999678");
        imageItems.add(i);
        return imageItems;
    }

    /**
     *
     * @return list data of in progress interventions
     */
    private List<ImageItem> getListData(final View view) {

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
                generateMap(images,view);
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

            System.out.println(imageList.get(0).getLongitude());


            if (layoutId.getVisibility() == View.VISIBLE) {
                // Its visible
                FragmentTransaction transactionMap = getFragmentManager().beginTransaction();
                transactionMap.replace(R.id.pictureMapLocation, imageMapFragment).commit();
            } else {
                // Either gone or invisible

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


    }


    @Override
    public void actualiser(Observable o) {

    }
}
