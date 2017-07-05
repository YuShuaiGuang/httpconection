package com.example.yu.week3a;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.List;

/**
 * Created by YU on 2017/6/27.
 */
public class MyAdapter extends BaseAdapter {
    private List<Bean.ListBean>list;
    private Context context;
    private int TYPE0=0;
    private int TYPE1=1;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;

    public MyAdapter(List<Bean.ListBean>list,Context context){
        this.list=list;
        this.context=context;
        imageLoader = ImageLoader.getInstance();
        File file=new File(Environment.getExternalStorageDirectory(),"bawei");
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(context)
                .diskCache(new UnlimitedDiskCache(file))
                .build();
        imageLoader.init(configuration);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheOnDisk(true)
                .build();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType()==1?TYPE0:TYPE1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        int type=getItemViewType(position);
        if (convertView==null){
            holder=new ViewHolder();
            if (type==TYPE0){
                convertView=View.inflate(context,R.layout.item1,null);
                holder.ID=(TextView)convertView.findViewById(R.id.id);
            }else if(type==TYPE1){
                convertView=View.inflate(context,R.layout.item2,null);
                holder.imag2= (ImageView) convertView.findViewById(R.id.image1);
            }
            holder.imag1=(ImageView)convertView.findViewById(R.id.image);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
            Bean.ListBean bean=list.get(position);
        holder.title.setText(bean.getTitle());
        if (type==TYPE0){
            holder.ID.setText(bean.getId()+"");
            imageLoader.displayImage(bean.getPic(),holder.imag1,options);
        }else {
            if (type == TYPE1) {
                String pic = bean.getPic();
                String[] urlpaths = pic.split("\\|");
                if (urlpaths.length >= 2) {
                    imageLoader.displayImage(urlpaths[0], holder.imag1, options);
                    imageLoader.displayImage(urlpaths[1], holder.imag1, options);
                }
            }
        }

        return convertView;
    }
    class ViewHolder{
        TextView title,ID;
        ImageView imag1,imag2;
    }
}
