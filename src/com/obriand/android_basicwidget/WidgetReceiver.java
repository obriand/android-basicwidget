package com.obriand.android_basicwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

//Tuto : http://sberfini.developpez.com/tutoriaux/android/appwidget/

public class WidgetReceiver extends AppWidgetProvider 
{
	
	public static final String ACTION_LANCER_APPLICATION = "com.obriand.android_basicwidget.LANCER_APPLICATION"; 
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show(); 
		// Pour chaque AppWidget MonWidgetDeveloppez (n'oubliez pas qu'on peut en ajouter tant qu'on veut), on les met a jour :
		for (int appWidgetId : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	// Cette methode est entierement libre, a vous de la modifier comme bon vous semble. Voici toutefois une base minimaliste
	public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) 
	{
		Toast.makeText(context, "updateAppWidget:"+appWidgetId, Toast.LENGTH_SHORT).show(); 
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main); // On recupere les Views de notre layout
		
		// Update the title
		remoteViews.setTextViewText(R.id.widget_title_tv, "Title updated !"); // On peut agir sur ces vues
		
		// *** On prepare un intent a lancer lors d'un clic pour lancer une activity directement
		Intent activityIntent = new Intent(context, MainActivity.class);		
		PendingIntent pendingActivityIntent = PendingIntent.getActivity(context, 0, activityIntent, 0); // On lie l'intent a l'action
		remoteViews.setOnClickPendingIntent(R.id.widget_activity_tv, pendingActivityIntent); // L'id de la view qui reagira au clic sur le widget.
		
		// *** Un intent pour lancer Google sur un click sur un autre TextView
		Intent googleIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
		PendingIntent pendingGoogleIntent = PendingIntent.getActivity(context, 0, googleIntent, PendingIntent.FLAG_UPDATE_CURRENT );
		remoteViews.setOnClickPendingIntent(R.id.widget_google_tv, pendingGoogleIntent);
		
		// *** Un intent pour lancer un boradcast, ensuite capter par la widget et lancer une activity dans le onReceive
		Intent broadcastIntent = new Intent();		
		broadcastIntent.setAction(ACTION_LANCER_APPLICATION); // Je cree ici ma propre action
		broadcastIntent.setClassName(context, WidgetReceiver.class.getName());		
		PendingIntent pendingBroadcastIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, 0); // On lie l'intent a l'action
		remoteViews.setOnClickPendingIntent(R.id.widget_broadcast_tv, pendingBroadcastIntent); // L'id de la view qui reagira au clic sur le widget.
		
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews); // On met ensuite a jour l'affichage du widget
	}
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
	    super.onReceive(context, intent);
	    Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show(); 
	    if (intent.getAction().equals(ACTION_LANCER_APPLICATION))
	    {
	    	Toast.makeText(context, "ACTION_LANCER_APPLICATION", Toast.LENGTH_SHORT).show(); 
	        lancerActivityPrincipale(context);
	    }
	}
	

	protected void lancerActivityPrincipale(Context context) 
	{
		Toast.makeText(context, "lancerActivityPrincipale avec broadcast", Toast.LENGTH_SHORT).show(); 
		Intent i = new Intent(context, MainActivity.class);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);
	}

}

