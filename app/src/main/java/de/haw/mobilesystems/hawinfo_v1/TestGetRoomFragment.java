package de.haw.mobilesystems.hawinfo_v1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.haw.mobilesystems.hawinfo_v1.SearchListHelper.SearchListBuilding;
import de.haw.mobilesystems.hawinfo_v1.SearchListHelper.SearchListRoom;
import de.haw.mobilesystems.hawinfo_v1.model.Room;
import de.haw.mobilesystems.hawinfo_v1.tasks.AsyncTaskResult;
import de.haw.mobilesystems.hawinfo_v1.tasks.GETRoomData;
import de.haw.mobilesystems.hawinfo_v1.tasks.ITaskObserver;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestGetRoomFragment extends Fragment implements ITaskObserver  {

    private static TestGetRoomFragment instTestGetRoomFragment;
Button button;

    public static TestGetRoomFragment instance() {
        return instTestGetRoomFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        instTestGetRoomFragment = this;
    }

    @Override
    public void onGETRoomDataComplete(AsyncTaskResult<Room> result) {
        if(result == null || result.getResult() == null || result.getError()!=null){
            //if the Error Msg is null, show general Error
            if(result.getError()!=null){
                Toast.makeText(getActivity(),"fail", Toast.LENGTH_SHORT).show();
            }
            else {

            }
        }
        else {



            Toast.makeText(getActivity(), result.getResult().getRoomID(), Toast.LENGTH_SHORT).show();
            Fragment roomInfo = new RoomInfoScreen();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            final Bundle bundle = new Bundle();
            bundle.putSerializable("room",result.getResult());
            roomInfo.setArguments(bundle);

           // transaction.replace(R.id.search_screen_container, roomInfo);
            transaction.replace(R.id.main_container, roomInfo);
            transaction.addToBackStack(null);


            transaction.commit();

        }
    }

    @Override
    public void onGETAllBuildingsComplete(AsyncTaskResult<ArrayList<SearchListBuilding>> result) {

    }

    @Override
    public void onGETRoomsFromBuildingComplete(AsyncTaskResult<ArrayList<SearchListRoom>> result) {

    }

    public TestGetRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        button = (Button) v.findViewById(R.id.button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GETRoomData get = new GETRoomData(TestGetRoomFragment.instance());
                        String uri = "http://goserver.haw-landshut.de:6061/hawinfo/public/getRoomData/HS%20013/" + System.currentTimeMillis();
                        Log.d("IMPORTANT", uri);
                        get.execute(uri);




                }

        });

        return v;
    }


}
