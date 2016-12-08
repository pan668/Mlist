package intent.com.example.pls668.mylist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import intent.com.example.pls668.mylist.Subjects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends ListFragment implements View.OnClickListener {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //因为允许修改列表，所以把Subjects.java的项拷贝到ArrayList
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0, z = Subjects.SUBJECTS.length; i < z ; i++) {
            items.add(Subjects.SUBJECTS[i]);
        }
        //设置填充器
        setListAdapter(new PopupAdapter(items));
    }
    //当点击列表的某项时，弹出一个消息
    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        String item = (String) listView.getItemAtPosition(position);
        //当用户点击是显示的toast消息
        Toast.makeText(getActivity(), "被点击的项: " + item, Toast.LENGTH_SHORT).show();
    }
    //当用户点击下拉列表按钮时，弹出下拉菜单
    @Override
    public void onClick(final View view) {
        //弹出菜单显示前，视图位置可能会改变，
        // 视图需要提交一个Runnable显示弹出菜单，确保弹出菜单正确放置.
        view.post(new Runnable() {
            @Override
            public void run() {
                showPopupMenu(view);
            }
        });
    }
    private void showPopupMenu(View view) {
        final PopupAdapter adapter = (PopupAdapter) getListAdapter();
        //从视图的tag取出已点击的项
        final String item = (String) view.getTag();
        //创建一个弹出菜单，赋给它点击的view为锚点
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //把菜单资源充入弹出菜单的菜单
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

        //设置一个监听器，当菜单项被点击时我们能收到通知
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_remove:
                        // 从adapter移除这个项
                        adapter.remove(item);
                        return true;
                }
                return false;
            }
        });
        //最后显示弹出菜单
        popup.show();
    }

    /**
     * 一个简单的array适配器，生成subjects列表
     */
    class PopupAdapter extends ArrayAdapter<String> {

        PopupAdapter(ArrayList<String> items) {
            super(getActivity(), R.layout.fragment_subject, android.R.id.text1, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            //让ArrayAdapter填充布局，设置文本
            View view = super.getView(position, convertView, container);

            //从填充的view取出弹出按钮
            View popupButton = view.findViewById(R.id.button_popup);

            //把项作为按钮的tag,tag可以起到保存附加信息作用，这样可以在后面取出来使用
            popupButton.setTag(getItem(position));

            //设置fragment实例作为OnClickListener
            popupButton.setOnClickListener(SubjectFragment.this);

            //返回显示的view
            return view;
        }
    }

}
