package com.example.testhybrid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
}

class PlaceholderFragment extends Fragment {

	WebView webView;
	
    public PlaceholderFragment() {
    }

    @SuppressLint("SetJavaScriptEnabled") @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        
        webView = (WebView)rootView.findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);
        
        Handler handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		String str = msg.getData().getString("str");
        		doJs("jsDoIt", str.replaceAll("\\d", ""));
        	}
        };
        webView.addJavascriptInterface(new MyJavascriptInterface(webView, handler), "javaObject");
        
        webView.loadUrl("file:///android_asset/webviews/index.html");
        
        return rootView;
    }
    
    //调用js方法，第一个参数是js方法名，后面的参数是js方法的参数列表
    void doJs(String function, Object... params){
    	StringBuilder result = new StringBuilder();
    	result.append("javascript:").append(function).append("(");
    	for(int i = 0; i < params.length; i++){
    		result.append("'").append(params[i].toString()).append("'");
    		if(i < params.length - 1){
    			result.append(",");
    		}
    	}
    	result.append(")");
    	String jsStr = result.toString();
    	webView.loadUrl(jsStr);
    }
}

//要用来被js调用的java对象
class MyJavascriptInterface{
	
	MyJavascriptInterface(WebView wv, Handler h){
		this.theWebView = wv;
		this.handler = h;
	}
	
	WebView theWebView;
	Handler handler;
	
	//要用来被js调用的java方法
	@JavascriptInterface
	public void javaDoIt(final String str){
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("str", str);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}

}