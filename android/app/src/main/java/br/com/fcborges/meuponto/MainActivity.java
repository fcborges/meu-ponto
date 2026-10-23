package br.com.fcborges.meuponto;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView web;
    private boolean loaded=false;
    private int pending=-1;
    private int requested(Intent intent){
        if(intent==null || intent.getAction()==null)return -1;
        String a=intent.getAction(),prefix="br.com.fcborges.meuponto.MARK_";
        if(!a.startsWith(prefix))return -1;
        try {int n=Integer.parseInt(a.substring(prefix.length()));return n>=0&&n<4?n:-1;}catch(Exception e){return -1;}
    }
    private void attempt(){
        if(!loaded||pending<0)return;
        int step=pending;pending=-1;
        // The web app owns the records. Only mark when this is the next expected step.
        web.evaluateJavascript("(function(){try{var d=new Date().toLocaleDateString('sv-SE');var a=data[d]||[];if(a.length==="+step+"){mark();return 'ok'}return 'order'}catch(e){return 'error'}})()", result->{
            if(!"\"ok\"".equals(result))Toast.makeText(this,"Confira a sequência de registros no aplicativo",Toast.LENGTH_LONG).show();
        });
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pending=requested(getIntent());
        web=new WebView(this);setContentView(web);
        WebSettings settings=web.getSettings();
        settings.setJavaScriptEnabled(true);settings.setDomStorageEnabled(true);web.addJavascriptInterface(new PontoBridge(this),"PontoAndroid");
        web.setWebViewClient(new WebViewClient(){@Override public void onPageFinished(WebView view,String url){loaded=true;attempt();}});
        web.setWebChromeClient(new WebChromeClient());
        web.loadUrl("https://fcborges.github.io/meu-ponto/");
    }
    @Override protected void onNewIntent(Intent intent){super.onNewIntent(intent);setIntent(intent);pending=requested(intent);attempt();}
    @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
    @Override protected void onDestroy(){web.destroy();super.onDestroy();}
}
