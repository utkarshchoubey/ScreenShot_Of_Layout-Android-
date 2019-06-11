package com.example.utkarsh.aboutvivifying;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


import static android.Manifest.*;

public class MainActivity extends AppCompatActivity {
    TextView nameText;
    EditText editText;
    ImageView imageView;
    String TAG = "Milcanx_TAG";

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //String name1=getIntent().getStringExtra("author");
        //nameText=(TextView)findViewById(R.id.textName);
        imageView=(ImageView)findViewById(R.id.imageView3);
        //nameText.setText(name1);
       // imgView=(ImageView) findViewById(R.id.imageView3);
        //Bitmap bmp = Bitmap.createBitmap(editText.getDrawingCache());
        Button save=(Button)findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outPuttingImage();
            }
        });
        Button btn_share_Image = (Button) findViewById(R.id.share);
        btn_share_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWriteStoragePermissionGranted();
                isReadStoragePermissionGranted();
                shareImage(ImageSavetoExternalStorageToFolder(outPuttingImage(),getApplicationContext()));


            }
        });
    }
    private Bitmap outPuttingImage() {

        // Oluşacak resmin içinde imleç gözükmemesi için
        // In the image to be formed,For the cursor not to appear
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setCursorVisible(false);

        // RelativeLayoutu Bitmap e çevirdik
        // RelativeLayout to Bitmap progress
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache(true);
        // creates immutable clone
        //Değiştirilemez klon oluşturur
        Bitmap bmp = Bitmap.createBitmap(relativeLayout.getDrawingCache());
        // clear drawing cache
        // çizim önbelleğini temizle
        relativeLayout.setDrawingCacheEnabled(false);


        if (bmp != null) {
            // (2)
            // Resmi Telefon Hafızasına Kaydetme Metodu
            // Method of saving the created bitmap to storage
            savetoStorage(bmp);

        } else {
            Toast.makeText(this, "Bitmap is Null / Bitmap Boş", Toast.LENGTH_SHORT).show();
        }


        // For the cursor to appear
        // artık imleç gözükebilir
        editText.setCursorVisible(true);

        return bmp;

    }


    // Save to Storage // hafızaya kaydet //////////////////////////////////////
    private void savetoStorage(Bitmap bm) {

        // Let's get our permits first
        // Create a method

        // Önce izinlerimizi alalım
        // Bir yöntem oluştur

        //First of all, add following Storage permission in our manifest File.
        // Öncelikle, manifest Dosyamızda Depolama iznini ekleyin.
        // 1  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        // 2  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

        // write following two methods in our activity for the request to granting Read/Write Permission.
        // Okuma / Yazma İzni verme talebi için etkinliğimizde iki yöntem iznini yazın.

        // (2-A)
        isWriteStoragePermissionGranted();
        isReadStoragePermissionGranted();

        // (2-B)

        // Create a method and I send the parameters required for the method
        // Metot oluşturalım ve Metot için gerekli olan parametreleri gönderelim
        ImageSavetoExternalStorageToFolder(bm, getApplicationContext());


    }


    // Getting permission // izin talep et //////////////////////////////////////////////
    private static final int REQUEST_WRITE_PERMISSION = 786;

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            //sdk <23 ise otomatik olarak izin verilir
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");

                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            //sdk <23 ise otomatik olarak izin verilir
            Log.v(TAG, "Permission is granted1");

            return true;
        }
    }

    // Getting permission response in our Activity.
    // Faaliyetimizde izin alınması.
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    // resume tasks needing this permission
                    // izni aldın ve bu izinle ilgili işlemlerini burada yapabilirsin


                } else {
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    // izni aldın ve bu izinle ilgili işlemlerini burada yapabilirsin


                } else {
                }
                break;
        }
    }


    String MAINFOLDER_NAME = "About Vivifying";
    String FILE_NAME = "AVImage";
    String MAINROOTDIR = Environment.getExternalStorageDirectory()
            + File.separator + MAINFOLDER_NAME;
    String FULLNAME;
    //get İmage full path
    String IMAGE_PATH;
    // return file
    File FILE;

    // Picture saving method
    //Resmi kaydetme metodumuz
    private File ImageSavetoExternalStorageToFolder(Bitmap bmp, Context context) {

        //Let's put dates for unique names
        //Eşsiz isimler için tarihler koyalım
        Calendar c = Calendar.getInstance();
        //We can even add a random number
        //Hatta rastgele bir sayı ekleyebiliriz
        Random r = new Random();
        String number = String.valueOf(r.nextInt(1000 - 0) + 1);
        // Combine all and create a name
        // Hepsini birleştir ve bir isim oluşturalım
        FULLNAME = FILE_NAME + c.get(Calendar.MILLISECOND) + number + ".jpg";

        // If our folder has never been created, Let's interrogate and create
        // Klasörümüz hiç oluşturulmamışsa, Sorgulayalım ve oluşturalım
        File pat = new File(MAINROOTDIR + File.separator);
        if (!pat.exists()) {
            pat.mkdirs();
        }

        //Create your image file
        //Resim dosyamızı oluşturalım
        File file = new File(pat, FULLNAME);
        try {

            OutputStream os = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }

        //oluşturulan resim dosyamızı döndürelim
        //Return your created image file
        return FILE = file;


    }


    // Share the created image / oluşturulan resmi paylaş ///////////////////////////////////////
    public  void shareImage(File Imagepath) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Imagepath));
        shareIntent.setType("image/jpg");

        try {
            startActivity(Intent.createChooser(shareIntent,"Select App"));

        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Failed to share", Toast.LENGTH_SHORT).show();

        }


    }







}






