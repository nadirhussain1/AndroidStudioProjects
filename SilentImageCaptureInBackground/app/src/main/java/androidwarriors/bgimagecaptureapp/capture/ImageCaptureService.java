package androidwarriors.bgimagecaptureapp.capture;


import android.app.Service;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import androidwarriors.bgimagecaptureapp.upload.ImageUploader;
import androidwarriors.bgimagecaptureapp.configurations.ConfigPreferences;


/**
 * Created by nadirhussain on 28/05/2016.
 */


public class ImageCaptureService extends Service {
    private static final String TAG = "ImageCaptureService";
    private static final int CAMERA = CameraCharacteristics.LENS_FACING_BACK;
    private int width = 320, height = 240; // These are default width and heights of image, in case no supported size found on specific device.
    public static int counter = 0; // This counter is for pictures being saved locally in sdCard for debugging.


    private CameraDevice camera;
    private CameraCaptureSession session;
    private ImageReader imageReader;


    // This is the first method that will be invoked when service is triggered from within AlarmReceiver.
    // This method gets camera id and tries to open the camera.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String cameraId = getCamera(manager);
            // Open camera and once camera is available ..you will be notified in callback
            manager.openCamera(cameraId, cameraStateCallback, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    // This method gets list of all cameras on device and then finds back camera. Then it gets all supported output sizes for the JPEG images
    //on particular device.
    public String getCamera(CameraManager manager) {
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CAMERA) {
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (map != null) {
                        findOptimalSupportedSize(map);
                    }
                    return cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Once camera is opened. This method is called to start a capture session.
    // Result of session will be back in sessionStateCallback
    // Result of Image captured by camera will be available back in ImageAvailableListener
    private void startCaptureSession() {
        imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1); //fps * 10 min
        imageReader.setOnImageAvailableListener(onImageAvailableListener, null);

        try {
            camera.createCaptureSession(Arrays.asList(imageReader.getSurface()), sessionStateCallback, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //Once session is available ... Then capture request is initiated.
    // This request sets paramter that whether to flash or not.

    private CaptureRequest createCaptureRequest() {
        try {
            CaptureRequest.Builder builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(imageReader.getSurface());
            if (ConfigPreferences.getInstance(getApplicationContext()).getFlashSwitchState()) {
                builder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
               // builder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            }else{
                builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            }
            return builder.build();
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     *  Convert Image data into bytes..and encode it to BASE64 string
     *  Then these encoded bytes are uploaded via uploader.
     *  these bytes are also saved locally in sdcard.
     */
    private void processImage(Image image) throws IOException {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] originalBytes = new byte[buffer.capacity()];
        buffer.get(originalBytes);
        String encodedImage = Base64.encodeToString(originalBytes, Base64.DEFAULT);
//        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        String sizeDimension = "" + width + "*" + "" + height;
        ImageUploader.uploadImage(getApplicationContext(), encodedImage, sizeDimension);
        saveBytes(originalBytes); // This is only for debugging. image is saved locally in sdcard
        closeCamera();
    }

    // This method is used to save images locally in sdcard folder of device.
    private void saveBytes(byte[] bytes) throws IOException {
        counter++;
        File file = new File(Environment.getExternalStorageDirectory() + "/pic_" + counter + ".jpg");
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
        } finally {
            if (null != output) {
                output.close();
            }
        }
    }

    // This method finds optimal supported output size based on configuration of user
    // whether small,medium or large is selected. range of sizes are defined by assumption.
    private void findOptimalSupportedSize(StreamConfigurationMap map) {
        android.util.Size[] sizes = map.getOutputSizes(ImageFormat.JPEG);
        int sizeIndex = ConfigPreferences.getInstance(getApplicationContext()).getSizeIndex();
        if (sizes.length > 0) {

            width = sizes[0].getWidth();
            height = sizes[0].getHeight();
            for (int count = 1; count < sizes.length; count++) {

                if (sizeIndex == 0) {
                    //find min dimension
                    if (sizes[count].getWidth() < 500) {
                        width = sizes[count].getWidth();
                        height = sizes[count].getHeight();
                        break;
                    }
                } else if (sizeIndex == 2) {
                    //find large dimension
                    if (sizes[count].getWidth() > 1500 && sizes[count].getWidth() < 2500) {
                        width = sizes[count].getWidth();
                        height = sizes[count].getHeight();
                        break;
                    }
                } else {
                    //find medium dimension
                    if (sizes[count].getWidth() > 500 && sizes[count].getWidth() < 1300) {
                        width = sizes[count].getWidth();
                        height = sizes[count].getHeight();
                        break;
                    }
                }
            }
        }
    }


    // This is callback of camera state. This lets us know whether call to open camera was successful
    // or ended with some error.
    private CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            ImageCaptureService.this.camera = camera;
            startCaptureSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }


    };


    // This is callback of Capture Session. This lets us know whether session was configued or not
    private CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            ImageCaptureService.this.session = session;
            try {
                session.capture(createCaptureRequest(), null, null);
            } catch (CameraAccessException e) {
                Log.e(TAG, e.getMessage());
            }

        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Log.d("ImageReader", "sessionStateCallBack configured failed");
        }
    };

    /// Once Image is available then this callback is invoked.
    // Here we get image and we can process it further.

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image img = reader.acquireLatestImage();
            try {
                processImage(img);
            } catch (IOException exception) {

            } finally {
                img.close();
            }
        }
        // }
    };

    // This is to close camera once image has been captured.
    //It is necessary to release camera so that oht
    private void closeCamera() {
        if (null != session) {
            session.close();
            session = null;
        }
        if (null != camera) {
            camera.close();
            camera = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }

    }


}

