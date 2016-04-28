package projet.istic.fr.firedrone.service;


import android.content.Context;
import android.telecom.Call;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.MoyenFragment;
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

    public static List<MeansItem> addMean(MeansItem meansItem,final Context context,final boolean isTableMoyen) {
        final Intervention oIntervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = oIntervention.getId();
        RestAdapter restAdapter = new RestAdapter.Builder().
                setEndpoint(FiredroneConstante.END_POINT).
                setLogLevel(RestAdapter.LogLevel.FULL).
                build();
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);
        meansApi.AddMean(sIntervId, meansItem, new Callback<List<MeansItem>>() {
            @Override
            public void success(List<MeansItem> ploMeans, Response response) {

                oIntervention.setWays(ploMeans);
                if(isTableMoyen){
                    MoyenFragment.getInstance().getMeans();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(context).show();
            }
        });
        return oIntervention.getWays();
    }

    public static List<MeansItem> editMean(MeansItem meansItem,final Context context) {
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
                FiredroneConstante.getToastError(context).show();
            }
        });
        return oIntervention.getWays();
    }

    public static void createListDefaultWay(final Context context){
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
                FiredroneConstante.getToastError(context).show();
            }
        });
    }

    public static List<MeansItem> getListDefaultMeansItem(){
        List<MeansItem> listMeansItem = new ArrayList<>();

        for( DefaultWay defautWay : listDefaultWays){
            MeansItem meansItem = new MeansItem();
            meansItem.setMsMeanCode(defautWay.getAcronym());
            meansItem.setMsColor(defautWay.getColor());
            listMeansItem.add(meansItem);
        }
        return listMeansItem;
    }
}
