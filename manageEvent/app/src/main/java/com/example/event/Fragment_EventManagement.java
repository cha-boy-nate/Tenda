package com.example.event;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.map.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_EventManagement extends Fragment {


    public Fragment_EventManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment__event_management, container, false);


        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>(); //存储数据的数组列表
//写死的数据，用于测试
        //int[] image_expense = new int[]{R.mipmap.detail_income, R.mipmap.detail_payout }; //存储图片
        String[] item_title = new String[] {"Team Chan Meeting", "Team Chan Meeting", "Team Chan Meeting", "Team Chan Meeting"};
        String[] item_content = new String[] {"Weekly Team Meeting for Software Engineering 1 class", "Weekly Team Meeting for Software Engineering 1 class", "Weekly Team Meeting for Software Engineering 1 class", "Weekly Team Meeting for Software Engineering 1 class"};
        String[] item_time = new String[] {"3 February 2019 at 6:45 PM", "4 February 2019 at 6:45 PM", "5 February 2019 at 6:45 PM", "6 February 2019 at 6:45 PM"};
        String[] item_location = new String[] {"Otto Miller Hall", "Otto Miller Hall", "Otto Miller Hall", "Otto Miller Hall"};
        for (int i = 0; i < item_title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            //map.put("image_expense", image_expense[i]);
            map.put("item_title", item_title[i]);
            map.put("item_content", item_content[i]);
            map.put("item_time", item_time[i]);
            map.put("item_location", item_location[i]);
            listitem.add(map);
        }

        //创建适配器
// 第一个参数是上下文对象
// 第二个是listitem
// 第三个是指定每个列表项的布局文件
// 第四个是指定Map对象中定义的两个键（这里通过字符串数组来指定）
// 第五个是用于指定在布局文件中定义的id（也是用数组来指定）
        SimpleAdapter adapter = new SimpleAdapter(getActivity()
                , listitem
                , R.layout.fragment__item
                , new String[]{"item_title", "item_content", "item_time", "item_location"}
                , new int[]{R.id.item_title, R.id.item_content, R.id.item_time, R.id.item_location});

        ListView listView = (ListView) view.findViewById(R.id.lv_expense);
        listView.setAdapter(adapter);


        return view;
    }

}
