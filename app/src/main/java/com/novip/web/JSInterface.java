package com.novip.web;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.novip.utils.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSInterface {

    @JavascriptInterface
    public void showSource(String html) {
        Log.d("HTML", html);
        Element body = Jsoup.parse(html).body();
        Elements elements = body.getElementsByAttribute("src");
        for(Element e: elements){
            if(e.tagName().equals("video")){
                for(DataNode node : e.dataNodes()){
                    if(node.nodeName().equals("src")){

                        Log.d("JSInterface",node.getWholeData());
                    }
                }
            }
            Log.d("JSInterface",e.toString());
        }
        /*Elements elements = body.getElementsByTag("body");
        if(elements != null && !elements.isEmpty()){
            elements = elements.get(0).getElementsByTag("video");
            if(elements != null && !elements.isEmpty()){
                Log.d("JSInterface",elements.get(0).toString());
                elements = elements.get(0).getElementsByAttribute("src");
                if(elements != null && !elements.isEmpty()){
                    Log.d("JSInterface",elements.get(0).toString());
                }
            }
        }else {
            Log.d("JSInterface","没有Video");
        }*/
        FileUtils.saveFile("video.html",html);
    }
}
