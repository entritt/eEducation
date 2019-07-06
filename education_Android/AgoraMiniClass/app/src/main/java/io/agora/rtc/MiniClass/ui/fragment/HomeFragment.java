package io.agora.rtc.MiniClass.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.agora.rtc.MiniClass.R;
import io.agora.rtc.MiniClass.model.config.UserConfig;
import io.agora.rtc.MiniClass.model.constant.Constant;
import io.agora.rtc.MiniClass.model.event.BaseEvent;
import io.agora.rtc.MiniClass.model.event.SimpleEvent;
import io.agora.rtc.MiniClass.model.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private TextView tvBtnRoleTeacher, tvBtnRoleStudent, tvBtnRoleAudience;
    private EditText edtClassName, edtUserName;
    private Constant.Role mRole = Constant.Role.AUDIENCE;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        TextView tvBtnJoin = root.findViewById(R.id.tv_btn_join);
        edtClassName = root.findViewById(R.id.edt_main_classroom_name);
        edtUserName = root.findViewById(R.id.edt_main_user_name);
        tvBtnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = edtClassName.getText().toString();
                String userName = edtUserName.getText().toString();
                if (TextUtils.isEmpty(className) ||
                    TextUtils.isEmpty(userName)) {
                    ToastUtil.showShort("Class name or your name cannot be empty.");
                    return;
                }

                if (className.length() > Constant.MAX_INPUT_NAME_LENGTH ||
                    userName.length() > Constant.MAX_INPUT_NAME_LENGTH) {
                    ToastUtil.showShort(R.string.account_too_long);
                    return;
                }

                UserConfig.setRole(mRole);
                UserConfig.setRtcChannelName(className);
                UserConfig.setRtmChannelName(className);
                UserConfig.setRtmUserName(userName);
                UserConfig.createUserId();
                chatManager().login();

                if (mListener != null) {
                    Event event = new Event(Event.EVENT_TYPE_CLICK_JOIN);
                    mListener.onFragmentEvent(event);
                }
            }
        });
        tvBtnRoleTeacher = root.findViewById(R.id.tv_select_role_teacher);
        tvBtnRoleAudience = root.findViewById(R.id.tv_select_role_audience);
        tvBtnRoleStudent = root.findViewById(R.id.tv_select_role_student);

        tvBtnRoleTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRoleSelected();
                tvBtnRoleTeacher.setSelected(true);
                mRole = Constant.Role.TEACHER;
            }
        });

        tvBtnRoleStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRoleSelected();
                tvBtnRoleStudent.setSelected(true);
                mRole = Constant.Role.STUDENT;
            }
        });

        tvBtnRoleAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRoleSelected();
                tvBtnRoleAudience.setSelected(true);
                mRole = Constant.Role.AUDIENCE;
            }
        });

        tvBtnRoleAudience.setSelected(true);
    }

    private void clearRoleSelected() {
        tvBtnRoleTeacher.setSelected(false);
        tvBtnRoleStudent.setSelected(false);
        tvBtnRoleAudience.setSelected(false);
    }


    @Override
    public void onActivityEvent(BaseEvent event) {

    }

    public static class Event extends BaseEvent {

        public static final int EVENT_TYPE_CLICK_JOIN = 100;

        public Event(int eventType) {
            super(eventType);
        }
    }

}
