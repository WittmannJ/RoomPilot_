package de.haw.mobilesystems.hawinfo_v1;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.haw.mobilesystems.hawinfo_v1.Pinnwand.comment_sent_mechanics.CommentFragment;
import de.haw.mobilesystems.hawinfo_v1.model.Room;


/**
 * A simple {@link Fragment} subclass.
 */
public class PinnwandFragment extends Fragment {



    String[] notizen = {"Kommentar0","Kommentar1","Kommentar2","Kommentar3","Kommentar4","Kommentar5","Kommentar6","Kommentar7","Kommentar8","Kommentar9"};

    RecyclerView recyclerView;
    PinnwandFragment.RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PinnwandFragment.Notiz> arrayList;
    Button comment, update;
    FragmentTransaction fragmentTransaction;
    String roomID;
    Room room;


    public PinnwandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pinnwand, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();

        Bundle args = getArguments();

        if (args != null) {
            this.room = (Room) getArguments().getSerializable("room");
            roomID = this.room.getRoomID();
            //Toast.makeText(getActivity(),roomID, Toast.LENGTH_SHORT).show();

        } else {
            this.room = new Room();
            roomID = "IF 007";

        }

        comment = (Button) v.findViewById(R.id.button_comment);
        update = (Button) v.findViewById(R.id.button_update);
        getData();
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getActivity(), comment.getText().toString(), Toast.LENGTH_SHORT).show();
                Fragment commentFragment = new CommentFragment();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                final Bundle bundle = new Bundle();
                bundle.putSerializable("room", room);
                commentFragment.setArguments(bundle);


                transaction.replace(R.id.main_container, commentFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                getData();
                //Toast.makeText(getActivity(), update.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;

    }

    private void getData() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...", false, false);

        //Creating a json array request
        //JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
       /* PostJsonArrayRequest jsonArrayRequest = new PostJsonArrayRequest(Config.DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        //loading.dismiss();

                        //calling method to parse json array
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        //trying to insert data like a bau5


        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }*/

       String url = PinnwandFragment.Config.DATA_URL + "roomid="+roomID+"&entries=10";

        url = url.replaceAll(" ","%20");


        Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           /* JSONObject jsonResponse = new JSONObject(response).getJSONObject("form");
                            String site = jsonResponse.getString("site"),
                                    network = jsonResponse.getString("network");
                            System.out.println("Site: " + site + "\nNetwork: " + network);*/
                            //parseData(new JSONArray(response));
                           // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                            loading.dismiss();
                            parseData(new JSONArray(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("roomid", "IF 007");
                params.put("entries", "1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Volley.newRequestQueue(getActivity()).add(postRequest);
        requestQueue.add(postRequest);
        //Toast.makeText(getActivity(),"testLOL",Toast.LENGTH_SHORT).show();
    }

    private void parseData(JSONArray array){
        for(int i = 0; i < array.length(); i++){
            PinnwandFragment.Notiz notiz = new PinnwandFragment.Notiz();
            JSONObject json = null;
            try{
                json = array.getJSONObject(i);
                notiz.setTimestamp(json.getString(PinnwandFragment.Config.MODTIME));
                notiz.setName(json.getString(PinnwandFragment.Config.USER_ID));
                notiz.setText(json.getString(PinnwandFragment.Config.COMMENT));


            }catch (JSONException e){
                e.printStackTrace();
            }
            arrayList.add(notiz);
        }

        //Finally initializing our adapter
        adapter = new PinnwandFragment.RecyclerAdapter(arrayList,getActivity());
        recyclerView.setAdapter(adapter);
    }

/*

        for(String Notiz : notizen){
            arrayList.add(new Notiz(Notiz));

        }

        adapter = new RecyclerAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);

       return v;

    }*/

}