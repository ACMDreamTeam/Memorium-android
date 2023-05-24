package com.acmdreamteam.memorium;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.acmdreamteam.memorium.Drawing.OverlayView;
import com.acmdreamteam.memorium.Drawing.BorderedText;
import com.acmdreamteam.memorium.Drawing.MultiBoxTracker;
import com.acmdreamteam.memorium.Drawing.OverlayView;
import com.acmdreamteam.memorium.Face_Recognition.FaceClassifier;
import com.acmdreamteam.memorium.Face_Recognition.TFLiteFaceRecognition;
import com.acmdreamteam.memorium.LiveFeed.CameraConnectionFragment;
import com.acmdreamteam.memorium.LiveFeed.ImageUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecogActivity extends AppCompatActivity implements ImageReader.OnImageAvailableListener{


    Handler handler;
    String name_;
    String imageURL_;
    private Matrix frameToCropTransform;
    private int sensorOrientation;
    private Matrix cropToFrameTransform;

    private static final boolean MAINTAIN_ASPECT = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private BorderedText borderedText;
    private MultiBoxTracker tracker;
    private Integer useFacing = null;
    private static final String KEY_USE_FACING = "use_facing";
    private static final int CROP_SIZE = 1000;
    private static final int TF_OD_API_INPUT_SIZE2 = 160;

    String mUri;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    StorageReference storageReference;

//    //TODO declare face detector
        FaceDetector detector;

    //TODO declare face recognizer
        private FaceClassifier faceClassifier;

    boolean registerFace = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog);
        handler = new Handler();




        //TODO handling permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 121);
            }
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("/SavedFaces/"+ firebaseUser.getUid());

        Intent intent = getIntent();
        useFacing = intent.getIntExtra(KEY_USE_FACING, CameraCharacteristics.LENS_FACING_BACK);

        //TODO show live camera footage

        setFragment();



        //TODO initialize the tracker to draw rectangles
        tracker = new MultiBoxTracker(this);


        //TODO initalize face detector
        // Multiple object detection in static images
            FaceDetectorOptions highAccuracyOpts =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                            .build();
            detector = FaceDetection.getClient(highAccuracyOpts);


        //TODO initialize FACE Recognition
            try {
                faceClassifier =
                       TFLiteFaceRecognition.create(
                                getAssets(),
                                "facenet.tflite",
                                TF_OD_API_INPUT_SIZE2,
                                false);

            } catch (final IOException e) {
                e.printStackTrace();
                Toast toast =
                        Toast.makeText(
                                getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }

        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFace = true;
            }
        });



        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        try {
            loadFaces();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 121 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setFragment();
        }
    }

    //TODO fragment which show live footage from camera
    int previewHeight = 0,previewWidth = 0;
    protected void setFragment() {
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = manager.getCameraIdList()[useFacing];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Fragment fragment;
        CameraConnectionFragment camera2Fragment =
                CameraConnectionFragment.newInstance(
                        new CameraConnectionFragment.ConnectionCallback() {
                            @Override
                            public void onPreviewSizeChosen(final Size size, final int rotation) {
                                previewHeight = size.getHeight();
                                previewWidth = size.getWidth();

                                final float textSizePx =
                                        TypedValue.applyDimension(
                                                TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
                                borderedText = new BorderedText(textSizePx);
                                borderedText.setTypeface(Typeface.MONOSPACE);


                                int cropSize = CROP_SIZE;

                                previewWidth = size.getWidth();
                                previewHeight = size.getHeight();

                                sensorOrientation = rotation - getScreenOrientation();

                                rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
                                croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888);

                                frameToCropTransform =
                                        ImageUtils.getTransformationMatrix(
                                                previewWidth, previewHeight,
                                                cropSize, cropSize,
                                                sensorOrientation, MAINTAIN_ASPECT);

                                cropToFrameTransform = new Matrix();
                                frameToCropTransform.invert(cropToFrameTransform);

                                trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
                                trackingOverlay.addCallback(
                                        new OverlayView.DrawCallback() {
                                            @Override
                                            public void drawCallback(final Canvas canvas) {
                                                tracker.draw(canvas);
                                                Log.d("tryDrawRect","inside draw");
                                            }
                                        });
                                tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
                            }
                        },
                        this,
                        R.layout.camera_fragment,
                        new Size(640, 480));

        camera2Fragment.setCamera(cameraId);
        fragment = camera2Fragment;
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();


    }


    //TODO getting frames of live camera footage and passing them to model
    private boolean isProcessingFrame = false;
    private byte[][] yuvBytes = new byte[3][];
    private int[] rgbBytes = null;
    private int yRowStride;
    private Runnable postInferenceCallback;
    private Runnable imageConverter;
    private Bitmap rgbFrameBitmap;
    Bitmap croppedBitmap;
    @Override
    public void onImageAvailable(ImageReader reader) {
        // We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return;
        }
        if (rgbBytes == null) {
            rgbBytes = new int[previewWidth * previewHeight];
        }
        try {
            final Image image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (isProcessingFrame) {
                image.close();
                return;
            }
            isProcessingFrame = true;
            final Image.Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);
            yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();

            imageConverter =
                    new Runnable() {
                        @Override
                        public void run() {
                            ImageUtils.convertYUV420ToARGB8888(
                                    yuvBytes[0],
                                    yuvBytes[1],
                                    yuvBytes[2],
                                    previewWidth,
                                    previewHeight,
                                    yRowStride,
                                    uvRowStride,
                                    uvPixelStride,
                                    rgbBytes);
                        }
                    };

            postInferenceCallback =
                    new Runnable() {
                        @Override
                        public void run() {
                            image.close();
                            isProcessingFrame = false;
                        }
                    };

            performFaceDetection();

        } catch (final Exception e) {
            Log.d("tryError",e.getMessage()+"abc ");
            return;
        }

    }

    protected void fillBytes(final Image.Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }
    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }


    List<FaceClassifier.Recognition> mappedRecognitions;

    //TODO Perform face detection
    public void performFaceDetection(){
        imageConverter.run();;
        rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mappedRecognitions = new ArrayList<>();
                InputImage image = InputImage.fromBitmap(croppedBitmap,0);
                detector.process(image)
                        .addOnSuccessListener(
                                        new OnSuccessListener<List<Face>>() {
                                            @Override
                                            public void onSuccess(List<Face> faces) {

                                                for(Face face:faces) {
                                                    final Rect bounds = face.getBoundingBox();
                                                    performFaceRecognition(face,croppedBitmap);
                                                }
                                                registerFace = false;
                                                tracker.trackResults(mappedRecognitions, 10);
                                                trackingOverlay.postInvalidate();
                                                postInferenceCallback.run();

                                            }
                                        })
                        .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                            }
                                        });



            }
        });
    }

    //TODO perform face recognition
    public void performFaceRecognition(Face face,Bitmap input){
        //TODO crop the face
        Rect bounds = face.getBoundingBox();
        if(bounds.top<0){
            bounds.top = 0;
        }
        if(bounds.left<0){
            bounds.left = 0;
        }
        if(bounds.left+bounds.width()>input.getWidth()){
            bounds.right = input.getWidth()-1;
        }
        if(bounds.top+bounds.height()>input.getHeight()){
            bounds.bottom = input.getHeight()-1;
        }

        Bitmap crop = Bitmap.createBitmap(input,
                bounds.left,
                bounds.top,
                bounds.width(),
                bounds.height());
        crop = Bitmap.createScaledBitmap(crop,TF_OD_API_INPUT_SIZE2,TF_OD_API_INPUT_SIZE2,false);


        final FaceClassifier.Recognition result = faceClassifier.recognizeImage(crop, registerFace);
        String title = "Unknown";
        float confidence = 0;
        if (result !=null) {
            if(registerFace){
                registerFaceDialogue(crop,result);
            }else {
                if (result.getDistance() < 0.99f) {
                    confidence = result.getDistance();
                    title = result.getTitle();
                }
            }
        }

        RectF location = new RectF(bounds);
        if (bounds != null) {
            if(useFacing == CameraCharacteristics.LENS_FACING_BACK) {
                location.right = input.getWidth() - location.right;
                location.left = input.getWidth() - location.left;
            }
            cropToFrameTransform.mapRect(location);
            FaceClassifier.Recognition recognition = new FaceClassifier.Recognition(face.getTrackingId()+"",title,confidence,location);
            mappedRecognitions.add(recognition);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //detector.close();
    }

    //TODO register face dialogue
    private void registerFaceDialogue(Bitmap croppedFace, FaceClassifier.Recognition rec) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.register_face_dialogue);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView ivFace = dialog.findViewById(R.id.dlg_image);
        EditText nameEd = dialog.findViewById(R.id.dlg_input);
        EditText phoneEd = dialog.findViewById(R.id.dlg_phone);
        EditText notesEd = dialog.findViewById(R.id.dlg_notes);
        Button register = dialog.findViewById(R.id.button2);
        ivFace.setImageBitmap(croppedFace);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEd.getText().toString();
                String phone = phoneEd.getText().toString();
                String notes = notesEd.getText().toString();

                if (name.isEmpty()) {
                    nameEd.setError("Enter Name");
                    return;
                }
                faceClassifier.register(name, rec);
                saveFave(name,croppedFace,phone,notes);
                Toast.makeText(RecogActivity.this, "Face Registered Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    //TODO switch camera
    public void switchCamera() {

        Intent intent = getIntent();

        if (useFacing == CameraCharacteristics.LENS_FACING_FRONT) {
            useFacing = CameraCharacteristics.LENS_FACING_BACK;
        } else {
            useFacing = CameraCharacteristics.LENS_FACING_FRONT;
        }

        intent.putExtra(KEY_USE_FACING, useFacing);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        restartWith(intent);

    }


    private void saveFave(String name, Bitmap croppedFace,String phone,String notes) {


        Uri resultUri = getImageUri(getApplicationContext(),croppedFace);

        StorageReference filepath = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(resultUri));


        StorageTask uploadTask = filepath.putFile(resultUri);
        uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filepath.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    mUri = downloadUri.toString();


                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    Map<String, String> user = new HashMap<>();
                    user.put("name",name);
                    user.put("imageURL",mUri);
                    user.put("phoneNo",phone);
                    user.put("notes",notes);


                    assert firebaseUser != null;
                    db.collection("user_data").document(firebaseUser.getUid()).collection("face_data").document(name)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //startActivity(new Intent(RecogActivity.this,QuestionnaireActivity.class));
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });


                }
            }
        });


    }




    private void loadFaces() throws IOException {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("user_data").document(firebaseUser.getUid()).collection("face_data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(QueryDocumentSnapshot documentSnapshots : task.getResult()){


                    imageURL_ = documentSnapshots.getString("imageURL");
                    name_ = documentSnapshots.getString("name");


                    URL url = null;
                    try {
                        url = new URL(imageURL_);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        final FaceClassifier.Recognition result = faceClassifier.recognizeImage(image, true);
                        faceClassifier.register(name_, result);
                        registerFace = false;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }


        });


    }


    private void restartWith(Intent intent) {
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = RecogActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
