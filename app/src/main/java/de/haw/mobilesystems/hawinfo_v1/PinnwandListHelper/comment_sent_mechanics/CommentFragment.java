package de.haw.mobilesystems.hawinfo_v1.PinnwandListHelper.comment_sent_mechanics;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import de.haw.mobilesystems.hawinfo_v1.PinnwandFragment;
import de.haw.mobilesystems.hawinfo_v1.R;
import de.haw.mobilesystems.hawinfo_v1.model.Room;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    String server_url ="http://goserver.haw-landshut.de:888/postpin.php";

    EditText comment;
    Button send;
    AlertDialog.Builder builder;
    TextView textView;

    String name;
    Room room;
    String roomID;

    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences prefs = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        name = prefs.getString("Login-Name","Could not retrieve Login-Name");

        View v = inflater.inflate(R.layout.fragment_comment, container, false);
        comment = (EditText) v.findViewById(R.id.edt_comment);
        textView = (TextView) v.findViewById(R.id.debug_textview);
        //builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            this.room = (Room) getArguments().getSerializable("room");
            roomID = this.room.getRoomID();
            //Toast.makeText(getActivity(),roomID, Toast.LENGTH_SHORT).show();

        } else {
            this.room = new Room();
            roomID = "IF 007";

        }



        send = (Button) v.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String kommentar;
                kommentar = comment.getText().toString();
                if(kommentar.isEmpty()){
                    Toast.makeText(getActivity(), "Geben sie was ein", Toast.LENGTH_SHORT).show();
                    return;
                }
                textView.setText(name);

                //hide Keyboard after clicking on the button
               View view2 = getActivity().getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                Fragment commentFragment = new PinnwandFragment();
                                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                final Bundle bundle = new Bundle();
                                bundle.putSerializable("room", room);
                                commentFragment.setArguments(bundle);


                                transaction.replace(R.id.main_container, commentFragment);
                                transaction.addToBackStack(null);

                                transaction.commit();
                                /*builder.setTitle("Server Response");
                                builder.setMessage("Response :" + response);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        comment.setText("");
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                //alertDialog.show();*/

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("roomid", roomID);
                        params.put("userid", name);
                        params.put("comment", kommentar);
                        return params;


                    }


                };

                MySingleton.getInstance(getActivity()).addToRequestqueue(stringRequest);

            }

        });

        return v;
    }



}