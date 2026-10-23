package br.com.fcborges.meuponto;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

public class PontoBridge {
    private final Context context;
    public PontoBridge(Context context){this.context=context.getApplicationContext();}
    @JavascriptInterface public void update(String json){
        context.getSharedPreferences("ponto_widget",Context.MODE_PRIVATE).edit().putString("marks",json).apply();
        Intent i=new Intent(context,PontoWidget.class);
        i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids=AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,PontoWidget.class));
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(i);
    }
}
