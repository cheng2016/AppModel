package com.uniaip.android.home.invoice.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.uniaip.android.R;
import com.uniaip.android.root.NoticeActivity;
import com.uniaip.android.root.SHDApplication;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.home.invoice.models.CheckInfo;
import com.uniaip.android.home.invoice.models.ConsumeTypeInfo;
import com.uniaip.android.utils.Contanls;
import com.uniaip.android.utils.EB;
import com.uniaip.android.utils.SP;
import com.uniaip.android.utils.TextUtil;


import net.bither.util.NativeUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 上传小票
 * 作者: ysc
 * 时间: 2016/12/28
 */
public class UploadInvoiceActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE_SELECT = 100;//相机
    private File fileImage;
    @BindView(R.id.tv_main_title_right)
    TextView butiejilu;//补贴记录
    @BindView(R.id.tv_view_button)
    TextView mTvButton;//上传支付凭证
    @BindView(R.id.img_help_msg)
    ImageView help_msg; //须知
    @BindView(R.id.tv_inv_place)
    TextView mTvPlace;//地点
    @BindView(R.id.et_inv_business)
    EditText mEtBusiness;//商家
    @BindView(R.id.tv_inv_type)
    TextView mTvType;//消费类型
    @BindView(R.id.tv_inv_con)
    EditText mEtCon;//消费金额
    @BindView(R.id.tv_inv_date)
    TextView mTvDate;//消费日期
    @BindView(R.id.tv_inv_integral)
    TextView mTvIntegral;//应得积分
    @BindView(R.id.tv_inv_bank_card)
    TextView mTvCard;//消费银行卡
    @BindView(R.id.tv_inv_num)
    TextView mTvNum;//可上传次数
    @BindView(R.id.lay_inv_imgs)
    LinearLayout mLayImgs; //多张图片展示
    @BindView(R.id.iv_inv_img1)
    ImageView mIvImg1;
    @BindView(R.id.iv_inv_img2)
    ImageView mIvImg2;
    @BindView(R.id.iv_inv_img3)
    ImageView mIvImg3;
    @BindView(R.id.iv_inv_img_x1)
    ImageView mIvx1;
    @BindView(R.id.iv_inv_img_x2)
    ImageView mIvx2;
    @BindView(R.id.iv_inv_img_x3)
    ImageView mIvx3;
    @BindView(R.id.iv_inv_add1)
    TextView mIvAdd1;
    @BindView(R.id.iv_inv_add2)
    TextView mIvAdd2;
    @BindView(R.id.iv_inv_add3)
    TextView mIvAdd3;

    private int timeType = 0;//0:今天 1:昨天
    private OptionsPickerView pvDate;//选择今天昨天
    private OptionsPickerView pvOptions;
    private ArrayList<String> items;//分类信息
    private ArrayList<String> mLtDate;//日期
    private int typeID = 2;//分类信息ID
    private BitmapFactory.Options options;
    private ArrayList<String> mLtImages = new ArrayList<>();//图片
    private List<ConsumeTypeInfo> mLtCon;//消费类型
    private String strImgs = "";//保存图片地址
    private int uploadNum = 0;//记录操作次数
    private CheckInfo info;
    // 构造上传请求
    private OSSCredentialProvider credentialProvider;
    private OSS oss;
    private boolean isSuccess = true;//记录图片上传是否成功
    private int maxImgCount = 3;
    private final SP sp = new SP();
    @Override
    protected int getLayoutID() {
        return R.layout.activity_upload_invoice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        getListener();
        initData();
        initoss();
    }


    /**
     * 初始化上传所需参数
     */
    private void initoss() {
        credentialProvider = new OSSPlainTextAKSKCredentialProvider(Contanls.accessKeyId, Contanls.accessKeySecret);
        oss = new OSSClient(getApplicationContext(), Contanls.endpoint, credentialProvider);
    }

    /**
     * 上传图片
     */
    private void uploadImage(String str) {
        String objectKey = "xiaopiao/android/" + sp.load(Contanls.UID,"-1") + "/bill/" + TextUtil.getImgName() + ".png";
        strImgs = strImgs + "," + objectKey;
        PutObjectRequest put = new PutObjectRequest(Contanls.bucketName, objectKey, str);
        // 指定数据类型，没有指定会自动根据后缀名判断
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentType("image/png");
        // 构造上传请求
        put.setMetadata(objectMeta);
        try {
            PutObjectResult putResult = oss.putObject(put);
            Log.e("PutObject", "--------上传-----------UploadSuccess");
            Log.e("ETag", putResult.getETag());
            Log.e("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            Log.e("错误>>", "本地网络异常");// 本地异常如网络异常等
            uploadNum = 0;
            mHandler.sendEmptyMessage(1099);
            isSuccess = false;
            e.printStackTrace();
        } catch (ServiceException e) {
            isSuccess = false;
            uploadNum = 0;
            mHandler.sendEmptyMessage(1099);
            Log.e("错误>>", "服务器异常");//服务器异常
        }
        put.setProgressCallback((request, currentSize, totalSize) -> {
            if (currentSize == totalSize)
                Log.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize + "|uploadFilePath:" + request.getUploadFilePath());
        });


        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                uploadNum++;
                if (!isSuccess) {
                    uploadNum = 0;
                    mHandler.sendEmptyMessage(1099);
                } else if (uploadNum == mLtImages.size() && isSuccess) {
                    uploadNum = 0;
                    Message message = new Message();
                    message.obj = strImgs;
                    message.what = 1001;
                    mHandler.sendMessage(message);
                }
                Log.e(">>>", "成功次数" + uploadNum);
                Log.e("PutObject", "UploadSuccess");
                Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {

                if (clientExcepion != null) { // 请求异常
                    Log.e(">>>", "请求异常");
                    uploadNum = 0;
                    mHandler.sendEmptyMessage(1099);
                    isSuccess = false;
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }

                if (serviceException != null) { //服务异常

                    uploadNum = 0;
                    mHandler.sendEmptyMessage(1099);
                    isSuccess = false;

                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EB.unregister(this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.text5));
        mTvButton.setText(getString(R.string.text3));
        pvOptions = new OptionsPickerView(UploadInvoiceActivity.this);
        pvDate = new OptionsPickerView(UploadInvoiceActivity.this);
        mEtCon.setSelection(mEtCon.getText().length());
        mTvCard.setText(sp.load(Contanls.BANKCARD,"-"));

        ViewGroup.LayoutParams mPar1 = mLayImgs.getLayoutParams();
        mPar1.height = (int) (SHDApplication.mManager.getDefaultDisplay().getWidth() / 2.5);
        mLayImgs.setLayoutParams(mPar1);
        showButton();
        fileImage = SHDApplication.fileImage;

    }


    public void goMap() {
        startActivityForResult(new Intent(mContext, MapActivity.class), 1101);
    }


    /**
     * 初始化监听
     */
    private void getListener() {
        mTvButton.setOnClickListener(this);
        mTvButton.setClickable(false);
        mTvButton.setTextColor(getResources().getColor(R.color.text_color19));
        mTvPlace.setOnClickListener(this);
        mTvType.setOnClickListener(this);
        mIvx1.setOnClickListener(this);
        mIvx2.setOnClickListener(this);
        mIvx3.setOnClickListener(this);
        butiejilu.setOnClickListener(this);
        mIvImg1.setOnClickListener(this);
        mIvImg2.setOnClickListener(this);
        mIvImg3.setOnClickListener(this);
        mTvDate.setOnClickListener(this);
        help_msg.setOnClickListener(this);
        pvDate.setOnoptionsSelectListener((options1, option2, options3) -> {
            timeType = options1;
            mTvDate.setText(mLtDate.get(options1));
        });

        pvOptions.setOnoptionsSelectListener((options1, option2, options3) -> {
            if (null != mLtCon && mLtCon.size() > 0) {
                typeID = mLtCon.get(options1).getType();
                mEtCon.setHint("金额范围(" + mLtCon.get(options1).getLeast() + "-" + mLtCon.get(options1).getMaximum() + ")");

                if (Double.parseDouble(mEtCon.getText().toString().equals("") ? "0" : mEtCon.getText().toString()) > Double.parseDouble(mLtCon.get(options1).getMaximum())) {
                    mEtCon.setText(mLtCon.get(options1).getMaximum());
                }
            } else {
                typeID = options1 + 1;
            }
            mTvType.setText(items.get(options1));
            showButton();
        });
        mEtCon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtCon.getText().toString().length() > 0) {
                    if (TextUtils.equals(mEtCon.getText().toString(), ".")) {
                        mEtCon.setText("0.");
                        mEtCon.setSelection(mEtCon.getText().length());
                    }
                    double moeny = Double.parseDouble(mEtCon.getText().toString());
                    if (moeny >= 1) {
                        double maximum = 3000;
                        if (null != mLtCon && mLtCon.size() > 0) {
                            maximum = Double.parseDouble(mLtCon.get(typeID - 1).getMaximum());
                        } else {
                            maximum = 3000;
                        }
                        if (moeny > maximum) {
                            mEtCon.setText(maximum+"");
                            mEtCon.setSelection(mEtCon.getText().length());
                            toast(getString(R.string.common_text88) + mLtCon.get(typeID - 1).getMaximum());
                        }
                        getsSubsidy(moeny > Double.parseDouble(mLtCon.get(typeID - 1).getMaximum()) ? Double.parseDouble(mLtCon.get(typeID - 1).getMaximum()) : moeny);
                    } else {
                        mTvIntegral.setText("");
                    }
                } else {
                    mTvIntegral.setText("");
                }
                showButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mTvPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEtBusiness.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * 显示完成按钮
     */
    private void showButton() {
        if (mTvPlace.getText().toString().length() == 0 || mEtBusiness.getText().toString().length() == 0 || mTvType.getText().toString().length() == 0 || mEtCon.getText().toString().length() == 0 || mLtImages.size() == 0) {
            mTvButton.setBackgroundResource(R.drawable.click_gray_bg);
            mTvButton.setClickable(false);
            mTvButton.setTextColor(getResources().getColor(R.color.text_color19));
        } else {
            mTvButton.setClickable(true);
            mTvButton.setBackgroundResource(R.drawable.click_determine_bg);
            mTvButton.setTextColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * 计算补贴
     *
     * @param moeny
     */
    private void getsSubsidy(double moeny) {
        double subsidy = moeny * 0.12;//总补贴
        mTvIntegral.setText(new BigDecimal(subsidy).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        items = new ArrayList<>();
        mLtCon = new ArrayList<>();

//        1餐饮/娱乐2日常消费品3交通/旅游4住宿5美容/保健
            items.add("餐饮/娱乐");
            items.add("日常消费品");
            items.add("交通/旅游");
            items.add("住宿");
            items.add("美容/保健");

            mLtCon.add(new ConsumeTypeInfo("24857530593181702", 1, "餐饮/娱乐", "0", "3000"));
            mLtCon.add(new ConsumeTypeInfo("24857530593181703", 2, "日常消费品", "0", "3000"));
            mLtCon.add(new ConsumeTypeInfo("24857530593181704", 3, "交通/旅游", "0", "3000"));
            mLtCon.add(new ConsumeTypeInfo("24857530593181705", 4, "住宿", "0", "3000"));
            mLtCon.add(new ConsumeTypeInfo("24857530593181706", 5, "美容/保健", "0", "3000"));
            pvOptions.setPicker(items, null, null, false);
            pvOptions.setCyclic(false, false, false);
            pvOptions.setSelectOptions(0, 0, 0);

        mLtDate = new ArrayList<>();
        mLtDate.add("今天");
        mLtDate.add("昨天");
        pvDate.setPicker(mLtDate, null, null, false);
        pvDate.setCyclic(false, false, false);
        pvDate.setSelectOptions(0, 0, 0);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_view_button://上传小票
//                if (UniaipApplication.user.getVoucherleft() <= 0) {
//                    toast(getString(R.string.common_text91));
//                } else {
//                    if (testingData()) {
//                        compressImage();
//                    }
//                }
                break;
            case R.id.tv_main_title_right:
                startActivity(new Intent(UploadInvoiceActivity.this,InvoiceRecordActivity.class));
                break;
            case R.id.img_help_msg://须知
                intent = new Intent(UploadInvoiceActivity.this, NoticeActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.tv_inv_place://地点
                startActivityForResult(new Intent(UploadInvoiceActivity.this, MapActivity.class), 1101);
                break;
            case R.id.tv_inv_type://消费类型
                pvOptions.show();
                ((InputMethodManager) UploadInvoiceActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(UploadInvoiceActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.iv_inv_img_x1:
                if (mLtImages.size() >= 1)
                    mLtImages.remove(0);
                showImages(mLtImages);
                break;
            case R.id.iv_inv_img_x2:
                if (mLtImages.size() >= 2)
                    mLtImages.remove(1);
                showImages(mLtImages);
                break;
            case R.id.iv_inv_img_x3:
                if (mLtImages.size() >= 3)
                    mLtImages.remove(2);
                showImages(mLtImages);
                break;
            case R.id.iv_inv_img1:
                if (mLtImages.size() < 1) {
                    getPower();
//                    getAlbum();
                } else {
//                    intent = new Intent(UploadInvoiceActivity.this, ShowImageActivity.class);
//                    intent.putStringArrayListExtra("images", mLtImages);
//                    intent.putExtra("num", 0);
//                    startActivity(intent);
                }
                break;
            case R.id.iv_inv_img2:
                if (mLtImages.size() < 2) {
                    getPower();
//                    getAlbum();
                } else {
//                    intent = new Intent(UploadInvoiceActivity.this, ShowImageActivity.class);
//                    intent.putStringArrayListExtra("images", mLtImages);
//                    intent.putExtra("num", 1);
//                    startActivity(intent);
                }
                break;
            case R.id.iv_inv_img3:
                if (mLtImages.size() < 3) {
                    getPower();
//                    getAlbum();
                } else {
//                    intent = new Intent(UploadInvoiceActivity.this, ShowImageActivity.class);
//                    intent.putStringArrayListExtra("images", mLtImages);
//                    intent.putExtra("num", 2);
//                    startActivity(intent);
                }
                break;
            case R.id.tv_inv_date://选择消费日期
                pvDate.show();
                ((InputMethodManager) UploadInvoiceActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(UploadInvoiceActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    /**
     * 压缩图片
     */
    private void compressImage() {
        showProgress();
        uploadNum = 0;

        if (!fileImage.exists()) {
            fileImage.mkdirs();
        }
        for (int i = 0; i < mLtImages.size(); i++) {
            final int num = i;
            new Thread() {
                public void run() {
                    File fileOutput = new File(fileImage, TextUtil.getImgName() + ".png");
                    NativeUtil.compressBitmap(mLtImages.get(num), fileOutput.getPath());
                    mLtImages.set(num, fileOutput.getPath());
                    Message message = new Message();
                    message.what = 1002;
                    message.arg1 = mLtImages.size();
                    mHandler.sendMessage(message);
                }
            }.start();
        }
    }

    private void uploadImg() {
        strImgs = "";//清空之前保存的地址
        uploadNum = 0;
        for (int i = 0; i < mLtImages.size(); i++) {
            int position = i;
            new Handler().postDelayed(() -> uploadImage(mLtImages.get(position)), 300);
        }
    }

//    /**
//     * 上传小票
//     */
//    private void getUploadvoucher(String obj) {
//        StringBuffer sb = new StringBuffer(obj);
//        obj = sb.subSequence(1, obj.length()).toString();
//        rxDestroy(UniaipAPI.uploadvoucher(UniaipApplication.user.getToken(), UniaipApplication.user.getId(),
//                mTvPlace.getText().toString(), info.getCity(),
//                info.getLongitude() + "," + info.getLatitude(),
//                mEtBusiness.getText().toString(), typeID + "", mEtCon.getText().toString(),
//                timeType + "", UniaipApplication.user.getCardid(), obj)).subscribe(subsidyDetailModel -> {
//            try {
//                if (subsidyDetailModel.isOK()) {
//                    if (null != subsidyDetailModel.getData().getId() && !TextUtils.equals(subsidyDetailModel.getData().getId(), "")) {
//                        Intent intent = new Intent(UploadInvoiceFragment.this.getContext(), SubsidyDetailsActivity.class);
//                        intent.putExtra("id", subsidyDetailModel.getData().getId());
//                        intent.putExtra("type", 1);
//                        startActivity(intent);
//                    }
//                    clearData();
//                    UtilsAll.deleteImages(fileImage);
//                } else if (TextUtils.equals(subsidyDetailModel.getCode(), "10002")) {
//                    EB.post(new BaseModel(14));
//                }
//                toast(subsidyDetailModel.getMsg());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            dismissProgress();
//        }, e -> {
//            toast(getString(R.string.error_text));
//            dismissProgress();
//        });
//    }


//    /**
//     * 上传完成后清除数据
//     */
//    private void clearData() {
//        typeID = 2;
//        mTvPlace.setText("");
//        mEtBusiness.setText("");
//        mTvType.setText("");
//        mEtCon.setText("");
////        mTvSubsidy.setText("");
//        mTvIntegral.setText("");
//        if (UniaipApplication.user.getVoucherleft() >= 1)
//            UniaipApplication.user.setVoucherleft(UniaipApplication.user.getVoucherleft() - 1);
//        mTvNum.setText(UniaipApplication.user.getVoucherleft() + "");
//
//        if (UniaipApplication.user.getVoucherleft() <= 0) {
//            mTvButton.setBackgroundResource(R.drawable.bg_button12);
//        }
//        mLtImages.clear();
//        showImages(mLtImages);
//    }


    /**
     * 检测数据是否完整
     *
     * @return
     */
    private boolean testingData() {
        if (mTvPlace.getText().length() < 1) {
            toast(getString(R.string.common_text79));
            return false;
        }

        if (mEtBusiness.getText().length() < 1) {
            toast(getString(R.string.common_text80));
            return false;
        }
        if (mTvType.getText().length() < 1) {//消费类型
            toast(getString(R.string.common_text89));
            return false;
        }

        if (Double.parseDouble(mEtCon.getText().toString().equals("") ? "0" : mEtCon.getText().toString()) < 0) {
            toast(getString(R.string.common_text92));
            return false;
        }
        if (mLtImages.size() < 1) {
            toast(getString(R.string.common_text98));
            return false;
        }
        return true;
    }


    /**
     * 相册
     */
    public void getAlbum() {
        int checkLocalSDPermission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkLocalSDPermission == PackageManager.PERMISSION_GRANTED) {//有SD卡权限就跳转到相册，没有就申请
            ImagePicker.getInstance().setCrop(false);//裁剪
            ImagePicker.getInstance().setSelectLimit(maxImgCount - mLtImages.size());
            Intent intent = new Intent(UploadInvoiceActivity.this, ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT);
        } else {
            ActivityCompat.requestPermissions(((Activity) mContext), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x13);
            return;
        }
    }

    /**
     * TODO
     * 获取权限
     */
    private void getPower() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            EB.post(new BaseModel(7));
        } else {
            getAlbum();
        }
    }

    private void showImages(List<String> img) {
        showButton();
        mIvImg1.setImageResource(0);
        mIvImg2.setImageResource(0);
        mIvImg3.setImageResource(0);
        for (int i = 0; i < img.size(); i++) {
            if (i == 0) {
                Glide.with(UploadInvoiceActivity.this).load(img.get(i)).into(mIvImg1);
            } else if (i == 1) {
                Glide.with(UploadInvoiceActivity.this).load(img.get(i)).into(mIvImg2);
            } else if (i == 2) {
                Glide.with(UploadInvoiceActivity.this).load(img.get(i)).into(mIvImg3);
            }
        }

        switch (img.size()) {
            case 1:
                mIvx1.setVisibility(View.VISIBLE);
                mIvx2.setVisibility(View.GONE);
                mIvx3.setVisibility(View.GONE);
                mIvAdd1.setVisibility(View.GONE);
                mIvAdd2.setVisibility(View.VISIBLE);
                mIvAdd3.setVisibility(View.VISIBLE);
                break;
            case 2:
                mIvx1.setVisibility(View.VISIBLE);
                mIvx2.setVisibility(View.VISIBLE);
                mIvx3.setVisibility(View.GONE);
                mIvAdd1.setVisibility(View.GONE);
                mIvAdd2.setVisibility(View.GONE);
                mIvAdd3.setVisibility(View.VISIBLE);
                break;
            case 3:
                mIvx1.setVisibility(View.VISIBLE);
                mIvx2.setVisibility(View.VISIBLE);
                mIvx3.setVisibility(View.VISIBLE);
                mIvAdd1.setVisibility(View.GONE);
                mIvAdd2.setVisibility(View.GONE);
                mIvAdd3.setVisibility(View.GONE);
                break;
            case 0:
//                mLayImgs.setVisibility(View.GONE);
//                mLayButton.setVisibility(View.VISIBLE);
                mIvx1.setVisibility(View.GONE);
                mIvx2.setVisibility(View.GONE);
                mIvx3.setVisibility(View.GONE);
                mIvAdd1.setVisibility(View.VISIBLE);
                mIvAdd2.setVisibility(View.VISIBLE);
                mIvAdd3.setVisibility(View.VISIBLE);
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isSuccess = true;
            switch (msg.what) {
                case 1001://上传小票
//                    getUploadvoucher(msg.obj.toString());
                    break;
                case 1002://获取压缩后的图片
                    uploadNum++;
                    if (uploadNum == msg.arg1) {
                        showImages(mLtImages);
                        uploadImg();
                    }
                    break;
                case 1099:
                    toast("由于网络问题导致上传失败，请尝试再次上传。");
//                    task.cancel();
                    dismissProgress();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == options) {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
        }
        switch (requestCode) {
            case REQUEST_CODE_SELECT://相册
                if (null == data)
                    return;
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<String> img = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    img.add(images.get(i).path);
                }
                mLtImages.addAll(img);
                showImages(mLtImages);
                break;
            case 1101:
                if (null == data)
                    return;
                info = (CheckInfo) data.getSerializableExtra("address");
                mTvPlace.setText(info.getAddress());
                mEtBusiness.setText(info.getName());
                break;
        }
    }

}
