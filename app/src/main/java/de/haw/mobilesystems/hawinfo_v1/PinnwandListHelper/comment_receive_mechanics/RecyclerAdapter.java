package de.haw.mobilesystems.hawinfo_v1.PinnwandListHelper.comment_receive_mechanics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.haw.mobilesystems.hawinfo_v1.R;
import de.haw.mobilesystems.hawinfo_v1.SearchListHelper.SearchListBuilding;
import de.haw.mobilesystems.hawinfo_v1.SearchListHelper.SearchListRoom;
import de.haw.mobilesystems.hawinfo_v1.model.PinnwandEintrag;
import de.haw.mobilesystems.hawinfo_v1.model.PinnwandEintragDetail;
import de.haw.mobilesystems.hawinfo_v1.model.Room;
import de.haw.mobilesystems.hawinfo_v1.tasks.AsyncTaskResult;
import de.haw.mobilesystems.hawinfo_v1.tasks.ITaskObserver;

/**
 * Created by jackl on 15.03.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements ITaskObserver {

    ArrayList<PinnwandEintrag> arrayList = new ArrayList<>();
    Context ctx;

    public RecyclerAdapter(ArrayList<PinnwandEintrag> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new MyViewHolder(view,ctx,arrayList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.c_eintrag.setText(arrayList.get(position).getText());
        holder.c_eintrag_info.setText(arrayList.get(position).getName()+" | " + arrayList.get(position).getTimestamp());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onGETRoomDataComplete(AsyncTaskResult<Room> result) {
        if(result == null || result.getResult() == null || result.getError()!=null){
            //if the Error Msg is null, show general Error
            if(result.getError()!=null){
//                showSnackbar(result.getError().getMessage());
            }
            else {
  //              showSnackbar(currentSearchViewHolder_ForRoomClick.instSearchScreen.getResources().getString(R.string.error_could_not_download_roomdata_from_room));
            }
        }
    }

    /*private void callServerAndDisplayRoom() {
        *//*if(isNetworkAvailable()){*//*
            GETRoomData get = new GETRoomData(this);
            String uri = Uri.parse(currentSearchViewHolder_ForRoomClick.instSearchScreen.getString(R.string.base_url)).buildUpon().
                    path(currentSearchViewHolder_ForRoomClick.instSearchScreen.getString(R.string.path_room_data_from_room)).
                    appendPath(String.valueOf(currentSearchViewHolder_ForRoomClick.tvSearchRoomID.getText())).appendPath(String.valueOf(System.currentTimeMillis())).toString();
            get.execute(uri);
        *//*}*//*
        *//*else {
            showSnackbar(context.getString(R.string.no_internet));
        }*//*
    }*/





    @Override
    public void onGETAllBuildingsComplete(AsyncTaskResult<ArrayList<SearchListBuilding>> result) {

    }

    @Override
    public void onGETRoomsFromBuildingComplete(AsyncTaskResult<ArrayList<SearchListRoom>> result) {

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView c_flag;
        TextView c_eintrag;
        TextView c_eintrag_info;

        ArrayList<PinnwandEintrag> notizen = new ArrayList<>();
        Context ctx;


        public MyViewHolder(View itemView, Context ctx, ArrayList<PinnwandEintrag> notizen) {
            super(itemView);
            this.notizen = notizen;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            c_flag = (ImageView) itemView.findViewById(R.id.flag);
            c_eintrag = (TextView) itemView.findViewById(R.id.eintrag);
            c_eintrag_info = (TextView) itemView.findViewById(R.id.eintrag_info);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            PinnwandEintrag pinnwandEintrag = this.notizen.get(position);
            Intent intent = new Intent(this.ctx, PinnwandEintragDetail.class);
            intent.putExtra("text", pinnwandEintrag.getText());
            intent.putExtra("name", pinnwandEintrag.getName());
            intent.putExtra("timestamp", pinnwandEintrag.getTimestamp());
            this.ctx.startActivity(intent);
        }
    }
}


