package com.uu.mahjong_analyse.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.blankj.utilcode.util.ToastUtils;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.databinding.ActivityAddNewGameBinding;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.utils.CommonApi;
import com.uu.mahjong_analyse.utils.Constant;
import com.uu.mahjong_analyse.utils.SPUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by Nagisa on 2016/6/24.
 */
public class AddNewGameActivity extends BaseActivity implements TextView.OnEditorActionListener {


    private List<String> datas;
    String[] select = new String[4];  //按照东南西北的顺序
    private ActivityAddNewGameBinding mBinding;


    @Override
    public void initData() {
        datas = DBDao.getPlayers();
    }

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_game);
        mBinding.setListener(mListener);
        mBinding.textInputLayout.setHint("输入新来小伙伴名字");
        mBinding.et.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        CommonApi.setToolbar(this, (Toolbar) findViewById(R.id.toolbar), "设置该局玩家");
    }

    @Override
    public void initEvent() {
        mBinding.textInputLayout.getEditText().setOnEditorActionListener(this);
    }

    public void showDialog(final View view) {
        OptionsPickerView selectPlayerDialog = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String mSelectName = datas.get(options1);
                switch (view.getId()) {
                    case R.id.tv_east:
                        select[0] = mSelectName;
                        mBinding.tvEast.setText(String.format(Locale.CHINA, "东家： %s", mSelectName));
                        SPUtils.putString(Constant.EAST, mSelectName);
                        break;
                    case R.id.tv_south:
                        select[1] = mSelectName;
                        mBinding.tvSouth.setText(String.format(Locale.CHINA, "南家： %s", mSelectName));
                        SPUtils.putString(Constant.SOUTH, mSelectName);
                        break;
                    case R.id.tv_west:
                        select[2] = mSelectName;
                        mBinding.tvWest.setText(String.format(Locale.CHINA, "西家： %s", mSelectName));
                        SPUtils.putString(Constant.WEST, mSelectName);
                        break;
                    case R.id.tv_north:
                        select[3] = mSelectName;
                        mBinding.tvNorth.setText(String.format(Locale.CHINA, "北家： %s", mSelectName));
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

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_ok) {
                Log.d(getClass().getSimpleName(), "ok: ============点击事件");
                for (int i = 0; i < select.length; i++) {
                    if (TextUtils.isEmpty(select[i])) {
                        ToastUtils.showShortSafe("你在逗我么，人选齐了？");
                        return;
                    }
                }
                Intent data = new Intent();
                data.putExtra("players", select);
                setResult(RESULT_OK, data);
                finish();
            } else if ((id == R.id.tv_east)
                    || (id == R.id.tv_south)
                    || (id == R.id.tv_west)
                    || (id == R.id.tv_north)) {
                if (datas.size() != 0) {
                    showDialog(v);
                } else {
                    ToastUtils.showShortSafe("请先添加小伙伴");
                }
            }

        }
    };


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            EditText et = mBinding.textInputLayout.getEditText();
            Editable text = et.getText();
            String trim = text.toString().trim();

            if (!TextUtils.isEmpty(trim)) {
                datas.add(trim);
                Toast.makeText(this, "小伙伴加入了,可以直接点击东家来进行设置", Toast.LENGTH_SHORT).show();
                et.setText("");
            } else {
                mBinding.textInputLayout.setError("小伙伴没名字？");
            }
            return true;
        }
        return false;
    }
}
