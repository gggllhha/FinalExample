package com.example.finalexample.ui.Weather;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import android.provider.MediaStore;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalexample.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Weather2Fragment#/newInstance} factory method to
 * create an instance of this fragment.
 */
public class Weather2Fragment extends Fragment {
    private EditText mInput;
    private ImageView img;
    private Button btn1,btn2;
    Uri imageUri;
    final int TAKE_PHOTO=1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInput=(EditText) view.findViewById(R.id.edit);
        img=(ImageView) view.findViewById(R.id.img_erweima);
        btn1=(Button)view.findViewById(R.id.btn_erweima);
        btn2=(Button)view.findViewById(R.id.btn_picture);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input=mInput.getText().toString().trim();
                if(TextUtils.isEmpty(input)){
                    Toast.makeText(getActivity(),"???????????????",Toast.LENGTH_LONG).show();
                }else
                createQRCodeBitmap(input,img,800,800,5);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File output=new File(getActivity().getExternalCacheDir(),"output_image.jpg");
                try {
                    if (output.exists()){
                        output.delete();
                    }
                    output.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=24){
//?????????????????????
                    imageUri= FileProvider.getUriForFile(getActivity(),"com.example.finalexample.fileprovider",output);
                }
                else { imageUri=Uri.fromFile(output);}
                //??????????????????????????????????????????
                if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},66);
                }else{
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    // ??????try???????????????????????????
                    try {
                        //???????????????
                        Bitmap bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        img.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            default:break;
        }
    }


    public static Bitmap createQRCodeBitmap(String content, ImageView iv_view,
                                            int width, int height, int margin) {
        // ?????????????????????
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // ?????????>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /** 1.??????????????????????????? */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // ????????????????????????
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // ???????????????
            hints.put(EncodeHintType.ERROR_CORRECTION, "H");
            // ??????????????????
            hints.put(EncodeHintType.MARGIN, margin + "");
            /** 2.????????????????????????QRCodeWriter???encode????????????BitMatrix(?????????)?????? */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            bitMatrix = updateBit(bitMatrix, margin);

            width = bitMatrix.getWidth();
            height = bitMatrix.getHeight();

            /** 3.??????????????????,?????????BitMatrix(?????????)????????????????????????????????? */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //bitMatrix.get(x,y)????????????true??????????????????false???????????????
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;//????????????????????????
                    } else {
                        pixels[y * width + x] = 0xffffffff;// ????????????????????????
                    }
                }
            }
            /** 4.??????Bitmap??????,????????????????????????Bitmap???????????????????????????,?????????Bitmap?????? */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);


            iv_view.setImageBitmap(bitmap);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static BitMatrix updateBit(BitMatrix matrix, int margin){
        int tempM = margin*2;
        int[] rec = matrix.getEnclosingRectangle();   //??????????????????????????????
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // ?????????????????????????????????BitMatrix
        resMatrix.clear();
        for(int i= margin; i < resWidth- margin; i++){   //??????????????????????????????????????????bitMatrix???
            for(int j=margin; j < resHeight-margin; j++){
                if(matrix.get(i-margin + rec[0], j-margin + rec[1])){
                    resMatrix.set(i,j);
                }
            }
        }
        return resMatrix;
    }


}