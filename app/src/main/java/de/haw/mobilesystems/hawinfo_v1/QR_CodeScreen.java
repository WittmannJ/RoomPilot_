package de.haw.mobilesystems.hawinfo_v1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import de.haw.mobilesystems.hawinfo_v1.QRCodeReader.NoScanResultException;
import de.haw.mobilesystems.hawinfo_v1.QRCodeReader.ScanResultReceiver;
import de.haw.mobilesystems.hawinfo_v1.SearchListHelper.SearchListBuilding;
import de.haw.mobilesystems.hawinfo_v1.SearchListHelper.SearchListRoom;
import de.haw.mobilesystems.hawinfo_v1.model.Room;
import de.haw.mobilesystems.hawinfo_v1.tasks.AsyncTaskResult;
import de.haw.mobilesystems.hawinfo_v1.tasks.GETRoomData;
import de.haw.mobilesystems.hawinfo_v1.tasks.ITaskObserver;

/**
 * Created by rajinders on 2/12/16.
 */

public class QR_CodeScreen extends Fragment implements ITaskObserver{

    private static QR_CodeScreen instQRCodeScreen;


    public static QR_CodeScreen instance() {
        return instQRCodeScreen;
    }

    @Override
    public void onStart() {
        super.onStart();
        instQRCodeScreen = this;
    }

    private String codeFormat,codeContent;
    private final String noResultErrorMsg = "No scan data received!";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        // use forSupportFragment or forFragment method to use fragments instead of activity
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        //integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);

        integrator.setOrientationLocked(false);

        integrator.initiateScan();
    }

    /**
     * function handle scan result
     * @param requestCode scanned code
     * @param resultCode  result of scanned code
     * @param intent intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();

        if (scanningResult != null) {

            if(scanningResult.getContents() ==null){
                Toast.makeText(getActivity(), "You cancelled the scanning", Toast.LENGTH_SHORT).show();
            }else {


                //we have a result
                codeContent = scanningResult.getContents();
                codeFormat = scanningResult.getFormatName();
                // send received data
                parentActivity.scanResultData(codeFormat, codeContent);
            }

        }else{
            // send exception

            parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }

    @Override
    public void onGETRoomDataComplete(AsyncTaskResult<Room> result) {

        if(result==null || result.getResult() == null || result.getError()!=null) {
            //if the Error Msg is null, show general Error
            if (result.getError() != null) {
                Toast.makeText(getActivity(),result.getError().getMessage(),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Error 404 :)", Toast.LENGTH_SHORT).show();
            }

        }else {

            Fragment roomInfo = new RoomInfoScreen();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            final Bundle bundle = new Bundle();
            bundle.putSerializable("room", result.getResult());
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

    public void showScannedRoom (String room){
        GETRoomData get = new GETRoomData(QR_CodeScreen.instance());
        String uri = Uri.parse("http://goserver.haw-landshut.de:6061").buildUpon().path("/hawinfo/public/getRoomData").appendPath(room).appendPath(String.valueOf(System.currentTimeMillis())).toString();
        Log.d("IMPORTANT", uri);
        get.execute(uri);
    }
}