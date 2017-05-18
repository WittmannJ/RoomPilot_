package de.haw.mobilesystems.hawinfo_v1.QRCodeReaderHelper;

/**
 * Created by jackl on 02.04.2017.
 */


    /**
     * Created by Rajinder on 12/10/2016.
     */
    public interface ScanResultReceiver {
        /**
         * function to receive scanresult
         * @param codeFormat format of the barcode scanned
         * @param codeContent data of the barcode scanned
         */
        public void scanResultData(String codeFormat, String codeContent);

        public void scanResultData(NoScanResultException noScanData);
    }

