package com.example.taopr.soool.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.R;

import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener{

    public static BottomSheetDialog getInstance() { return new BottomSheetDialog(); }

    private LinearLayout msgLo;
    private LinearLayout emailLo;
    private LinearLayout cloudLo;
    private LinearLayout bluetoothLo;
    private Button okBtn, cancleBtn;
    public ArrayList<String> tagArray = new ArrayList<>();
    private BottomSheetDialoggListener bottomSheetDialoggListener;

    //인터페이스 설정
    public interface BottomSheetDialoggListener{
        void onPositiveClicked(ArrayList<String> arrayList);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(BottomSheetDialoggListener bottomSheetDialoggListener){
        this.bottomSheetDialoggListener = bottomSheetDialoggListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container,false);
        msgLo = (LinearLayout) view.findViewById(R.id.msgLo);
        emailLo = (LinearLayout) view.findViewById(R.id.emailLo);
        cloudLo = (LinearLayout) view.findViewById(R.id.cloudLo);
        bluetoothLo = (LinearLayout) view.findViewById(R.id.bluetoothLo);
        okBtn = view.findViewById(R.id.okBtn);
        cancleBtn = view.findViewById(R.id.cancleBtn);


        msgLo.setOnClickListener(this);
        emailLo.setOnClickListener(this);
        cloudLo.setOnClickListener(this);
        bluetoothLo.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.msgLo:
                Toast.makeText(getContext(),"Message",Toast.LENGTH_SHORT).show();
                tagArray.add("Message");
                break;
            case R.id.emailLo:
                Toast.makeText(getContext(),"Email",Toast.LENGTH_SHORT).show();
                tagArray.add("Email");
                break;
            case R.id.cloudLo:
                Toast.makeText(getContext(),"Cloud",Toast.LENGTH_SHORT).show();
                tagArray.add("Cloud");
                break;
            case R.id.bluetoothLo:
                Toast.makeText(getContext(),"Bluetooth",Toast.LENGTH_SHORT).show();
                tagArray.add("Bluetooth");
                break;
            case R.id.okBtn:
                bottomSheetDialoggListener.onPositiveClicked(tagArray);
                dismiss();
                break;
            case R.id.cancleBtn:
                dismiss();
                break;
        }
    }
}
