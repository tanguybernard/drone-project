package projet.istic.fr.firedrone;


import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.adapter.CustomListAdapter;
import projet.istic.fr.firedrone.adapter.ImageAdapter;
import projet.istic.fr.firedrone.map.TabMapFragment;
import projet.istic.fr.firedrone.model.Image;
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
    List<Image> listImage;
    ListView lv1 = null;
    ArrayAdapter<Intervention> listAdapter;
    //Map<String, Intervention> mapIntervention;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){

        MyObservable p = MyObservable.getInstance();
        p.ajouterObservateur(this);

        view = inflater.inflate(R.layout.image_gallery, container, false);


        GridView gridview = (GridView) view.findViewById(R.id.girdPicture);
        gridview.setAdapter(new ImageAdapter(this.getContext()));


        //Création de la liste et affichage dans la listview
        listImage = getListData(view);

        //lv1 = (ListView) view.findViewById(R.id.interventionList);

        //listAdapter = new CustomListAdapter(this.getContext(), listIntervention);

        //lv1.setAdapter(listAdapter);

        //Un clic sur une intervention de la liste permet de la sélectionner

        return view;
    }

    /**
     *
     * @return list data of in progress interventions
     */
    private List<Image> getListData(final View view) {

        final List<Image> results2 = new ArrayList<Image>();//bouchon
        generateMap(results2,view);//bouchon

        return results2;



        /**RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);

        final List<Image> results = new ArrayList<Image>();

        String id= InterventionSingleton.getInstance().getIntervention().getId();

        interventionAPI.getImages(id, new Callback<List<Image>>() {

            @Override
            public void success(List<Image> images, Response response) {
                results.addAll(images);
                //Si success, appel de méthode de génération de la map avec la liste des interventions
                generateMap(images,view);
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(getContext()).show();
            }
        });

        return results;*/
    }

    //Méthode de génération de la google map à la place du FrameLayout de la liste d'intervention
    public void generateMap(List<Image> imageList, View view){

        FrameLayout frame = (FrameLayout) view.findViewById(R.id.pictureMapLocation);

        //Test de la connexion de la tablette au réseau
        if (isOnline() == true) {
            //Replace de la frame par google map
            ImageMapFragment imageMapFragment = new ImageMapFragment();

            //On envoi la liste des interventions que l'on a récupérée de la base
            imageMapFragment.setListImage(imageList);

            View layoutId = view.findViewById(R.id.pictureMapLocation);

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

            frame.addView(notConnected);
        }
    }

    //Méthode de vérification de la connexion wifi
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void updateListImage(String coordinate){

    }


    @Override
    public void actualiser(Observable o) {

    }
}
