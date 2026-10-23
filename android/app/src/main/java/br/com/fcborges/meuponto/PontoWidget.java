package br.com.fcborges.meuponto;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class PontoWidget extends AppWidgetProvider {
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id:ids) {
            RemoteViews v=new RemoteViews(context.getPackageName(),R.layout.ponto_widget);
            int[] buttons={R.id.widget_entry,R.id.widget_lunch,R.id.widget_return,R.id.widget_exit};
            for(int i=0;i<4;i++){
                Intent intent=new Intent(context,MainActivity.class);
                intent.setAction("br.com.fcborges.meuponto.MARK_"+i);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pi=PendingIntent.getActivity(context,i,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
                v.setOnClickPendingIntent(buttons[i],pi);
            }
            manager.updateAppWidget(id,v);
        }
    }
}
