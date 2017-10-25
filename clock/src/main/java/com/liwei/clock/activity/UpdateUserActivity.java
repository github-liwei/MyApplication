package com.liwei.clock.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import com.liwei.clock.R;
import com.liwei.clock.config.LogMy;
import com.liwei.clock.config.PermissionHelper;

import java.io.*;

import static android.support.v7.app.AlertDialog.*;

public class UpdateUserActivity extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    protected static Uri tempUri;
    private PermissionHelper permissionHelper;
    private boolean b = true;
    UserInfo userInfo;

    @BindView(R.id.iv_personal_icon)
    public ImageView image;
    @BindView(R.id.et_nikename)
    public EditText nickName;
    @BindView(R.id.bt_send)
    public Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        ButterKnife.bind(this);
        b = true;
        getApplicationContext().getExternalFilesDir(null);
        permissionHelper = new PermissionHelper(this);
        permissionHelper.checkPermisson(new PermissionHelper.OnPermissionListener() {
            @Override
            public void onAgreePermission() {
                Toast.makeText(getApplication(), "权限通过", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDeniedPermission() {
                Toast.makeText(getApplication(), "权限不成功", Toast.LENGTH_LONG).show();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        userInfo = JMessageClient.getMyInfo();
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (i == 0) {
                    LogMy.i(this.getClass(), "onCreate " + ", responseCode = " + i + " ; registerDesc = " + s);
                    image.setImageBitmap(bitmap);
                } else {
                    LogMy.e(this.getClass(), "onCreate  " + ", responseCode = " + i + " ; registerDesc = " + s);
                    image.setImageResource(R.drawable.ico);
                }
            }
        });

    }

    @OnClick(R.id.iv_personal_icon)
    public void changClick() {
        Builder builder = new Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.bt_send)
    void sendClick() {
        UserInfo userInfo = JMessageClient.getMyInfo();
        userInfo.setNickname(nickName.getText().toString().trim());
        JMessageClient.updateMyInfo(UserInfo.Field.all, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    LogMy.i(this.getClass(), "JMessageClient.updateUser " + ", responseCode = " + i + " ; registerDesc = " + s);
                } else {
                    b = false;
                    LogMy.i(this.getClass(), "JMessageClient.updateUser " + ", responseCode = " + i + " ; registerDesc = " + s);
                }
            }
        });
        if (b == true) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        permissionHelper.onResume();
        super.onResume();
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            LogMy.e(this.getClass(), "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            image.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String path = Environment.getExternalStorageDirectory() + "/liwei";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            boolean bool = dirFile.mkdirs();
            LogMy.e(this.getClass(), "文件夹创建  bool = " + bool);
            b = bool;
        }
        File file = new File(path + "/updateImage.jpg");
        BufferedOutputStream bos = null;
        try {
            file.delete();
            if (!file.exists()) {
                file.createNewFile();
                LogMy.e(this.getClass(), "成功创建文件夹");
            }
            // 简单起见，先删除老文件，不管它是否存在。
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            if (file.exists()) {
                // 拿着imagePath上传了
                JMessageClient.updateUserAvatar(file, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            LogMy.e(this.getClass(), "updateUserAvatar " + ", responseCode = " + i + " ; registerDesc = " + s);
                        } else {
                            b = false;
                            Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                            LogMy.e(this.getClass(), "updateUserAvatar " + ", responseCode = " + i + " ; registerDesc = " + s);
                        }
                    }
                });
            } else {
                LogMy.e(this.getClass(), "不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (Exception e) {
                    LogMy.e(this.getClass(), "FileSave saveDrawableToFile, close error" + e);
                }
            }
        }
    }


}
