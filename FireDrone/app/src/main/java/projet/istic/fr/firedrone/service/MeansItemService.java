package projet.istic.fr.firedrone.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.model.DefaultWay;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ramage on 26/04/16.
 */
public class MeansItemService {

    private static List<DefaultWay> listDefaultWays ;

    public static List<MeansItem> addMean(MeansItem meansItem) {
        final Intervention oIntervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = oIntervention.getId();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FiredroneConstante.END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);
        meansApi.AddMean(sIntervId,meansItem , new Callback<List<MeansItem>>() {
            @Override
            public void success(List<MeansItem> ploMeans, Response response) {
                oIntervention.setWays(ploMeans);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("CONNEXION ERROR", error.getMessage());
            }
        });
        return oIntervention.getWays();
    }

    public static List<MeansItem> editMean(MeansItem meansItem) {
        final Intervention oIntervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = oIntervention.getId();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FiredroneConstante.END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);
        meansApi.EditMean(sIntervId, meansItem, new Callback<List<MeansItem>>() {
            @Override
            public void success(List<MeansItem> ploMeans, Response response) {
                oIntervention.setWays(ploMeans);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("CONNEXION ERROR", error.getMessage());
            }
        });
        return oIntervention.getWays();
    }

    public static void createListDefaultWay(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();

        final MeansAPI meansAPI = restAdapter.create(MeansAPI.class);

        meansAPI.getWay(new Callback<List<DefaultWay>>() {
            @Override
            public void success(List<DefaultWay> meansItems, Response response) {
                listDefaultWays = meansItems;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static List<MeansItem> getListDefaultMeansItem(){
        List<MeansItem> listMeansItem = new ArrayList<>();

        for( DefaultWay defautWay : listDefaultWays){
            MeansItem meansItem = new MeansItem();
            meansItem.setMsMeanCode(defautWay.getAcronym());
            meansItem.setMsColor(defautWay.getColor());
            meansItem.setMsMeanName(defautWay.getAcronym());
            listMeansItem.add(meansItem);
        }
        return listMeansItem;
    }
}
