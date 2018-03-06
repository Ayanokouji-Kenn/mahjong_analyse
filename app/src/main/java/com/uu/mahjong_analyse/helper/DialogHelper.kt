package com.uu.mahjong_analyse.helper

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/12
 *     desc  :
 * </pre>
 */

import android.support.v7.app.AlertDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.OnRationaleListener.ShouldRequest
import com.uu.mahjong_analyse.R

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2018/01/10
 * desc  : 对话框工具类
</pre> *
 */
object DialogHelper {

    fun showRationaleDialog(shouldRequest: ShouldRequest) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity,R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(R.string.permission_rationale_message)
                .setPositiveButton(android.R.string.ok, { _, _ -> shouldRequest.again(true) })
                .setNegativeButton(android.R.string.cancel, { _, _ -> shouldRequest.again(false) })
                .setCancelable(false)
                .create()
                .show()

    }

    fun showOpenAppSettingDialog() {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity,R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(R.string.permission_denied_forever_message)
                .setPositiveButton(android.R.string.ok, { _, _ -> PermissionUtils.openAppSettings() })
                .setNegativeButton(android.R.string.cancel, { _, _ -> })
                .setCancelable(false)
                .create()
                .show()
    }

//    fun showKeyboardDialog() {
//        val topActivity = ActivityUtils.getTopActivity() ?: return
//        val dialogView = LayoutInflater.from(topActivity).inflate(R.layout.dialog_keyboard, null)
//        val etInput = dialogView.findViewById(R.id.et_input)
//        val dialog = AlertDialog.Builder(topActivity).setView(dialogView).create()
//        dialog.setCanceledOnTouchOutside(false)
//        val listener = View.OnClickListener { v ->
//            when (v.id) {
//                R.id.btn_hide_soft_input -> KeyboardUtils.hideSoftInput(etInput)
//                R.id.btn_show_soft_input -> KeyboardUtils.showSoftInput(etInput)
//                R.id.btn_toggle_soft_input -> KeyboardUtils.toggleSoftInput()
//                R.id.btn_close_dialog -> {
//                    KeyboardUtils.hideSoftInput(etInput)
//                    dialog.dismiss()
//                }
//            }
//        }
//        dialogView.findViewById(R.id.btn_hide_soft_input).setOnClickListener(listener)
//        dialogView.findViewById(R.id.btn_show_soft_input).setOnClickListener(listener)
//        dialogView.findViewById(R.id.btn_toggle_soft_input).setOnClickListener(listener)
//        dialogView.findViewById(R.id.btn_close_dialog).setOnClickListener(listener)
//        dialog.show()
//    }
}