package com.example.taopr.soool.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taopr.soool.Adapter.DrawUpTagAdapter;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.QnaDrawUpPresenter;
import com.example.taopr.soool.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class QnaDrawUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, QnaDrawUpPresenter.View {

    private static final String TAG = "QnaDrawUpActivity";
    private static final int MY_PERMISSION_STORAGE = 1111;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    Spinner sp_drawupTag;
    EditText et_drawupTitle, et_drawupContent;
    ImageButton ib_drawupImagebtn;
    ImageView iv_drawupImage;
    Button btn_drawupDeleteBtn;

    ArrayList<String> tagArray = new ArrayList<>();

    private Uri mImageCaptureUri;
    private int id_view;
    String absoultePath;

    DrawUpTagAdapter drawUpTagAdapter;
    QnaDrawUpPresenter qnaDrawUpPresenter;
    QnaBoardItem qnaBoardItem = new QnaBoardItem();

    Uri image;
    String title = "", content = "", tag = "", imgPath, imgName;
    static String UploadImgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_drawup);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지
        checkPermission();

        try{

            // custom_list.xml 을 가져와 XmlPullParser 에 넣는다.
            XmlPullParser customList = getResources().getXml(R.xml.drawup_tag);

            // 파싱한 xml 이 END_DOCUMENT(종료 태그)가 나올때 까지 반복한다.
            while(customList.getEventType()!=XmlPullParser.END_DOCUMENT){
                // 태그의 첫번째 속성일 때,
                if(customList.getEventType()==XmlPullParser.START_TAG){
                    // 이름이 "custom" 일때, 첫번째 속성값을 ArrayList에 저장
                    if(customList.getName().equals("custom")){
                        tagArray.add(customList.getAttributeValue(0));
                    }

                }
                // 다음 태그로 이동
                customList.next();
            }
        }catch(XmlPullParserException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        drawUpTagAdapter = new DrawUpTagAdapter(this, tagArray);
        sp_drawupTag.setAdapter(drawUpTagAdapter);
    }

    private void DoBinding() {
        qnaDrawUpPresenter = new QnaDrawUpPresenter(this, this);
        qnaDrawUpPresenter.setView(this);

        // 뷰들 선언하는 부분입니다.
        sp_drawupTag = findViewById(R.id.drawupTag);
        et_drawupTitle =findViewById(R.id.drawupTitle);
        et_drawupContent =findViewById(R.id.drawupContent);
        ib_drawupImagebtn =findViewById(R.id.drawupImageBtn);
        iv_drawupImage =findViewById(R.id.drawupImage);
        btn_drawupDeleteBtn =findViewById(R.id.drawupDeleteBtn);

        iv_drawupImage.setVisibility(View.GONE);
        btn_drawupDeleteBtn.setVisibility(View.GONE);

        // 뷰의 리스너 선언 부분입니다.
        sp_drawupTag.setOnItemSelectedListener(this);
        ib_drawupImagebtn.setOnClickListener(this);
        btn_drawupDeleteBtn.setOnClickListener(this);
    }

    // 권한 묻는 부분.
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

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
                        Toast.makeText(QnaDrawUpActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
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
        View actionbar = inflater.inflate(R.layout.drawup_actionbar, null);

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

    //스피너 리스너 부분
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "선택한 태그 : "+tagArray.get(position), Toast.LENGTH_SHORT).show();
        tag = tagArray.get(position).toString();
        Log.d(TAG, "onItemSelected: "+tag);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawupImageBtn:
                //앨범 연동.
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 1);
//                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        doTakePhotoAction();
//                    }
//                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
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
//                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("취소", cancelListener)
                        .setNegativeButton("앨범선택", albumListener)
                        .show();
                break;
            case R.id.drawupDeleteBtn:
                Toast.makeText(this, "이미지가 삭제됩니다.", Toast.LENGTH_SHORT).show();
                iv_drawupImage.setImageBitmap(null);
                iv_drawupImage.setVisibility(View.GONE);
                btn_drawupDeleteBtn.setVisibility(View.GONE);

                UploadImgPath = null;
                qnaBoardItem.setImage(UploadImgPath);

                break;
            case R.id.drawupBack:
                Toast.makeText(this, "뒤로가기 클릭", Toast.LENGTH_SHORT).show();
                //이전 액티비티로 인탠트 사용해줘야할 부분.
                break;
            case R.id.drawupEnroll:
                Toast.makeText(this, "등록 클릭", Toast.LENGTH_SHORT).show();
                if(tag.equals(null)) {
                    Log.d(TAG, "onClick: 태그 값을 선택해주세요.");
                }else if(et_drawupTitle.getText().length() == 0) {
                    Log.d(TAG, "onClick: 제목을 입력해주세요.");
                }else if(et_drawupContent.getText().length() == 0) {
                    Log.d(TAG, "onClick: 내용을 입력해주세요.");
                }else if(UploadImgPath == null) {
                    Log.d(TAG, "enroll onClick: " + "태그 : " + tag + " 제목 : "
                            + et_drawupTitle.getText().toString() + " 내용 : " + et_drawupContent.getText().toString());

                    qnaBoardItem.setTag(tag);
                    qnaBoardItem.setTitle(et_drawupTitle.getText().toString());
                    qnaBoardItem.setContent(et_drawupContent.getText().toString());

                    qnaDrawUpPresenter.enrollmentReq(qnaBoardItem);
                }else {
                    Log.d(TAG, "enroll onClick: " + "태그 : " + tag + " 제목 : "
                            + et_drawupTitle.getText().toString() + " 내용 : " + et_drawupContent.getText().toString() + " 이미지 : " + UploadImgPath);

                    qnaBoardItem.setTag(tag);
                    qnaBoardItem.setTitle(et_drawupTitle.getText().toString());
                    qnaBoardItem.setContent(et_drawupContent.getText().toString());
                    qnaBoardItem.setImage(UploadImgPath);

                    qnaDrawUpPresenter.enrollmentReq(qnaBoardItem);
                }
                break;
        }
    }
//    /**
//
//     * 카메라에서 사진 촬영
//
//     */
//    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
//    {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // 임시로 사용할 파일의 경로를 생성
//        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//        startActivityForResult(intent, PICK_FROM_CAMERA);
//    }
    /**

     * 앨범에서 이미지 가져오기

     */
    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
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
                image = mImageCaptureUri;

                //절대경로 획득**
                Cursor c = getContentResolver().query(Uri.parse(mImageCaptureUri.toString()), null, null, null, null);
                c.moveToNext();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

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
                Bitmap resized = Bitmap.createScaledBitmap( src, width/4, height/4, true );

                saveBitmaptoJpeg(resized, "soool", name_Str);
                ////리사이징

                iv_drawupImage.setVisibility(View.VISIBLE);
                btn_drawupDeleteBtn.setVisibility(View.VISIBLE);
//                iv_drawupImage.setImageURI(mImageCaptureUri);

                if(!UploadImgPath.equals(null)) {
                    File file = new File(UploadImgPath);
                    Uri test = Uri.fromFile(file);
                    iv_drawupImage.setImageURI(test);
                }

                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                // CROP할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기
                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기
                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동

                break;
            }
            case CROP_FROM_iMAGE:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                if(resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();
                // CROP된 이미지를 저장하기 위한 FILE 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                        "/SmartWheel/"+System.currentTimeMillis()+".jpg";

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP
                    iv_drawupImage.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌
                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                    absoultePath = filePath;
                    break;
                }
                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());

                if(f.exists())
                {
                    f.delete();
                }
            }
        }
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

    /*

     * Bitmap을 저장하는 부분

     */
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

