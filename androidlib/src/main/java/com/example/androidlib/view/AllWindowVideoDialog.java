package com.example.androidlib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidlib.R;
import com.example.androidlib.R2;

import butterknife.BindView;

public class AllWindowVideoDialog extends Dialog {

    @BindView(R2.id.cl_dialog_video)
    ConstraintLayout clDialogVideo;
    private Context mContext;

    public AllWindowVideoDialog(@NonNull Context context) {
        super(context, R.style.my_tip_dialog);
        mContext = context;
    }

    public AllWindowVideoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AllWindowVideoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video_dialog);
        addVideoView();
    }

    private void addVideoView() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        clDialogVideo.addView(VideoPlayerIJK.getInstance(mContext), params);
    }
}
