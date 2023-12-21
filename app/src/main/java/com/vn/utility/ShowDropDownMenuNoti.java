package com.vn.utility;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vn.appdesign.R;

public class ShowDropDownMenuNoti {
    public static void showDropdownMenu(View anchorView) {
        View popupView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.custom_menu_option_notification, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        int x = location[0] - popupView.getWidth() - 700;
        int y = location[1] + 150;

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
//
//        TextView mtTodo = popupView.findViewById(R.id.move_to_todo);
//        TextView mtDoing = popupView.findViewById(R.id.move_to_doing);
//        TextView mtDone = popupView.findViewById(R.id.move_to_done);
//        TextView mtDelete = popupView.findViewById(R.id.move_to_delete);

    }
}
