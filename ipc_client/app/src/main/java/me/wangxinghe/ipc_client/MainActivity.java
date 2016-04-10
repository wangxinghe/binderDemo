package me.wangxinghe.ipc_client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.wangxinghe.ipc.ICalculator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String ACTION_CALCULATOR_SERVICE = "com.wangxinghe.ipc_server.CalculatorService";
    private Button mBindBtn;
    private Button mUnbindBtn;
    private Button mAddBtn;
    private Button mMinusBtn;
    private TextView mResultTv;

    private ICalculator mCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBindBtn = (Button)findViewById(R.id.bind_service);
        mUnbindBtn = (Button)findViewById(R.id.unbind_service);
        mAddBtn = (Button)findViewById(R.id.add);
        mMinusBtn = (Button)findViewById(R.id.minus);
        mResultTv = (TextView)findViewById(R.id.result);

        mBindBtn.setOnClickListener(this);
        mUnbindBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mMinusBtn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCalculator = ICalculator.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCalculator = null;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_service:
                Intent intent = new Intent(ACTION_CALCULATOR_SERVICE);
                bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(mServiceConnection);
                break;
            case R.id.add:
                int result = 0;
                try {
                    if (mCalculator != null) {
                        result = mCalculator.add(2, 1);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mResultTv.setText(result + "");
                break;
            case R.id.minus:
                int _result = 0;
                try {
                    if (mCalculator != null) {
                        _result = mCalculator.minus(2, 1);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mResultTv.setText(_result + "");
                break;
        }
    }
}
