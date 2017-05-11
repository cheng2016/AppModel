package com.uniaip.android.home.invoice.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.home.invoice.models.CheckInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 补贴详情
 * 作者: ysc
 * 时间: 2017/1/3
 */

public class SubsidyDetailsActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE_SELECT = 100;//相机
    private final File fileImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/uniaip");
    @BindView(R.id.tv_main_title_right)
    TextView mTvRight;//上传
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
    @BindView(R.id.rlay_inv_img1)
    RelativeLayout mRlayImg1;
    @BindView(R.id.rlay_inv_img2)
    RelativeLayout mRlayImg2;
    @BindView(R.id.rlay_inv_img3)
    RelativeLayout mRlayImg3;

    private final int maxImgCount = 3;
    private BitmapFactory.Options options;
    private CheckInfo info;
    private OptionsPickerView pvOptions;

    private int typeID = 2;//分类信息ID
    private ArrayList<String> items;//分类信息
//    private SubsidyInfo mSubsidyInfo;
//    private OSSCredentialProvider credentialProvider;
//    private OSS oss;
    private boolean isSuccess = true;//记录图片上传是否成功
    private ArrayList<String> mLtImages; //图片
    private List<String> mImgs;//可上传图片
    private int uploadNum;//记录上传次数
    private int type;//0:列表进入 1:报销页进入

    @Override
    protected int getLayoutID() {
        return R.layout.activity_upload_invoice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        getListener();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.common_text39));
        mTvRight.setVisibility(View.GONE);
        findViewById(R.id.lay_inv_upload_num).setVisibility(View.GONE);
        findViewById(R.id.in_inv_button).setVisibility(View.GONE);


        ViewGroup.LayoutParams mPar1 = mLayImgs.getLayoutParams();
//        mPar1.height = (int) (UniaipApplication.mManager.getDefaultDisplay().getWidth() / 2.5);
        mLayImgs.setLayoutParams(mPar1);

        pvOptions = new OptionsPickerView(this);
    }

    private void getListener() {
        mIvx1.setOnClickListener(this);
        mIvx2.setOnClickListener(this);
        mIvx3.setOnClickListener(this);
        mIvImg1.setOnClickListener(this);
        mIvImg2.setOnClickListener(this);
        mIvImg3.setOnClickListener(this);
        mTvType.setOnClickListener(this);
        mTvPlace.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
//                if (null == UniaipApplication.consumeData) {
//                    typeID = options1 + 1;
//                } else {
//                    typeID = UniaipApplication.consumeData.getConsumes().get(options1).getType();
//                    mEtCon.setHint("金额范围(" + UniaipApplication.consumeData.getConsumes().get(options1).getLeast() + "-" + UniaipApplication.consumeData.getConsumes().get(options1).getMaximum() + ")");
//
//                    if (Double.parseDouble(mEtCon.getText().toString().equals("") ? "0" : mEtCon.getText().toString()) > Double.parseDouble(UniaipApplication.consumeData.getConsumes().get(options1).getMaximum())) {
//                        mEtCon.setText(UniaipApplication.consumeData.getConsumes().get(options1).getMaximum());
//                    }
//                }
                mTvType.setText(items.get(options1));
            }
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
//                    if (moeny >= 20) {
//
//                        if (moeny > Double.parseDouble(UniaipApplication.consumeData.getConsumes().get(typeID - 1).getMaximum())) {
//                            toast(getString(R.string.common_text88) + UniaipApplication.consumeData.getConsumes().get(typeID - 1).getMaximum());
//                            mEtCon.setText(UniaipApplication.consumeData.getConsumes().get(typeID - 1).getMaximum());
//                            mEtCon.setSelection(mEtCon.getText().length());
//                        }
//                        getsSubsidy(moeny > Double.parseDouble(UniaipApplication.consumeData.getConsumes().get(typeID - 1).getMaximum()) ? Double.parseDouble(UniaipApplication.consumeData.getConsumes().get(typeID - 1).getMaximum()) : moeny);
//                    } else {
//                        mTvIntegral.setText("");
//                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
//        String vouId = getIntent().getStringExtra("id");
//        type = getIntent().getIntExtra("type", 0);
//        getVoucherdetail(vouId);
//
//        items = new ArrayList<>();
//        if (UniaipApplication.consumeData != null) {
//            for (int i = 0; i < UniaipApplication.consumeData.getConsumes().size(); i++) {
//                items.add(UniaipApplication.consumeData.getConsumes().get(i).getName());
//            }
//        } else {
//            //1餐饮/娱乐2日常消费品3交通/旅游4住宿5美容/保健
//            items.add("餐饮/娱乐");
//            items.add("日常消费品");
//            items.add("交通/旅游");
//            items.add("住宿");
//            items.add("美容/保健");
//        }
//        pvOptions.setPicker(items, null, null, false);
//        pvOptions.setCyclic(false, false, false);
//        pvOptions.setSelectOptions(0, 0, 0);
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

//    /**
//     * 初始化上传所需参数
//     */
//    private void initoss() {
//        if (null == credentialProvider) {
//            credentialProvider = new OSSPlainTextAKSKCredentialProvider(Contanls.accessKeyId, Contanls.accessKeySecret);
//            oss = new OSSClient(UniaipApplication.AppCtx, Contanls.endpoint, credentialProvider);
//        }
//    }
//
//    /**
//     * TODO 上传图片
//     */
//    private void uploadImage(String str,final int position) {
//        String objectKey = "xiaopiao/android/" + UniaipApplication.user.getId() + "/bill/" + TextUtil.getImgName() + ".png";
//        PutObjectRequest put = new PutObjectRequest(Contanls.bucketName, objectKey, str);
//        mImgs.set(position,objectKey);//记录上传图片位置
//        // 指定数据类型，没有指定会自动根据后缀名判断
//        ObjectMetadata objectMeta = new ObjectMetadata();
//        objectMeta.setContentType("image/png");
//        // 构造上传请求
//        put.setMetadata(objectMeta);
//
//        try {
//            PutObjectResult putResult = oss.putObject(put);
//            Log.e("PutObject", "--------上传-----------UploadSuccess");
//            Log.e("ETag", putResult.getETag());
//            Log.e("RequestId", putResult.getRequestId());
//        } catch (ClientException e) {
//            Log.e("错误>>", "本地网络异常");// 本地异常如网络异常等
//            uploadNum = 0;
//            mHandler.sendEmptyMessage(1099);
//            isSuccess = false;
//            e.printStackTrace();
//        } catch (ServiceException e) {
//            isSuccess = false;
//            uploadNum = 0;
//            mHandler.sendEmptyMessage(1099);
//            Log.e("错误>>", "服务器异常");//服务器异常
//        }
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                if (currentSize == totalSize)
//                    Log.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize + "|uploadFilePath:" + request.getUploadFilePath());
//            }
//        });
//
//
//        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                uploadNum++;
//                if (!isSuccess) {
//                    uploadNum = 0;
//                    mHandler.sendEmptyMessage(1099);
//                } else if (uploadNum == mImgs.size() && isSuccess) {
//                    uploadNum = 0;
//                    for (int i = mLtImages.size() - 1; i >= 0; i--) {
//                        if (!mLtImages.get(i).contains("http://")) {
//                            mLtImages.remove(i);
//                        }
//                    }
//                    mLtImages.addAll(mImgs);//获取位置
//                    mHandler.sendEmptyMessage(1001);
//                }
//                Log.e(">>>", "成功次数" + uploadNum);
//                Log.e("PutObject", "UploadSuccess");
//                Log.e("ETag", result.getETag());
//                Log.e("RequestId", result.getRequestId());
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//
//                if (clientExcepion != null) { // 请求异常
//                    Log.e(">>>", "请求异常");
//                    uploadNum = 0;
//                    mHandler.sendEmptyMessage(1099);
//                    isSuccess = false;
//                    // 本地异常如网络异常等
//                    clientExcepion.printStackTrace();
//                }
//
//                if (serviceException != null) { //服务异常
//
//                    uploadNum = 0;
//                    mHandler.sendEmptyMessage(1099);
//                    isSuccess = false;
//
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//            }
//        });
//    }
//
//    private void showData(SubsidyInfo info) {
//        mEtBusiness.setText(info.getBusinessname());
//        mTvPlace.setText(info.getAddress());//消费地点
//        mEtCon.setText(info.getMoney() + "");//消费金额
//        mTvDate.setText(TextUtil.getStrTime(info.getCreatetime() + ""));//消费时间
//        mTvCard.setText(info.getBankcard());//消费银行卡
//        mTvIntegral.setText(info.getPoints() + "");//积分
//        if (info.getStatus() == -1) { //未通过
//            mEtState.setText(getString(R.string.common_text45));
//            mEtState.setTextColor(getResources().getColor(R.color.text_color9));
//            mLayReas.setVisibility(View.VISIBLE);
//            mTvReason.setText(info.getObjection());
//        } else if (info.getStatus() == 0 || info.getStatus() == 1) { //审核中、通过
//            mEtState.setText(getString(R.string.common_text97));
//            mEtState.setTextColor(getResources().getColor(R.color.text_color10));
//            mLayReas.setVisibility(View.GONE);
//        } else if (info.getStatus() == 2) { //到账
//            mEtState.setText(getString(R.string.common_text17));
//            mEtState.setTextColor(getResources().getColor(R.color.text_color10));
//            mLayReas.setVisibility(View.GONE);
//        } else if (info.getStatus() == -2) { //待提交
//            mEtState.setText(getString(R.string.common_text104));
//            mEtState.setTextColor(getResources().getColor(R.color.text_color10));
//            mLayReas.setVisibility(View.GONE);
//        }
//        if (TextUtils.equals(info.getConsumetype(), "1")) {
//            mTvType.setText("餐饮/娱乐");
//        } else if (TextUtils.equals(info.getConsumetype(), "2")) {
//            mTvType.setText("日常消费品");
//        } else if (TextUtils.equals(info.getConsumetype(), "3")) {
//            mTvType.setText("交通/旅游");
//        } else if (TextUtils.equals(info.getConsumetype(), "4")) {
//            mTvType.setText("住宿");
//        } else if (TextUtils.equals(info.getConsumetype(), "5")) {
//            mTvType.setText("美容/保健");
//        }
//        typeID = Integer.parseInt(info.getConsumetype());
//        StringTokenizer token = new StringTokenizer(info.getImgs(), ",");
//        mLtImages = new ArrayList<>();
//        while (token.hasMoreElements()) {
//            mLtImages.add(token.nextToken());
//        }
//        showImages(mLtImages);
//        showEdit(info.getStatus() == -2);
//    }
//
//    /**
//     * 提交状态显示可编辑
//     *
//     * @param isSubmit true:编辑
//     */
//    private void showEdit(boolean isSubmit) {
//        if (isSubmit) {//可编辑
//            mTvRight.setVisibility(View.VISIBLE);
//        } else {//不可编辑
//            mIvx1.setVisibility(View.GONE);
//            mIvx2.setVisibility(View.GONE);
//            mIvx3.setVisibility(View.GONE);
//            mTvPlace.setClickable(false);
//            mEtBusiness.setFocusable(false);
//
//            mTvType.setClickable(false);
//            mEtCon.setFocusable(false);
//
//        }
//    }
//
//    @Override
    public void onClick(View v) {
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.tv_main_title_right://上传
//                if (testingData()) {
//                    if (isImage() == mLtImages.size()) {
//                        getEditvoucher(mLtImages);
//                    } else {
//                        compressImage();
//                    }
//                }
//                break;
//            case R.id.tv_inv_place://消费地点
//                startActivityForResult(new Intent(SubsidyDetailsActivity.this, MapActivity.class), 1101);
//                break;
//            case R.id.tv_inv_type://消费类型
//                pvOptions.show();
//                ((InputMethodManager) SubsidyDetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SubsidyDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                break;
//            case R.id.iv_inv_img_x1:
//                if (mLtImages.size() >= 1)
//                    mLtImages.remove(0);
//                showImages(mLtImages);
//                break;
//            case R.id.iv_inv_img_x2:
//                if (mLtImages.size() >= 2)
//                    mLtImages.remove(1);
//                showImages(mLtImages);
//                break;
//            case R.id.iv_inv_img_x3:
//                if (mLtImages.size() >= 3)
//                    mLtImages.remove(2);
//                showImages(mLtImages);
//                break;
//            case R.id.iv_inv_img1:
//                if (mLtImages.size() < 1) {
//                    getAlbum();
//                } else {
//                    intent = new Intent(SubsidyDetailsActivity.this, ShowImageActivity.class);
//                    intent.putStringArrayListExtra("images", mLtImages);
//                    intent.putExtra("num", 0);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.iv_inv_img2:
//                if (mLtImages.size() < 2) {
//                    getAlbum();
//                } else {
//                    intent = new Intent(SubsidyDetailsActivity.this, ShowImageActivity.class);
//                    intent.putStringArrayListExtra("images", mLtImages);
//                    intent.putExtra("num", 1);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.iv_inv_img3:
//                if (mLtImages.size() < 3) {
//                    getAlbum();
//                } else {
//                    intent = new Intent(SubsidyDetailsActivity.this, ShowImageActivity.class);
//                    intent.putStringArrayListExtra("images", mLtImages);
//                    intent.putExtra("num", 2);
//                    startActivity(intent);
//                }
//                break;
//        }
    }
//
//    /**
//     * 确认是否替换过
//     */
//    private int isImage() {
//        mImgs = new ArrayList<>();
//        int num = 0;
//        for (int i = 0; i < mLtImages.size(); i++) {
//            if (mLtImages.get(i).contains("http://")) {
//                num++;
//            } else { //可上传图片数量
//                mImgs.add(mLtImages.get(i));
//            }
//        }
//
//        return num;
//    }
//
//    private boolean testingData() {
//        if (mTvPlace.getText().length() < 1) {
//            toast(getString(R.string.common_text79));
//            return false;
//        }
//
//        if (mEtBusiness.getText().length() < 1) {
//            toast(getString(R.string.common_text80));
//            return false;
//        }
//        if (mTvType.getText().length() < 1) {//消费类型
//            toast(getString(R.string.common_text89));
//            return false;
//        }
//
//        if (Double.parseDouble(mEtCon.getText().toString().equals("") ? "0" : mEtCon.getText().toString()) < 20) {
//            toast(getString(R.string.common_text92));
//            return false;
//        }
//        if (mLtImages.size() < 1) {
//            toast(getString(R.string.common_text98));
//            return false;
//        }
//        return true;
//    }
//
//    private void getAlbum() {
//        ImagePicker.getInstance().setSelectLimit(maxImgCount - mLtImages.size());
//        Intent intent = new Intent(SubsidyDetailsActivity.this, ImageGridActivity.class);
//        startActivityForResult(intent, REQUEST_CODE_SELECT);
//    }
//
//    private void showImages(List<String> img) {
//        mIvImg1.setImageResource(0);
//        mIvImg2.setImageResource(0);
//        mIvImg3.setImageResource(0);
//        for (int i = 0; i < img.size(); i++) {
//            if (i == 0) {
//                Glide.with(this).load(img.get(i)).into(mIvImg1);
//            } else if (i == 1) {
//                Glide.with(this).load(img.get(i)).into(mIvImg2);
//            } else if (i == 2) {
//                Glide.with(this).load(img.get(i)).into(mIvImg3);
//            }
//        }
//
//        if (mSubsidyInfo.getStatus() == -2) {
//            switch (img.size()) {
//                case 1:
//                    mIvx1.setVisibility(View.VISIBLE);
//                    mIvx2.setVisibility(View.GONE);
//                    mIvx3.setVisibility(View.GONE);
//                    mIvAdd1.setVisibility(View.GONE);
//                    mIvAdd2.setVisibility(View.VISIBLE);
//                    mIvAdd3.setVisibility(View.VISIBLE);
//                    break;
//                case 2:
//                    mIvx1.setVisibility(View.VISIBLE);
//                    mIvx2.setVisibility(View.VISIBLE);
//                    mIvx3.setVisibility(View.GONE);
//                    mIvAdd1.setVisibility(View.GONE);
//                    mIvAdd2.setVisibility(View.GONE);
//                    mIvAdd3.setVisibility(View.VISIBLE);
//                    break;
//                case 3:
//                    mIvx1.setVisibility(View.VISIBLE);
//                    mIvx2.setVisibility(View.VISIBLE);
//                    mIvx3.setVisibility(View.VISIBLE);
//                    mIvAdd1.setVisibility(View.GONE);
//                    mIvAdd2.setVisibility(View.GONE);
//                    mIvAdd3.setVisibility(View.GONE);
//                    break;
//                case 0:
//                    mIvx1.setVisibility(View.GONE);
//                    mIvx2.setVisibility(View.GONE);
//                    mIvx3.setVisibility(View.GONE);
//                    mIvAdd1.setVisibility(View.VISIBLE);
//                    mIvAdd2.setVisibility(View.VISIBLE);
//                    mIvAdd3.setVisibility(View.VISIBLE);
//                    break;
//            }
//        } else {
//            switch (img.size()) {
//                case 1:
//                    mRlayImg2.setVisibility(View.INVISIBLE);
//                    mRlayImg3.setVisibility(View.INVISIBLE);
//                    break;
//                case 2:
//                    mRlayImg3.setVisibility(View.INVISIBLE);
//                    break;
//                case 0:
//                    mLayImgs.setVisibility(View.GONE);
//                    break;
//            }
//            mIvAdd1.setVisibility(View.GONE);
//            mIvAdd2.setVisibility(View.GONE);
//            mIvAdd3.setVisibility(View.GONE);
//        }
//
//    }
//
//    private void getVoucherdetail(String id) {
//        showProgress();
//        rxDestroy(UniaipAPI.voucherdetail(UniaipApplication.user.getToken(), id)).subscribe(subsidyDetailModel -> {
//            if (subsidyDetailModel.isOK()) {
//                mSubsidyInfo = subsidyDetailModel.getData();
//                showData(mSubsidyInfo);
//            } else if (TextUtils.equals(subsidyDetailModel.getCode(), "10002")) {
//                Intent intent = new Intent(this, LoginActivity.class);
//                intent.putExtra("type", 99);
//                startActivity(intent);
//                exit();
//            } else {
//                toast(subsidyDetailModel.getMsg());
//            }
//            dismissProgress();
//        }, e -> {
//            toast(getString(R.string.error_text));
//            dismissProgress();
//        });
//    }
//
//    /**
//     * 压缩图片
//     */
//    private void compressImage() {
//        showProgress();
//        uploadNum = 0;
//        if (!fileImage.exists()) {
//            fileImage.mkdirs();
//        }
//        for (int i = 0; i < mImgs.size(); i++) {
//            final int num = i;
//            new Thread() {
//                public void run() {
//                    File fileOutput = new File(fileImage, TextUtil.getImgName() + ".png");
//                    NativeUtil.compressBitmap(mImgs.get(num), fileOutput.getPath());
//                    mImgs.set(num, fileOutput.getPath());
//                    Message message = new Message();
//                    message.what = 1002;
//                    message.arg1 = mImgs.size();
//                    mHandler.sendMessage(message);
//                }
//            }.start();
//        }
//    }
//
//    /**
//     * 上传图片
//     */
//    private void uploadImg() {
//        initoss();
//        uploadNum = 0;
//        showProgress(R.string.img_progress);
//        for (int i = 0; i < mImgs.size(); i++) {
//            int position = i;
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    uploadImage(mImgs.get(position),position);
//                }
//            }, 300);
//        }
//    }
//
//    /**
//     * 修改消费凭证
//     */
//    private void getEditvoucher(List<String> imgs) {
//        String obj = "";
//        for (int i = 0; i < imgs.size(); i++) {
//            if (i == 0) {
//                obj = imgs.get(i);
//            } else {
//                obj = obj + "," + imgs.get(i);
//            }
//        }
//        mSubsidyInfo.setAddress(mTvPlace.getText().toString());
//        mSubsidyInfo.setMoney(mEtCon.getText().toString());
//        mSubsidyInfo.setBusinessname(mEtBusiness.getText().toString());
//        mSubsidyInfo.setConsumetype(typeID+"");
//
//        Log.e(">>>", "小票:" + mSubsidyInfo.getId() + "|名称:" + mSubsidyInfo.getBusinessname() + "|类型:" + typeID +
//                "|金额:" + mSubsidyInfo.getMoney() + "|图片:" +
//                obj + "|经纬度:" + mSubsidyInfo.getTitude() + "|城市:" + mSubsidyInfo.getCity() + "|地址:" + mSubsidyInfo.getAddress());
//        rxDestroy(UniaipAPI.editvoucher(UniaipApplication.user.getToken(), UniaipApplication.user.getId(),
//                mSubsidyInfo.getId(), mSubsidyInfo.getBusinessname(),
//                mSubsidyInfo.getConsumetype(), mSubsidyInfo.getMoney(), obj, mSubsidyInfo.getTitude(),
//                mSubsidyInfo.getCity(), mSubsidyInfo.getAddress())).subscribe(baseModel -> {
//            if (baseModel.isOK()) {
//                toast(baseModel.getMsg());
//                UtilsAll.deleteImages(fileImage);
//                if (type == 0) { //列表进入更新列表状态
//                    Intent intent = new Intent();
//                    intent.putExtra("position", getIntent().getIntExtra("position", -1));
//                    setResult(1201, intent);
//                }
//                finish();
//            } else if (TextUtils.equals(baseModel.getCode(), "10002")) {
//                Intent intent = new Intent(this, LoginActivity.class);
//                intent.putExtra("type", 99);
//                startActivity(intent);
//                exit();
//            } else {
//                toast(baseModel.getMsg());
//            }
//            dismissProgress();
//        }, e -> {
//            toast(getString(R.string.error_text));
//            dismissProgress();
//        });
//    }
//
//    /**
//     * 计算补贴
//     *
//     * @param moeny
//     */
//    private void getsSubsidy(double moeny) {
//        double subsidyMoney;
//        double integral;
//        if (UniaipApplication.consumeData == null) {
//            double subsidy = moeny * 0.12;//总补贴
//            subsidyMoney = subsidy * 0.7;//可补贴金额
//            integral = subsidy * 0.3;//积分
//        } else {
//            double subsidy = moeny * Double.parseDouble(UniaipApplication.consumeData.getSubsidyRatio());//总补贴
//            subsidyMoney = subsidy * Double.parseDouble(UniaipApplication.consumeData.getCashRatio());//可补贴金额
//            integral = subsidy * Double.parseDouble(UniaipApplication.consumeData.getScoreRatio());//积分
//        }
//        mTvIntegral.setText(new BigDecimal(integral).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
//    }
//
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1001://修改
//                    getEditvoucher(mLtImages);
//                    break;
//                case 1002://获取压缩后的图片
//                    uploadNum++;
//                    if (uploadNum == msg.arg1) {
//                        uploadImg();
//                    }
//                    break;
//                case 1099:
//                    toast("由于网络问题导致上传失败，请尝试再次上传。");
//                    dismissProgress();
//                    break;
//            }
//        }
//    };
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (null == options) {
//            options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//        }
//        switch (requestCode) {
//            case REQUEST_CODE_SELECT://相册
//                if (null == data)
//                    return;
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                ArrayList<String> img = new ArrayList<>();
//                for (int i = 0; i < images.size(); i++) {
//                    img.add(images.get(i).path);
//                }
//                mLtImages.addAll(img);
//                showImages(mLtImages);
//                break;
//            case 1101:
//                if (null == data)
//                    return;
//                info = (CheckInfo) data.getSerializableExtra("address");
//                mSubsidyInfo.setCity(info.getCity());
//                mSubsidyInfo.setTitude(info.getLongitude() + "," + info.getLatitude());
//                mSubsidyInfo.setAddress(info.getAddress());
//                mSubsidyInfo.setBusinessname(info.getName());
//                mTvPlace.setText(info.getAddress());
//                mEtBusiness.setText(info.getName());
//                break;
//        }
//    }
}
