package com.test.aasanload.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraSelector.Builder;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysis.Analyzer;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.text.Text;
import com.test.aasanload.R;
import com.test.aasanload.constants.MyConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MyCustomCamera.kt */
public final class MyCustomCamera  {
    /*public static final Companion Companion = new Companion();
    private static final String FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final String PHOTO_EXTENSION = ".jpg";
    private static final double RATIO_16_9_VALUE = 1.7777777777777777d;
    private static final double RATIO_4_3_VALUE = 1.3333333333333333d;
    private static final String TAG = "CameraXBasic";
    private LocalBroadcastManager broadcastManager;
    private Camera camera;
    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;
    private ConstraintLayout container;
    private int displayId = -1;
   private final Lazy displayManager$delegate = LazyKt.lazy((Function0) new LuminosityAnalyzer(this));
    private boolean flash;
    private ImageAnalysis imageAnalyzer;
    private ImageCapture imageCapture;
    private boolean isFirstTime = true;
    private int lensFacing = 1;
    private String network = "";
    private File outputDirectory;
    private Preview preview;
    private PreviewView viewFinder;




    public static final class Companion {
        private Companion() {
        }

        public *//* synthetic *//* Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B2\u0012+\b\u0002\u0010\u0002\u001a%\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b\u0018\u00010\u0003j\u0004\u0018\u0001`\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u000e\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u001bJ-\u0010\u001d\u001a\u00020\u001e2%\u0010\u0002\u001a!\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0003j\u0002`\tJ\f\u0010\u001f\u001a\u00020 *\u00020!H\u0002R\u000e\u0010\u000b\u001a\u00020\fXD¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u0011@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\\\u0010\u0016\u001aP\u0012#\u0012!\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0003j\u0002`\t0\u0017j'\u0012#\u0012!\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0003j\u0002`\t`\u0018X\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/easy/recharge/activities/MyCustomCamera$LuminosityAnalyzer;", "Landroidx/camera/core/ImageAnalysis$Analyzer;", "listener", "Lkotlin/Function1;", "Landroid/graphics/Bitmap;", "Lkotlin/ParameterName;", "name", "bitmap", "", "Lcom/easy/recharge/activities/analyzedImage;", "(Lcom/easy/recharge/activities/MyCustomCamera;Lkotlin/jvm/functions/Function1;)V", "frameRateWindow", "", "frameTimestamps", "Ljava/util/ArrayDeque;", "", "<set-?>", "", "framesPerSecond", "getFramesPerSecond", "()D", "lastAnalyzedTimestamp", "listeners", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "analyze", "image", "Landroidx/camera/core/ImageProxy;", "imageToBitmap", "onFrameAnalyzed", "", "toByteArray", "", "Ljava/nio/ByteBuffer;", "app_release"}, k = 1, mv = {1, 4, 2})
    *//* compiled from: MyCustomCamera.kt *//*
    private final class LuminosityAnalyzer implements Analyzer {
        private final int frameRateWindow;
        private final ArrayDeque<Long> frameTimestamps;
        private double framesPerSecond;
        private long lastAnalyzedTimestamp;
        private final ArrayList<Function1<Bitmap, Unit>> listeners;

        public LuminosityAnalyzer(Function1<? super Bitmap, Unit> function1) {
            this.frameRateWindow = 8;
            this.frameTimestamps = new ArrayDeque(5);
            ArrayList arrayList = new ArrayList();
            this.listeners = arrayList;
            this.framesPerSecond = -1.0d;
            if (function1 != null) {
                arrayList.add(function1);
            }
        }

        public LuminosityAnalyzer(MyCustomCamera myCustomCamera, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker, int frameRateWindow, ArrayDeque<Long> frameTimestamps, ArrayList<Function1<Bitmap, Unit>> listeners) {
            this.frameRateWindow = frameRateWindow;
            this.frameTimestamps = frameTimestamps;
            this.listeners = listeners;
            if ((i & 1) != 0) {
                function1 = (Function1) null;
            }

        }

        public final double getFramesPerSecond() {
            return this.framesPerSecond;
        }

        public final boolean onFrameAnalyzed(Function1<? super Bitmap, Unit> function1) {
            Intrinsics.checkNotNullParameter(function1, "listener");
            return this.listeners.add((Function1<Bitmap, Unit>) function1);
        }

        private final byte[] toByteArray(ByteBuffer byteBuffer) {
            byteBuffer.rewind();
            byte[] bArr = new byte[byteBuffer.remaining()];
            byteBuffer.get(bArr);
            return bArr;
        }

        public void analyze(ImageProxy imageProxy) {
            Intrinsics.checkNotNullParameter(imageProxy, "image");
            if (this.listeners.isEmpty()) {
                imageProxy.close();
                return;
            }
            Bitmap imageToBitmap = imageToBitmap(imageProxy);
            int height = imageToBitmap.getHeight();
            int width = imageToBitmap.getWidth();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("analyze: ");
            stringBuilder.append(height);
            stringBuilder.append("   ");
            stringBuilder.append(width);
            Log.d(MyCustomCamera.TAG, stringBuilder.toString());
            float dpToPixels = MyCustomCamera.this.dpToPixels(80);
            imageToBitmap = Bitmap.createBitmap(imageToBitmap, (int) (((float) (width / 2)) - (dpToPixels / ((float) 2))), 0, (int) dpToPixels, height);
            for (Function1 function1 : this.listeners) {
                Intrinsics.checkNotNullExpressionValue(imageToBitmap, "resizedbitmap");
                function1.invoke(imageToBitmap);
            }
            imageProxy.close();
        }

        public final Bitmap imageToBitmap(ImageProxy imageProxy) {
            Intrinsics.checkNotNullParameter(imageProxy, "image");
            ImageProxy.PlaneProxy obj = imageProxy.getPlanes()[0];

            ByteBuffer buffer = obj.getBuffer();

            ImageProxy.PlaneProxy obj2 = imageProxy.getPlanes()[2];

            ByteBuffer buffer2 = obj2.getBuffer();

            int remaining = buffer.remaining();
            int remaining2 = buffer2.remaining();
            byte[] bArr = new byte[(remaining + remaining2)];
            buffer.get(bArr, 0, remaining);
            buffer2.get(bArr, remaining, remaining2);
            YuvImage yuvImage = new YuvImage(bArr, 17, imageProxy.getWidth(), imageProxy.getHeight(), null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 50, byteArrayOutputStream);
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
            Intrinsics.checkNotNullExpressionValue(decodeByteArray, "BitmapFactory.decodeByte…ytes, 0, imageBytes.size)");
            return decodeByteArray;
        }
    }

    private final DisplayManager getDisplayManager() {
        return (DisplayManager) this.displayManager$delegate.getValue();
    }

    public final ExecutorService getCameraExecutor() {
        return this.cameraExecutor;
    }

    public final void setCameraExecutor(ExecutorService executorService) {
        this.cameraExecutor = executorService;
    }

    public final boolean getFlash() {
        return this.flash;
    }

    public final void setFlash(boolean z) {
        this.flash = z;
    }

    *//* Access modifiers changed, original: protected *//*
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_camerax);

        this.network = String.valueOf(getIntent().getStringExtra(MyConstants.Companion.getNETWORK_KEY()));
        ((ConstraintLayout) findViewById(R.id.cl_flashLight)).setOnClickListener(new MyCustomCamera$onCreate$2(this));
    }

    *//* Access modifiers changed, original: protected *//*
    public void onResume() {
        super.onResume();
    }

    public final void prepareCameraView() {
        this.flash = false;
        ((ImageView) findViewById(R.id.iv_flash)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_torch_off, null));
        View findViewById = findViewById(R.id.view_finder);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.view_finder)");
        this.viewFinder = (PreviewView) findViewById;
        this.cameraExecutor = Executors.newSingleThreadExecutor();
        Context context = (Context) this;
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        Intrinsics.checkNotNullExpressionValue(instance, "LocalBroadcastManager.getInstance(this)");
        this.broadcastManager = instance;
        ImageView imageView = (ImageView) findViewById(R.id.iv_scan_bar);
        Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.translate);
        Intrinsics.checkNotNullExpressionValue(imageView, "iv_scan_bar");
        imageView.setAnimation(loadAnimation);
        imageView.getAnimation().start();
        PreviewView previewView = this.viewFinder;
        if (previewView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("viewFinder");
        }
        previewView.post(new MyCustomCamera$prepareCameraView$1(this));
    }

    *//* Access modifiers changed, original: protected *//*
    public void onDestroy() {
        super.onDestroy();
        ExecutorService executorService = this.cameraExecutor;
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    *//* Access modifiers changed, original: protected *//*
    public void onPause() {
        super.onPause();
    }

    private final void setUpCamera() {
        Context context = (Context) this;
        ListenableFuture instance = ProcessCameraProvider.getInstance(context);
        Intrinsics.checkNotNullExpressionValue(instance, "ProcessCameraProvider.ge…ance(this@MyCustomCamera)");
        instance.addListener(new MyCustomCamera$setUpCamera$1(this, instance), ContextCompat.getMainExecutor(context));
    }

    private final boolean hasBackCamera() {
        ProcessCameraProvider processCameraProvider = this.cameraProvider;
        return processCameraProvider != null ? processCameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) : false;
    }

    private final boolean hasFrontCamera() {
        ProcessCameraProvider processCameraProvider = this.cameraProvider;
        return processCameraProvider != null ? processCameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) : false;
    }

    private final void bindCameraUseCases() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        PreviewView previewView = this.viewFinder;
        String str = "viewFinder";
        if (previewView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        previewView.getDisplay().getRealMetrics(displayMetrics);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Screen metrics: ");
        stringBuilder.append(displayMetrics.widthPixels);
        stringBuilder.append(" x ");
        stringBuilder.append(displayMetrics.heightPixels);
        String stringBuilder2 = stringBuilder.toString();
        String str2 = MyConstants.TAG;
        Log.d(str2, stringBuilder2);
        int aspectRatio = aspectRatio(displayMetrics.widthPixels, displayMetrics.heightPixels);
        stringBuilder = new StringBuilder();
        stringBuilder.append("Preview aspect ratio: ");
        stringBuilder.append(aspectRatio);
        Log.d(str2, stringBuilder.toString());
        previewView = this.viewFinder;
        if (previewView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        Display display = previewView.getDisplay();
        Intrinsics.checkNotNullExpressionValue(display, "viewFinder.display");
        int rotation = display.getRotation();
        ProcessCameraProvider processCameraProvider = this.cameraProvider;
        if (processCameraProvider != null) {
            CameraSelector build = new Builder().requireLensFacing(this.lensFacing).build();
            Intrinsics.checkNotNullExpressionValue(build, "CameraSelector.Builder()…acing(lensFacing).build()");
            this.preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).setTargetRotation(rotation).build();
            this.imageCapture = new ImageCapture.Builder().setCaptureMode(1).setTargetAspectRatio(aspectRatio).setTargetRotation(rotation).build();
            ImageAnalysis build2 = new ImageAnalysis.Builder().setTargetAspectRatio(aspectRatio).setTargetRotation(rotation).build();
            ExecutorService executorService = this.cameraExecutor;
            if (executorService != null) {
                build2.setAnalyzer(executorService, (Analyzer) new LuminosityAnalyzer((Function1) new MyCustomCamera$bindCameraUseCases$$inlined$also$lambda$1(build2, this)));
            }
            Unit unit = Unit.INSTANCE;
            this.imageAnalyzer = build2;
            processCameraProvider.unbindAll();
            try {
                this.camera = processCameraProvider.bindToLifecycle((LifecycleOwner) this, build, new UseCase[]{(UseCase) this.preview, (UseCase) this.imageCapture, (UseCase) this.imageAnalyzer});
                Preview preview = this.preview;
                if (preview != null) {
                    previewView = this.viewFinder;
                    if (previewView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException(str);
                    }
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());
                    return;
                }
                return;
            } catch (Exception e) {
                Log.e(str2, "Use case binding failed", e);
                return;
            }
        }
        throw new IllegalStateException("Camera initialization failed.");
    }

    public final void receiveDetections(SparseArray<Text.TextBlock> sparseArray) {
        Intrinsics.checkNotNullParameter(sparseArray, "detectedItems");
        ArrayList arrayList = new ArrayList();
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            Object valueAt = sparseArray.valueAt(i) ;
            Text.TextBlock textBlock = (Text.TextBlock) valueAt;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("receiveDetections: ");
            stringBuilder.append(textBlock.getText());
            String stringBuilder2 = stringBuilder.toString();
            String str = TAG;
            Log.d(str, stringBuilder2);
            String value = textBlock.getText();
            Intrinsics.checkNotNullExpressionValue(value, "textBlock.value");
            value = isCodeNumber(value);
            if (value != null) {
                if ((((CharSequence) value).length() > 0 ? 1 : null) == 1) {
                    arrayList.add(value);
                    Log.d(str, value.toString());
                }
            }
        }
        Collection collection = arrayList;
        if (!collection.isEmpty() && !this.isFirstTime) {
            ExecutorService executorService = this.cameraExecutor;
            if (executorService != null) {
                executorService.shutdown();
            }
            Intent intent = new Intent((Context) this, DialerActivity.class);
            intent.putExtra(MyConstants.Companion.getCODE_ARRAY(), arrayList);
            intent.putExtra(MyConstants.Companion.getNETWORK_KEY(), this.network);
            startActivity(intent);
            finish();
        } else if (!collection.isEmpty()&& this.isFirstTime) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    receiveDetections(collection);
                }
            }, 500);
        }
    }

    private final String isCodeNumber(String str) {
        String str2 = "";
        if (str.length() < 14) {
            return str2;
        }
        for (String str3 : str.split("\n")) {
            if (str3.length() >= 14) {
                int length = str3.length();
                String str4 = str2;
                for (int i = 0; i < length; i++) {
                    char charAt = str3.charAt(i);
                    if (Character.isDigit(charAt)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(str4);
                        stringBuilder.append(charAt);
                        str4 = stringBuilder.toString();
                    } else if (charAt != ' ') {
                        str4 = str2;
                    }
                }
                return str4.length() == 14 ? str4 : str2;
            }
        }
        return str2;
    }

    private int aspectRatio(int i, int i2) {
        double max = ((double) Math.max(i, i2)) / ((double) Math.min(i, i2));
        return Math.abs(max - RATIO_4_3_VALUE) <= Math.abs(max - RATIO_16_9_VALUE) ? 0 : 1;
    }

    public final float dpToPixels(int i) {
        float f = (float) i;
        Resources resources = getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "this@MyCustomCamera.resources");
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1f, resources.getDisplayMetrics());
    }

*/
}
