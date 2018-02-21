package com.example.fishingport.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx0e5b8dcd46f493b8", false);
        api.registerApp("wx0e5b8dcd46f493b8");
        api.handleIntent(getIntent(), this);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq baseReq) {
        Log.i("w微信回调===",baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("微信回调",baseResp.toString()+"/"+baseResp.errStr+"/"+baseResp.getType()+"/"+baseResp.errCode);
        switch (baseResp.getType()) {
            case 2:
                finish();
                break;
        }

    }
}

