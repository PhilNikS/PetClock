package com.lessons.petclock.alarmClockFragment;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class newClass {

    public Map<String,String> getNotificationAlarm(Context context){
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();


        Map<String,String> list = new HashMap<>();
        while(cursor.moveToFirst()){
            String alarmTitle  = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String alarmUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            list.put(alarmTitle,alarmUri);
        }
        return list;
    }
}
