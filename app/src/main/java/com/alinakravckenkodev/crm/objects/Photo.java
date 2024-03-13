package com.alinakravckenkodev.crm.objects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.alinakravckenkodev.crm.R;
import com.alinakravckenkodev.crm.Services;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo {
    Form form;
    public PopupWindow popupWindow;
    public String currentPhotoPath = "";
    public Uri photoURI;
    public Bitmap photoBitmap;

    public Photo (Form form)  {
        this.form = form;
        System.out.println("Photo_class_constructor___0001");
        System.out.println("form.photoByteArray = " + String.valueOf(form.photoByteArray));


        if (form.photoByteArray!=null)
        {
            photoBitmap = Services.getBitmapFromByteArray( form.photoByteArray);
        }

    }

    public void createWindow() {
        LayoutInflater inflater = form.activity.getLayoutInflater();
        View mainView = inflater.inflate(R.layout.photo, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;
        popupWindow = new PopupWindow(mainView, width, height, focusable);
        popupWindow.showAtLocation(form.activity.findViewById(R.id.tableList), Gravity.CENTER, 0, 0);


        if (photoBitmap!=null) {
            ImageView photoView = mainView.findViewById(R.id.photoImage);
            photoView.setImageBitmap(photoBitmap);
            photoView.setRotation(90);

        } else if (photoURI!=null) {
            ImageView photoView = mainView.findViewById(R.id.photoImage);
            photoView.setImageURI(photoURI);
        }
    }

    public void takePhoto() {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(form.activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(form.activity,
                        "com.example.android.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                form.activity.startActivityForResult(takePictureIntent, 1);
            }
        } else {
            Toast t = new Toast(form.activity);
            t.setText("Камера недоступна");
            t.show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = form.activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        form.activity.sendBroadcast(mediaScanIntent);
    }



}
