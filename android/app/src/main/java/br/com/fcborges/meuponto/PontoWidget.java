package br.com.fcborges.meuponto;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import org.json.JSONObject;
import org.json.JSONArray;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PontoWidget extends AppWidgetProvider {
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        String today=new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date());
        String[] labels={"Entrada","Almoço","Retorno","Saída"};
        String[] times={"--:--","--:--","--:--","--:--"};
        try{
            JSONObject root=new JSONObject(context.getSharedPreferences("ponto_widget",Context.MODE_PRIVATE).getString("marks","{}"));
            JSONArray marks=root.optJSONArray(today);
            if(marks!=null)for(int j=0;j<Math.min(4,marks.length());j++)times[j]=marks.optString(j,"--:--");
        }catch(Exception ignored){}
        for(int id:ids) {
            RemoteViews v=new RemoteViews(context.getPackageName(),R.layout.ponto_widget);
            v.setTextViewText(R.id.widget_status,"⏱ Meu Ponto • "+new SimpleDateFormat("dd/MM",Locale.getDefault()).format(new Date()));
            v.setTextViewText(R.id.widget_recent,labels[0]+": "+times[0]+"   "+labels[1]+": "+times[1]+"\n"+labels[2]+": "+times[2]+"   "+labels[3]+": "+times[3]);
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
