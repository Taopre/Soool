package com.example.taopr.soool.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaVotePresenter;
import com.example.taopr.soool.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QnaVoteActivity extends AppCompatActivity implements View.OnClickListener, QnaVotePresenter.View {

    private static final int MY_PERMISSION_STORAGE = 1111;
    private static final int PICK_FROM_ALBUM = 1;

    ImageView iv_choice1stImage, iv_choice2ndImage;
    EditText et_choiceTitle, et_choiceContent;

    String imgPath, imgName, TAG = "QnaVoteActivity";
    static String UploadImgPath;
    private Uri mImageCaptureUri;
    String image1st, image2nd;
    boolean ivNumber = false;

    QnaVotePresenter qnaVotePresenter;
    QnaVoteItem qnaVoteItem = new QnaVoteItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnavote);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지
        checkPermission();
    }

    private void DoBinding() {
        qnaVotePresenter = new QnaVotePresenter(this, this);
        qnaVotePresenter.setView(this);

        // 뷰들 선언하는 부분입니다.
        iv_choice1stImage = findViewById(R.id.choice1stImage);
        iv_choice2ndImage = findViewById(R.id.choice2ndImage);
        et_choiceTitle = findViewById(R.id.choiceTitle);
        et_choiceContent = findViewById(R.id.choiceContent);
        // 뷰의 리스너 선언 부분입니다.
        iv_choice1stImage.setOnClickListener(this);
        iv_choice2ndImage.setOnClickListener(this);
    }

    // 권한 묻는 부분.
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
//             ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(QnaVoteActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.qna_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        ImageButton ib_drawupBack = findViewById(R.id.drawupBack);
        Button btn_drawupEnroll = findViewById(R.id.drawupEnroll);

        ib_drawupBack.setOnClickListener(this);
        btn_drawupEnroll.setOnClickListener(this);

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choice1stImage:
                //앨범 연동.
                ivNumber = true;

                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction("first");
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setNeutralButton("취소", cancelListener)
                        .setNegativeButton("앨범선택", albumListener)
                        .show();
                break;
            case R.id.choice2ndImage:
                ivNumber = false;

                DialogInterface.OnClickListener album2ndListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction("second");
                    }
                };
                DialogInterface.OnClickListener cancel2ndListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setNeutralButton("취소", cancel2ndListener)
                        .setNegativeButton("앨범선택", album2ndListener)
                        .show();
                break;
            case R.id.drawupBack:
                Toast.makeText(this, "뒤로가기 클릭", Toast.LENGTH_SHORT).show();
                //이전 액티비티로 인탠트 사용해줘야할 부분.
                break;
            case R.id.drawupEnroll:

                // 제목 / 내용 / 이미지 1, 2 예외 처리해주는 부분
                // 위의 값들이 다 있어야만 객체를 통해 모델로 값을 넘겨주어 서버로 저장할 수 있도록 하였습니다.

                Toast.makeText(this, "등록 클릭", Toast.LENGTH_SHORT).show();
                if(et_choiceTitle.getText().length() == 0) {
                    Log.d(TAG, "onClick: 제목을 입력해주세요.");
                }else if(ivNumber == true && image1st == null) {
                    Log.d(TAG, "onClick: 첫번째 이미지를 골라주세요.");
                }else if(ivNumber == false && image2nd == null) {
                    Log.d(TAG, "onClick: 두번째 이미지를 골라주세요.");
                }else if(et_choiceContent.getText().length() == 0) {
                    Log.d(TAG, "onClick: 내용을 입력해주세요.");
                }else {
                    Log.d(TAG, "onClick: " + "제목 : " + et_choiceTitle.getText().toString() + " 내용 : "
                            + et_choiceContent.getText().toString() + " 1번째 이미지 : " + image1st + " 2번째 이미지 : " + image2nd);

                    qnaVoteItem.setTitle(et_choiceTitle.getText().toString());
                    qnaVoteItem.setContent(et_choiceContent.getText().toString());
                    qnaVoteItem.setFirst_image(image1st);
                    qnaVoteItem.setSecond_image(image2nd);

                    qnaVotePresenter.enrollmentVoteReq(qnaVoteItem);
                }
                break;
        }
    }

    /**

     * 앨범에서 이미지 가져오기

     */
    public void doTakeAlbumAction(String i) // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                String name_Str = getImageNameToUri(data.getData());
                mImageCaptureUri = data.getData();
                Log.d(TAG, "onActivityResult: "+mImageCaptureUri);

                //절대경로 획득**
                Cursor c = getContentResolver().query(Uri.parse(mImageCaptureUri.toString()), null, null, null, null);
                c.moveToNext();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                Log.d(TAG, "onActivityResult: " + absolutePath);
                //이미지 데이터를 비트맵으로 받아옴
                Bitmap image_bitmap = null;
                try {
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ///리사이징
                int height = image_bitmap.getHeight();
                int width = image_bitmap.getWidth();

                Bitmap src = BitmapFactory.decodeFile(absolutePath);
                Bitmap resized = Bitmap.createScaledBitmap(src, width/4, height/4, true);

                saveBitmaptoJpeg(resized, "soool-test", name_Str);
                //리사이징

                int degree = getExifOrientation(UploadImgPath);
                Log.d(TAG, "onActivityResult: 각도 상태는? " + degree);
                Bitmap bitmap = BitmapFactory.decodeFile(UploadImgPath);
                bitmap = getRotatedBitmap(bitmap, degree);

                saveBitmaptoJpeg(bitmap, "soool-test", name_Str);

                // 현재 몇몇의 이미지들은 회전이 되어 이미지뷰에 보여지는 현상이 있음.
                // 오리엔테이션으로 값을 확인해봤지만 다들 상태가 0이어서 어떻게 처리해야할지 현재 몰라 방치해둔 상황.

                if(!UploadImgPath.equals(null)) {

                    // ivNumber를 flag값으로 임의의로 지정하여
                    // 이미지뷰를 구별하였습니다.
                    // true = 첫번째 이미지뷰
                    // false = 두번째 이미지뷰
                    // 이미지 뷰의 가로 세로 값을 정확히 정해서 glide 처리해야할 것 같습니다.

                    if (ivNumber == true) {
                        image1st = UploadImgPath;
                        UploadImgPath = null;

                        File file1 = new File(image1st);
                        Uri test1 = Uri.fromFile(file1);

                        Glide.with(this)
                                .load(test1)
                                .override(100,100)
                                .centerCrop()
                                .into(iv_choice1stImage);
                    } else {
                        image2nd = UploadImgPath;
                        UploadImgPath = null;

                        File file2 = new File(image2nd);
                        Uri test2 = Uri.fromFile(file2);

                        Glide.with(this)
                                .load(test2)
                                .override(100,100)
                                .centerCrop()
                                .into(iv_choice2ndImage);
                    }
                }

                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
            }
        }
    }

    private int getExifOrientation(String filePath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return 90;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return 180;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return 270;
                }
            }
        }

        return 0;
    }

    private Bitmap getRotatedBitmap(Bitmap bitmap, int degree) {
        if (degree != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            try {
                Bitmap tmpBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                if (bitmap != tmpBitmap) {
                    bitmap.recycle();
                    bitmap = tmpBitmap;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }


    /////   Uri 에서 파일명을 추출하는 로직
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        imgPath = cursor.getString(column_index);
        imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    //비트맵을 jpg로
    public static void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/"+folder+"/";
        String file_name = name;
        String string_path = ex_storage+foler_name;
        UploadImgPath = string_path+file_name;

        File file_path;
        try{
            file_path = new File(string_path);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
    }

    // 모델에서 서버로 부터의 응답을 받아와 처리하기 위해 만든 메서드.

    @Override
    public void enrollmentVoteRespGoToView(boolean response) {
        if (response == true) {
//            Intent intent = new Intent(this, QnaActivity.class);
//            intent.addFlags();
//            startActivity(intent);
        }else {
            Toast.makeText(this, "Vote 게시물 작성에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
