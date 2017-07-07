package com.uu.mahjong_analyse.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.CommonApi;
import com.uu.mahjong_analyse.Utils.Constant;
import com.uu.mahjong_analyse.Utils.SPUtils;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.db.DBDao;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nagisa on 2016/6/24.
 */
public class AddNewGameActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.tv_east)
    TextView mTvEast;
    @BindView(R.id.tv_south)
    TextView mTvSouth;
    @BindView(R.id.tv_west)
    TextView mTvWest;
    @BindView(R.id.tv_north)
    TextView mTvNorth;
    @BindView(R.id.text_input_layout)
    TextInputLayout mTextInputLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_ok)
    TextView mTvOk;
    private List<String> datas;
    String[] select = new String[4];  //按照东南西北的顺序


    @Override
    public void initData() {
        datas = DBDao.getPlayers();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_new_game);
        ButterKnife.bind(this);
        mTextInputLayout.setHint("输入新来小伙伴名字");
        CommonApi.setToolbar(this, mToolbar,"设置该局玩家");
    }

    @Override
    public void initEvent() {
        mTextInputLayout.getEditText().setOnEditorActionListener(this);
    }

    public void showDialog(final View view) {
        OptionsPickerView selectPlayerDialog = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String mSelectName = datas.get(options1);
                switch (view.getId()) {
                    case R.id.tv_east:
                        select[0] = mSelectName;
                        mTvEast.setText(String.format(Locale.CHINA, "东家： %s", mSelectName));
                        SPUtils.putString(Constant.EAST, mSelectName);
                        break;
                    case R.id.tv_south:
                        select[1] = mSelectName;
                        mTvSouth.setText(String.format(Locale.CHINA, "南家： %s", mSelectName));
                        SPUtils.putString(Constant.SOUTH, mSelectName);
                        break;
                    case R.id.tv_west:
                        select[2] = mSelectName;
                        mTvWest.setText(String.format(Locale.CHINA, "西家： %s", mSelectName));
                        SPUtils.putString(Constant.WEST, mSelectName);
                        break;
                    case R.id.tv_north:
                        select[3] = mSelectName;
                        mTvNorth.setText(String.format(Locale.CHINA, "北家： %s", mSelectName));
                        SPUtils.putString(Constant.NORTH, mSelectName);
                        break;
                }
            }
        })

                .setCancelColor(getResources().getColor(R.color.colorAccent))
                .setSubmitColor(getResources().getColor(R.color.colorAccent))
                .build();
        selectPlayerDialog.setPicker(datas);
        selectPlayerDialog.show();


//        final WheelViewDialog dialog = new WheelViewDialog(this);
//        dialog.setTitle(getString(R.string.choose_player))
//                .setItems(datas)
//                .setButtonText(getString(R.string.confirm))
//                .setDialogStyle(getResources().getColor(R.color.colorPrimary))
//                .setCount(5)
//                .show();
//        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
//            @Override
//            public void onItemClick(int position, Object s) {
//
//                }
//            }
//        });
    }

    @OnClick({R.id.tv_east, R.id.tv_south, R.id.tv_west, R.id.tv_north})
    public void onClick(View view) {
        if(datas.size() != 0) {
            showDialog(view);
        }else {
            Toast.makeText(this, "请先添加小伙伴", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_ok)
    public void ok(View view) {
        Log.d(getClass().getSimpleName(), "ok: ============点击事件");
        for(int i = 0; i < select.length; i++) {
            if(TextUtils.isEmpty(select[i])) {
                Toast.makeText(this, "你在逗我么，人选齐了？", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent data = new Intent();
        data.putExtra("players", select);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            EditText et = mTextInputLayout.getEditText();
            Editable text = et.getText();
            String trim = text.toString().trim();

            if(!TextUtils.isEmpty(trim)) {
                datas.add(trim);
                Toast.makeText(this, "小伙伴加入了,可以直接点击东家来进行设置", Toast.LENGTH_SHORT).show();
                et.setText("");
            }else {
                mTextInputLayout.setError("小伙伴没名字？");
            }
            return true;
        }
        return false;
    }
}
