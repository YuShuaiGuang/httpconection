package com.example.yu.week3a;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener{
    private XListView lv;
    private List<Bean.ListBean>list;
    private int page=1;
    private String path="http://qhb.2dyt.com/Bwei/news?type=1&postkey=1503d&page=";
    private MyAdapter adapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson=new Gson();
            Bean bean=gson.fromJson(msg.obj.toString(),Bean.class);
            adapter.notifyDataSetChanged();
            xlistDat();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(list.size()>0&&position<list.size()){
                        View v=View.inflate(MainActivity.this,R.layout.item_dialog,null);
                        ImageView image1=(ImageView)v.findViewById(R.id.image1);
                        ImageView image2=(ImageView)v.findViewById(R.id.image2);
                        TextView cancle=(TextView)v.findViewById(R.id.cancle);
                        TextView submit=(TextView)v.findViewById(R.id.submit);
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this).setView(v);
                        final Dialog dialog=builder.create();
                        Bean.ListBean bean=list.get(position-1);
                        if (bean.getType()==1){
                            image2.setVisibility(View.GONE);
                            imageLoader.displayImage(bean.getPic(),image1,options);
                        }else  if (bean.getType()==2){
                            String pic=bean.getPic();
                            String[]urlpaths=pic.split("\\|");
                            if (urlpaths.length>=2){
                                imageLoader.displayImage(urlpaths[0],image1,options);
                                imageLoader.displayImage(urlpaths[1],image1,options);
                            }
                        }
                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
            });

        }
    };
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private void xlistDat() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime("刚刚");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();//初始化数据
        loadData();
    }
    //初始化数据
    private void initData() {
        lv=(XListView)findViewById(R.id.lv);
        list=new ArrayList<>();
        adapter = new MyAdapter(list, MainActivity.this);
        lv.setAdapter(adapter);
        lv.setXListViewListener(this);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        imageLoader = ImageLoader.getInstance();
        File file=new File(Environment.getExternalStorageDirectory(),"bawei");
        if (!file.exists())
            file.mkdirs();
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(MainActivity.this)
                .diskCache(new UnlimitedDiskCache(file))
                .build();
        imageLoader.init(configuration);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheOnDisk(true)
                .build();

    }
    //网络请求
    private void loadData() {
        new Thread(){
            @Override
            public void run() {
                String result=UrlUtils.getData(path+page);
                Message msg=Message.obtain();
                msg.obj=result;
                msg.what=1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    //上拉刷新
    @Override
    public void onRefresh() {
        page=1;
        list.clear();
        loadData();

    }
    //下拉加载
    @Override
    public void onLoadMore() {
        page=list.size();
        loadData();
    }
}
